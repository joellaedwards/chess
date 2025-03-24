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
    private String currAuthToken = null;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


    public String eval(String input) throws ResponseException {
        System.out.println("input: " + input);
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (state == State.SIGNEDOUT) {
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> quit();
                default -> help();
            };
        }
        else {
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "quit" -> quit();
                default -> help();
            };
        }
    }



    public String createGame(String... params) throws ResponseException {
        System.out.println("in creategame in chess client");
        System.out.println("params length: " + params.length);
        if (params.length >= 1) {
            System.out.println("params at 1: " + params[1]);
            Object returnedGame = server.createGame(currAuthToken, params[1]);
            System.out.println("returned game: " + returnedGame.toString());
            if (returnedGame != null) {
                return "success!";
            }
        }

        return "something went wrong in create game!";
    }

    public String logout() throws ResponseException {
        System.out.println("in logout in chessclient");
        if (currAuthToken != null) {
            if (server.logoutUser(currAuthToken) != null) {
                System.out.println("logged out wohoo!");
                state = State.SIGNEDOUT;
                currAuthToken = null;
                return "successfully logged out!";
            }
        }

        return "something went wrong. try logging out again";
    }


    public String register(String... params) throws ResponseException {
        System.out.println("register within chessClient!!");
        if (params.length >= 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            AuthData auth = server.registerUser(user);
            if (auth != null) {
                currAuthToken = auth.authToken();
                state = State.SIGNEDIN;
                System.out.println("registered and signed in!!");
                return "success";
            }
        }
        else {
            currAuthToken = null;
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
                currAuthToken = auth.authToken();
                System.out.println("logged in!");
                return "success";
            }
        }
        return "please enter a valid username and password";
    }

    public String quit() {
        return "quit";
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
