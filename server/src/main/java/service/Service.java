package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
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

    public AuthData login(LoginRequest user) throws ErrorMessage {
        if (user.password() != null && user.username() != null && !user.username().isEmpty() && !user.password().isEmpty()) {
            try {
                UserData data = userDAO.findUser(user.username());
                if(data == null){
                    throw new ErrorMessage(401, "Error: unauthorized");
                } else {
                    if (Objects.equals(data.password(), user.password())) {
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
                if(Objects.equals(playerColor, "BLACK")){
                    if(game.blackUsername() != null){
                        throw new ErrorMessage(403, "Error: already taken");
                    }
                    updated = new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(),game.game());
                } else if (Objects.equals(playerColor, "WHITE")){
                    if(game.whiteUsername() != null){
                        throw new ErrorMessage(403, "Error: already taken");
                    }
                    updated = new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(),game.game());
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
