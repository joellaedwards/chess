package dataaccess;
import model.UserData;

import java.util.Collection;
// idk maybe make a whole list of users and then also
// a list of authObjects etc just to keep track
// for now w no database

public interface DataAccess {
    UserData addUser(UserData user);


}
