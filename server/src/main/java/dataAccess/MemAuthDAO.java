package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemAuthDAO implements AuthDAO {
    private int nextId = 1;
    final private HashMap<String, AuthData> tokens = new HashMap<>();
    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        tokens.put(auth.authToken(),auth);
    }

    @Override
    public AuthData findAuth(String token) throws DataAccessException {
        return tokens.get(token);
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        tokens.remove(token);
    }

    @Override
    public void clearTable() throws DataAccessException {
        tokens.clear();
    }
}
