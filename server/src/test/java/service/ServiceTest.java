package service;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceTest {
    @Test
    public void registerTest() throws Exception {
        Service s = new Service();
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
        Assertions.assertEquals(user.username(),r.username());
        Assertions.assertNotNull(r.authToken());
    }

    @Test
    public void registerFailTest(){
        Service s = new Service();
        var user = new UserData("", "pass", "email");
        Assertions.assertThrows(Exception.class, () -> s.register(user));
    }

    @Test
    public void clearTest() throws Exception {
        Service s = new Service();
        var user = new UserData("Name", "pass", "email");
        AuthData r = s.register(user);
        Assertions.assertEquals(user.username(),r.username());
        Assertions.assertNotNull(r.authToken());
    }

    @Test
    public void clearFailTest(){
        Service s = new Service();
        var user = new UserData("", "pass", "email");
        Assertions.assertThrows(Exception.class, () -> s.register(user));
    }
}
