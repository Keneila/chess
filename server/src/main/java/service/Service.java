package service;

import model.AuthData;
import model.UserData;

public class Service {

    public AuthData register(UserData user) throws Exception {
        if (user.username() != null && user.username().length() > 0) {
            return new AuthData("ergerged", user.username());
        }
        throw new Exception("bad param");
    }
}
