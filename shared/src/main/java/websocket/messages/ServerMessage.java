package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String game;
    String errorMessage;
    ChessGame chessGame;
    ChessGame.TeamColor myColor;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String game, String message, String errorMessage, ChessGame chessGame) {
        this.serverMessageType = type;
        this.game = game;
        this.message = message;
        this.errorMessage = errorMessage;
        this.chessGame = chessGame;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getServerMessage() {
        if (this.errorMessage != null) {
            return this.errorMessage;
        }
        return this.message;
    }

    public String getGameName() {
        return this.game;
    }

    public ChessGame getChessGame() {
        return this.chessGame;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}