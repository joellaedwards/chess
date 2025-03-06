package server;
import com.google.gson.Gson;

import dataaccess.*;
import model.*;
import service.*;
import spark.*;
import java.util.Map;


public class Server {
//    private final UserService service;

//    public Server(UserService service) {
//        this.service = service;
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // provide method, path, and functional interface method
        // path can have variables, designate that w a :

        Spark.post("/user", this::registerUser);
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
        System.out.println("inside registerUser in the server");
        var user = new Gson().fromJson(req.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            res.status(400);
            Map<String, String> messageMap = Map.of("message", "Error: bad request");
            return new Gson().toJson(messageMap);
        }


        System.out.println("entering try catch");
        try {
            System.out.println("inside try");
            var registeredInfo = new UserService(dataAccess).registerUser(user);
            System.out.println("registeredInfo: " + registeredInfo);

            if (registeredInfo != null) {
                // success!
                System.out.println("success! not null");
                res.status(200);
                System.out.println("gson stuff: " + new Gson().toJson(registeredInfo));
                return new Gson().toJson(registeredInfo);
            } else {
                System.out.println("already taken");
                res.status(403);
                Map<String, String> messageMap = Map.of("message", "Error: already taken");
                return new Gson().toJson(messageMap);
            }
        } catch (Error e) {
            System.out.println("catch uh oh");
            res.status(500);
            Map<String, String> messageMap = Map.of("message", "Error: " + e);
            return new Gson().toJson(messageMap);
        }
    }



    private Object clearAll(Request req, Response res) {
        System.out.println("inside clearAll in the server");
        // if clear return success else return 500



        System.out.println("entering clear try catch");
        try {
            System.out.println("inside clear try");

            new UserService(dataAccess).clearUsers();
            new AuthService(dataAccess).clearAuth();
            new GameService(dataAccess).clearGames();

            res.status(200);
            return new Gson().toJson("");

        } catch (Error e) {
            System.out.println("catch uh oh");
            res.status(500);
            Map<String, String> messageMap = Map.of("message", "Error: " + e);
            return new Gson().toJson(messageMap);
        }
    }







}