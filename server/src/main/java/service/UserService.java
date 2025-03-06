package service;

import dataaccess.DataAccess;
import model.*;

import java.util.ArrayList;
import java.util.Objects;

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
    }


    public AuthData loginUser(UserData user) {
        // get user. if get user is null do createUser and then createAuth

        UserData foundUser = dataAccess.getUser(user.username());
        System.out.println("foundUser: " + foundUser);

        if (foundUser == null) {
            System.out.println("user not found in login user returning null");
            return null;
        }

        if (Objects.equals(foundUser.password(), user.password())) {
            System.out.println("user found! password correct! login (create auth token)");
            return dataAccess.addAuth(user.username());
        }
        // user already exists
        return null;
    }


    public void clearUsers() {
        System.out.println("clearing users service");
        dataAccess.clearUserList();

    }


    public ArrayList<UserData> listUsers() {

        return dataAccess.listUsers();


    }





}
