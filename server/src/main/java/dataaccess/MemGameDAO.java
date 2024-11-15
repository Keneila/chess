package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemGameDAO implements GameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();
    @Override
    public void clearTable() throws DataAccessException {
        games.clear();
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        game = new GameData(nextId++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.remove(game.gameID());
        games.put(game.gameID(), game);
    }
}
