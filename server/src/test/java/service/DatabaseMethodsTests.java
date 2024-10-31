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
}
