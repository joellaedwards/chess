package service;

import dataaccess.DataAccess;
import model.AuthData;


public class AuthService {

    private final DataAccess dataAccess;

    public AuthService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public boolean logout(String authToken) {
        AuthData authFound = dataAccess.getAuth(authToken);
        if (authFound != null) {
            // authtoken found, valid user
            dataAccess.deleteAuth(authFound);
            return true;
        }
        return false;
    }


    // this should return the things how they should be but
    // idk so its just void for now
    public void clearAuth() {
        dataAccess.clearAuthList();

        // TODO create something here that's like hey u failed
    }


//    public ArrayList<AuthData> listAuth() {
//
//        return dataAccess.listAuth();
//
//
//    }





}
