package ui;

import chess.ChessBoard;
import model.AuthData;
import ui.server.ServerFacade;

public class InGameClient implements Client {
    private final ServerFacade server;
    private final String serverUrl;
    private State state;
    private AuthData auth = null;

    public InGameClient(ServerFacade server, String serverUrl, State state) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.state = state;
    }

    @Override
    public String eval(String line) {
        String result = "";
        if (line.equals("help")){
            //return help();
        } else if (line.equals("quit")){
            return line;
        }
        return result;
    }

    public String printBoard(ChessBoard board, String color){
        return board.toString();
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
