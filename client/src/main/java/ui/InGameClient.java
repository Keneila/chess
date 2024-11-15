package ui;

import chess.ChessBoard;
import model.AuthData;
import ui.server.ServerFacade;

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
        String borderBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a   b   c  d   e  f   g   h    "+ SET_BG_COLOR_DARK_GREY + "\n";
        String b = board.toString();
        String[] rows = b.split("\n");
        int num = 9;
        int up = -1;
        if(color.equals("white")){
            num = 1;
            up = 1;
            Collections.reverse(Arrays.asList(rows));
            //borderBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h   g   f  e   d  c   b   a    "+ SET_BG_COLOR_DARK_GREY + "\n";
        }
        s.append(borderBoard);
        for (String row : rows){
            if (!(num == 9)) {
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
