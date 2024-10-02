package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PawnMoveCalculator extends PieceMoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition postion) {
        HashSet<ChessMove> options = new HashSet<>();
        int d = 0;
        int start = 0;
        switch (board.getPiece(postion).getTeamColor()){
            case WHITE -> {
                d = 1;
                start = 2;
            }
            case BLACK -> {
                d = -1;
                start = 7;
            }
        }
        directions = new int[][]{{d,0},{d,1},{d,-1},{d*2,0}};
        ChessPosition newPos = postion;
        for (int i = 0; i < directions.length; i++){
            newPos = new ChessPosition(postion.getRow()+directions[i][0], postion.getColumn()+directions[i][1]);
            if(pawnAllowed(board,postion,newPos, start,i)){
                if (newPos.getRow() == 1 || newPos.getRow() == 8){
                    ChessMove newMove = new ChessMove(postion,newPos, ChessPiece.PieceType.ROOK);
                    options.add(newMove);
                    newMove = new ChessMove(postion,newPos, ChessPiece.PieceType.QUEEN);
                    options.add(newMove);
                    newMove = new ChessMove(postion,newPos, ChessPiece.PieceType.BISHOP);
                    options.add(newMove);
                    newMove = new ChessMove(postion,newPos, ChessPiece.PieceType.KNIGHT);
                    options.add(newMove);
                } else {
                    ChessMove newMove = new ChessMove(postion, newPos, null);
                    options.add(newMove);
                }
            }
        }
        return options;
    }

    private boolean pawnAllowed(ChessBoard board, ChessPosition postion, ChessPosition newPos, int start, int i) {
        boolean allowed = true;
        if(!allowed(board,postion,newPos)){
            return false;
        }
        if (i == 0) {
            if (board.getPiece(newPos) != null) {
                return false;
            }
        }
        if(i == 3) {
            if (postion.getRow() != start) {
                return false;
            }
            if (board.getPiece(newPos) != null) {
                return false;
            }
            if (board.getPiece(new ChessPosition(postion.getRow() + directions[0][0], postion.getColumn() + directions[0][1])) != null) {
                return false;
            }
        }
        if (i==1 || i == 2) {
            if (board.getPiece(newPos) == null) {
                return false;
            }
        }

        return allowed;
        }

    }

