package dataaccess;

import model.*;
import java.util.*;

public class InMemoryDataAccess implements DataAccess {
    private final ArrayList<UserData> userList = new ArrayList<>();
    private final ArrayList<AuthData> authList = new ArrayList<>();
    private final ArrayList<GameData> gameList = new ArrayList<>();

    @Override
    public void addUser(UserData user) {
        userList.add(user);
        System.out.println("length after add: " + userList.size());
    }

    @Override
    public ArrayList<UserData> listUsers() {
        return userList;
    }

    @Override
    public UserData getUser(String username) {
        System.out.println("looking for username: " + username);
        for (UserData currUser : userList) {
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

    @Override
    public void clearUserList() {
        userList.clear();
    }

    @Override
    public void clearAuthList() {
        authList.clear();
    }

    @Override
    public void clearGameList() {
        gameList.clear();
    }


}