package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;



/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private Boolean done = false;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece p = board.getPiece(startPosition);
        if(p != null) {
            Collection<ChessMove> allMoves = p.pieceMoves(board, startPosition);
            HashSet<ChessMove> valid = new HashSet<>();
            for (var move : allMoves) {
                ChessBoard testBoard = new ChessBoard(board);
                testBoard.addPiece(startPosition, null);
                if (move.getPromotionPiece() == null) {
                    testBoard.addPiece(move.getEndPosition(), p);
                } else {
                    ChessPiece newP = new ChessPiece(p.getTeamColor(), move.getPromotionPiece());
                    testBoard.addPiece(move.getEndPosition(), newP);
                }
                if (!isInCheck(p.getTeamColor(), testBoard)) {
                    valid.add(move);
                }
                testBoard.addPiece(move.getEndPosition(), null);
                testBoard.addPiece(move.getStartPosition(), p);
            }
            return valid;
        } else {
            return null;
        }

    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece p = board.getPiece(move.getStartPosition());
        if (p == null){
            throw new InvalidMoveException();
        }
        if (p.getTeamColor() != teamTurn){
            throw new InvalidMoveException();
        }
        var valid = validMoves(move.getStartPosition());
        if (valid == null){
            throw new InvalidMoveException();
        }

        if (valid.contains(move)){
            board.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() == null) {
                board.addPiece(move.getEndPosition(), p);
            } else {
                ChessPiece newP = new ChessPiece(p.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), newP);
            }
            switch (teamTurn){
                case WHITE -> teamTurn = TeamColor.BLACK;
                case BLACK -> teamTurn = TeamColor.WHITE;
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = board.getTeam(teamColor).getKingPos();
        TeamColor enemyColor = TeamColor.BLACK;
        if (teamColor == TeamColor.BLACK){
            enemyColor = TeamColor.WHITE;
        }
        ChessTeam enemy = board.getTeam(enemyColor);
        return enemy.canMoveTo(king,board);
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard nBoard) {
        ChessPosition king = nBoard.getTeam(teamColor).getKingPos();
        TeamColor enemyColor = TeamColor.BLACK;
        if (teamColor == TeamColor.BLACK){
            enemyColor = TeamColor.WHITE;
        }
        ChessTeam enemy = nBoard.getTeam(enemyColor);
        return enemy.canMoveTo(king,nBoard);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            done = isIn(teamColor);
            return isIn(teamColor);
        } else {
            return false;
        }
    }
    public boolean isIn(TeamColor teamColor){
        ChessTeam team = board.getTeam(teamColor);
        for (var pos : team.getPieces()) {
            if (!validMoves(pos).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            done = isIn(teamColor);
            return isIn(teamColor);
        } else {
            return false;
        }
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getBoard(), chessGame.getBoard()) && getTeamTurn() == chessGame.getTeamTurn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), getTeamTurn());
    }

}
