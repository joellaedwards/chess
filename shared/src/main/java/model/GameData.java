package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

//    public GameData setGameID(int gameID) {
//        return new GameData(gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
//    }

    public GameData setWhite(String whiteUsername) {
        return new GameData(this.gameID, whiteUsername, this.blackUsername, this.gameName, this.game);
    }

    public GameData setBlack(String blackUsername) {
        return new GameData(this.gameID, this.whiteUsername, blackUsername, this.gameName, this.game);
    }

//    public GameData setGameName(String gameName) {
//        return new GameData(this.gameID, this.whiteUsername, this.blackUsername, gameName, this.game);
//    }

    public GameData setGame(ChessGame game) {
        return new GameData(this.gameID, this.whiteUsername, this.blackUsername, this.gameName, game);
    }

}