package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class ServiceTest {
    private static Service s;
    GameDAO gameDAO;
    @BeforeEach
    public void setUp() throws DataAccessException {
        UserDAO userDAO = new SQLUserDAO();
        userDAO.clearTable();
        gameDAO = new MemGameDAO();
        AuthDAO authDAO = new SQLAuthDAO();
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
    public void listTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        CreateGameRequest req = new CreateGameRequest("GameName", auth.authToken());
        s.createGame(req);
        CreateGameRequest req2 = new CreateGameRequest("GameName34", auth.authToken());
        s.createGame(req2);
        Collection<GameData> games = s.listGames(auth.authToken());
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void listFailTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        CreateGameRequest req = new CreateGameRequest("GameName", auth.authToken());
        s.createGame(req);
        CreateGameRequest req2 = new CreateGameRequest("GameName34", auth.authToken());
        s.createGame(req2);
        Assertions.assertThrows(Exception.class, () ->  s.listGames("auth.authToken()"));
    }

    @Test
    public void createTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        CreateGameRequest req = new CreateGameRequest("GameName", auth.authToken());
        Assertions.assertNotNull(gameDAO.findGame(s.createGame(req)));
    }

    @Test
    public void createFailTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        CreateGameRequest req = new CreateGameRequest("GameName", "auth.authToken()");
        Assertions.assertThrows(Exception.class, () ->  s.createGame(req));
        CreateGameRequest req2 = new CreateGameRequest("", auth.authToken());
        Assertions.assertThrows(Exception.class, () ->  s.createGame(req2));
    }

    @Test
    public void joinTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        CreateGameRequest req = new CreateGameRequest("GameName", auth.authToken());
        int id = s.createGame(req);
        Assertions.assertDoesNotThrow(() -> s.joinGame(auth.authToken(),"BLACK",id));
        Assertions.assertEquals(auth.username(), gameDAO.findGame(id).blackUsername());
    }

    @Test
    public void joinFailTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        CreateGameRequest req = new CreateGameRequest("GameName", auth.authToken());
        int id = s.createGame(req);
        Assertions.assertThrows(Exception.class,() -> s.joinGame("auth.authToken()","BLACK",id));
        Assertions.assertThrows(Exception.class,() -> s.joinGame(auth.authToken(),"BACK",id));
        Assertions.assertThrows(Exception.class,() -> s.joinGame(auth.authToken(),"BLACK",3478));
    }

    @Test
    public void clearTest() throws Exception {
        var user = new UserData("Name", "pass", "email");
        AuthData auth = s.register(user);
        var user2 = new UserData("Name4", "pa3ss", "emai5l");
        AuthData auth2 = s.register(user2);
        CreateGameRequest req = new CreateGameRequest("GameName", auth.authToken());
        s.createGame(req);
        CreateGameRequest req2 = new CreateGameRequest("GameName34", auth2.authToken());
        s.createGame(req2);
        s.clearALL();
        Assertions.assertThrows(Exception.class,() -> s.listGames(auth.authToken()));
        Assertions.assertThrows(Exception.class,() -> s.login(new LoginRequest(user.username(),user.password())));
    }

}
