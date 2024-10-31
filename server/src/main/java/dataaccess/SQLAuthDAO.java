package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authtokens (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `authtoken` varchar(256) NOT NULL,
                          `username` varchar(256) NOT NULL,
                          PRIMARY KEY (`id`),
                          INDEX(authtoken)
                        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }

    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData findAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clearTable() throws DataAccessException {

    }
}
