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

    public boolean canMoveTo(ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = getAllMovesToSpot(position, board);
        return !moves.isEmpty();
    }
    public boolean NotKingCanMoveTo(ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = getAllMovesToSpot(position, board);
        if (!moves.isEmpty()){
            for (var move : moves){
                if (board.getPiece(move.getStartPosition()).getPieceType() != KING){
                    return true;
                }
            }
        }
        return false;
    }

    public HashSet<ChessMove> getAllMovesToSpot(ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = new HashSet<>();
        for (var piece : pieces){
            var pieceMoves = board.getPiece(piece).pieceMoves(board,piece);
            for (var move : pieceMoves){
                if(move.getEndPosition() == position){
                    moves.add(move);
                }
            }
        }
        return moves;
    }



}
