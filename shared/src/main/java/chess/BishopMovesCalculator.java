package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{
    public BishopMovesCalculator() {
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> options = new HashSet<>();
        int[][] directions = {{1,1},{1,-1},{-1,-1},{-1,1}};
        ChessPosition newPos = position;
        for (int i = 0; i < 4; i++){
            newPos = new ChessPosition((newPos.getRow()+directions[i][0]), (newPos.getColumn()+directions[i][1]));
            if (newPos.getColumn() < 9 && newPos.getRow() < 9 && newPos.getColumn() > 0 && newPos.getRow() > 0) {
                if(checkIfHit(board,newPos)){
                    if (!checkIfMyPiece(board, position, newPos)){
                        ChessMove newMove = new ChessMove(position,newPos,null);
                        options.add(newMove);
                    }
                } else {
                    ChessMove newMove = new ChessMove(position,newPos,null);
                    options.add(newMove);
                }
            }
            while (!checkIfHit(board,newPos)){
                newPos = new ChessPosition((newPos.getRow()+directions[i][0]), (newPos.getColumn()+directions[i][1]));
                if (checkIfHit(board,newPos)){
                    if (!checkIfMyPiece(board, position, newPos)){
                        ChessMove newMove = new ChessMove(position,newPos,null);
                        options.add(newMove);
                    }
                } else {
                    ChessMove newMove = new ChessMove(position,newPos,null);
                    options.add(newMove);
                }

            }
            newPos = position;
        }
        return options;
    }

    private boolean checkIfHit (ChessBoard board, ChessPosition position){
        int x = position.getColumn();
        int y = position.getRow();
        if (x < 8 && x> 1){
            if (y < 8 && y> 1){
                if (board.getPiece(position) == null){
                    return false;
                } else {
                    return true;
                }

            }
        }
        return true;

    }
    private boolean checkIfMyPiece (ChessBoard board, ChessPosition position, ChessPosition newPos){
        if (board.getPiece(position) != null && board.getPiece(newPos) != null) {
            if (board.getPiece(position).getTeamColor() == board.getPiece(newPos).getTeamColor()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
