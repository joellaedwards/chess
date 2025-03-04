package service;

import dataaccess.DataAccess;
import model.*;

import java.util.ArrayList;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }


    // this should return the things how they should be but
    // idk so its just void for now
    public AuthData registerUser(UserData user) {
        // get user. if get user is null do createUser and then createAuth
        if (dataAccess.getUser(user.username()) == null) {
            System.out.println("getUser returned null");
            dataAccess.addUser(user);
            return dataAccess.addAuth(user.username());
        }
        // user already exists
        return null;

        // TODO create something here that's like hey u failed
    }


    public ArrayList<UserData> listUsers() {

        return dataAccess.listUsers();


    }





}
