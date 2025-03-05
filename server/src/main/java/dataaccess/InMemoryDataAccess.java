package dataaccess;

import model.*;
import java.util.*;

public class InMemoryDataAccess implements DataAccess {
    private final ArrayList<UserData> users = new ArrayList<>();
    private final ArrayList<AuthData> authList = new ArrayList<>();

    @Override
    public void addUser(UserData user) {
        users.add(user);
        System.out.println("length after add: " + users.size());
    }

    @Override
    public ArrayList<UserData> listUsers() {
        return users;
    }

    @Override
    public UserData getUser(String username) {
        System.out.println("looking for username: " + username);
        for (UserData currUser : users) {
            System.out.println("curr username: " + currUser.username());
            if (Objects.equals(currUser.username(), username)) {
                System.out.println("found! returning user");
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