package dataaccess;

import model.AuthData;

public interface AuthDAO {
    /**
     * Login User by creating and saving authtoken
     * @return nothing
     */
    public void createAuth(AuthData auth) throws DataAccessException;
    /**
     * Determine if authorized token by searching for it in database
     * @return AuthData - token and username
     */
    public AuthData findAuth(String token) throws DataAccessException;
    /**
     * Logout User by deleting Authtoken
     * @return nothing
     */
    public void deleteAuth(String token) throws DataAccessException;
    /**
     * Clear Entire Auth Table
     * @return nothing
     */
    public void clearTable() throws DataAccessException;


}
