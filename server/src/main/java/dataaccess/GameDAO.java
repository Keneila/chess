package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    /**
     * Clear Entire Game Table
     * @return nothing
     */
    public void clearTable() throws DataAccessException;
    /**
     * List Entire Game Table
     * @return List of Games
     */
    public Collection<GameData> listGames() throws DataAccessException;
    /**
     * Adds new Game to Database
     * @param game
     * @return GameData
     */
    public GameData createGame(GameData game) throws DataAccessException;
    /**
     * Finds Game by ID
     * @param gameID
     * @return GameData
     */
    public GameData findGame(int gameID) throws DataAccessException;
    //public void deleteGame(int gameID) throws DataAccessException;
    /**
     * Adds Any Chnages to Existing Game
     * @param game
     * @return nothing
     */
    public void updateGame(GameData game) throws DataAccessException;
}
