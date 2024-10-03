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
    private ChessTeam White;
    private ChessTeam Black;
    public ChessBoard() {
        White = new ChessTeam();
        Black = new ChessTeam();
    }

    public ChessBoard(ChessBoard board) {
        squares = board.squares.clone();
        White = new ChessTeam();
        White.setKingPos(board.White.getKingPos());
        White.setPieces(board.White.getPieces());
        Black = new ChessTeam();
        Black.setKingPos(board.Black.getKingPos());
        Black.setPieces(board.Black.getPieces());
    }

    public ChessTeam getTeam(ChessGame.TeamColor color) {
        switch (color){
            case BLACK -> {
                return Black;
            }
            case WHITE -> {
                return White;
            }
        }
        return null;
    }


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece != null) {
            if ((piece.getPieceType() == ChessPiece.PieceType.KING)){
                switch (piece.getTeamColor()) {
                    case WHITE -> White.setKingPos(position);
                    case BLACK -> Black.setKingPos(position);
                }
            }
            switch (piece.getTeamColor()) {
                case WHITE -> White.addToPieces(position);
                case BLACK -> Black.addToPieces(position);
            }
        } else {
            if (White.getPieces().contains(position)){
                White.removeFromPieces(position);
            } else if (Black.getPieces().contains(position)){
                Black.removeFromPieces(position);
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
        //System.out.println(b.toString());
        ChessPosition W = new ChessPosition(x,y);
        ChessPosition B = new ChessPosition(z,y);
        //System.out.println(B.toString());
        addPiece(B,b);
        addPiece(W, w);
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
                    s.append(p.getPieceType().name() + p.getTeamColor().name());
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
