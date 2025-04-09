package websocket;

import chess.ChessGame;
import com.google.gson.*;
import websocket.messages.ServerMessage;

import java.lang.reflect.Type;

public class Deserializer implements JsonDeserializer<ServerMessage> {


    @Override
    public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ServerMessage serverMessage = null;
        JsonObject obj = jsonElement.getAsJsonObject();
        String messageType = obj.get("serverMessageType").getAsString();
        if("LOAD_GAME".equals(messageType)) {
            System.out.println("in loadgame");
            String gameName = obj.get("game").getAsString();
            Gson gson = new Gson();
            ChessGame chessGame = gson.fromJson(obj.get("chessGame"), ChessGame.class);
            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameName, null, null, chessGame);
            System.out.println("servermessage game name here: " + serverMessage.getGameName());
        } else if ("ERROR".equals(messageType)) {
            String errorMessage = obj.get("errorMessage").getAsString();
            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, errorMessage, null);
        } else if ("NOTIFICATION".equals(messageType)) {
            String message = obj.get("message").getAsString();
            Gson gson = new Gson();
            ChessGame chessGame = gson.fromJson(obj.get("chessGame"), ChessGame.class);
            serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, message, null, chessGame);
        }
        assert serverMessage != null;
        System.out.println("at end of deserialize: " + serverMessage.getServerMessageType());
        System.out.println("load gamename");
        System.out.println(serverMessage.getGameName());
        return serverMessage;
    }
}
