package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemUserDAO implements UserDAO{

    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData findUser(String userName) throws DataAccessException {
        return users.get(userName);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.put(user.username(),user);
    }

    @Override
    public void clearTable() throws DataAccessException {
        users.clear();
    }
}
