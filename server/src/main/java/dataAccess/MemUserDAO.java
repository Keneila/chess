package dataAccess;

import model.UserData;

public class MemUserDAO implements UserDAO{
    @Override
    public UserData findUser(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public void clearTable() throws DataAccessException {

    }
}
