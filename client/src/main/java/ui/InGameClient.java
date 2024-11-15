package ui;

import chess.ChessBoard;
import model.AuthData;
import ui.server.ServerFacade;
import static ui.EscapeSequences.*;

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
        StringBuilder s = new StringBuilder();
        String whiteBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a   b   c   d  e  f  g   h   \n";
        s.append(whiteBoard);
        String b = board.toString();
        String[] rows = b.split("\n");
        int num = 0;
        int up = 1;
        if(color.equals("black")){
            num = 9;
            up = -1;
        }
        for (String row : rows){
            if ((num != 0 && up == 1) || (num != 9 && up == -1)) {
                s.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                s.append(" " + num + " ");
                s.append(row);
                s.append(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                s.append(" " + num + " ");
                s.append("\n");
            }
            num = num + up;
        }
        s.append(whiteBoard);
        return s.toString();
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
