package dataaccess;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Collection;

public class DataAccessTest {

    private final UserDAO user = new SQLUserDAO();
    private UserData user1 = new UserData("user","pass","email");
    private UserData user2 = new UserData("use2r","pa2ss","ema2il");
    private UserData userfail1 = new UserData(null,"pa2ss","ema2il");
    private UserData userfail2 = new UserData("D",null,"ema2il");
    private final AuthDAO auth = new SQLAuthDAO();
    private AuthData authtoken1 = new AuthData("egeebncbxe", "user");
    private AuthData authtoken2 = new AuthData("egergre", "user2");
    private AuthData authtokenfail = new AuthData(null, "user");
    private AuthData authtokenfail2 = new AuthData("egerg9675re", null);
    private final GameDAO gameDAO = new SQLGameDAO();
    private GameData game1 = new GameData(1, null, null, "name", new ChessGame());
    private GameData game2 = new GameData(2, null, null, "name2", new ChessGame());
    private GameData gamefail1 = new GameData(1, "white", null, null, new ChessGame());
    private GameData gameup1 = new GameData(1, "white", null, "name", new ChessGame());
    private GameData gameup2 = new GameData(1, "white", "black", "name", new ChessGame());

    public DataAccessTest() throws DataAccessException {
    }

    @Test
    public void createUser() throws Exception{
        user.createUser(user1);
        Assertions.assertTrue(BCrypt.checkpw(user1.password(), user.findUser(user1.username()).password()));
        user.createUser(user2);
        Assertions.assertTrue(BCrypt.checkpw(user1.password(), user.findUser(user1.username()).password()));
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
        Assertions.assertTrue(BCrypt.checkpw(user1.password(), user.findUser(user1.username()).password()));
        Assertions.assertTrue(BCrypt.checkpw(user2.password(), user.findUser(user2.username()).password()));
        user.clearTable();
    }

    @Test
    public void createUserFail() throws Exception{
        user.clearTable();
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
        auth.clearTable();
        Assertions.assertNull(auth.findAuth(authtoken1.authToken()));
        Assertions.assertNull(auth.findAuth(authtoken2.authToken()));
    }

    @Test
    public void findAuth() throws Exception{
        auth.createAuth(authtoken1);
        auth.createAuth(authtoken2);
        Assertions.assertEquals(authtoken1, auth.findAuth(authtoken1.authToken()));
        Assertions.assertEquals(authtoken2, auth.findAuth(authtoken2.authToken()));
        auth.clearTable();
    }

    @Test
    public void createAuthFail() throws Exception{
        Assertions.assertThrows(Exception.class, () ->  auth.createAuth(authtokenfail2));
        Assertions.assertThrows(Exception.class, () ->  auth.createAuth(authtokenfail));
        auth.clearTable();
    }

    @Test
    public void findAuthFail() throws Exception{
        auth.createAuth(authtoken1);
        Assertions.assertNull(auth.findAuth(authtoken2.authToken()));
        auth.clearTable();
    }

    @Test
    public void deleteAuth() throws Exception{
        auth.createAuth(authtoken1);
        auth.createAuth(authtoken2);
        auth.deleteAuth(authtoken2.authToken());
        Assertions.assertEquals(authtoken1, auth.findAuth(authtoken1.authToken()));
        Assertions.assertNull(auth.findAuth(authtoken2.authToken()));
    }

    @Test
    public void clearGames() throws Exception{
        GameData g = gameDAO.createGame(game1);
        GameData g2 = gameDAO.createGame(game2);
        gameDAO.clearTable();
        Assertions.assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    public void createGame() throws Exception{
        gameDAO.clearTable();
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(game1));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(game2));
        gameDAO.clearTable();
    }

    @Test
    public void createGameFail() throws Exception{
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(gamefail1));
        gameDAO.clearTable();
    }

    @Test
    public void findGame() throws Exception {
        gameDAO.clearTable();
        int id1 = gameDAO.createGame(game1).gameID();
        int id2 = gameDAO.createGame(game2).gameID();
        Assertions.assertEquals(game1, gameDAO.findGame(id1));
        Assertions.assertEquals(game2, gameDAO.findGame(id2));
        gameDAO.clearTable();
    }

    @Test
    public void findGameFail() throws Exception {
        gameDAO.clearTable();
        int id1 = gameDAO.createGame(game1).gameID();
        int id2 = gameDAO.createGame(game2).gameID();
        Assertions.assertNull(gameDAO.findGame(10));
        gameDAO.clearTable();
    }

    @Test
    public void listGames() throws Exception{
        gameDAO.clearTable();
        gameDAO.createGame(game2);
        gameDAO.createGame(game1);
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertFalse(games.isEmpty());
        Assertions.assertEquals(2, games.size());
        gameDAO.clearTable();
    }

    @Test
    public void listGamesFail() throws Exception{
        gameDAO.clearTable();
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertTrue(games.isEmpty());
        gameDAO.clearTable();
    }

    @Test
    public void updateGame() throws Exception {
        gameDAO.clearTable();
        int id = gameDAO.createGame(game1).gameID();
        gameDAO.createGame(game2);
        gameDAO.updateGame(gameup1);
        Assertions.assertEquals("white", gameDAO.findGame(id).whiteUsername());
        Assertions.assertNull(gameDAO.findGame(id).blackUsername());
        gameDAO.updateGame(gameup2);
        Assertions.assertEquals("black", gameDAO.findGame(id).blackUsername());
        gameDAO.clearTable();
    }

    @Test
    public void updateGameFail() throws Exception {
        gameDAO.clearTable();
        Assertions.assertThrows(Exception.class, () -> gameDAO.updateGame(gameup1));
        gameDAO.clearTable();
    }

}
