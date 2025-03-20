package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;


import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public static class createObj {
        public String authToken;
        public String gameName;
        public createObj(String authToken, String gameName) {
            this.authToken = authToken;
            this.gameName = gameName;
        }
    }
    public static class ListGameObj {
        public int gameID;
        public String whiteUsername;
        public String blackUsername;
        public String gameName;
    }

    public static class JoinGameObj {
        public ChessGame.TeamColor playerColor;
        public int gameID;
        public JoinGameObj(ChessGame.TeamColor playerColor, int gameID) {
            this.playerColor = playerColor;
            this.gameID = gameID;
        }
    }

    public static class joinObj {
        public JoinGameObj joinGameObj;
        public String authToken;
        public joinObj(JoinGameObj joinGameObj, String authToken) {
            this.joinGameObj = joinGameObj;
            this.authToken = authToken;
        }
    }

        public AuthData registerUser(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData loginUser(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public boolean logoutUser(String authToken) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, authToken, boolean.class);
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        var path = "/game";
        // TODO check this with the two params
        createObj newCreate = new createObj(authToken, gameName);
        return this.makeRequest("POST", path, newCreate, int.class);
    }

    public ArrayList listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, authToken, ArrayList.class);
    }

    public int joinGame(JoinGameObj joinObj, String authToken) throws ResponseException {
        var path = "/game";

        joinObj newJoin = new joinObj(joinObj, authToken);

        return this.makeRequest("PUT", path, newJoin, int.class);
    }

    public void clearAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
