package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;


public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static class JoinGameObj {
        public ChessGame.TeamColor playerColor;
        public int gameID;
    }

    public static class ListGameObj {
        public int gameID;
        public String whiteUsername;
        public String blackUsername;
        public String gameName;

        public ListGameObj(int gameID, String whiteUsername, String blackUsername, String gameName) {
            this.gameID = gameID;
            this.whiteUsername = whiteUsername;
            this.blackUsername = blackUsername;
            this.gameName = gameName;
        }
    }

    public int createGame(String authToken, String gameName) {
        System.out.println("in create game in gameservice");
        AuthData authFound = dataAccess.getAuth(authToken);
        if (authFound != null) {
            // authtoken found, valid user
            return dataAccess.addGame(gameName);
        }
        return 0;
    }

    public int joinGame(JoinGameObj joinObj, String authToken) {
        System.out.println("in joingame in gameservice");
        AuthData authFound = dataAccess.getAuth(authToken);
        if (authFound != null) {
            System.out.println("authFound!");
            //authToken found, valid user
            GameData gameToJoin = dataAccess.getGame(joinObj.gameID);
            if (gameToJoin == null) {
                System.out.println("no game to join.");
                return 3;
            }
            boolean success = dataAccess.joinGame(gameToJoin, joinObj.playerColor, authFound.username());
            if (success) {
                System.out.println("wohoo we did it in gameservice");
                return 1;
            }
            else {
                return 2;
            }
        }
        return 0;
    }

    public ArrayList<ListGameObj> listGames(String authToken) {
        AuthData authResult = dataAccess.getAuth(authToken);
        ArrayList<ListGameObj> returnGameList = new ArrayList<>();

        if (authResult != null) {
            ArrayList<GameData> fullList = dataAccess.listGames();
            for (GameData game : fullList) {
                ListGameObj currGame = new ListGameObj(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
                returnGameList.add(currGame);
            }
            return returnGameList;
        }
        return null;
    }

    public void clearGames() {
        dataAccess.clearGameList();
    }

}
