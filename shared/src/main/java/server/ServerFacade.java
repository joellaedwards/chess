package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


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

        public Object registerUser(UserData user) throws ResponseException {
        var path = "/user";
        try {
            return this.makeRequest("POST", path, user, AuthData.class, null);
        } catch (ResponseException ex) {
            return "User already registered.";
        }
    }

    public AuthData loginUser(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public Object logoutUser(String authToken) throws ResponseException {
        var path = "/session";
        System.out.println("authtoken hereee: " + authToken);
        return this.makeRequest("DELETE", path, null, Object.class, authToken);
    }

    public Object createGame(String authToken, String gameName) throws ResponseException {
        var path = "/game";
        // TODO check this with the two params
        System.out.println("in checkgame serverfacade");

        GameData gameData = new GameData(0, null, null, gameName, null);

        return this.makeRequest("POST", path, gameData, Object.class, authToken);
    }

    public ArrayList listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, authToken, ArrayList.class, null);
    }

    public int joinGame(JoinGameObj joinObj, String authToken) throws ResponseException {
        var path = "/game";
        // TODO check what this returns in the server fr
        joinObj newJoin = new joinObj(joinObj, authToken);

        return this.makeRequest("PUT", path, newJoin, int.class, null);
    }

    public void clearAll() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        System.out.println("making request");
        System.out.println("authtoken in makerequest " + authToken);
        try {

            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null && !authToken.isEmpty()) {
                http.setRequestProperty("Authorization", authToken);  // "Bearer" is used for OAuth2, but it can be different depending on your server setup
                System.out.println("Authorization header set: " + authToken);
            }

            writeBody(request, http);
            System.out.println("request after writeBody: " + request);
            http.connect();
            System.out.println("after connect: " + request);
            System.out.println("full http: " + http);
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
            System.out.println("auth token in write body: " + request);
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            System.out.println("reqData: " + reqData);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
//            System.out.println("httpurlconection: " + http);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        System.out.println("in throw if not successful");
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            System.out.println("not successful status");
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        System.out.println("in read body");
        T response = null;
        if (http.getContentLength() < 0) {
            System.out.println("passed first if");
            try (InputStream respBody = http.getInputStream()) {
                System.out.println("trying!");
                System.out.println("respBody: " + respBody);
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
//                    System.out.println("responseClass not null");
//                    System.out.println("reader: " + reader);
//                    BufferedReader bufferedReader = new BufferedReader(reader);

//                    String responseBody = bufferedReader.lines().collect(Collectors.joining());
//                    System.out.println("raw reader: " + responseBody);
//                    System.out.println("responseClass: " + responseClass);
                    response = new Gson().fromJson(reader, responseClass);
                    System.out.println("response here: ");
                }
            }
        }
//        System.out.println("response in readBody: " + response);
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
