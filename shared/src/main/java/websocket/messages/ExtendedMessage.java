package websocket.messages;

import chess.ChessGame;

public class ExtendedMessage extends ServerMessage {
    String message= null;
    ChessGame game = null;
    public ExtendedMessage(ServerMessageType type) {
        super(type);
    }
    public ExtendedMessage(ServerMessageType type, String message){
        super(type);
        this.message = message;
    }
    public ExtendedMessage(ServerMessageType type, ChessGame game, String message){
        super(type);
        this.game = game;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public ChessGame getGame() {
        return game;
    }
}
