package ui;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import server.ServerFacade;


public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
