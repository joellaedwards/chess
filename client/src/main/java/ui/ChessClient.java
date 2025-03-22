package ui;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import server.ServerFacade;
import model.*;

import java.util.Arrays;


public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


    public String eval(String input) throws ResponseException {
        System.out.println("input: " + input);
        var tokens = input.toLowerCase().split(" ");
        System.out.println("Toekns length: " + tokens.length);
        for (var token : tokens) {
            System.out.println("token here: " + token);
        }
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            default -> help();
        };
    }

    public String register(String... params) throws ResponseException {
        System.out.println("register within chessClient!!");
        if (params.length >= 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            AuthData auth = server.registerUser(user);
            if (auth != null) {
                state = State.SIGNEDIN;
                System.out.println("registered and signed in!!");
                return "success";
            }
        }
        else {
            return "make sure to enter username email and password";
        }
        return null;
    }

    public String login(String... params) throws ResponseException {
        System.out.println("login within chessClient!");
        if (params.length >= 2) {
            UserData user = new UserData(params[0], params[1], null);
            AuthData auth = server.loginUser(user);
            if (auth != null) {
                state = State.SIGNEDIN;
                System.out.println("logged in!");
                return "success";
            }
        }
        return "please enter a valid username and password";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - Register <username> <password> <email>
                    - Login <username> <password>
                    - Quit playing chess
                    - Help
                    """;
            // Help - Displays text informing user what actions to take

            // Quit exists program

            // Register - prompts user to input registration info. if success
            // client should be logged in and go to postlogin UI
            //
        }
        else if (state == State.SIGNEDIN) {
            return """
                    - Logout
                    - Create Game <GameName>
                    - List Games
                    - Play Game <Game Number> <Color>
                    
                    - Help
                    """;
        }
        return """
                omg youre signed in yay!
                """;
    }



}
