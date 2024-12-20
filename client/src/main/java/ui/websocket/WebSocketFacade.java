package ui.websocket;

import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import model.AuthData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler notificationHandler;

    public WebSocketFacade(String url, ServerMessageHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if (notification.getMessage() == null){
                        notification.setMessage("");
                    }
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String auth, Integer gameID){
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void leave(String auth, Integer gameID){
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void makeMove(String auth, Integer gameID, ChessMove move){
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, auth, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resign(String auth, Integer gameID) {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, auth, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}

