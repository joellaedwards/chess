package dataaccess;
import chess.ChessGame;
import model.*;
import java.util.ArrayList;
// idk maybe make a whole list of users and then also
// a list of authObjects etc just to keep track
// for now w no database

public interface DataAccess {


    void addUser(UserData user) throws Exception;
    UserData getUser(String username);

    int addGame(String gameName);
    GameData getGame(int gameId);

    void makeMoveDataBase(ChessGame game, int gameId);
    void endGame(int gameId);

    boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username);
    ArrayList<GameData> listGames();

    AuthData addAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(AuthData authObj);



    void clearUserList();
    void clearAuthList();
    void clearGameList();

}
