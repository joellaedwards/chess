package service;

import dataaccess.DataAccess;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

public class UserService {
    private final DataAccess dataAccess;
//    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData registerUser(UserData user) throws Exception {
        System.out.println("entered registerUser");
        if (dataAccess.getUser(user.username()) == null) {
            System.out.println("registering user");
            dataAccess.addUser(user);
            AuthData returnAuth = dataAccess.addAuth(user.username());
            System.out.println("register user returning: " + returnAuth.toString());
            return returnAuth;
        }
        // user already exists
        System.out.println("null in register user");
        return null;
    }

    public AuthData loginUser(UserData user) {
        System.out.println("in loginUser");
        UserData foundUser = dataAccess.getUser(user.username());
        if (foundUser == null) {
            System.out.println("no user found");
            return null;
        }

        var hashedPassword = foundUser.password();

        if (BCrypt.checkpw(user.password(), hashedPassword)) {
            System.out.println("adding auth");
            return dataAccess.addAuth(user.username());
        }
        return null;
    }

    public void clearUsers() {
        dataAccess.clearUserList();
    }

}
