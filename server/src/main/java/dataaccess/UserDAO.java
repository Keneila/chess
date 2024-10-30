package dataaccess;

import model.UserData;

public interface UserDAO {
    /**
     * Finds User by Username
     * @param userName
     * @return UserData containing username, password, email
     */
    public UserData findUser(String userName) throws DataAccessException;
    /**
     * Adds new User to Database
     * @param user
     * @return nothing
     */
    public void createUser(UserData user) throws DataAccessException;
    /**
     * Clear Entire User Table
     * @return nothing
     */
    public void clearTable() throws DataAccessException;



}
