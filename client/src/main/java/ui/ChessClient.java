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
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            default -> help();
        };
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            AuthData auth = server.registerUser(user);
            if (auth != null) {
                state = State.SIGNEDIN;
            }

            // do something here register and also idk if that's the
            // righ num of params
        }
        System.out.println("not enough params in register in ChessClient");
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - Login <username> <password>
                    - Register <username> <email> <password>
                    - Quit playing chess
                    - Help
                    """;
            // Help - Displays text informing user what actions to take

            // Quit exists program

            // Login - prompts user to input login info.
            // when successfully logged in transition to poslogin UI

            // Register - prompts user to input registration info. if success
            // client should be logged in and go to postlogin UI
            //
        }
        return """
                omg youre signed in yay!
                """;
    }


}
