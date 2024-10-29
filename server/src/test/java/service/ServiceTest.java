package service;

import dataAccess.*;
import model.AuthData;
import model.LoginRequest;
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
    public void loginTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        s.register(user);
        LoginRequest u = new LoginRequest(user.username(),user.password());
        AuthData r = s.login(u);
        Assertions.assertEquals(user.username(),r.username());
        Assertions.assertNotNull(r.authToken());
    }

    @Test
    public void loginFailTest() throws Exception{
        var user = new UserData("Name", "pass", "email");
        s.register(user);
        LoginRequest u = new LoginRequest(user.username(),"");
        Assertions.assertThrows(Exception.class, () -> s.login(u));
        LoginRequest u2 = new LoginRequest(user.username(),"wrong");
        Assertions.assertThrows(Exception.class, () -> s.login(u2));
        LoginRequest u3 = new LoginRequest("Wrong", user.password());
        Assertions.assertThrows(Exception.class, () -> s.login(u3));
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
    public void logoutTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        s.register(user);
        LoginRequest u = new LoginRequest(user.username(),user.password());
        AuthData r = s.login(u);
        Assertions.assertDoesNotThrow(() -> s.logout(r.authToken()));
    }

    @Test
    public void logoutFailTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        s.register(user);
        LoginRequest u = new LoginRequest(user.username(),user.password());
        AuthData r = s.login(u);
        Assertions.assertThrows(Exception.class, () -> s.logout("r.authToken()"));
    }

    @Test
    public void clearTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
        s.clearALL();
        //s.register(user);
        //Assertions.assertEquals(user.username(),r.username());
        //Assertions.assertNotNull(r.authToken());
    }

}
