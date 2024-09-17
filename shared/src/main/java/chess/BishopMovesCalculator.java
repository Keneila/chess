package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator{
    public BishopMovesCalculator() {
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> options = new ArrayList<>();
        return options;
    }

    private boolean checkIfHit (ChessBoard board, ChessPosition position){
        int x = position.getColumn();
        int y = position.getRow();
        if (x < 8 && x> 0){
            if (y < 8 && y> 0){
                return false;
            }
        }
        return true;

    }
}
