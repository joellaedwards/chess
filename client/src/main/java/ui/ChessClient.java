package ui;

import chess.ChessGame;
import com.google.gson.internal.LinkedTreeMap;
import model.*;
import exception.ResponseException;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


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


    public Object eval(String input) throws ResponseException {
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
                case "list" -> listGames();
                case "quit" -> quit();
                case "play" -> playGame(params);
                default -> help();
            };
        }
    }


    public String playGame(String... params) throws ResponseException {
        System.out.println("in playgame");
        System.out.println("num params: " + params.length);
        if (params.length >= 3) {
            int id = Integer.parseInt(params[1]);
            String color = params[2];
            System.out.println("color: " + color);
            ChessGame.TeamColor myColor;
            if (Objects.equals(color, "white")) {
                myColor = ChessGame.TeamColor.WHITE;
            }
            else if (Objects.equals(color, "black")){
                System.out.println("color black");
                myColor = ChessGame.TeamColor.BLACK;
            }
            else {
                return "please enter a valid color";
            }
            ServerFacade.JoinGameObj joinObj = new ServerFacade.JoinGameObj(myColor, id);
            Object joinInfo = server.joinGame(joinObj, currAuthToken);

            if (joinInfo != null) {
                return "success!";
            }
            return "game not joined";
            // TODO handle if the color is already taken
        }
        return "game not joined 2";
    }


    public String listGames() throws ResponseException {

        Object listOfGames = server.listGames(currAuthToken);

        if (listOfGames instanceof LinkedTreeMap<?, ?>) {
            LinkedTreeMap<String, ArrayList<Object>> treeMap = (LinkedTreeMap<String, ArrayList<Object>>) listOfGames;
            StringBuilder fullList = new StringBuilder();
            int i = 1;
            for (Object game : treeMap.get("games")) {
                if (game instanceof LinkedTreeMap<?, ?>) {
                    LinkedTreeMap<String, String> LinkedTreeMap;
                    LinkedTreeMap<String, String> gameInfo = (LinkedTreeMap<String, String>) game;
                    String stringToAdd = i + ".   " + gameInfo.get("gameName") + ",   " + gameInfo.get("whiteUsername")
                            + ",   " + gameInfo.get("blackUsername");
                    ++i;
                    fullList.append(stringToAdd).append('\n');
                }
            }
            return fullList.toString();
        }
        return null;
    }


    public String createGame(String... params) throws ResponseException {
        System.out.println("in creategame in chess client");
        System.out.println("params length: " + params.length);
        if (params.length >= 1) {
            System.out.println("params at 1: " + params[1]);
            Object returnedGame = server.createGame(currAuthToken, params[1]);
            if (returnedGame != null) {
                System.out.println("returned game: " + returnedGame);
                return "success in creategame";
            }
        }
        return "something went wrong in create game";
    }

    public String logout() {
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


    public String register(String... params) {
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
            return "User already registered.";
        }
        else {
            currAuthToken = null;
            return "make sure to enter username email and password";
        }
    }

    public String login(String... params) {
        System.out.println("login within chessClient!");
        if (params.length >= 2) {
            UserData user = new UserData(params[0], params[1], null);
            AuthData auth = server.loginUser(user);
            if (auth != null) {
                state = State.SIGNEDIN;
                username = user.username();
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
