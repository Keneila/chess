package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
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
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "quit" -> "quit";
                default -> "type leave to leave the game.";
            };

        } catch(Exception ex){
                return ex.getMessage();
            }
        }

    private String leave() {
        state = State.LOGGED_IN;
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
        StringBuilder s = new StringBuilder();
        int checker = 0;
        var list = squares;
        if(direction){
            list = Arrays.asList(list).reversed().toArray(new ChessPiece[8][8]);
            for (int i=0; i < list.length; i++) {
                var row = list[i];
                Arrays.asList(row).reversed();
                list[i] = Arrays.asList(row).reversed().toArray(new ChessPiece[8]);
            }
        }
        for (ChessPiece[] r : list){
            s.append("\n");
            checker = checker-1;
            for(ChessPiece p : r){
                if (checker == 0){
                    s.append(SET_BG_COLOR_RED);
                    checker = 1;
                } else {
                    s.append(SET_BG_COLOR_WHITE);
                    checker = 0;
                }
                String colorSet = colorSet = SET_TEXT_COLOR_BLUE;;
                String piece = EMPTY;
                if (p != null){
                    ChessGame.TeamColor color = p.getTeamColor();
                    switch (color){
                        case BLACK -> colorSet = SET_TEXT_COLOR_BLACK;
                        case WHITE -> colorSet = SET_TEXT_COLOR_BLUE;
                    }
                    switch (p.getPieceType()){
                        case ROOK -> piece = BLACK_ROOK;
                        case KING -> piece = BLACK_KING;
                        case BISHOP -> piece = BLACK_BISHOP;
                        case QUEEN -> piece = BLACK_QUEEN;
                        case PAWN -> piece = BLACK_PAWN;
                        case KNIGHT -> piece = BLACK_KNIGHT;
                        case null, default -> piece = " ";
                    }
                }
                s.append(new String(colorSet + piece));
                s.append("");

            }
        }
        s.append("\n\n");
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
