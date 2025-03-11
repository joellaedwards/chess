package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
// idk maybe make a whole list of users and then also
// a list of authObjects etc just to keep track
// for now w no database

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        System.out.println("creating mysqldataaccess");
        configureDatabase();
    }

    // TODO these should be implementations
    @Override
    public void addUser(UserData user) {

    }
    @Override
    public UserData getUser(String username) {
        return null;
    }
    @Override
    public ArrayList<UserData> listUsers() {
        return null;
    }
    @Override
    public int addGame(String gameName){
        return 0;
    }
    @Override
    public GameData getGame(int gameId){
        return null;
    }
    @Override
    public boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username){
        return true;
    }
    @Override
    public ArrayList<GameData> listGames(){
        return null;
    }

    @Override
    public AuthData addAuth(String username){
        return null;
    }
    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }
    @Override
    public void deleteAuth(AuthData authObj){

    }


    @Override
    public void clearUserList(){

    }
    @Override
    public void clearAuthList(){

    }
    @Override
    public void clearGameList(){

    }


    final String[] createStatements = {
            // gameID autoincrement
            // whiteUsername string can be null
            // blackUsername string can be null
            // gamename string
            // game ChessGame

            """
            CREATE TABLE IF NOT EXISTS gameTable (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256),
            `blackUsername` varchar(256),
            `gameName` varchar(256) NOT NULL,
            `ChessGame` JSON NOT NULL,
            PRIMARY KEY (`gameID`) )
""",
            """
            CREATE TABLE IF NOT EXISTS authTable (
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`username`) )
""",
            """
            CREATE TABLE IF NOT EXISTS userTable (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`username`) )
"""

    };


    private void configureDatabase() throws DataAccessException {
        System.out.println("in configdatabase");
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("error configuring database");
        }
    }
}
