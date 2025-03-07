package dataaccess;
import chess.ChessGame;
import model.*;
import java.util.ArrayList;
// idk maybe make a whole list of users and then also
// a list of authObjects etc just to keep track
// for now w no database

public interface DataAccess {


    void addUser(UserData user);
    ArrayList<UserData> listUsers();

    ArrayList<GameData> listGames();

    UserData getUser(String username);


    AuthData getAuth(String authToken);
    void deleteAuth(AuthData authObj);
    AuthData addAuth(String username);
//    ArrayList<AuthData> listAuth();

    int addGame(String gameName);


    void clearUserList();
    void clearAuthList();
    void clearGameList();

    GameData getGame(int gameId);
    boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username);
}
