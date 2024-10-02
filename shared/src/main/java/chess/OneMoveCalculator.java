package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class OneMoveCalculator extends PieceMoveCalculator{
    public OneMoveCalculator(ChessPiece.PieceType type) {
        switch(type){
            case KING -> directions = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{-1,0},{0,1},{0,-1}};
            case KNIGHT -> directions = new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{-1,2},{1,-2},{-1,-2}};
        }
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition postion) {
        HashSet<ChessMove> options = new HashSet<>();
        for (int i = 0; i < directions.length; i++){
            ChessPosition newPos = postion;
            newPos = new ChessPosition(newPos.getRow()+directions[i][0], newPos.getColumn()+directions[i][1]);
            if(allowed(board,postion,newPos)){
                ChessMove newMove = new ChessMove(postion,newPos,null);
                options.add(newMove);
            }
        }
        return options;
    }
}
