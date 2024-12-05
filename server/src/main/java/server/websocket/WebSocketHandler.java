package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command.getAuthToken(),command.getGameID());
            case MAKE_MOVE -> makeMove(session, command.getAuthToken(),command.getGameID());
            case LEAVE -> leave(command.getAuthToken(),command.getGameID());
            case RESIGN -> resign(command.getAuthToken(),command.getGameID());
        }
    }

    private void resign(String authToken, Integer gameID) throws IOException {
        connections.remove(authToken);
        var notif = new ServerMessage(NOTIFICATION, "resigned");
        connections.broadcast(authToken, gameID, notif);
    }

    private void leave(String authToken, Integer gameID) throws IOException {
        connections.remove(authToken);
        var notif = new ServerMessage(NOTIFICATION, "left");
        connections.broadcast(authToken, gameID, notif);
    }

    private void makeMove(Session session, String authToken, Integer gameID) {
        
    }

    private void connect(Session session, String authToken, Integer gameID) throws IOException {
        connections.add(session,authToken,gameID);
        var notif = new ServerMessage(NOTIFICATION, "joined");
        connections.broadcast(authToken,gameID, notif);
    }

}