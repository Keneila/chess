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

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        for (int i= 0; i< 8; i++){
            ChessPiece pawnB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPiece pawnW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition B = new ChessPosition(1,i);
            ChessPosition W = new ChessPosition(6,i);
            addPiece(B, pawnB);
            addPiece(W, pawnW);
        }
        for (ChessPiece.PieceType type : ChessPiece.PieceType.values()){
            if (type != ChessPiece.PieceType.PAWN){
                int y = 0;
                int j = 0;
                boolean two = true;
                switch (type){
                    case KING:
                        y = 4;
                        two = false;
                        break;
                    case QUEEN:
                        y = 3;
                        two = false;
                        break;
                    case KNIGHT:
                        y = 1;
                        j=6;
                        break;
                    case ROOK:
                        y = 0;
                        j = 7;
                        break;
                    case BISHOP:
                        y = 2;
                        j = 5;
                        break;
                }
                ChessPiece b = new ChessPiece(ChessGame.TeamColor.BLACK, type);
                ChessPiece w = new ChessPiece(ChessGame.TeamColor.WHITE, type);
                //System.out.println(b.toString());
                ChessPosition B = new ChessPosition(0,y);
                ChessPosition W = new ChessPosition(7,y);
                //System.out.println(B.toString());
                addPiece(B,b);
                addPiece(W, w);
                if (two){
                    ChessPiece pieceB = new ChessPiece(ChessGame.TeamColor.BLACK, type);
                    ChessPiece pieceW = new ChessPiece(ChessGame.TeamColor.WHITE, type);
                    //System.out.println(pieceW.toString());
                    ChessPosition posB = new ChessPosition(0,j);
                    ChessPosition posW = new ChessPosition(7,j);
                    //System.out.println(posW.toString());
                    addPiece(posB,pieceB);
                    addPiece(posW, pieceW);
                }
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        int x = 0;
        int y = 0;
        for (ChessPiece[] r :  squares){
            x=0;
            s.append("\n");
            for(ChessPiece p : r){
                if (p != null){
                    s.append(p.getPieceType().name());
                    s.append(" ");
                }
                else {
                    s.append("(" + x + "," + y + ") ");
                }
                x++;

            }
            y++;
        }
        return s.toString();
    }
}
