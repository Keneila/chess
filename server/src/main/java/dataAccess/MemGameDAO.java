package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class MemGameDAO implements GameDAO {
    @Override
    public void clearTable() throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData findGame(String gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteGame(String gameID) throws DataAccessException {

    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }
}
