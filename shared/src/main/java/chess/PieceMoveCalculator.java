package chess;

import java.util.Collection;

public abstract class PieceMoveCalculator {
    int[][]directions;
    public abstract Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition postion);
    boolean allowed(ChessBoard board, ChessPosition position, ChessPosition newPos){
        if(newPos.getRow() > 0 && newPos.getColumn() > 0 && newPos.getColumn() < 9 && newPos.getRow() < 9){
            if (board.getPiece(position) != null && board.getPiece(newPos) != null){
                if (board.getPiece(position).getTeamColor() == board.getPiece(newPos).getTeamColor()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
