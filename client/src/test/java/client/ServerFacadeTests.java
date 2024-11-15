package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ErrorMessage;
import ui.State;
import ui.server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    public void toDo() throws ErrorMessage {
        facade.deleteDB();
    }
    @Test
    public void registerTest() throws ErrorMessage {
        Assertions.assertDoesNotThrow(()-> facade.register(new UserData("u","p","e")));
        Assertions.assertDoesNotThrow(()-> facade.register(new UserData("y","f","r")));
    }
    @Test
    public void registerFailTest() throws ErrorMessage {
        Assertions.assertDoesNotThrow(()-> facade.register(new UserData("u","p","e")));
        Assertions.assertThrows(Exception.class, ()-> facade.register(new UserData("u","p","e")));
        Assertions.assertThrows(Exception.class, ()-> facade.register(new UserData(null,"p","e")));
    }
    @Test
    public void loginTest() throws ErrorMessage {
        Assertions.assertDoesNotThrow(()-> facade.register(new UserData("u","p","e")));
        Assertions.assertDoesNotThrow(()-> facade.login(new LoginRequest("u","p")));
    }
    @Test
    public void loginFailTest() throws ErrorMessage {
        Assertions.assertDoesNotThrow(()-> facade.register(new UserData("u","p","e")));
        Assertions.assertThrows(Exception.class, ()-> facade.login(new LoginRequest("u","e")));
        Assertions.assertThrows(Exception.class, ()-> facade.login(new LoginRequest("e","p")));
        Assertions.assertThrows(Exception.class, ()-> facade.login(new LoginRequest(null,"p")));
    }
    @Test
    public void logoutTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertDoesNotThrow(()-> facade.logout(auth.authToken()));
    }
    @Test
    public void logoutFailTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertThrows(Exception.class, ()-> facade.logout("g"));
        Assertions.assertDoesNotThrow(()-> facade.logout(auth.authToken()));
        Assertions.assertThrows(Exception.class, ()-> facade.logout(auth.authToken()));
    }
    @Test
    public void createGameTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name",auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name2",auth.authToken()));
    }
    @Test
    public void createGameFailTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertThrows(Exception.class, ()-> facade.createGame("Name","auth.authToken()"));
        Assertions.assertThrows(Exception.class, ()-> facade.createGame(null,auth.authToken()));
    }
    @Test
    public void listGameTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertEquals(0, facade.listGames(auth.authToken()).size());
        Assertions.assertDoesNotThrow(()-> facade.listGames(auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name",auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name2",auth.authToken()));
        Assertions.assertEquals(2, facade.listGames(auth.authToken()).size());
    }
    @Test
    public void listGamesFailTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertThrows(Exception.class, ()-> facade.listGames("auth.authToken()"));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name",auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name2",auth.authToken()));
        Assertions.assertThrows(Exception.class, ()-> facade.listGames(null));
    }
    @Test
    public void joinGameTest() throws ErrorMessage {
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name",auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name2",auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.joinGame(auth.authToken(), "white", 1));
    }
    @Test
    public void joinGameFailTest() throws ErrorMessage {
        Assertions.assertThrows(Exception.class, ()-> facade.joinGame("", "white", 1));
        AuthData auth = facade.register(new UserData("u","p","e"));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name",auth.authToken()));
        Assertions.assertDoesNotThrow(()-> facade.createGame("Name2",auth.authToken()));
        Assertions.assertThrows(Exception.class, ()-> facade.joinGame("", "white", 1));
        Assertions.assertThrows(Exception.class, ()-> facade.joinGame(auth.authToken(), "white", 19));
        Assertions.assertDoesNotThrow(()-> facade.joinGame(auth.authToken(), "white", 1));
        Assertions.assertThrows(Exception.class, ()-> facade.joinGame(auth.authToken(), "white", 1));

    }



}
