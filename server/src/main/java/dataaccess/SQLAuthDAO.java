package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authtoken = rs.getString("authtoken");
        var username = rs.getString("username");
        return new AuthData(authtoken, username);
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO authtokens (authtoken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
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
        var statement = "TRUNCATE authtokens";
        executeUpdate(statement);
    }
}
