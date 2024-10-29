package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO {
    public void createAuth(AuthData auth) throws DataAccessException;
    public AuthData findAuth(String token) throws DataAccessException;
    public void deleteAuth(String token) throws DataAccessException;
    public void clearTable() throws DataAccessException;


}
