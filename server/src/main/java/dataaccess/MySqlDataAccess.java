package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;


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
        System.out.println("inserting user...");
        var query = "INSERT INTO usertable (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);{

                stmt.setString(1, user.username());
                stmt.setString(2, user.password());
                stmt.setString(3, user.email());

                var response = stmt.executeUpdate();

                System.out.println("num rows affected: " + response);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) {

        System.out.println("in getuser...");

        var query = "SELECT username, password, email FROM usertable WHERE username=?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(query);

            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();

            if (rs.next() && rs.getString("username") != null) {
                System.out.println("returning something");
                System.out.println("username: " + rs.getString("username"));
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("get user didnt work");
            throw new RuntimeException(e);
        }
        System.out.println("returning null from getUser");
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
        System.out.println("inserting auth...");
        AuthData currAuth = new AuthData(UUID.randomUUID().toString(), username);
        var query = "INSERT INTO authtable (authtoken, username) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);{

                stmt.setString(1, currAuth.authToken());
                stmt.setString(2, currAuth.username());

                var response = stmt.executeUpdate();

                System.out.println("num rows affected: " + response);
                return currAuth;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
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
        System.out.println("in clear user list");
        var query = "TRUNCATE TABLE usertable";

        try (var conn = DatabaseManager.getConnection()) {
            Statement stmt = conn.createStatement();

            var response = stmt.executeUpdate(query);
            System.out.println("num rows deleted: " + response);

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void clearAuthList(){
        System.out.println("in clear auth list");
        var query = "TRUNCATE TABLE authtable";

        try (var conn = DatabaseManager.getConnection()) {
            Statement stmt = conn.createStatement();

            var response = stmt.executeUpdate(query);
            System.out.println("num rows deleted: " + response);

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void clearGameList(){
        System.out.println("in clear game list");
        var query = "TRUNCATE TABLE gametable";

        try (var conn = DatabaseManager.getConnection()) {
            Statement stmt = conn.createStatement();

            var response = stmt.executeUpdate(query);
            System.out.println("num rows deleted: " + response);

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            UNIQUE (`gameID`) )
""",
            """
            CREATE TABLE IF NOT EXISTS authTable (
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            UNIQUE (`authToken`) )
""",
            """
            CREATE TABLE IF NOT EXISTS userTable (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            UNIQUE (`username`) )
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
