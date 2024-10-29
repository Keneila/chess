package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface UserDAO {
    public UserData findUser(String userName) throws DataAccessException;
    public void createUser(UserData user) throws DataAccessException;
    public void clearTable() throws DataAccessException;



}
