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
        System.out.println("logout bool authToken: " + authToken);
        AuthData authFound = dataAccess.getAuth(authToken);
        System.out.println("authfound from logout: " + authFound);
        if (authFound != null) {
            // authtoken found, valid user
            dataAccess.deleteAuth(authFound);
            System.out.println("returning true");
            return true;
        }
        return false;
    }

    public void clearAuth() {
        dataAccess.clearAuthList();
    }

}
