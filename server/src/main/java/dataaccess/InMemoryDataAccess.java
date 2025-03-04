package dataaccess;

import model.*;
import java.util.*;

public class InMemoryDataAccess implements DataAccess {

    private final ArrayList<UserData> users = new ArrayList<>();
    private final ArrayList<AuthData> authList = new ArrayList<>();


    @Override
    public void addUser(UserData user) {
        users.add(user);
    }

    @Override
    public ArrayList<UserData> listUsers() {
        return users;
    }

    @Override
    public UserData getUser(String username) {
        for (UserData currUser : users) {
            if (Objects.equals(currUser.username(), username)) {
                return currUser;
            }
        }
        return null;
    }

    @Override
    public AuthData addAuth(String username) {
        AuthData currAuth = new AuthData(UUID.randomUUID().toString(), username);
        authList.add(currAuth);
        System.out.println("current authdata uuid: " + currAuth.authToken());
        return currAuth;
    }


}