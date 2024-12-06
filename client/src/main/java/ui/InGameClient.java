package ui;

import chess.*;
import model.AuthData;
import model.GameData;
import ui.server.ServerFacade;
import ui.websocket.ServerMessageHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import java.util.*;

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
    private String color;
    private int resigning=0;

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
            if (resigning==0) {
                if (state == State.WATCHING) {
                    return switch (cmd) {
                        case "redraw" -> redraw();
                        case "highlight" -> highlight(params);
                        case "leave" -> leave();
                        default -> helpObs();
                    };
                } else {
                    return switch (cmd) {
                        case "redraw" -> redraw();
                        case "move" -> makeMove(params);
                        case "highlight" -> highlight(params);
                        case "resign" -> resign();
                        case "leave" -> leave();
                        default -> help();
                    };
                }
            } else if (resigning==1){
                return switch (cmd) {
                    case "yes" -> resign();
                    case "no" -> noResign();
                    default -> "please input yes or no";
                };
            } else {
                return switch (cmd) {
                    case "redraw" -> redraw();
                    case "highlight" -> highlight(params);
                    case "leave" -> leave();
                    default -> helpObs();
                };
            }

        } catch(Exception ex){
                return ex.getMessage();
            }
        }

    private String noResign() {
        resigning=0;
        return "Glad you're still playing.";
    }

    private String resign() {
        if(resigning==0) {
            resigning = 1;
            return "Are you sure you want to forfeit? Please input yes or no to confirm.";
        } else {
            ws.resign(auth.authToken(),gameID);
            resigning=3;
            return "You are free to leave whenever you'd like.";
        }
    }

    private String highlight(String[] params) {
        try {
            for (GameData g : server.listGames(auth.authToken())) {
                if (gameID == g.gameID()) {
                    game = g.game();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (params.length == 1) {
            String[] coor = params[0].split(",");
            if(coor.length==2){
                ChessPosition pos = parseCoor(coor, false);
                if(pos == null){
                    return "Please input coordinates within the range of a-h and 1-8";
                }
                Collection<ChessMove> moves = game.validMoves(pos);
                Collection<ChessPosition> spots = new java.util.ArrayList<>(List.of());
                int x = 9 - pos.getRow();
                int y = 9 - pos.getColumn();
                spots.add(new ChessPosition( x,y ));
                if(moves!= null) {
                    for (var move : moves) {
                        x = 9 -move.getEndPosition().getRow();
                        y = 9 - move.getEndPosition().getColumn();
                        spots.add(new ChessPosition(x,y));
                    }
                }
                if (color == null || color.equals("obs")){
                    return printBoard(game.getBoard(),"white",spots);
                } else {
                    return printBoard(game.getBoard(), color, spots);
                }
            }
        }
        return "Please input coordinates in this syntax: moves x,y";
    }

    private ChessPosition parseCoor(String[] params, boolean draw){

        int[] coor = new int[2];
        try{
            int y = Integer.parseInt(params[1]);
            if(y > 0 && y <9){
                if (draw) {
                    coor[1] = 9 - y;
                } else {
                    coor[1] = y;
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }

        if ((long) params[0].length() == 1){
            Character x = params[0].toLowerCase().charAt(0);
            if(x >= 'a' && x <= 'h'){
                int a = x-'a'+1;
                if (draw) {
                    coor[0] = a;
                } else {
                    coor[0] = 9-a;
                }
            } else {
                return null;
            }
        }
        return new ChessPosition(coor[1], coor[0]);
    }

    private String makeMove(String[] params) {
        if (params.length == 2 || params.length == 3) {
            String[] coorS = params[0].split(",");
            String[] coorE = params[1].split(",");
            if (coorS.length == 2 && coorE.length == 2) {
                ChessPosition starPos = parseCoor(coorS, false);
                ChessPosition endPos = parseCoor(coorE, false);
                if (starPos == null || endPos == null) {
                    return "Please input coordinates within the range of a-h and 1-8";
                }
                ChessPiece.PieceType promo = null;
                if(params.length == 3) {
                    promo = determinePromo(starPos, endPos, params[2]);
                } else {
                    promo = determinePromo(starPos, endPos, null);
                }
                ChessMove move = new ChessMove(starPos,endPos,promo);
                ws.makeMove(auth.authToken(), gameID, move);
                return "Moved Piece.";
            }
        }
        return "Please input coordinates in this syntax:make x,y x2,y2";
    }

    private ChessPiece.PieceType determinePromo(ChessPosition starPos, ChessPosition endPos, String type) {
        if (game.getBoard().getPiece(starPos) != null) {
            ChessPiece.PieceType p = game.getBoard().getPiece(starPos).getPieceType();
            if ((p == ChessPiece.PieceType.PAWN) && (endPos.getRow() == 1 || endPos.getRow() == 8)) {
                    return switch (type) {
                        case "b" -> ChessPiece.PieceType.BISHOP;
                        case "r" -> ChessPiece.PieceType.ROOK;
                        case "k" -> ChessPiece.PieceType.KNIGHT;
                        default -> ChessPiece.PieceType.QUEEN;
                    };
            }
        }
        return null;
    }

    private String redraw() throws Exception {
        for (GameData g : server.listGames(auth.authToken())) {
          if(gameID==g.gameID())  {
              game = g.game();
          }
        }
        if(state == State.WATCHING){
            return printBoard(game.getBoard(), "white", null);
        }
        return printBoard(game.getBoard(), color , null);
    }

    public String help() {
        return """
                redraw - chess board
                move <x,y> <x2,y2> - move piece to second coordinates
                highlight <x,y> - highlight legal moves of piece at coordinates
                leave - to exit game
                resign - forfeit game
                help - with possible commands
                """;
    }
    public String helpObs() {
        return """
                redraw - chess board
                highlight <x,y> - highlight legal moves of piece at coordinates
                leave - to exit game
                help - with possible commands
                """;
    }

    public void join(Integer gameID, ChessGame game, String color) throws Exception {
        this.gameID = gameID;
        this.game = game;
        this.color = color;
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.joinGame(auth.authToken(), gameID);
    }

    private String leave() {
        state = State.LOGGED_IN;
        ws.leave(auth.authToken(), gameID);
        return "Left game";
    }


    public String printBoard(ChessBoard board, String color, Collection<ChessPosition> positions){
        StringBuilder s = new StringBuilder();
        String borderBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h   g   f  e   d  c   b   a    " + SET_BG_COLOR_DARK_GREY + "\n";
        String b = toStringBoard(false, board.getSquares(), positions);
        int num = 0;
        int up = 1;
        if(color.equals("white") || color.equals("obs")){
            b = toStringBoard(true, board.getSquares(), positions);
            num = 9;
            up = -1;
            borderBoard = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a   b   c  d   e  f   g   h    "+ SET_BG_COLOR_DARK_GREY + "\n";
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

    public String toStringBoard(boolean direction, ChessPiece[][] squares, Collection<ChessPosition> positions) {
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
        int x=0;
        for (ChessPiece[] r : list){
            x++;
            s.append("\n");
            checker = checker-1;
            int y=0;
            for(ChessPiece p : r){
                y++;
                    if (checker == 0) {
                        s.append(SET_BG_COLOR_RED);
                        checker = 1;
                    } else {
                        s.append(SET_BG_COLOR_WHITE);
                        checker = 0;
                    }
                    if(!direction){
                        if(positions != null && !positions.isEmpty() &&
                                positions.contains(new ChessPosition(9-x,9-y))){
                            s.append(SET_BG_COLOR_GREEN);
                        }
                    } else {
                        if (positions != null && !positions.isEmpty() &&
                                positions.contains(new ChessPosition(x, y))) {
                            s.append(SET_BG_COLOR_GREEN);
                        }
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

    public String getColor() {
        return color;
    }
}
