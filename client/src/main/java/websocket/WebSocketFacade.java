package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
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

            System.out.println("original url: " + url);
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            System.out.println("new url: " + socketURI);
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            Gson gson = new GsonBuilder().registerTypeAdapter(ServerMessage.class, new Deserializer()).create();
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println("in this other onMessage dont be too excited");
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    System.out.println("servermessagetype: " + serverMessage.getServerMessageType());
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

    public void connectToGame(String currAuthToken, int gameId) throws ResponseException {
        try {
            System.out.println("in connectToGame in facade");
//            System.out.println("session: " + this.session);
//
//            if (this.session.isOpen()) {
//                System.out.println("session is open!");
//            }
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currAuthToken, gameId);
            System.out.println("json sent: ");
            System.out.println(new Gson().toJson(command));

            System.out.println("sending to handler");
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

    }

    public void leaveCurrGame(String currAuthToken, int gameId) throws ResponseException {
        try {
            System.out.println("leaving curr game from facade");
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
