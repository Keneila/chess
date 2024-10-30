package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void clearTable() throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public GameData createGame(GameData game) throws DataAccessException;
    public GameData findGame(int gameID) throws DataAccessException;
    //public void deleteGame(int gameID) throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException;
}
