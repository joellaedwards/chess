package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import server.ServerFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.util.Map;

// make a list of current games. have it hold gameId. cause then that has
// to somehow be together w the game id they enter. check it through.
// maybe literally just have it in the list and then gameid that they enter
// is just the num/index in the list

// have list of games hold the actual ChessGame object.

@WebSocket
public class WebSocketHandler {

    private final server.websocket.ConnectionManager connections = new server.websocket.ConnectionManager();

    public WebSocketHandler()  {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        DataAccess dataAccess = new MySqlDataAccess();

        System.out.println("in onMessage");
        System.out.println("message: " + message);


        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand.getGameID(), userGameCommand.getAuthToken(), session, dataAccess);
            // idk there's probs some type of string of params or something here.
            // test what comes through.
            case MAKE_MOVE -> makeMove(makeMoveCommand.getAuthToken(), makeMoveCommand.getGameID(), makeMoveCommand.getMove(), session, dataAccess);
//            case LEAVE -> leaveGame;
//            case RESIGN -> resign;
        }
    }

    private void makeMove(String authToken, int gameId, ChessMove move, Session session, DataAccess dataAccess) throws IOException {
        GameData currGameData = dataAccess.getGame(gameId);
        ChessGame currGame = currGameData.game();

        if (dataAccess.getAuth(authToken) == null) {
            connections.broadcast(gameId, authToken, session,false, "Invalid auth.", UserGameCommand.CommandType.MAKE_MOVE);
        } else {
            try {
                System.out.println("try make move");
                currGame.makeMove(move);
                connections.broadcast(gameId, authToken, session, true, "Valid move.", UserGameCommand.CommandType.MAKE_MOVE);
            } catch (InvalidMoveException e) {
                System.out.println("not a valid move");
                connections.broadcast(gameId, authToken, session,false, "Not a valid move.", UserGameCommand.CommandType.MAKE_MOVE);
            }
        }
    }


    private void connect(int gameId, String authToken, Session session, DataAccess dataAccess) throws IOException {
        System.out.println("inside connect");
        System.out.println("gameid in connect: " + gameId);



        if (connections.add(gameId, authToken, session, dataAccess)) {
            System.out.println("should be loading game now..");
            connections.broadcast(gameId, authToken, session, true, authToken + "joined your game!", UserGameCommand.CommandType.CONNECT);
        }
        else {
            System.out.println("gameid not found");
            connections.broadcast(gameId, authToken, session, false, "gameid not found.", UserGameCommand.CommandType.CONNECT);
        }

        // connect to the game in the whole huge listOfChessGames list
        // that corresponds in one way or another to the gameId

        // connections should maybe be a hashmap that has the game id as key
        // and then the vals can be like white: black: observer


        // oops no jk that's something u should do in the create/join game moment
        // here is just for connecting to the WebSocket. for player but also for observer

    }




}
