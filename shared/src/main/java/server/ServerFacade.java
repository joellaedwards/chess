package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;


import java.io.*;
import java.net.*;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public static class JoinGameObj {
        public ChessGame.TeamColor playerColor;
        public int gameID;
        public JoinGameObj(ChessGame.TeamColor playerColor, int gameID) {
            this.playerColor = playerColor;
            this.gameID = gameID;
        }
    }

        public AuthData registerUser(UserData user) {
        var path = "/user";
        try {
            return this.makeRequest("POST", path, user, AuthData.class, null);
        } catch (ResponseException ex) {
//            System.out.println("ResponseException: " + ex);
            return null;
        }
    }

    public AuthData loginUser(UserData user) {
        var path = "/session";
        try {
            return this.makeRequest("POST", path, user, AuthData.class, null);
        } catch (ResponseException ex) {
//            System.out.println("ResponseException: " + ex);
            return null;
        }
    }

    public Object logoutUser(String authToken) {
        var path = "/session";
        try {
            return this.makeRequest("DELETE", path, null, Object.class, authToken);
        } catch (ResponseException ex) {
//            System.out.println("ResponseException: " + ex);
            return null;
        }
    }

    public Object createGame(String authToken, String gameName) throws ResponseException {
        var path = "/game";
//        System.out.println("in checkgame serverfacade");
        GameData gameData = new GameData(0, null, null, gameName, null);
        try {
            return this.makeRequest("POST", path, gameData, Object.class, authToken);
        } catch (ResponseException ex) {
//            System.out.println("Response exception in createGame: " + ex);
            return null;
        }
    }

    public Object listGames(String authToken) throws ResponseException {
        var path = "/game";
        try {
            return this.makeRequest("GET", path, null, Object.class, authToken);
        } catch (ResponseException ex) {
//            System.out.println("ResponseException in listGames: " + ex);
            return null;
        }
    }

    public Object joinGame(JoinGameObj joinObj, String authToken) throws ResponseException {
        var path = "/game";

        try {
//            System.out.println("trying makerequest in joingame");
            return this.makeRequest("PUT", path, joinObj, Object.class, authToken);
        } catch (ResponseException ex) {
//            System.out.println("ResponseException in joinGame: " + ex);
            return null;
        }
    }


    public void clearAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
//        System.out.println("making request");
        try {

            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null && !authToken.isEmpty()) {
                http.setRequestProperty("Authorization", authToken);
                // "Bearer" is used for OAuth2, but it can be different depending on your server setup
//                System.out.println("Authorization header set: " + authToken);
            }

            writeBody(request, http);
//            System.out.println("request after writeBody: " + request);
            http.connect();
//            System.out.println("after connect: " + request);
//            System.out.println("full http: " + http);
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
//            System.out.println("auth token in write body: " + request);
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
//            System.out.println("reqData: " + reqData);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
//            System.out.println("httpurlconection: " + http);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
//        System.out.println("in throw if not successful");
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
//            System.out.println("not successful status");
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
//        System.out.println("in read body");
        T response = null;
        if (http.getContentLength() < 0) {
//            System.out.println("passed first if");
            try (InputStream respBody = http.getInputStream()) {
//                System.out.println("trying!");
//                System.out.println("respBody: " + respBody);
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
//                    System.out.println("response here: ");
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
