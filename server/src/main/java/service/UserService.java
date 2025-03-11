package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.Objects;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData registerUser(UserData user) throws DataAccessException {
        if (dataAccess.getUser(user.username()) == null) {
            dataAccess.addUser(user);
            return dataAccess.addAuth(user.username());
        }
        // user already exists

        return null;
    }

    public AuthData loginUser(UserData user) {
        UserData foundUser = dataAccess.getUser(user.username());
        if (foundUser == null) {
            return null;
        }
        if (Objects.equals(foundUser.password(), user.password())) {
            return dataAccess.addAuth(user.username());
        }
        return null;
    }

    public void clearUsers() {
        dataAccess.clearUserList();
    }

    public ArrayList<UserData> listUsers() {
        return dataAccess.listUsers();
    }

}
