package service;

import dataaccess.DataAccess;
import dataaccess.InMemoryDataAccess;
import model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UnitTests {


    @Test
    public void testRegisterUserSuccess() {
        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
        DataAccess dataAccess = new InMemoryDataAccess();
        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser);

        assertNotNull(registeredInfo.authToken(), "UUID should not be null");
        assertEquals("myUsername", registeredInfo.username(), "Username should be 'myUsername");
    }

    @Test
    public void testRegisterUserFail() {
        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
        DataAccess dataAccess = new InMemoryDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        UserData testUser2 = new UserData("myUsername", "myPassword", "myEmail");
        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser2);

        assertNull(registeredInfo);
    }

    @Test
    public void testClearSuccess() {
        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
        DataAccess dataAccess = new InMemoryDataAccess();
        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser);

        UserData testUser2 = new UserData("newUser", "password", "email.com");
        new UserService(dataAccess).registerUser(testUser2);

        new UserService(dataAccess).clearUsers();

        ArrayList<UserData> testList = new UserService(dataAccess).listUsers();

        assert testList.isEmpty();
    }

}