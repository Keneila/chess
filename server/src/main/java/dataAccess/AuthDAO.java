package dataAccess;

import chess.AuthData;

import java.util.Collection;

public interface AuthDAO {
    void insert (AuthData auth) throws DataAccessException;
    Collection<AuthData> listtokens() throws DataAccessException;
    AuthData findByUserName(String username) throws DataAccessException;
    AuthData findByToken(String token) throws DataAccessException;
    void delete(String token) throws DataAccessException;
    void clear() throws DataAccessException;

}
