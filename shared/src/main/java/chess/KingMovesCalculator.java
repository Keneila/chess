package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> options = new HashSet<>();
        int[][] directions = {{1,1},{1,-1},{-1,-1},{-1,1},{0,1},{0,-1},{-1,0},{1,0}};
        ChessPosition newPos = position;
        for (int i = 0; i < directions.length; i++){
            newPos = new ChessPosition((position.getRow()+directions[i][0]), (position.getColumn()+directions[i][1]));
            if (!checkIfNotAllowed(board, position, newPos)){
                ChessMove newMove = new ChessMove(position,newPos,null);
                options.add(newMove);
            }
        }
        return options;
    }
    private boolean checkIfNotAllowed (ChessBoard board, ChessPosition position, ChessPosition newPos){
        if (newPos.getColumn() < 9 && newPos.getRow() < 9 && newPos.getColumn() > 0 && newPos.getRow() > 0) {
            if (board.getPiece(position) != null && board.getPiece(newPos) != null) {
                if (board.getPiece(position).getTeamColor() == board.getPiece(newPos).getTeamColor()) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } else {
            return true;
        }

    }
}
