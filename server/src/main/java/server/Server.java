package server;
import com.google.gson.Gson;

import dataaccess.*;
import model.*;
import service.*;
import spark.*;
import service.GameService;

import java.util.ArrayList;
import java.util.Map;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // provide method, path, and functional interface method
        // path can have variables, designate that w a :

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearAll);

        System.out.println("port: " + Spark.port());
        //This line initializes the server and can be removed once you have a functioning endpoint
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    DataAccess dataAccess = new InMemoryDataAccess();


    // hi this is a handler
    private Object registerUser(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            res.status(400);
            Map<String, String> messageMap = Map.of("message", "Error: bad request");
            return new Gson().toJson(messageMap);
        }


        try {
            var registeredInfo = new UserService(dataAccess).registerUser(user);
            if (registeredInfo != null) {
                res.status(200);
                return new Gson().toJson(registeredInfo);
            } else {
                System.out.println("already taken");
                res.status(403);
                Map<String, String> messageMap = Map.of("message", "Error: already taken");
                return new Gson().toJson(messageMap);
            }
        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
    }

    private Object loginUser(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        if (user.username() == null || user.password() == null) {
            res.status(400);
            Map<String, String> messageMap = Map.of("message", "Error: bad request");
            return new Gson().toJson(messageMap);
        }
        try {
            var loginInfo = new UserService(dataAccess).loginUser(user);
            if (loginInfo != null) {
                // success!
                System.out.println("success! not null");
                res.status(200);
                System.out.println("gson stuff: " + new Gson().toJson(loginInfo));
                return new Gson().toJson(loginInfo);
            } else {
                return dealWithUnauthorized(res);
            }
        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
    }

    private Object logoutUser(Request req, Response res) {
        String authHeader = req.headers("Authorization");
        try {
            var logoutInfo = new AuthService(dataAccess).logout(authHeader);
            if (logoutInfo) {
                System.out.println("status code 200");
                res.status(200);
                return new Gson().toJson(new Object());
            } else {
                return dealWithUnauthorized(res);
            }
        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
    }

    private Object listGames(Request req, Response res) {
        String authHeader = req.headers("Authorization");
        try {
            var gamesList = new GameService(dataAccess).listGames(authHeader);
            if (gamesList != null) {
                System.out.println("status code 200");
                res.status(200);
                Map<String, ArrayList<GameService.ListGameObj>> messageMap = Map.of("games", gamesList);
                return new Gson().toJson(messageMap);
            } else {
                return dealWithUnauthorized(res);
            }
        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
    }

    private Object createGame(Request req, Response res) {
        String authHeader = req.headers("Authorization");
        var game = new Gson().fromJson(req.body(), GameData.class);
        String gameName = game.gameName();
        if (gameName == null || authHeader == null) {
            res.status(400);
            Map<String, String> messageMap = Map.of("message", "Error: bad request");
            return new Gson().toJson(messageMap);
        }
        try {
            int newGameInfo = new GameService(dataAccess).createGame(authHeader, gameName);

            if (newGameInfo != 0) {
                // success!
                System.out.println("success! not null");
                res.status(200);
                Map<String, Integer> messageMap = Map.of("gameID", newGameInfo);
                return new Gson().toJson(messageMap);
            } else {
                return dealWithUnauthorized(res);
            }
        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
    }

    private Object joinGame(Request req, Response res) {
        String authHeader = req.headers("Authorization");
        var joinData = new Gson().fromJson(req.body(), GameService.JoinGameObj.class);
        if (authHeader == null || joinData.playerColor == null || joinData.gameID == 0) {
            res.status(400);
            Map<String, String> messageMap = Map.of("message", "Error: bad request");
            return new Gson().toJson(messageMap);
        }
        System.out.println("entering joinGame try catch");
        try {
            System.out.println("inside try");
            int joinGameInfo = new GameService(dataAccess).joinGame(joinData, authHeader);
            System.out.println("joinGame: " + joinGameInfo);

            if (joinGameInfo == 1) {
                // success!
                System.out.println("success! not null");
                res.status(200);
                return new Gson().toJson(new Object());
            } else if (joinGameInfo == 0) {
                return dealWithUnauthorized(res);
            } else if (joinGameInfo == 2) {
                return alreadyTaken(res);
            } else if (joinGameInfo == 3) {
                System.out.println("game not found");
                res.status(400);
                Map<String, String> messageMap = Map.of("message", "Error: bad request");
                return new Gson().toJson(messageMap);
            }
        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
        return null;
    }

    private Object clearAll(Request req, Response res) {
        System.out.println("inside clearAll in the server");
        System.out.println("entering clear try catch");
        try {
            System.out.println("inside clear try");

            new UserService(dataAccess).clearUsers();
            new AuthService(dataAccess).clearAuth();
            new GameService(dataAccess).clearGames();

            res.status(200);
            return new Gson().toJson(new Object());

        } catch (Error e) {
            return dealWithUnknownError(res, e);
        }
    }

    private Object dealWithUnknownError(Response res, Error e) {
        System.out.println("catch uh oh");
        res.status(500);
        Map<String, String> messageMap = Map.of("message", "Error: " + e);
        return new Gson().toJson(messageMap);
    }

    private Object dealWithUnauthorized(Response res) {
        System.out.println("unauthorized");
        res.status(401);
        Map<String, String> messageMap = Map.of("message", "Error: unauthorized");
        return new Gson().toJson(messageMap);
    }

    private Object alreadyTaken(Response res) {
        System.out.println("already taken");
        res.status(403);
        Map<String, String> messageMap = Map.of("message", "Error: already taken");
        return new Gson().toJson(messageMap);
    }
}
