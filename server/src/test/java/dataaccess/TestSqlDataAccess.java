package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestSqlDataAccess {

    // MySQL tests

    @Test
    public void testAddUserGood() throws Exception {
        DataAccess dataAccess = new MySqlDataAccess();
        UserData testUser = new UserData("newUser2", "myPassword", "myEmail");

        dataAccess.addUser(testUser);
        UserData foundUser = dataAccess.getUser("newUser2");


        assertEquals(foundUser.username(), "newUser2");
    }

    @Test
    public void testAddUserFail() throws Exception {
        DataAccess dataAccess = new MySqlDataAccess();

        UserData testUser = new UserData("newUser2", null, "myEmail");

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            dataAccess.addUser(new UserData("newUser2", "password", "email@example.com"));
        });

    }

    @Test
    public void testGetUserGood() throws Exception {
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearUserList();

        UserData testUser = new UserData("newUser2", "password", "myEmail");
        dataAccess.addUser(testUser);

        UserData foundUser = dataAccess.getUser("newUser2");

        assertEquals(foundUser.username(), "newUser2");
        assertEquals(foundUser.email(), "myEmail");

    }

    @Test
    public void testGetUserFail() throws Exception {
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearUserList();

        UserData testUser = new UserData("newUser2", "password", "myEmail");
        dataAccess.addUser(testUser);

        UserData foundUser = dataAccess.getUser("randomUsername");

        assertNull(foundUser);

    }


    @Test
    public void testAddGameGood() throws Exception {
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearGameList();

        int gameNum = dataAccess.addGame("newGame");

        assertEquals(1, gameNum);

    }

    @Test
    public void testAddBadGame() throws Exception {

        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearGameList();

        int gameNum = dataAccess.addGame("newGame");

        int newGameNum = dataAccess.addGame("newGame");

        assertEquals(2, newGameNum);


    }

    @Test
    public void testRegisterUserSuccessSql() throws Exception {

        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).clearUsers();
        new AuthService(dataAccess).clearAuth();
        new GameService(dataAccess).clearGames();

        UserData testUser = new UserData("newUser2", "myPassword", "myEmail");
        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser);

        UserData foundUser = dataAccess.getUser("newUser2");

        assertEquals(foundUser.username(), "newUser2", "usernames match up!");
        assertNotNull(registeredInfo.authToken(), "UUID should not be null");
        assertEquals("newUser2", registeredInfo.username(), "Username should be 'newUser1");
    }


        @Test
    public void testRegisterUserSqlFail() throws Exception {
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).clearUsers();
        new AuthService(dataAccess).clearAuth();
        new GameService(dataAccess).clearGames();

        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
        new UserService(dataAccess).registerUser(testUser);

        UserData testUser2 = new UserData("myUsername", "myPassword", "myEmail");
        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser2);

        assertNull(registeredInfo);
    }




    @Test
    public void loginSqlSuccess() throws Exception {
        UserData testUser = new UserData("newUser", "myPassword", "email.com");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);

        assertNotNull(loginInfo.authToken(), "UUID should not be null");
        assertEquals("newUser", loginInfo.username(), "Username should be 'myUsername");
    }

    @Test
    public void loginSqlWrongPassword() throws Exception {
        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        UserData testUser2 = new UserData("myUsername", "wrongPassword", "myemail");
        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser2);

        assertNull(loginInfo);

    }




    @Test
    public void logoutSqlSuccess() throws Exception {
        UserData testUser = new UserData("myUsername", "myPassword", "thisemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
        String authToken = loginInfo.authToken();

        boolean logoutReturn = new AuthService(dataAccess).logout(authToken);

        assertTrue(logoutReturn);

    }


    @Test
    public void logoutSqlFail() throws Exception {
        UserData testUser = new UserData("myUsername", "myPassword", "thisemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        String authToken = "1234567";
        boolean logoutReturn = new AuthService(dataAccess).logout(authToken);

        assertFalse(logoutReturn);

    }


    @Test
    public void createSqlGamePass() throws Exception {
        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
        String authToken = loginInfo.authToken();

        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");

        assertNotEquals(0, gameId);

    }


    @Test
    public void createSqlGameFail() throws Exception {
        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        new UserService(dataAccess).loginUser(testUser);
        String authToken = "12345";

        var gameId = new GameService(dataAccess).createGame(authToken, "myGame!");

        assertEquals(0, gameId);

    }




    @Test
    public void joinGameSqlSuccess() throws Exception {

        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
        String authToken = loginInfo.authToken();

        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");

        GameService.JoinGameObj joinObj = new GameService.JoinGameObj();
        joinObj.gameID = gameId;
        joinObj.playerColor = ChessGame.TeamColor.BLACK;

        System.out.println("trying to join game");
        int result = new GameService(dataAccess).joinGame(joinObj, authToken);

        assertEquals(1, result);

    }



    @Test
    public void joinGameSqlFail() throws Exception {

        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
        String authToken = loginInfo.authToken();

        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");

        GameService.JoinGameObj joinObj = new GameService.JoinGameObj();
        joinObj.gameID = gameId;
        joinObj.playerColor = ChessGame.TeamColor.BLACK;

        GameService.JoinGameObj joinObj2 = new GameService.JoinGameObj();
        joinObj.gameID = gameId;
        joinObj.playerColor = ChessGame.TeamColor.BLACK;

        System.out.println("trying to join game");
        int result = new GameService(dataAccess).joinGame(joinObj2, authToken);

        assertEquals(3, result);
    }





    @Test
    public void testListGamesSqlPass() throws Exception {

        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        dataAccess.clearGameList();

        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
        String authToken = loginInfo.authToken();

        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");
        int gameId2 = new GameService(dataAccess).createGame(authToken, "newGame");

        ArrayList<GameService.ListGameObj> listOfGames = new GameService(dataAccess).listGames(authToken);
        ArrayList<GameService.ListGameObj> expectedList = new ArrayList<>();

        expectedList.add(new GameService.ListGameObj(gameId, null, null, "myGame!"));
        expectedList.add(new GameService.ListGameObj(gameId2, null, null, "newGame"));

        for (int i = 0; i < expectedList.size(); ++i) {
            assertEquals(expectedList.get(i).gameID, listOfGames.get(i).gameID);
            assertEquals(expectedList.get(i).blackUsername, listOfGames.get(i).blackUsername);
            assertEquals(expectedList.get(i).whiteUsername, listOfGames.get(i).whiteUsername);
            assertEquals(expectedList.get(i).gameName, listOfGames.get(i).gameName);
        }
    }


    @Test
    public void testEmptySqlList() throws Exception {
        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).registerUser(testUser);

        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
        String authToken = loginInfo.authToken();

        ArrayList<GameService.ListGameObj> listOfGames = new GameService(dataAccess).listGames(authToken);

        assert(listOfGames.isEmpty());
    }


    @Test
    public void testClearSqlSuccess() throws Exception {

        DataAccess dataAccess = new MySqlDataAccess();
        new UserService(dataAccess).clearUsers();
        new AuthService(dataAccess).clearAuth();
        new GameService(dataAccess).clearGames();


        UserData testUser = new UserData("newww1", "myPassword", "myEmail");
        new UserService(dataAccess).registerUser(testUser);

        UserData testUser2 = new UserData("newww1", "password", "email.com");
        new UserService(dataAccess).registerUser(testUser2);

        new UserService(dataAccess).clearUsers();
        UserData foundUser = dataAccess.getUser("newww1");

        assertNull(foundUser);
            // add same user again should work

    }

}