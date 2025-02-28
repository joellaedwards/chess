package service;

import dataaccess.DataAccess;
import model.UserData;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }


    // this should return the things how they should be but
    // idk so its just void for now
    public UserData registerUser(UserData user) {
        // get user. if get user is null do createUser and then createAuth
        if (getUser(user) == null) {
            // add user
        }

        return dataAccess.addUser(user);
    }

}
