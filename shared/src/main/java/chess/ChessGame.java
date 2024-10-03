package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;


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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
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

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            ChessPosition king = board.getTeam(teamColor).getKingPos();
            TeamColor enemyColor = TeamColor.BLACK;
            if (teamColor == TeamColor.BLACK){
                enemyColor = TeamColor.WHITE;
            }
            ChessTeam enemy = board.getTeam(enemyColor);
            var moves = enemy.getAllMovesToSpot(king, board);
            var openings = spotsToBlock(moves);
            for (ChessPosition pos : openings){
                if (board.getTeam(teamColor).NotKingCanMoveTo(pos, board)){
                    return false;
                }
            }
            return true;

        } else {
            return false;
        }
    }

    public HashSet<ChessPosition> spotsToBlock(HashSet<ChessMove> attacks){
        HashSet<ChessPosition> openings = new HashSet<>();
        for (var move : attacks){
            if (board.getPiece(move.getStartPosition()).getPieceType() != ChessPiece.PieceType.KNIGHT) {
                int x = move.getStartPosition().getRow() - move.getEndPosition().getRow();
                int y = move.getStartPosition().getColumn() - move.getEndPosition().getColumn();
                int row;
                int col;
                if (x == 0){
                    row = 0;
                } else {
                    row = abs(x) / x;
                }
                if (y == 0){
                    col = 0;
                } else {
                    col = abs(y)/y;
                    }
                if (col != 0 && row != 0) {
                    for (int i = move.getStartPosition().getRow(); i != move.getEndPosition().getRow(); i = i + row) {
                        for (int j = move.getStartPosition().getColumn(); j != move.getEndPosition().getColumn(); j = j + col) {
                            ChessPosition spot = new ChessPosition(i, j);
                            openings.add(spot);
                        }
                    }
                } else if (col != 0){
                    for (int j = move.getStartPosition().getColumn(); j != move.getEndPosition().getColumn(); j = j + col) {
                        ChessPosition spot = new ChessPosition(move.getStartPosition().getRow(), j);
                        openings.add(spot);
                    }
                } else if (row != 0){
                    for (int i = move.getStartPosition().getRow(); i != move.getEndPosition().getRow(); i = i + row) {
                        ChessPosition spot = new ChessPosition(i, move.getEndPosition().getColumn());
                        openings.add(spot);
                    }
                }


            }
        }
        return openings;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getBoard(), chessGame.getBoard()) && getTeamTurn() == chessGame.getTeamTurn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), getTeamTurn());
    }

}
