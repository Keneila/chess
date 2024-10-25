package service;

import model.AuthData;
import model.UserData;

public class Service {

    public AuthData register(UserData user) {
        return new AuthData("ergerged", user.username());

    }
}
