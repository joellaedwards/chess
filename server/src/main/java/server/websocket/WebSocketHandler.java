package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;


import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final server.websocket.ConnectionManager connections = new server.websocket.ConnectionManager();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        System.out.println("in onMessage");

        DataAccess dataAccess = new MySqlDataAccess();

        System.out.println("message: " + message);


        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand.getGameID(), userGameCommand.getAuthToken(), session, dataAccess);
            case MAKE_MOVE -> makeMove(makeMoveCommand.getAuthToken(), makeMoveCommand.getGameID(),
                    makeMoveCommand.getMove(), session, dataAccess);
            case LEAVE -> leaveGame(userGameCommand.getGameID(), userGameCommand.getAuthToken(), session, dataAccess);
            case RESIGN -> resign(userGameCommand.getGameID(), userGameCommand.getAuthToken(), session, dataAccess);
        }
    }



    private int leaveGame(int gameId, String authToken, Session session, DataAccess dataAccess) throws IOException,
            DataAccessException {
        System.out.println("leaving game from handler!!");
        if (dataAccess.getAuth(authToken) == null) {
            connections.broadcast(gameId, authToken, session,false, "Invalid auth.",
                    UserGameCommand.CommandType.LEAVE, null, null);
        }
        else {
            AuthData currAuth = dataAccess.getAuth(authToken);
            GameData currGame = dataAccess.getGame(gameId);
            if (Objects.equals(currGame.blackUsername(), currAuth.username())) {
                dataAccess.playerLeaveGame(gameId, ChessGame.TeamColor.BLACK);
                System.out.println("broadcasting!");
                connections.broadcast(gameId, authToken, session, true, currAuth.username() +
                        " left your game", UserGameCommand.CommandType.LEAVE, null, null);
            }
            else if (Objects.equals(currGame.whiteUsername(), currAuth.username())) {
                dataAccess.playerLeaveGame(gameId, ChessGame.TeamColor.WHITE);
                System.out.println("broadcasting!");
                connections.broadcast(gameId, authToken, session, true, currAuth.username() +
                        " left your game", UserGameCommand.CommandType.LEAVE, null, null);
            }
            else {
                connections.broadcast(gameId, authToken, session, true, currAuth.username() +
                        " left your game", UserGameCommand.CommandType.LEAVE, null, null);

            }

        }
        return 0;
    }

    private int resign(int gameId, String authToken, Session session, DataAccess dataAccess) throws IOException {
        if (dataAccess.getAuth(authToken) == null) {
            connections.broadcast(gameId, authToken, session,false, "Invalid auth.",
                    UserGameCommand.CommandType.RESIGN, null, null);
        }
        else if (dataAccess.gameIsOver(gameId)) {
            connections.broadcast(gameId, authToken, session, false, "Game over.",
                    UserGameCommand.CommandType.RESIGN, null, null);
        } else {
            GameData currGameData = dataAccess.getGame(gameId);
            AuthData currAuth = dataAccess.getAuth(authToken);

            if (!Objects.equals(currAuth.username(), currGameData.blackUsername()) &&
                    !Objects.equals(currAuth.username(), currGameData.whiteUsername())) {
                connections.broadcast(gameId, authToken, session, false, "Cannot resign as an observer.",
                        UserGameCommand.CommandType.RESIGN, null, null);
                return 0;
            }
            dataAccess.endGame(gameId);
            connections.broadcast(gameId, authToken, session, true, currAuth.username() +
                    " resigned. Game over.", UserGameCommand.CommandType.RESIGN, null, null);
        }
        return 0;
    }


    private int makeMove(String authToken, int gameId, ChessMove move, Session session, DataAccess dataAccess)
            throws IOException {
        GameData currGameData = dataAccess.getGame(gameId);
        ChessGame currGame = currGameData.game();
        String gameName = currGameData.gameName();

        // send notification to all clients.
        if (dataAccess.getAuth(authToken) == null) {
            connections.broadcast(gameId, authToken, session,false, "Invalid auth.",
                    UserGameCommand.CommandType.MAKE_MOVE, null, null);
        } else {

            boolean isOver = dataAccess.gameIsOver(gameId);
            if (isOver) {
                connections.broadcast(gameId, authToken, session, false, "Game is over you cant move.",
                        UserGameCommand.CommandType.MAKE_MOVE, null, null);
                return 1;
            }

            AuthData currUser = dataAccess.getAuth(authToken);

            if (currGame.getTeamTurn() == ChessGame.TeamColor.BLACK) {
                if (!Objects.equals(currGameData.blackUsername(), currUser.username())) {
                    connections.broadcast(gameId, authToken, session, false, "Not your turn.",
                            UserGameCommand.CommandType.MAKE_MOVE, null, null);
                    return 0;
                }
            } else {
                if (!Objects.equals(currGameData.whiteUsername(), currUser.username())) {
                    connections.broadcast(gameId, authToken, session, false, "Not your turn.",
                            UserGameCommand.CommandType.MAKE_MOVE, null, null);
                    return 0;
                }
            }
            if (currGame.getBoard().getPiece(move.getStartPosition()) == null) {
                connections.broadcast(gameId, authToken, session, false,
                        "No piece found at starting position.", UserGameCommand.CommandType.MAKE_MOVE,
                        null, null);
                return 0;
            }
            if (currGame.getBoard().getPiece(move.getStartPosition()).getTeamColor() != currGame.getTeamTurn()) {
                connections.broadcast(gameId, authToken, session, false, "That isn't your piece.",
                        UserGameCommand.CommandType.MAKE_MOVE, null, null);
                return 0;
            }

            try {
                currGame.makeMove(move);
                GameData dataGame = dataAccess.getGame(gameId);
                ChessGame gamefromData = dataGame.game();

                if (currGame.isInCheckmate(ChessGame.TeamColor.WHITE) ||
                        currGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(gameId, authToken, session, true, currUser.username() +
                            " moved " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString(),
                            UserGameCommand.CommandType.MAKE_MOVE, gameName, gamefromData);
                    connections.broadcast(gameId, authToken, session, true, "In checkmate. Game over.",
                            null, null, null);
                    dataAccess.endGame(gameId);
                    return 0;

                } else if (currGame.isInStalemate(ChessGame.TeamColor.WHITE) ||
                        currGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(gameId, authToken, session, true, currUser.username() +
                            " moved " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString(),
                            UserGameCommand.CommandType.MAKE_MOVE, gameName, gamefromData);
                    connections.broadcast(gameId, authToken, session, true, "In stalemate. Game over.",
                            null, null, null);
                    dataAccess.endGame(gameId);
                    return 0;
                } else if (currGame.isInCheck(ChessGame.TeamColor.WHITE) ||
                        currGame.isInCheck(ChessGame.TeamColor.BLACK)) {
                    connections.broadcast(gameId, authToken, session, true, currUser.username() +
                            " moved " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString(),
                            UserGameCommand.CommandType.MAKE_MOVE, gameName, gamefromData);
                    connections.broadcast(gameId, authToken, session, true, "In check.", null,
                            null, null);
                }
                dataAccess.makeMoveDataBase(currGame, gameId);

                dataGame = dataAccess.getGame(gameId);
                gamefromData = dataGame.game();
                connections.broadcast(gameId, authToken, session, true, currUser.username() + " moved " +
                        move.getStartPosition().toString() + " to " + move.getEndPosition().toString(),
                        UserGameCommand.CommandType.MAKE_MOVE, gameName, gamefromData);
                return 0;
            } catch (InvalidMoveException e) {
                connections.broadcast(gameId, authToken, session,false, "Not a valid move.",
                        UserGameCommand.CommandType.MAKE_MOVE, null, null);
                return 0;
            }
        }
        return 0;
    }


    private void connect(int gameId, String authToken, Session session, DataAccess dataAccess) throws IOException {
        System.out.println("inside connect");
        System.out.println("gameid in connect: " + gameId);

        if (connections.add(gameId, authToken, session, dataAccess)) {
            AuthData authData = dataAccess.getAuth(authToken);
            String username = authData.username();
            GameData gameData = dataAccess.getGame(gameId);
            if (Objects.equals(gameData.whiteUsername(), username)) {
                connections.broadcast(gameId, authToken, session, true, username +
                        " joined your game as white.", UserGameCommand.CommandType.CONNECT, gameData.gameName(),
                        gameData.game());
            } else if (Objects.equals(gameData.blackUsername(), username)) {
                connections.broadcast(gameId, authToken, session, true, username +
                        " joined your game as black.", UserGameCommand.CommandType.CONNECT, gameData.gameName(),
                        gameData.game());
            } else {
                connections.broadcast(gameId, authToken, session, true, username +
                        " joined your game as an observer.", UserGameCommand.CommandType.CONNECT, gameData.gameName(),
                        gameData.game());

            }
        }
        else {
            System.out.println("gameid not found");
            connections.broadcast(gameId, authToken, session, false, "gameid not found.",
                    UserGameCommand.CommandType.CONNECT, null, null);
        }
    }

}
