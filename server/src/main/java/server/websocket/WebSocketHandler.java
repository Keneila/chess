package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.Objects;

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
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, username,command.getAuthToken(), command.getGameID());
            case MAKE_MOVE -> makeMove(session, command.getAuthToken(),command.getGameID());
            case LEAVE -> leave(username,command.getGameID());
            case RESIGN -> resign(username,command.getGameID());
        }
    }

    private void resign(String user, Integer gameID) throws IOException {
        connections.remove(user);
        var message = String.format("%s resigned from the game.", user);
        var notif = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(user, gameID, notif);
    }

    private void leave(String user, Integer gameID) throws IOException{
        connections.remove(user);
        try {

            GameData data = gameDAO.findGame(gameID);
            if (data.blackUsername().equals(user)){
                data = new GameData(data.gameID(), data.whiteUsername(), null, data.gameName(), data.game());
            }
            if (data.whiteUsername().equals(user)){
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

    private void makeMove(Session session, String authToken, Integer gameID) {
        
    }

    private void connect(Session session, String user, String auth, Integer gameID) throws IOException {
        connections.add(session,user,gameID);
        String color = "";
        ServerMessage notif;
        try {
            GameData data = gameDAO.findGame(gameID);
            if (Objects.equals(data.blackUsername(), user)){
                color = "black";
            } else if (Objects.equals(data.whiteUsername(), user)){
                color = "white";
            } else {
                color = "an observer";
            }
        } catch (DataAccessException e) {
            var message = String.format("Error with connecting.");
            notif = new ServerMessage(ERROR, message);
        }
        var message = String.format("%s joined the game as %s.", user, color);
        notif = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(user,gameID, notif);
    }

}