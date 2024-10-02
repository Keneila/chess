package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class LineMoveCalculator extends PieceMoveCalculator{
    public LineMoveCalculator(ChessPiece.PieceType type) {
        switch(type){
            case QUEEN -> directions = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{-1,0},{0,1},{0,-1}};
            case BISHOP -> directions = new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}};
            case ROOK -> directions = new int[][]{{1,0},{-1,0},{0,1},{0,-1}};
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
            while(allowed(board,postion,newPos) && board.getPiece(newPos) == null) {
                newPos = new ChessPosition(newPos.getRow()+directions[i][0], newPos.getColumn()+directions[i][1]);
                if(allowed(board,postion,newPos)){
                    ChessMove newMove = new ChessMove(postion,newPos,null);
                    options.add(newMove);
                }
            }
        }
        return options;
    }
    }
