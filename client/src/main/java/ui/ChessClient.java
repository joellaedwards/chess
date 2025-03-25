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
    public static State state = State.SIGNEDOUT;
    private String currAuthToken = null;


    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


    public Object eval(String input) throws ResponseException {
//        System.out.println("input: " + input);
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
                case "observe" -> observeGame(params);
                case "clear" -> clearAll();
                default -> help();
            };
        }
    }



    public String clearAll() throws ResponseException {
//        System.out.println("clearing...");
        server.logoutUser(currAuthToken);
        state = State.SIGNEDOUT;
        server.clearAll();
        return "reset!";
    }

    public String observeGame(String... params) throws ResponseException {
        int id = 0;
        if (params.length == 1 ) {
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return "Please enter a number value for game number.";
            }

            Object listOfGames = server.listGames(currAuthToken);
            if (listOfGames instanceof LinkedTreeMap<?,?>) {
                LinkedTreeMap<String, ArrayList<Object>> treeMap = (LinkedTreeMap<String, ArrayList<Object>>) listOfGames;
                ArrayList<Object> onlyGames = treeMap.get("games");
                if (onlyGames.size() >= id && id >= 1) {
                    return "Observing game.";
                }
            }
        }
        return "Please enter a 'observe' and a valid game number.";

    }

    public String playGame(String... params) throws ResponseException {
//        System.out.println("in playgame");
//        System.out.println("num params: " + params.length);
        int id = 0;
        if (params.length == 2) {
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return "Please enter a number value for game number.";
            }
            String color = params[1];
//            System.out.println("color: " + color);
            ChessGame.TeamColor myColor;
            if (Objects.equals(color, "white")) {
                myColor = ChessGame.TeamColor.WHITE;
            }
            else if (Objects.equals(color, "black")){
                myColor = ChessGame.TeamColor.BLACK;
            }
            else {
                return "Please enter a valid color. Black or White.";
            }
            ServerFacade.JoinGameObj joinObj = new ServerFacade.JoinGameObj(myColor, id);
            Object joinInfo = server.joinGame(joinObj, currAuthToken);
//            System.out.println("joinInfo: " + joinInfo);
            if (joinInfo != null) {
                return "Game joined as " + color.toLowerCase();
            }
            return "Please enter a valid game number and unassigned color.";
        }
        return "Please enter a game number and the color you wish to play as.";
    }


    public String listGames() throws ResponseException {

        Object listOfGames = server.listGames(currAuthToken);

        if (listOfGames instanceof LinkedTreeMap<?, ?>) {
            LinkedTreeMap<String, ArrayList<Object>> treeMap = (LinkedTreeMap<String, ArrayList<Object>>) listOfGames;
            StringBuilder fullList = new StringBuilder();
            int i = 1;
            for (Object game : treeMap.get("games")) {
                if (game instanceof LinkedTreeMap<?, ?>) {
                    LinkedTreeMap<String, String> gameInfo = (LinkedTreeMap<String, String>) game;
                    String stringToAdd = i + ".   " + gameInfo.get("gameName") + ",   " + gameInfo.get("whiteUsername")
                            + ",   " + gameInfo.get("blackUsername");
                    ++i;
                    fullList.append(stringToAdd).append('\n');
                }
            }
            if (fullList.isEmpty()) {
                return "No games found.";
            }
            return fullList.toString();
        }
        return null;
    }


    public String createGame(String... params) throws ResponseException {
//        System.out.println("in creategame in chess client");
//        System.out.println("params length: " + params.length);
        if (params.length == 1) {
//            System.out.println("params at 0: " + params[0]);
            Object returnedGame = server.createGame(currAuthToken, params[0]);
            if (returnedGame != null) {
//                System.out.println("returned game: " + returnedGame);
                return "Game created!";
            }
            return "You are not authorized to create a new game.";
        }
        return "Please enter a name for your new game.";
    }

    public String logout() {
//        System.out.println("in logout in chessclient");
        if (currAuthToken != null) {
            if (server.logoutUser(currAuthToken) != null) {
                state = State.SIGNEDOUT;
                currAuthToken = null;
                return "Successfully logged out!";
            }
        }

        return "Something went wrong. Please try logging out again";
    }


    public String register(String... params) {
//        System.out.println("register within chessClient!!");
        if (params.length == 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            AuthData auth = server.registerUser(user);
            if (auth != null) {
                currAuthToken = auth.authToken();
                state = State.SIGNEDIN;
//                System.out.println("Registered and signed in");
                return "Registered and signed in. Welcome to chess!";
            }
            return "User already registered. Login with username and password.";
        }
        else {
            currAuthToken = null;
            return "Make sure to enter a valid username, password, and email.";
        }
    }

    public String login(String... params) {
//        System.out.println("login within chessClient!");
        if (params.length >= 2) {
            UserData user = new UserData(params[0], params[1], null);
            AuthData auth = server.loginUser(user);
            if (auth != null) {
                state = State.SIGNEDIN;
                username = user.username();
                currAuthToken = auth.authToken();
//                System.out.println("logged in!");
                return "Logged in! Type 'help' for options.";
            }
        }
        return "Please enter a valid username and password.";
    }

    public String quit() {
        return "quit";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    
                    - Register <username> <password> <email>
                    - Login <username> <password>
                    - Help
                    - Quit
                    """;

        }
        else if (state == State.SIGNEDIN) {
            return """
                    
                    - Logout
                    - Create <GameName> - create new game
                    - List - list all games
                    - Play <Game Number> <Color> - play game
                    - Observe <Game Number> - observe a game
                    - Help - list options
                    """;
        }

        return "Something went really wrong. Check the state.";
    }

}
