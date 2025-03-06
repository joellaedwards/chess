package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
//import model.JoinGameObj;






public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static class JoinGameObj {
        public ChessGame.TeamColor playerColor;
        public int gameID;
    }

    public int createGame(String authToken, String gameName) {
        AuthData authFound = dataAccess.getAuth(authToken);
        if (authFound != null) {
            // authtoken found, valid user
            return dataAccess.addGame(gameName);
        }

        return 0;
    }

    public int joinGame(JoinGameObj joinObj, String authToken) {
        AuthData authFound = dataAccess.getAuth(authToken);
        if (authFound != null) {
            //authToken found, valid user
            System.out.println("passed null check");
            GameData gameToJoin = dataAccess.getGame(joinObj.gameID);
            if (gameToJoin == null) {
                System.out.println("authorized but cant find game");
                return 2;
            }
            System.out.println("game id: " + gameToJoin);
            if (dataAccess.joinGame(gameToJoin, joinObj.playerColor, authFound.username())) {
                System.out.println("success! returning 1");
                return 1;
            }
            else {
                System.out.println("color taken");
                return 2;
            }
        }
        System.out.println("not authorized");
        return 0;

    }


    // this should return the things how they should be but
    // idk so its just void for now
    public void clearGames() {
        dataAccess.clearGameList();
    }

//    // should return a gameID
//    public int createGame(String gameName) {
//
//
//
//    }





}
