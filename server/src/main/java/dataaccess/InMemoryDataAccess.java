package dataaccess;

import chess.ChessGame;
import model.*;
import java.util.*;

public class InMemoryDataAccess implements DataAccess {
    private final ArrayList<UserData> userList = new ArrayList<>();
    private final ArrayList<AuthData> authList = new ArrayList<>();
    private final ArrayList<GameData> gameList = new ArrayList<>();
    private int gameNum = 0;

    @Override
    public void addUser(UserData user) {
        userList.add(user);
        System.out.println("length after add: " + userList.size());
    }

    @Override
    public ArrayList<UserData> listUsers() {
        return userList;
    }

    @Override
    public UserData getUser(String username) {
        System.out.println("looking for username: " + username);
        for (UserData currUser : userList) {
            System.out.println("curr username: " + currUser.username());
            if (Objects.equals(currUser.username(), username)) {
                System.out.println("found! returning user");
                return currUser;
            }
        }
        System.out.println("user not found ...");
        return null;
    }




    @Override
    public AuthData getAuth(String authToken) {
        System.out.println("looking for auth: " + authToken);
        for (AuthData currAuth : authList) {
            System.out.println("curr token: " + currAuth.authToken());
            if (Objects.equals(currAuth.authToken(), authToken)) {
                System.out.println("found! returning user");
                return currAuth;
            }
        }
        System.out.println("user not found ...");
        return null;
    }



    public int addGame(String gameName) {
        gameNum++;
        GameData newGame = new GameData(gameNum, null, null, gameName, new ChessGame());
        gameList.add(newGame);
        return gameNum;
    }


    public GameData getGame(int gameId) {
        for (GameData currGame : gameList) {
            if (gameId == currGame.gameID()) {
                return currGame;
            }
        }
        return null;
    }

    public boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username) {

        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() == null) {
                game.setWhite(username);
                return true;
            }
        }
        if (teamColor == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() == null) {
                game.setBlack(username);
                return true;
            }
        }
        return false;
    }


    public void deleteAuth(AuthData authObj) {
        authList.remove(authObj);
    }

    @Override
    public AuthData addAuth(String username) {
        AuthData currAuth = new AuthData(UUID.randomUUID().toString(), username);
        authList.add(currAuth);
        System.out.println("current authdata uuid: " + currAuth.authToken());
        return currAuth;
    }

    @Override
    public void clearUserList() {
        userList.clear();
    }

    @Override
    public void clearAuthList() {
        authList.clear();
    }

    @Override
    public void clearGameList() {
        gameList.clear();
    }


}