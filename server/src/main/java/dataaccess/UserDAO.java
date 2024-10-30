package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData findUser(String userName) throws DataAccessException;
    public void createUser(UserData user) throws DataAccessException;
    public void clearTable() throws DataAccessException;



}
