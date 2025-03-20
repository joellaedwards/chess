package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
// TODO add unit tests!
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
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
    public void clearPass() {

    }

    @Test
    public void registerUserPass() throws ResponseException {

        UserData myUser = new UserData("name4", "password2", "email@gmail.com");

        AuthData returnedData = facade.registerUser(myUser);
        System.out.println("returnedData: " + returnedData.toString());
        assertNotNull(returnedData.authToken());

        assertEquals(returnedData.username(), "name4");
    }
}