package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

import java.io.IOException;

// make a list of current games. have it hold gameId. cause then that has
// to somehow be together w the game id they enter. check it through.
// maybe literally just have it in the list and then gameid that they enter
// is just the num/index in the list

// have list of games hold the actual ChessGame object.

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand.getAuthToken(), userGameCommand.getGameID());
            // idk there's probs some type of string of params or something here.
            // test what comes through.
            case MAKE_MOVE -> makeMove(userGameCommand.getAuthToken(), userGameCommand.getGameID(), move);
//            case LEAVE -> leaveGame;
//            case RESIGN -> resign;
        }
    }


    private void connect(String authToken, Session session) {
        connections.add(authToken, session);

        // connect to the game in the whole huge listOfChessGames list
        // that corresponds in one way or another to the gameId


        // oops no jk that's something u should do in the create/join game moment
        // here is just for connecting to the WebSocket. for player but also for observer

    }

    private void makeMove(String authToken, int gameId, ChessMove move) {

    }


}
