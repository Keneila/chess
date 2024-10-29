package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

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
}
