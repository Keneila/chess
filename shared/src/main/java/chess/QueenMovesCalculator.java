package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> options = new HashSet<>();
        int[][] directions = {{1,1},{1,-1},{-1,-1},{-1,1},{0,1},{0,-1},{-1,0},{1,0}};
        ChessPosition newPos = position;
        for (int i = 0; i < directions.length; i++){
            newPos = new ChessPosition((newPos.getRow()+directions[i][0]), (newPos.getColumn()+directions[i][1]));
            if (newPos.getColumn() < 9 && newPos.getRow() < 9 && newPos.getColumn() > 0 && newPos.getRow() > 0) {
                if(checkIfHit(board,newPos)){
                    if (!checkIfNotAllowed(board, position, newPos)){
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
                    if (!checkIfNotAllowed(board, position, newPos)){
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
        //System.out.println(options);
        return options;
    }

    private boolean checkIfHit (ChessBoard board, ChessPosition position){
        int x = position.getColumn();
        int y = position.getRow();
        if (x < 9 && x> 0){
            if (y < 9 && y> 0){
                if (board.getPiece(position) == null){
                    return false;
                } else {
                    return true;
                }

            }
        }

        return true;

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
