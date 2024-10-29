package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceTest {
    @Test
    public void registerTest() throws Exception {
        UserDAO userDAO = new MemUserDAO();
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        Service s = new Service(userDAO,authDAO,gameDAO);
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
        Assertions.assertEquals(user.username(),r.username());
        Assertions.assertNotNull(r.authToken());
    }

    @Test
    public void registerFailTest(){
        UserDAO userDAO = new MemUserDAO();
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        Service s = new Service(userDAO,authDAO,gameDAO);
        var user = new UserData("", "pass", "email");
        Assertions.assertThrows(Exception.class, () -> s.register(user));
        var user2 = new UserData("ertt", "", "email");
        Assertions.assertThrows(Exception.class, () -> s.register(user2));
    }

    @Test
    public void clearTest() throws Exception {
        UserDAO userDAO = new MemUserDAO();
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        Service s = new Service(userDAO,authDAO,gameDAO);
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
    }

}
