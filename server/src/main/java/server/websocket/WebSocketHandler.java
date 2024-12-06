package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static websocket.messages.ServerMessage.ServerMessageType.*;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private Service service;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(Service service, UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.service = service;

        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String username = service.getUsername(command.getAuthToken());
        try {
            if (authDAO.findAuth(command.getAuthToken()) == null) {
                throw new DataAccessException("Bad authtoken");
            }
        } catch (Exception e) {
            var notif = new ServerMessage(ERROR);
            notif.setErrorMessage(e.getMessage());
            session.getRemote().sendString(notif.toString());
            return;
        }
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, username,command.getAuthToken(), command.getGameID());
            case MAKE_MOVE -> makeMove(session, command.getAuthToken(),command.getGameID(),command.getMove());
            case LEAVE -> leave(username,command.getGameID());
            case RESIGN -> resign(username,command.getGameID());
        }
    }

    private void resign(String user, Integer gameID) throws IOException {
        try {
            GameData data = gameDAO.findGame(gameID);
            if (!Objects.equals(data.whiteUsername(), user) && !Objects.equals(data.blackUsername(), user) ) {
                throw new DataAccessException("You aren't a player.");
            }
            if(data.game().getDone()){
                var message = "Game is already over.";
                var notif = new ServerMessage(ERROR);
                notif.setErrorMessage(message);
                connections.sendOne(user, notif);
            } else {
                data.game().setDone(true);
                gameDAO.updateGame(new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), data.game()));
                var message = String.format("%s resigned from the game.", user);
                var notif = new ServerMessage(NOTIFICATION, message);
                connections.sendOne(user,notif);
                connections.broadcast(user, gameID, notif);
            }
        } catch (Exception e) {
                var notif = new ServerMessage(ERROR);
                notif.setErrorMessage(e.getMessage());
                connections.sendOne(user, notif);
                return;
            }

    }

    private void leave(String user, Integer gameID) throws IOException{
        connections.remove(user);
        try {
            GameData data = gameDAO.findGame(gameID);
            if (Objects.equals(data.whiteUsername(),user)){
                data = new GameData(data.gameID(), data.whiteUsername(), null, data.gameName(), data.game());
            }
            if (Objects.equals(data.whiteUsername(),user)){
                data = new GameData(data.gameID(), null, data.blackUsername(), data.gameName(), data.game());
            }
            gameDAO.updateGame(data);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var message = String.format("%s left the game.", user);
        var notif = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(user, gameID, notif);
    }

    private void makeMove(Session session, String authToken, Integer gameID, ChessMove move) throws IOException {
        ChessGame.TeamColor color = WHITE;
        ChessGame.TeamColor other = BLACK;
        String user = service.getUsername(authToken);
        ServerMessage notif;
        String message;
        try {
            GameData data = gameDAO.findGame(gameID);
            if(data == null){
                throw new DataAccessException("Game Not Available.");
            }
            if (data.game().getDone()){
                throw new DataAccessException("Game is over, no more moves.");
            }
            if (Objects.equals(data.blackUsername(), user)){
                color = BLACK;
                other = WHITE;
            } else if (!Objects.equals(data.whiteUsername(), user)){
                throw new DataAccessException("You aren't a player.");
            }
            if(data.game().getTeamTurn() != color){
                throw new DataAccessException("Not your turn.");
            }
            if(data.game().validMoves(move.getStartPosition()) == null){
                throw new DataAccessException("No Piece there.");
            }
            if (data.game().validMoves(move.getStartPosition()).contains(move)){
                if (data.game().getBoard().getPiece(move.getStartPosition()).getTeamColor() == color){
                    message = String.format("%s moved %s to %s.", user,
                            data.game().getBoard().getPiece(move.getStartPosition()).getPieceType(),
                            move.getEndPosition().toString());
                    data.game().makeMove(move);
                    gameDAO.updateGame(new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), data.game()));
                    check(data, color, other, user, gameID);
                } else {
                    throw new DataAccessException("Invalid Move. Not your piece.");
                }
            } else {
                throw new DataAccessException("Invalid Move.");
            }
            notif = new ServerMessage(LOAD_GAME,data.game(), null);
            connections.sendOne(user,notif);
            connections.broadcast(user, gameID, notif);
        } catch (Exception e) {
            notif = new ServerMessage(ERROR);
            notif.setErrorMessage(e.getMessage());
            connections.sendOne(user, notif);
            return;
        }
        notif = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(user, gameID, notif);
    }
    private void check(GameData data, ChessGame.TeamColor color, ChessGame.TeamColor other, String user, Integer gameID) throws IOException {
        if(data.game().isInCheck(color) || data.game().isInCheck(other)
                || (data.game().isInCheckmate(color) || data.game().isInCheckmate(other))
                || data.game().isInStalemate(color)) {
            ServerMessage checking = null;
            if (data.game().isInCheckmate(color)) {
                checking = new ServerMessage(NOTIFICATION, String.format("Checkmate. %s lost.", color));
            } else if (data.game().isInCheckmate(other)) {
                checking = new ServerMessage(NOTIFICATION, String.format("Checkmate. %s lost.", other));
            } else if (data.game().isInStalemate(color)){
                checking = new ServerMessage(NOTIFICATION, "stalemate reached");
            } else if (data.game().isInCheck(color)){
                checking = new ServerMessage(NOTIFICATION, String.format("%s is in check", color));
            } else {
                checking = new ServerMessage(NOTIFICATION, String.format("%s is in check", other));
            }
            connections.sendOne(user,checking);
            connections.broadcast(user, gameID, checking);
        }
    }

    private void connect(Session session, String user, String auth, Integer gameID) throws IOException {
        String color = "";
        ServerMessage notif;
        String message;
        connections.add(session,user,gameID);
        try {
            GameData data = gameDAO.findGame(gameID);
            if(data == null){
                throw new DataAccessException("Invalid Input");
            }
            if (Objects.equals(data.blackUsername(), user)){
                color = "black";
            } else if (Objects.equals(data.whiteUsername(), user)){
                color = "white";
            } else {
                color = "an observer";
            }
            message = String.format("%s joined the game as %s.", user, color);
            notif = new ServerMessage(NOTIFICATION, message);
            connections.broadcast(user,gameID, notif);
            notif = new ServerMessage(LOAD_GAME,data.game(), null);
            connections.sendOne(user, notif);
        } catch (DataAccessException e) {
            notif = new ServerMessage(ERROR);
            notif.setErrorMessage(e.getMessage());
            connections.sendOne(user, notif);
        }
    }

}