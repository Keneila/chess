package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static chess.ChessPiece.PieceType.*;

public class ChessTeam {
    private ChessPosition kingPos;
    private HashSet<ChessPosition> pieces;

    public ChessTeam() {
        this.pieces = new HashSet<>();
    }

    public void setKingPos(ChessPosition kingPos) {
        this.kingPos = kingPos;
    }

    public ChessPosition getKingPos() {
        return kingPos;
    }

    public HashSet<ChessPosition> getPieces() {
        return pieces;
    }

    public void addToPieces(ChessPosition pos){
        pieces.add(pos);
    }
    public void removeFromPieces(ChessPosition pos){
        pieces.remove(pos);
    }

    public HashSet<ChessPosition> getAllMoves(ChessBoard board){
        HashSet<ChessPosition> moves = new HashSet<>();
        for(ChessPosition piece : pieces){
            var pieceMoves = board.getPiece(piece).pieceMoves(board,piece);
            for (var move : pieceMoves){
                moves.add(move.getEndPosition());
            }
        }
        return moves;
    }
    public boolean canMoveTo(ChessPosition position, ChessBoard board){
        boolean can = false;
        HashSet<ChessPosition> moves = getAllMoves(board);
        if (moves.contains(position)){
            can = true;
        }
        return can;
    }
}
