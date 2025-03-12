package service;

import chess.ChessGame;
import dataaccess.DataAccess;
//import dataaccess.InMemoryDataAccess;
import dataaccess.MySqlDataAccess;
import model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class UnitTests {

//
//    @Test
//    public void testRegisterUserSuccess() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser);
//
//        assertNotNull(registeredInfo.authToken(), "UUID should not be null");
//        assertEquals("nothing", registeredInfo.username(), "Username should be 'myUsername");
//    }
//
//    @Test
//    public void testRegisterUserFail() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        UserData testUser2 = new UserData("myUsername", "myPassword", "myEmail");
//        AuthData registeredInfo = new UserService(dataAccess).registerUser(testUser2);
//
//        assertNull(registeredInfo);
//    }
//
//    @Test
//    public void loginSuccess() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", null);
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//
//        assertNotNull(loginInfo.authToken(), "UUID should not be null");
//        assertEquals("myUsername", loginInfo.username(), "Username should be 'myUsername");
//    }
//
//    @Test
//    public void loginWrongPassword() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", null);
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        UserData testUser2 = new UserData("myUsername", "wrongPassword", null);
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser2);
//
//        assertNull(loginInfo);
//
//    }
//
//    @Test
//    public void logoutSuccess() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", null);
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//        String authToken = loginInfo.authToken();
//
//        boolean logoutReturn = new AuthService(dataAccess).logout(authToken);
//
//        assertTrue(logoutReturn);
//
//    }
//
//
//    @Test
//    public void logoutFail() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", null);
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        String authToken = "1234567";
//        boolean logoutReturn = new AuthService(dataAccess).logout(authToken);
//
//        assertFalse(logoutReturn);
//
//    }
//
//
//
//    @Test
//    public void createGamePass() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//        String authToken = loginInfo.authToken();
//
//        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");
//
//        assertNotEquals(0, gameId);
//
//    }
//
//
//    @Test
//    public void createGameFail() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        new UserService(dataAccess).loginUser(testUser);
//        String authToken = "12345";
//
//        var gameId = new GameService(dataAccess).createGame(authToken, "myGame!");
//
//        assertEquals(0, gameId);
//
//    }
//
//
//
//    @Test
//    public void joinGameSuccess() throws Exception {
//
//        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//        String authToken = loginInfo.authToken();
//
//        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");
//
//        GameService.JoinGameObj joinObj = new GameService.JoinGameObj();
//        joinObj.gameID = gameId;
//        joinObj.playerColor = ChessGame.TeamColor.BLACK;
//
//        System.out.println("trying to join game");
//        int result = new GameService(dataAccess).joinGame(joinObj, authToken);
//
//        assertEquals(1, result);
//
//    }
//
//
//    @Test
//    public void joinGameFail() throws Exception {
//
//        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//        String authToken = loginInfo.authToken();
//
//        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");
//
//        GameService.JoinGameObj joinObj = new GameService.JoinGameObj();
//        joinObj.gameID = gameId;
//        joinObj.playerColor = ChessGame.TeamColor.BLACK;
//
//        GameService.JoinGameObj joinObj2 = new GameService.JoinGameObj();
//        joinObj.gameID = gameId;
//        joinObj.playerColor = ChessGame.TeamColor.BLACK;
//
//        System.out.println("trying to join game");
//        int result = new GameService(dataAccess).joinGame(joinObj2, authToken);
//
//        assertEquals(3, result);
//    }
//
//    @Test
//    public void testListGamesPass() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//        String authToken = loginInfo.authToken();
//
//        int gameId = new GameService(dataAccess).createGame(authToken, "myGame!");
//        int gameId2 = new GameService(dataAccess).createGame(authToken, "newGame");
//
//        ArrayList<GameService.ListGameObj> listOfGames = new GameService(dataAccess).listGames(authToken);
//        ArrayList<GameService.ListGameObj> expectedList = new ArrayList<>();
//
//        expectedList.add(new GameService.ListGameObj(gameId, null, null, "myGame!"));
//        expectedList.add(new GameService.ListGameObj(gameId2, null, null, "newGame"));
//
//        for (int i = 0; i < expectedList.size(); ++i) {
//            assertEquals(expectedList.get(i).gameID, listOfGames.get(i).gameID);
//            assertEquals(expectedList.get(i).blackUsername, listOfGames.get(i).blackUsername);
//            assertEquals(expectedList.get(i).whiteUsername, listOfGames.get(i).whiteUsername);
//            assertEquals(expectedList.get(i).gameName, listOfGames.get(i).gameName);
//        }
//    }
//
//
//    @Test
//    public void testEmptyList() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myemail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        AuthData loginInfo = new UserService(dataAccess).loginUser(testUser);
//        String authToken = loginInfo.authToken();
//
//        ArrayList<GameService.ListGameObj> listOfGames = new GameService(dataAccess).listGames(authToken);
//
//        assert(listOfGames.isEmpty());
//    }
//
//
//
//    @Test
//    public void testClearSuccess() throws Exception {
//        UserData testUser = new UserData("myUsername", "myPassword", "myEmail");
//        DataAccess dataAccess = new InMemoryDataAccess();
//        new UserService(dataAccess).registerUser(testUser);
//
//        UserData testUser2 = new UserData("newUser", "password", "email.com");
//        new UserService(dataAccess).registerUser(testUser2);
//
//        new UserService(dataAccess).clearUsers();
//
//        ArrayList<UserData> testList = new UserService(dataAccess).listUsers();
//
//        assert testList.isEmpty();
//    }



    // MySQL tests

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