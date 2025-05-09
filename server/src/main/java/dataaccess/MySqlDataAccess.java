package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;


public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        System.out.println("creating mysqldataaccess");
        configureDatabase();
    }

    @Override
    public void addUser(UserData user) {
        System.out.println("inserting user...");
        var query = "INSERT INTO usertable (username, password, email) VALUES (?, ?, ?)";

        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);{

                stmt.setString(1, user.username());
                stmt.setString(2, hashedPassword);
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
    public int addGame(String gameName){
        System.out.println("inserting game..");
        int gameNum = -1;

        var query = "INSERT INTO gametable (whiteUsername, blackUsername, gameName, ChessGame) VALUES (?, ?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);{

                // Convert ChessGame object to JSON
                ChessGame chessGame = new ChessGame();
                Gson gson = new Gson();
                String chessGameJson = gson.toJson(chessGame);

                stmt.setString(1, null);
                stmt.setString(2, null);
                stmt.setString(3, gameName);
                stmt.setString(4, chessGameJson);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted <= 0) {
                    return -1;
                }
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        gameNum = rs.getInt(1);
                    }
                }

                // get gameId and return that
                return gameNum;
            }
        } catch (SQLException | DataAccessException e) {
            return -1;
        }
    }

    @Override
    public boolean gameIsOver(int gameId) {
        System.out.println("checking if game is over");
        var query = "SELECT GameOver FROM gametable WHERE gameID=?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(query);

            stm.setInt(1, gameId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                System.out.println("returning something in gameover");
                System.out.println("gameover: " + rs.getBoolean("GameOver"));

                return rs.getBoolean("GameOver");
            }

            System.out.println("nothing found but the try worked");



        } catch (SQLException | DataAccessException e) {
            System.out.println("so that didnt work");
            throw new RuntimeException(e);
        }
        return false;
    }



    @Override
    public GameData getGame(int gameId){
        System.out.println("in getgame...");
        var query = "SELECT gameID, whiteUsername, blackUsername, gameName, ChessGame FROM gametable WHERE gameID=?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(query);

            stm.setInt(1, gameId);
            ResultSet rs = stm.executeQuery();

            if (rs.next() && rs.getString("gameID") != null) {
                System.out.println("returning something");
                System.out.println("gameName: " + rs.getString("gameName"));

                String chessGameString = rs.getString("ChessGame");
                Gson gson = new Gson();
                ChessGame chessGame = gson.fromJson(chessGameString, ChessGame.class);

                return new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                        rs.getString("blackUsername"), rs.getString("gameName"), chessGame);
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("get game didnt work");
            throw new RuntimeException(e);
        }


        return null;
    }


    @Override
    public void makeMoveDataBase(ChessGame game, int gameId) {

        System.out.println("updating gametable w new move!");


        System.out.println("team in database updating: " + game.getTeamTurn());

        var query = "UPDATE gametable SET ChessGame = ? WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                Gson gson = new Gson();
                String chessGameJson = gson.toJson(game);
                stmt.setString(1, chessGameJson);stmt.setInt(2, gameId);
                stmt.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            System.out.println("problem in making move in database");
        }


    }

    @Override
    public boolean joinGame(GameData game, ChessGame.TeamColor teamColor, String username){
        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() == null) {
                // if whiteUsername is open, update it
                var query = "UPDATE gametable SET whiteUsername = ? WHERE gameID = ?";
                return updateColorUsername(game, username, query);
            }
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() == null) {
                // if blackUsername is open, update it
                var query = "UPDATE gametable SET blackUsername = ? WHERE gameID = ?";
                return updateColorUsername(game, username, query);
            }
        }
        return false;
    }

    @Override
    public void playerLeaveGame(int gameId, ChessGame.TeamColor userColor) throws DataAccessException {
        System.out.println("leaving game in database");
        var query = "";
        if (userColor == ChessGame.TeamColor.WHITE) {
            query = "UPDATE gametable SET whiteusername = NULL WHERE gameID = ?";
        } else if (userColor == ChessGame.TeamColor.BLACK) {
            query = "UPDATE gametable SET blackusername = NULL WHERE gameID = ?";
        }

            try (var conn = DatabaseManager.getConnection()) {

                System.out.println("trying to leave");
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setInt(1, gameId);
                stmt.executeUpdate();


            } catch (SQLException e) {
                System.out.println("eh she didnt work");
                throw new RuntimeException(e);
            }

    }

    @Override
    public void endGame(int gameId) {
        var query = "UPDATE gametable SET GameOver = true WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, gameId);
            stmt.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }


    }

    private boolean updateColorUsername(GameData game, String username, String query) {
        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, username);
            stmt.setInt(2, game.gameID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public ArrayList<GameData> listGames(){
        ArrayList<GameData> gameList = new ArrayList<>();

        var query = "SELECT * FROM gametable";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(query);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String chessGameString = rs.getString("ChessGame");
                Gson gson = new Gson();
                ChessGame chessGame = gson.fromJson(chessGameString, ChessGame.class);
                GameData currGameData = new GameData(rs.getInt("gameID"),
                        rs.getString("whiteUsername"), rs.getString("blackUsername"),
                        rs.getString("gameName"), chessGame);
                gameList.add(currGameData);
            }

            return gameList;
        } catch (SQLException | DataAccessException e) {
            System.out.println("get user didnt work");
            throw new RuntimeException(e);
        }

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
    public AuthData getAuth(String givenAuthToken) {
        System.out.println("in getauth...");

        System.out.println("looking for authtoken: " + givenAuthToken);

        var query = "SELECT authToken, username FROM authtable WHERE authToken=?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(query);

            stm.setString(1, givenAuthToken);
            ResultSet rs = stm.executeQuery();
            System.out.println("query executed..");
            if (rs.next()) {
                System.out.println("returning something");
                System.out.println("username: " + rs.getString("username"));
                return new AuthData(rs.getString("authToken"), rs.getString("username"));
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("get auth didnt work");
            throw new RuntimeException(e);
        }
        System.out.println("returning null from getauth");
        return null;


    }



    @Override
    public void deleteAuth(AuthData authObj){
        System.out.println("in clear user list");
        var query = "DELETE FROM authtable WHERE authToken=?";

        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, authObj.authToken());
            stmt.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            CREATE TABLE IF NOT EXISTS gametable (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256),
            `blackUsername` varchar(256),
            `gameName` varchar(256) NOT NULL,
            `ChessGame` JSON NOT NULL,
            `GameOver` boolean default false,
            UNIQUE (`gameID`) )
""",
            """
            CREATE TABLE IF NOT EXISTS authtable (
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            UNIQUE (`authToken`) )
""",
            """
            CREATE TABLE IF NOT EXISTS usertable (
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
