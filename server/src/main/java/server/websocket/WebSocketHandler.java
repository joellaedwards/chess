package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import org.eclipse.jetty.websocket.api.Session;
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


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("in onMessage");
        System.out.println("message: " + message);
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand.getGameID(), userGameCommand.getAuthToken(), session);
            // idk there's probs some type of string of params or something here.
            // test what comes through.
//            case MAKE_MOVE -> makeMove(userGameCommand.getAuthToken(), userGameCommand.getGameID(), move);
//            case LEAVE -> leaveGame;
//            case RESIGN -> resign;
        }
    }


    private void connect(int gameId, String authToken, Session session) throws IOException {
        System.out.println("inside connect");
        connections.add(gameId, authToken, session);
        Map<String, String> messageMap = Map.of("message", "bestie just got added! wohoo!");
        //        var message = String.format("%s is in the game", authToken);
        System.out.println("should be loading game now..");
//        System.out.println("length of connections: " + connections.);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "wohoo! in game!");
        connections.broadcast(gameId, notification, authToken);

        // connect to the game in the whole huge listOfChessGames list
        // that corresponds in one way or another to the gameId

        // connections should maybe be a hashmap that has the game id as key
        // and then the vals can be like white: black: observer


        // oops no jk that's something u should do in the create/join game moment
        // here is just for connecting to the WebSocket. for player but also for observer

    }

    private void makeMove(String authToken, int gameId, ChessMove move) {

    }


}
