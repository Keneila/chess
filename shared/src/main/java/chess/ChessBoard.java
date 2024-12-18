package chess;

import java.util.Arrays;
import java.util.Objects;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    private ChessTeam white;
    private ChessTeam black;
    public static final String BLACK_KING = " ♚ ";
    public static final String BLACK_QUEEN = " ♛ ";
    public static final String BLACK_BISHOP = " ♝ ";
    public static final String BLACK_KNIGHT = " ♞ ";
    public static final String BLACK_ROOK = " ♜ ";
    public static final String BLACK_PAWN = " ♟ ";
    public static final String EMPTY = " \u2003 ";
    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";
    public static final String SET_TEXT_COLOR_BLACK = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String SET_BG_COLOR_RED = SET_BG_COLOR + "160m";
    public static final String SET_BG_COLOR_WHITE = SET_BG_COLOR + "15m";
    public ChessBoard() {
        white = new ChessTeam();
        black = new ChessTeam();
    }

    public ChessBoard(ChessBoard board) {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                squares[i][j] = board.getSquares()[i][j];
            }
        }
        white = new ChessTeam(board.white);
        black = new ChessTeam(board.black);
    }

    public ChessTeam getTeam(ChessGame.TeamColor color) {
        switch (color){
            case BLACK -> {
                return black;
            }
            case WHITE -> {
                return white;
            }
        }
        return null;
    }


    public ChessPiece[][] getSquares() {
        return squares;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ChessPiece orig = squares[position.getRow()-1][position.getColumn()-1];
        if (orig != null){
            switch(orig.getTeamColor()){
                case BLACK -> black.removeFromPieces(position);
                case WHITE -> white.removeFromPieces(position);
            }
        }
        if (piece != null) {
            if ((piece.getPieceType() == ChessPiece.PieceType.KING)){
                switch (piece.getTeamColor()) {
                    case WHITE -> white.setKingPos(position);
                    case BLACK -> black.setKingPos(position);
                }
            }
            switch (piece.getTeamColor()) {
                case WHITE -> white.addToPieces(position);
                case BLACK -> black.addToPieces(position);
            }
        } else {
            if (white.getPieces().contains(position)){
                white.removeFromPieces(position);
            } else if (black.getPieces().contains(position)){
                black.removeFromPieces(position);
            }
        }
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        for (int i= 1; i< 9; i++){
            addBothPieces(ChessPiece.PieceType.PAWN, 2, 7, i);
        }
        for (ChessPiece.PieceType type : ChessPiece.PieceType.values()){
            if (type != ChessPiece.PieceType.PAWN){
                int y = 0;
                int j = 0;
                boolean two = true;
                switch (type){
                    case KING:
                        y = 5;
                        two = false;
                        break;
                    case QUEEN:
                        y = 4;
                        two = false;
                        break;
                    case KNIGHT:
                        y = 2;
                        j=7;
                        break;
                    case ROOK:
                        y = 1;
                        j = 8;
                        break;
                    case BISHOP:
                        y = 3;
                        j = 6;
                        break;
                }
                addBothPieces(type, 1, 8, y);
                if (two){
                    addBothPieces(type, 1, 8, j);
                }
            }
        }

    }

    private void addBothPieces(ChessPiece.PieceType type, int x, int z, int y){
        ChessPiece b = new ChessPiece(ChessGame.TeamColor.BLACK, type);
        ChessPiece w = new ChessPiece(ChessGame.TeamColor.WHITE, type);
        ChessPosition wPos = new ChessPosition(x,y);
        ChessPosition bPos = new ChessPosition(z,y);
        addPiece(bPos,b);
        addPiece(wPos, w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int checker = 0;
        for (ChessPiece[] r : Arrays.asList(squares).reversed()){
            s.append("\n");
            checker = checker-1;
            for(ChessPiece p : r){
                s.append(new String(String.valueOf(p)));
                s.append(" ");

            }
        }
        s.append("\n\n");
        return s.toString();
    }


}
