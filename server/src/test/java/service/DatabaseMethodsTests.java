package service;
import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class DatabaseMethodsTests {
    private UserDAO user = new SQLUserDAO();
    private UserData user1 = new UserData("user","pass","email");
    private UserData user2 = new UserData("use2r","pa2ss","ema2il");
    private UserData userfail1 = new UserData(null,"pa2ss","ema2il");
    private UserData userfail2 = new UserData("D",null,"ema2il");
    private AuthDAO auth = new SQLAuthDAO();
    private AuthData authtoken1 = new AuthData("egeebncbxe", "user");
    private AuthData authtoken2 = new AuthData("egergre", "user2");
    private AuthData authtokenfail = new AuthData(null, "user");
    private AuthData authtokenfail2 = new AuthData("egerg9675re", null);

    public DatabaseMethodsTests() throws DataAccessException {
    }

    @Test
    public void createUser() throws Exception{
        user.createUser(user1);
        Assertions.assertEquals(user1, user.findUser(user1.username()));
        user.clearTable();
    }

    @Test
    public void deleteUserALL() throws Exception{
        user.createUser(user1);
        user.createUser(user2);
        user.clearTable();
        Assertions.assertNull(user.findUser(user1.username()));
        Assertions.assertNull(user.findUser(user2.username()));
    }

    @Test
    public void findUser() throws Exception{
        user.createUser(user1);
        user.createUser(user2);
        Assertions.assertEquals(user1, user.findUser(user1.username()));
        Assertions.assertEquals(user2, user.findUser(user2.username()));
        user.clearTable();
    }

    @Test
    public void createUserFail() throws Exception{
        UserDAO user = new SQLUserDAO();
        Assertions.assertThrows(Exception.class, () ->  user.createUser(userfail2));
        Assertions.assertThrows(Exception.class, () ->  user.createUser(userfail1));
        user.clearTable();
    }

    @Test
    public void findUserFail() throws Exception{
        user.createUser(user1);
        user.createUser(user2);
        Assertions.assertNull(user.findUser("rth"));
        user.clearTable();
    }

    @Test
    public void createAuth() throws Exception{
        auth.createAuth(authtoken1);
        Assertions.assertEquals(authtoken1, auth.findAuth(authtoken1.authToken()));
        auth.createAuth(authtoken2);
        Assertions.assertEquals(authtoken2, auth.findAuth(authtoken2.authToken()));
        auth.clearTable();
    }

    @Test
    public void deleteAuthALL() throws Exception{
        auth.createAuth(authtoken1);
        auth.createAuth(authtoken2);
        user.clearTable();
        Assertions.assertNull(auth.findAuth(authtoken1.authToken()));
        Assertions.assertNull(auth.findAuth(authtoken2.authToken()));
    }

    @Test
    public void findAuth() throws Exception{
        auth.createAuth(authtoken1);
        auth.createAuth(authtoken2);
        Assertions.assertEquals(authtoken1, auth.findAuth(authtoken1.authToken()));
        Assertions.assertEquals(authtoken2, auth.findAuth(authtoken2.authToken()));
        user.clearTable();
    }

    @Test
    public void createAuthFail() throws Exception{
    }

    @Test
    public void findAuthFail() throws Exception{

    }

    @Test
    public void clearGames() throws Exception{
        GameDAO gameDAO = new SQLGameDAO();
        GameData game1 = new GameData(1, "white", null, "name", new ChessGame());
         GameData g = gameDAO.createGame(game1);
        gameDAO.findGame(g.gameID());
        gameDAO.clearTable();
    }

}
