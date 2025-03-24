package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import dataaccess.DataAccess;
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.AuthService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
// TODO add unit tests!
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        facade.clearAll();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }


    @Test
    public void clearAll() throws ResponseException {

        UserData myUser = new UserData("name8", "password2", "email@gmail.com");
        facade.registerUser(myUser);

        facade.clearAll();

        AuthData returnedData = facade.registerUser(myUser);
        assertEquals(returnedData.username(), "name8");

    }

    @Test
    public void registerUserPass() throws ResponseException {
        facade.clearAll();

        UserData myUser = new UserData("name4", "password2", "email@gmail.com");

        AuthData returnedData = facade.registerUser(myUser);
        System.out.println("returnedData: " + returnedData.toString());
        assertNotNull(returnedData.authToken());

        assertEquals(returnedData.username(), "name4");
    }

    @Test
    public void registerUserFail() throws ResponseException {
        facade.clearAll();
        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        facade.registerUser(myUser);
        Object returnedData = facade.registerUser(myUser);
        System.out.println(returnedData);
        assertNull(returnedData);
    }


    @Test
    public void logoutPass() throws ResponseException {
        facade.clearAll();

        UserData testUser = new UserData("newUser", "myPassword", "email.com");
        AuthData registerInfo = facade.registerUser(testUser);

        System.out.println("register info: " + registerInfo.toString());
        System.out.println("authtoken: " + registerInfo.authToken());

        Object logoutData = facade.logoutUser(registerInfo.authToken());
        LinkedTreeMap<String, Object> expected = new LinkedTreeMap<>();
        assertEquals(expected, logoutData);
    }



    @Test
    public void logoutFail() throws ResponseException {
        facade.clearAll();
        UserData testUser = new UserData("myUsername", "myPassword", "thisemail");
        facade.loginUser(testUser);

        String authToken = "123";

        Object logoutReturn = facade.logoutUser(authToken);
        assertNull(logoutReturn);
    }


    @Test
    public void loginUserPass() throws ResponseException {
        facade.clearAll();
        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        facade.logoutUser(returnedData.authToken());
        AuthData loginInfo = facade.loginUser(myUser);

        assertNotNull(loginInfo.authToken(), "UUID should not be null");
        assertEquals("name4", loginInfo.username(), "Username should be 'myUsername");
    }

    @Test
    public void loginUserFail() throws ResponseException {
        facade.clearAll();
        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        UserData wrongUser = new UserData("name4", "wrongpassword", null);
        facade.logoutUser(returnedData.authToken());
        AuthData loginInfo = facade.loginUser(wrongUser);

        assertNull(loginInfo);
    }




}