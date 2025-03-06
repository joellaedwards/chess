package service;

import dataaccess.DataAccess;
import model.AuthData;


public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }


    public int createGame(String authToken, String gameName) {
        AuthData authFound = dataAccess.getAuth(authToken);
        if (authFound != null) {
            // authtoken found, valid user
            dataAccess.deleteAuth(authFound);

            return dataAccess.addGame(gameName);
        }

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
