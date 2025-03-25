package client;

import chess.ChessGame;
import com.google.gson.internal.LinkedTreeMap;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Set;

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

    @Test
    public void createGamePass() throws ResponseException {
        facade.clearAll();
        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        Object createdGame = facade.createGame(returnedData.authToken(), "myGame");
        System.out.println("createdGame: " + createdGame);
        assertNotNull(createdGame);
    }

    @Test
    public void createGameFail() throws ResponseException {
        facade.clearAll();
        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        Object createdGame = facade.createGame(returnedData.authToken(), "myGame");

        createdGame = facade.createGame("1234", "newGame");
        System.out.println("createdGame: " + createdGame);
        assertNull(createdGame);
    }


    @Test
    public void listGamePass() throws ResponseException {
        facade.clearAll();
        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        Object createdGame = facade.createGame(returnedData.authToken(), "myGame");
        createdGame = facade.createGame(returnedData.authToken(), "newGame");
        createdGame = facade.createGame(returnedData.authToken(), "game3");

        Object listOfGames = facade.listGames(returnedData.authToken());

        ArrayList<String> stringList = new ArrayList<>();


        if (listOfGames instanceof LinkedTreeMap<?, ?>) {
            LinkedTreeMap<String, ArrayList<Object>> treeMap = (LinkedTreeMap<String, ArrayList<Object>>) listOfGames;
            stringList = new ArrayList<>();
            Set keySet = ((LinkedTreeMap<?, ?>) listOfGames).keySet();
            int i = 1;
            for (Object game : treeMap.get("games")) {
                if (game instanceof LinkedTreeMap<?, ?>) {
                    LinkedTreeMap<String, String> gameInfo = (LinkedTreeMap<String, String>) game;
                    String stringToAdd = i + ".   " + gameInfo.get("gameName") + ",   " + gameInfo.get("whiteUsername") + ",   " + gameInfo.get("blackUsername");
                    ++i;
                    stringList.add(stringToAdd);
                }

            }
            System.out.println("printing stringlist:");
            for (String s : stringList) {
                System.out.println(s);
            }
            assertEquals(3, stringList.size());
        }
    }

    @Test
    public void playGameSucceed() throws ResponseException {
        facade.clearAll();

        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        Object createdGame = facade.createGame(returnedData.authToken(), "myGame");

        System.out.println("createdgame: " + createdGame);

        ServerFacade.JoinGameObj joinObj = new ServerFacade.JoinGameObj(ChessGame.TeamColor.WHITE, 1);
        Object joined = facade.joinGame(joinObj, returnedData.authToken());

        LinkedTreeMap<?, ?> expected = new LinkedTreeMap<>();

        assertEquals(expected, joined);
    }

    @Test
    public void playGameFail() throws ResponseException {
        facade.clearAll();

        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        Object createdGame = facade.createGame(returnedData.authToken(), "myGame");
        System.out.println("createdgame: " + createdGame);

        ServerFacade.JoinGameObj joinObj = new ServerFacade.JoinGameObj(ChessGame.TeamColor.WHITE, 1);
        facade.joinGame(joinObj, returnedData.authToken());


        facade.logoutUser(returnedData.authToken());
        UserData myUser2 = new UserData("name5", "pass", "email");
        facade.registerUser(myUser2);

        Object joined2 = facade.joinGame(joinObj, returnedData.authToken());

        assertNull(joined2);
    }


    @Test
    public void playGamePass2() throws ResponseException {
        facade.clearAll();

        UserData myUser = new UserData("name4", "password2", "email@gmail.com");
        AuthData returnedData = facade.registerUser(myUser);

        Object createdGame = facade.createGame(returnedData.authToken(), "myGame");
        System.out.println("createdgame: " + createdGame);

        ServerFacade.JoinGameObj joinObj = new ServerFacade.JoinGameObj(ChessGame.TeamColor.WHITE, 1);
        facade.joinGame(joinObj, returnedData.authToken());


        facade.logoutUser(returnedData.authToken());
        UserData myUser2 = new UserData("name5", "pass", "email");
        ServerFacade.JoinGameObj join2 = new ServerFacade.JoinGameObj(ChessGame.TeamColor.BLACK, 1);
        returnedData = facade.registerUser(myUser2);

        Object joined2 = facade.joinGame(join2, returnedData.authToken());

        LinkedTreeMap<?, ?> expected = new LinkedTreeMap<>();

        assertEquals(expected, joined2);    }

}