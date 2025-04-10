package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.internal.LinkedTreeMap;
import model.*;
import exception.ResponseException;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    public static State state = State.SIGNEDOUT;
    private String currAuthToken = null;
    private ChessBoard currBoard = null;
    private String currUser = null;
    private int currGameId = -2;
    private ChessGame.TeamColor currColor = null;


    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
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
        else if (state == State.SIGNEDIN) {
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
        } else if (state == State.INGAME) {
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "move" -> movePiece(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        }
        return null;
    }


    public String highlight(String ... params) {
        if (params.length != 1) {
            return "Please enter a starting square for which you want possible moves highlighted";
        }

        ArrayList<String> validInputs = new ArrayList<>();

        String letter = "a";
        for (int i = 1; i <= 8; ++i) {
            validInputs.add("a" + i);
            validInputs.add("b" + i);
            validInputs.add("c" + i);
            validInputs.add("d" + i);
            validInputs.add("e" + i);
            validInputs.add("f" + i);
            validInputs.add("g" + i);
            validInputs.add("h" + i);
        }

        if (!validInputs.contains(params[0])) {
            return "Please enter a valid starting position.";
        }

        char col = params[0].charAt(0);
        char row = params[0].charAt(1);
        int rowInt = row - '0';
        int colInt = getColInt(col);

        if (currColor == ChessGame.TeamColor.WHITE) {
            return "Highlight white " + rowInt + " " + colInt;
        } else {
            return "Highlight black " + rowInt + " " + colInt;
        }


    }

    private static int getColInt(char col) {
        if (col == 'a') {
            return 1;
        } else if (col == 'b') {
            return 2;
        } else if (col == 'c') {
            return 3;
        } else if (col == 'd') {
            return 4;
        } else if (col == 'e') {
            return 5;
        } else if (col == 'f') {
            return 6;
        } else if (col == 'g') {
            return 7;
        } else if (col == 'h') {
            return 8;
        }

        return -2;
    }

    public String movePiece(String ... params) throws ResponseException {
        if (params.length != 2) {
            System.out.print("length: " + params.length);
            return "Please enter a starting and ending position.";
        }

        String start = params[0];
        String end = params[1];

        if (start.length() < 2 || end.length() < 2 || start.length() > 3 || end.length() > 3) {
            System.out.println("wrong length of inputs");
            return "Please enter valid starting and ending positions";
        }

        char col = start.charAt(0);
        char row = start.charAt(1);
        int rowInt = row - '0';
        int colInt = getColInt(col);

        if (colInt == -2) {
            return "Please enter valid starting and ending positions.";
        }

        ChessPosition startingPos = new ChessPosition(rowInt, colInt);

        col = end.charAt(0);
        rowInt = end.charAt(1) - '0';
        colInt = getColInt(col);
        if (colInt == -2) {
            return "Please enter valid starting and ending positions.";
        }

        ChessPosition endPos = new ChessPosition(rowInt, colInt);

        ChessMove move = new ChessMove(startingPos, endPos, null);

        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.makeMove(currAuthToken, currGameId, move);

        return "piece moved";
    }


    public String resign() throws ResponseException {

//        state = State.SIGNEDIN;
        System.out.print("Resign? Type 'y' if yes.");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (Objects.equals(input, "y")) {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.resignFromGame(currAuthToken, currGameId);

            return "Resigned game";
        }

        return "no change";
    }

    public String leaveGame() throws ResponseException {

        state = State.SIGNEDIN;

        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.leaveCurrGame(currAuthToken, currGameId);

        return "Left game.";
    }

    public String redrawBoard() throws ResponseException {
//        ws = new WebSocketFacade(serverUrl, notificationHandler);
//        ws.redrawBoard();
        // jk this shouldnt need a websocket cause it's only drawn for the one
        // user.


        if (currColor == ChessGame.TeamColor.BLACK) {
//            System.out.print("color is black");
            return "redraw black";
        } else {
            return "redraw white";
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
                    state = State.INGAME;
                    currGameId = id;


                    ws = new WebSocketFacade(serverUrl, notificationHandler);
                    ws.connectToGame(currAuthToken, id);

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
            if (Objects.equals(color, "white")) {
                currColor = ChessGame.TeamColor.WHITE;
            }
            else if (Objects.equals(color, "black")){
                currColor = ChessGame.TeamColor.BLACK;
            }
            else {
                return "Please enter a valid color. Black or White.";
            }
            ServerFacade.JoinGameObj joinObj = new ServerFacade.JoinGameObj(currColor, id);
            Object joinInfo = server.joinGame(joinObj, currAuthToken);

            //            System.out.println("joinInfo: " + joinInfo);
            if (joinInfo != null) {
                state = State.INGAME;
                currGameId = id;

                ws = new WebSocketFacade(serverUrl, notificationHandler);
                ws.connectToGame(currAuthToken, id);
                if (currColor == ChessGame.TeamColor.BLACK) {
                    return "join black";
                }
                else {
                    return "join white";
                }
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
            return "Games: \n" + fullList;
        }
        return null;
    }

    // fix the board stuff and make the repl file prettier lol

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
                currUser = null;
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
                currUser = auth.username();
                state = State.SIGNEDIN;
//                System.out.println("Registered and signed in");
                return "Registered and signed in. Welcome to chess!";
            }
            return "User already registered. Login with username and password.";
        }
        else {
            currAuthToken = null;
            currUser = null;
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
                currAuthToken = auth.authToken();
                currUser = auth.username();
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
        } else if (state == State.INGAME) {
            return """
                    - Redraw - redraw board
                    - Leave - leave game
                    - Move <Starting square> <Ending square>
                    - Resign - forfeit and leave game
                    - Highlight <starting square> - highlight legal moves
                    - Help - list options
                    
                    """;
        }

        return "Something went really wrong. Check the state.";
    }

}
