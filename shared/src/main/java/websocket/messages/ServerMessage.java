package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    String message= null;
    ChessGame game = null;
    ServerMessageType serverMessageType;
    String errorMessage= null;
    public void setMessage(String m) {
        this.message = m;
    }

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }
    public ServerMessage(ServerMessageType type, String message){
        this.serverMessageType = type;
        this.message = message;
    }
    public ServerMessage(ServerMessageType type, ChessGame game, String message){
        this.serverMessageType = type;
        this.game = game;
        this.message = message;
    }
    public String toString() {
        return new Gson().toJson(this);
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    public String getMessage() {
        return message;
    }

    public ChessGame getGame() {
        return game;
    }
}
