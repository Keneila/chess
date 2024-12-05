package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.UUID;

public class Service {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public Service(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /**
     * Registers User
     * @param user
     * @return AuthData for the useable token
     */
    public AuthData register(UserData user) throws ErrorMessage {
        if (user.password() != null && user.email() != null && user.username()
                != null && !user.username().isEmpty() && !user.email().isEmpty() &&
                !user.password().isEmpty()) {
            try {
                if(userDAO.findUser(user.username()) == null){
                    userDAO.createUser(user);
                    String token = generateToken();
                    AuthData auth = new AuthData(token, user.username());
                    authDAO.createAuth(auth);
                    return auth;
                } else {
                    throw new ErrorMessage(403, "Error: already taken");
                }
            } catch(DataAccessException e){
                throw new ErrorMessage(500, "Error: Something went wrong with DAOs");
            }
        }
        throw new ErrorMessage(400, "Error: bad request");
    }

    /**
     * Clears each DAO's Table
     * @return nothing. If something goes wrong it throws an Error
     */
    public void clearALL() throws ErrorMessage {
        try {
            userDAO.clearTable();
            gameDAO.clearTable();
            authDAO.clearTable();
        } catch (DataAccessException e){
            throw new ErrorMessage(500, "Error: Data Access Error For Clear");
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public  String getUsername(String token) {
        try {
            return authDAO.findAuth(token).username();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Logs User In by creating an Authtoken
     * and checking the username and password ro see it they exist.
     * @param user
     * @return AuthData for the useable token
     */
    public AuthData login(LoginRequest user) throws ErrorMessage {
        if (user.password() != null && user.username() != null && !user.username().isEmpty() && !user.password().isEmpty()) {
            try {
                UserData data = userDAO.findUser(user.username());
                if(data == null){
                    throw new ErrorMessage(401, "Error: unauthorized");
                } else {
                    if ( BCrypt.checkpw(user.password(), data.password())) {
                        AuthData auth = new AuthData(generateToken(), user.username());
                        authDAO.createAuth(auth);
                        return auth;
                    }
                    throw new ErrorMessage(401, "Error: unauthorized");
                }
            } catch (DataAccessException e){
                throw new ErrorMessage(500, "Error: Data Access Error on Login");
                }
        } else {
            throw new ErrorMessage(401, "Error: unauthorized");
        }

    }

    /**
     * Logs Out the User by deleting the authtoken
     * @param token
     * @return nothing just an error if messed up
     */
    public void logout(String token) throws ErrorMessage{
        if(token != null && !token.isEmpty()){
            try {
                if(authDAO.findAuth(token) == null){
                    throw new ErrorMessage(401, "Error: unauthorized");
                }
                authDAO.deleteAuth(token);
            } catch (DataAccessException e) {
                throw new ErrorMessage(500, "Error: Data Access Error on Logout");
            }
        } else {
            throw new ErrorMessage(401, "Error: unauthorized");
        }
    }

    /**
     * Creates A New Game and Adds to DataBase
     * @param req has the gameName and Authtoken information
     * @return gameID or an Error Message
     */
    public int createGame(CreateGameRequest req) throws ErrorMessage{
        if(req.gameName() == null || req.gameName().isEmpty()){
            throw new ErrorMessage(400, "Error: bad request");
        }
        if(req.token() != null && !req.token().isEmpty()){
            try {
                if(authDAO.findAuth(req.token()) == null){
                    throw new ErrorMessage(401, "Error: unauthorized");
                }
                ChessGame newGame = new ChessGame();
                GameData game = new GameData(0,null,null, req.gameName(), newGame);
                GameData created = gameDAO.createGame(game);
                return created.gameID();

            } catch (DataAccessException e) {
                throw new ErrorMessage(500, "Error: Data Access Error on Logout");
            }
        } else {
            throw new ErrorMessage(401, "Error: unauthorized");
        }
    }

    /**
     * Lists All Games in Database
     * @param token to authorize
     * @return The List of Games as a Collection
     */
    public Collection<GameData> listGames(String token) throws ErrorMessage{
        if(token != null && !token.isEmpty()){
            try {
                if(authDAO.findAuth(token) == null){
                    throw new ErrorMessage(401, "Error: unauthorized");
                }
                return gameDAO.listGames();
            } catch (DataAccessException e) {
                throw new ErrorMessage(500, "Error: Data Access Error on Logout");
            }
        } else {
            throw new ErrorMessage(401, "Error: unauthorized");
        }
    }

    /**
     * Updates Game to Include User as a Black or White Player
     * @param token
     * @param gameID
     * @param playerColor
     * @return nothing except a possible error message
     */
    public void joinGame(String token, String playerColor, int gameID) throws ErrorMessage {
        if(token != null && !token.isEmpty()){
            try {
                AuthData auth = authDAO.findAuth(token);
                GameData game = gameDAO.findGame(gameID);
                if(auth == null){
                    throw new ErrorMessage(401, "Error: unauthorized");
                }
                if(game == null){
                    throw new ErrorMessage(400, "Error: bad request");
                }
                GameData updated;

                if("black".equalsIgnoreCase(playerColor)){
                    if(game.blackUsername() != null){
                        throw new ErrorMessage(403, "Error: already taken");
                    }
                    updated = new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(),game.game());
                } else if ("white".equalsIgnoreCase(playerColor)){
                    if(game.whiteUsername() != null){
                        throw new ErrorMessage(403, "Error: already taken");
                    }
                    updated = new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(),game.game());
                } else if ("obs".equalsIgnoreCase(playerColor)){
                    updated = new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(),game.game());
                } else {
                    throw new ErrorMessage(400, "Error: bad request");
                }
                gameDAO.updateGame(updated);
            } catch (DataAccessException e) {
                throw new ErrorMessage(500, "Error: Data Access Error on Logout");
            }
        } else {
            throw new ErrorMessage(401, "Error: unauthorized");
        }
    }
}
