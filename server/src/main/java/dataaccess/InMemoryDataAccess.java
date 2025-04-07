package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.*;

public class InMemoryDataAccess implements DataAccess {
    private final ArrayList<UserData> userList = new ArrayList<>();
    private final ArrayList<AuthData> authList = new ArrayList<>();
    private final ArrayList<GameData> gameList = new ArrayList<>();
    private int gameNum = 0;

    // user functions
    @Override
    public void addUser(UserData user) {
        userList.add(user);
    }

    @Override
    public UserData getUser(String username) {
        for (UserData currUser : userList) {
            if (Objects.equals(currUser.username(), username)) {
                return currUser;
            }
        }
        return null;
    }

    // game functions
    public int addGame(String gameName) {
        gameNum++;
        GameData newGame = new GameData(gameNum, null, null, gameName, new ChessGame());
        gameList.add(newGame);
        return gameNum;
    }

    @Override
    public boolean gameIsOver(int gameId) {
        return false;
    }

    public GameData getGame(int gameId) {
        for (GameData currGame : gameList) {
            if (gameId == currGame.gameID()) {
                return currGame;
            }
        }
        return null;
    }

    @Override
    public void makeMoveDataBase(ChessGame game, int gameId) {

    }

    @Override
    public void endGame(int gameId) {

    }


    public boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() == null) {
                gameList.remove(game);
                game = game.setWhite(username);
                gameList.add(game);
                return true;
            }
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() == null) {
                gameList.remove(game);
                game = game.setBlack(username);
                gameList.add(game);
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return gameList;
    }

    // auth functions
    @Override
    public AuthData addAuth(String username) {
        AuthData currAuth = new AuthData(UUID.randomUUID().toString(), username);
        authList.add(currAuth);
        return currAuth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        for (AuthData currAuth : authList) {
            if (Objects.equals(currAuth.authToken(), authToken)) {
                return currAuth;
            }
        }
        return null;
    }

    public void deleteAuth(AuthData authObj) {
        authList.remove(authObj);
    }

    // clear functions
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