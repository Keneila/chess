package service;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class DatabaseMethodsTests {
    @Test
    public void createUser() throws Exception{
        UserDAO user = new SQLUserDAO();
        user.createUser(new UserData("user","pass","email"));
        user.createUser(new UserData("use2r","pa2ss","ema2il"));
    }

    @Test
    public void deleteUserALL() throws Exception{
        UserDAO user = new SQLUserDAO();
        user.createUser(new UserData("user","pass","email"));
        user.createUser(new UserData("use2r","pa2ss","ema2il"));
        user.clearTable();
    }

    @Test
    public void findUser() throws Exception{
        UserDAO user = new SQLUserDAO();
        UserData user1 = new UserData("user","pass","email");
        UserData user2 = new UserData("use2r","pa2ss","ema2il");
        user.createUser(user1);
        user.createUser(user2);
        Assertions.assertEquals(user1, user.findUser("user"));
        Assertions.assertEquals(user2, user.findUser("use2r"));
    }
}
