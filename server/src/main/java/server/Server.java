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
        // /user is for registering

        // provide method, path, and functional interface method
        // path can have variables, designate that w a :
//        Spark.post("/user", (req, res) -> new handler.UserHandler().registerHandler(req.body()));

        Spark.post("/user", this::registerUser);
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // hi this is a handler
    private Object registerUser(Request req, Response res) {
        // create user that has all the info from request but this actually
        // wont really work, you'll need to create probably a
        // different kind of object w just username and password
        // OH NO SIKE UR GOOD leave it as UserData.class
        var user = new Gson().fromJson(req.body(), UserData.class);

        // then use that user w all the right data to call this
        // and it will return the response data and call it user
        // but it'll end up being username and authtoken as long
        // as it's successful
        DataAccess dataAccess = new InMemoryDataAccess();

        try {
            var registeredInfo = new UserService(dataAccess).registerUser(user);


            if (registeredInfo != null) {
                // success!
                return new Gson().toJson(registeredInfo);
            } else {
                Map<String, String> messageMap = Map.of("message", "Error: already taken");
                return new Gson().toJson(messageMap);
            }
        } catch (Error e) {
            Map<String, String> messageMap = Map.of("message", "Error: bad request");
            return new Gson().toJson(messageMap);
        }



//        if (registeredInfo instanceof AuthData) {
//            return registeredInfo;
//        }
        // ELSE tbh i want the other things to just deal with this so then
        // i can be like yeah else just return what we got here

        // make it pretty and returnnn success or failure message
        // and all that goes w it
    }

}