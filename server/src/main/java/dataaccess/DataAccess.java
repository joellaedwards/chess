package dataaccess;
import model.*;
import java.util.ArrayList;
// idk maybe make a whole list of users and then also
// a list of authObjects etc just to keep track
// for now w no database

public interface DataAccess {
    void addUser(UserData user);
    ArrayList<UserData> listUsers();
    UserData getUser(String username);

    AuthData addAuth(String username);
}
