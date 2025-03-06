package service;

import dataaccess.DataAccess;


public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
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
