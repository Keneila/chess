package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceTest {
    private static Service s;
    @BeforeEach
    public void setUp(){
        UserDAO userDAO = new MemUserDAO();
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        s = new Service(userDAO,authDAO,gameDAO);
    }
    @Test
    public void registerTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
        Assertions.assertEquals(user.username(),r.username());
        Assertions.assertNotNull(r.authToken());
    }

    @Test
    public void registerFailTest(){
        var user = new UserData("", "pass", "email");
        Assertions.assertThrows(Exception.class, () -> s.register(user));
        var user2 = new UserData("ertt", "", "email");
        Assertions.assertThrows(Exception.class, () -> s.register(user2));
    }

    @Test
    public void clearTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
        s.clearALL();
        s.register(user);
        Assertions.assertEquals(user.username(),r.username());
        Assertions.assertNotNull(r.authToken());
    }

}
