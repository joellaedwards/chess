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
    public ArrayList<GameData> listGames() {
        return gameList;
    }

    @Override
    public UserData getUser(String username) {
        for (UserData currUser : userList) {
            if (Objects.equals(currUser.username(), username)) {
                return currUser;
            }
        }
        System.out.println("user not found ...");
        return null;
    }




    @Override
    public AuthData getAuth(String authToken) {
        for (AuthData currAuth : authList) {
            if (Objects.equals(currAuth.authToken(), authToken)) {
                System.out.println("found! returning user");
                return currAuth;
            }
        }
        System.out.println("auth not found ...");
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
                System.out.println("returning game: " + currGame);
                System.out.println("correct game black: " + currGame.blackUsername());
                return currGame;
            }
        }
        return null;
    }

    public boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username) {
        System.out.println("username right inside joinGame: " + username);
        if (teamColor == ChessGame.TeamColor.WHITE) {
            System.out.println("white username: " + game.whiteUsername());
            if (game.whiteUsername() == null) {
                gameList.remove(game);
                game = game.setWhite(username);
                gameList.add(game);
                return true;
            }
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            System.out.println("black username: " + game.blackUsername());
            if (game.blackUsername() == null) {

//                int index = gameList.indexOf(game);
//                if (index != -1) {
//                    game = game.setBlack(username);
//                    System.out.println("black after update " + game.blackUsername());
//                    gameList.set(index, game);
//                    return true;
//                }

                gameList.remove(game);
                game = game.setBlack(username);
                System.out.println("black after update: " + game.blackUsername());
                gameList.add(game);
                return true;
            }
        }
        System.out.println("return false from join game cant take another color");
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