package chess;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.*;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return List.of();
    }
    private boolean isInBounds (ChessBoard board, ChessPosition position){
        int x = position.getColumn();
        int y = position.getRow();
        if (x < 9 && x>= 0){
            if (y < 9 && y>= 0){
                return true;
            }
        }
        return false;
    }
}
