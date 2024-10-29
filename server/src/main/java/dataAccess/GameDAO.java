package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface GameDAO {
    public void clearTable() throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void createGame(GameData game) throws DataAccessException;
    public GameData findGame(String gameID) throws DataAccessException;
    public void deleteGame(String gameID) throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException;
}
