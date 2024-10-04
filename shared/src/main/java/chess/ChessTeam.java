package chess;

import java.util.HashSet;


public class ChessTeam {
    private ChessPosition kingPos = null;
    private HashSet<ChessPosition> pieces;

    public ChessTeam() {
        this.pieces = new HashSet<>();
    }

    public ChessTeam(ChessTeam newT) {
        if (newT.getKingPos() != null) {
            this.kingPos = new ChessPosition(newT.getKingPos().getRow(), newT.getKingPos().getColumn());
        }
        this.pieces = new HashSet<>();
        for (var p : newT.getPieces()){
            pieces.add(p);
        }
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

    public HashSet<ChessMove> getAllMovesToSpot(ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = new HashSet<>();
        for (var piece : pieces){
            var pieceMoves = board.getPiece(piece).pieceMoves(board,piece);
            for (var move : pieceMoves){
                if(move.getEndPosition().equals(position)){
                    moves.add(move);
                }
            }
        }
        return moves;
    }




}
