package service;

import model.AuthData;
import model.UserData;

public class Service {

    public AuthData register(UserData user) throws ErrorMessage {
        if (user.username() != null && !user.username().isEmpty()) {
            return new AuthData("ergerged", user.username());
        }
        throw new ErrorMessage(400, "Error: bad request");
    }

    public void clearALL() throws ErrorMessage {
    }
}
