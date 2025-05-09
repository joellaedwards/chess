package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


// When a user begins playing or observes a game, their client should establish a WebSocket connection with the server.
// doesnt need it for like register etc i dont think.


public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            Gson gson = new GsonBuilder().registerTypeAdapter(ServerMessage.class, new Deserializer()).create();
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    notificationHandler.notify(serverMessage);
                }
            });

        } catch (URISyntaxException | DeploymentException | IOException e){
            System.out.println("error in websocketfacade initialization");
            throw new ResponseException(500, e.getMessage());
        }
    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }



    public void makeMove(String currAuthToken, int gameId, ChessMove chessMove) {
        try {
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, currAuthToken, gameId, chessMove);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void connectToGame(String currAuthToken, int gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currAuthToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

    }

    public void leaveCurrGame(String currAuthToken, int gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, currAuthToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resignFromGame(String currAuthToken, int gameId) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, currAuthToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
