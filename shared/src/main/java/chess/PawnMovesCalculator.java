package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> options = new HashSet<>();
        int d = 0;
        int start = -1;
        switch (board.getPiece(position).getTeamColor()){
            case BLACK -> {
                d= -1;
                start = 7;
            }
            case WHITE -> {
                d = 1;
                start = 2;
            }
        }
        int[][] directions = {{d,0}, {d, 1},{d,-1}, {(d*2),0}};
        for (int i = 0; i < directions.length; i++) {
            ChessPosition newPos = new ChessPosition((position.getRow() + directions[i][0]), (position.getColumn() + directions[i][1]));
            boolean allowed = true;
            if (i != 0){
                if (i == 3){
                    if (position.getRow() != start){
                        allowed = false;
                    }
                    if (board.getPiece(new ChessPosition((position.getRow() + directions[0][0]), (position.getColumn() + directions[0][1]))) != null) {
                        allowed = false;
                    }
                } else {
                    if (!checkIfNotAllowed(board, position, newPos)) {
                        if (board.getPiece(newPos) == null) {
                            allowed = false;
                        }
                    }
                }
            }
            if (i == 0){
                if (board.getPiece(newPos) != null) {
                    allowed = false;
                }
            }
            if (allowed) {
                if (!checkIfNotAllowed(board, position, newPos)) {
                    if (i != 3) {
                        if (newPos.getRow() == 1 || newPos.getRow() == 8) {
                            ChessMove newMove = new ChessMove(position, newPos, ChessPiece.PieceType.QUEEN);
                            options.add(newMove);
                            newMove = new ChessMove(position, newPos, ChessPiece.PieceType.ROOK);
                            options.add(newMove);
                            newMove = new ChessMove(position, newPos, ChessPiece.PieceType.BISHOP);
                            options.add(newMove);
                            newMove = new ChessMove(position, newPos, ChessPiece.PieceType.KNIGHT);
                            options.add(newMove);

                        } else {
                            ChessMove newMove = new ChessMove(position, newPos, null);
                            options.add(newMove);
                        }
                    } else {
                        if (board.getPiece(newPos) == null){
                            ChessMove newMove = new ChessMove(position, newPos, null);
                            options.add(newMove);
                        }
                    }
                }
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
