package service;

import dataaccess.DataAccess;
import model.AuthData;


public class
AuthService {

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

    public void clearAuth() {
        dataAccess.clearAuthList();
    }

}
