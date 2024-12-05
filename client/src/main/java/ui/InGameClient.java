package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.AuthData;
import model.GameData;
import ui.server.ServerFacade;
import ui.websocket.ServerMessageHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ui.EscapeSequences.*;

public class InGameClient implements Client {
    private final ServerFacade server;
    private final String serverUrl;
    private State state;
    private AuthData auth = null;
    private ChessGame game = null;
    private ServerMessageHandler notificationHandler;
    private WebSocketFacade ws;
    private Integer gameID;

    public InGameClient(ServerFacade server, String serverUrl, State state, ServerMessageHandler handler) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.state = state;
        this.notificationHandler = handler;
    }

    @Override
    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(state == State.WATCHING){
                return switch (cmd) {
                    case "redraw" -> redraw();
                    case "moves" -> highlight(params);
                    case "leave" -> leave();
                    default -> helpObs();
                };
            } else {
            return switch (cmd) {
                case "redraw" -> redraw();
                case "make" -> makeMove(params);
                case "moves" -> highlight(params);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
            }

        } catch(Exception ex){
                return ex.getMessage();
            }
        }

    private String resign() {
    }

    private String highlight(String[] params) {
    }

    private String makeMove(String[] params) {
    }

    private String redraw() {
    }

    public String help() {
        return """
                redraw - chess board
                make move <x,y> <x2,y2> - move piece to second coordinates
                moves <x,y> - highlight legal moves of piece at coordinates
                leave - to exit game
                resign - forfeit game
                help - with possible commands
                """;
    }
    public String helpObs() {
        return """
                redraw - chess board
                moves <x,y> - highlight legal moves of piece at coordinates
                leave - to exit game
                help - with possible commands
                """;
    }

    public void join(Integer gameID, ChessGame game) throws Exception {
        this.gameID = gameID;
        this.game = game;
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.joinGame(auth.authToken(), gameID);
    }

    private String leave() {
        state = State.LOGGED_IN;
        ws.leave(auth.authToken(), gameID);
        return "Left game";
    }


    public String printBoard(ChessBoard board, String color){
        StringBuilder s = new StringBuilder();
        String borderBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a   b   c  d   e  f   g   h    "+ SET_BG_COLOR_DARK_GREY + "\n";
        String b = toStringBoard(true, board.getSquares());
        int num = 9;
        int up = -1;
        if(color.equals("white")){
            b = toStringBoard(false, board.getSquares());
            num = 0;
            up = 1;
            borderBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h   g   f  e   d  c   b   a    "+ SET_BG_COLOR_DARK_GREY + "\n";
        }
        String[] rows = b.split("\n");
        s.append(borderBoard);
        for (String row : rows){
            if (!(num == 9 || num == 0)) {
                s.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                s.append(" " + num + " ");
                s.append(row);
                s.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                s.append(" " + num + " ");
                s.append(SET_BG_COLOR_DARK_GREY + "\n");
            }
            num = num + up;
        }
        s.append(borderBoard);
        return s.toString();
    }

    public String toStringBoard(boolean direction, ChessPiece[][] squares) {
        return null;
    }

    @Override
    public void updateState(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setAuth(AuthData auth) {
        this.auth = auth;
    }
}
