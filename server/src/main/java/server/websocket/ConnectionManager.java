package server.websocket;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.awt.*;
import java.util.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> sessionMap = new ConcurrentHashMap<>();


    public boolean add(int gameId, String authToken, Session session, DataAccess dataAccess) {

        System.out.println("inside add with authToken: " + authToken);
        var connection = new Connection(authToken, session);

        // TODO check list of all games see if the gameid is there

        var authList = dataAccess.getAuth(authToken);
        var gamesList = dataAccess.listGames();

        if (authList == null) {
            System.out.println("authToken not found. returning false");
            return false;
        }

        for (var game : gamesList) {
            if (game.gameID() == gameId) {
                if (sessionMap.containsKey(gameId)) {
                    System.out.println("adding to gameId");
                    sessionMap.get(gameId).add(connection);
                }
                else {
                    System.out.println("new set thing bc game has no other users");
                    ArrayList<Connection> currSet = new ArrayList<>();
                    currSet.add(connection);
                    sessionMap.put(gameId, currSet);
                }
                return true;
            }
        }

        return false;





//        var listOfGames = facade.listGames(authToken);
//        ArrayList<String> stringList = new ArrayList<>();
//
//        if (listOfGames instanceof LinkedTreeMap<?,?>) {
//            LinkedTreeMap<String, ArrayList<Object>> treeMap = (LinkedTreeMap<String, ArrayList<Object>>) listOfGames;
//            stringList = new ArrayList<>();
//            Set keySet = ((LinkedTreeMap<?, ?>) listOfGames).keySet();
//            int i = 1;
//            for (Object game : treeMap.get("games")) {
//                if (game instanceof LinkedTreeMap<?, ?>) {
//                    LinkedTreeMap<String, String> gameInfo = (LinkedTreeMap<String, String>) game;
//                    String stringToAdd = i + ".   " + gameInfo.get("gameName") +
//                            ",   " + gameInfo.get("whiteUsername") + ",   " + gameInfo.get("blackUsername");
//                    ++i;
//                    stringList.add(stringToAdd);
//                }
//
//            }
//        }
//
//        if (!stringList.contains(gameId)) {
//
//            return false;
//        }


    }

    public void broadcast(int gameId, String exceptAuthToken, Session currSession, boolean success, String msg, UserGameCommand.CommandType commandType, String gameName, chess.ChessGame chessGame) throws IOException {

        // could legit just pass in a ServerMessage then u don't have to deal w the whole omg this is an error thing

        System.out.println("inside broadcast");

        System.out.println("gameid: " + gameId);
        if (!success) {
            var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, msg, null);
            var errorJson = new Gson().toJson(errorMessage);
            currSession.getRemote().sendString(errorJson);
        } else {
        for (var k : sessionMap.keySet()) {
            if (k == gameId) {
                // all sessions associated w the current gameid
                ArrayList<Connection> currConnections = sessionMap.get(k);


                if (commandType == null) {
                    for (var c : currConnections) {
                        if (c.session.isOpen()) {
                            var notif = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, msg, null, null);
                            var jsonString = new Gson().toJson(notif);
                            c.session.getRemote().sendString(jsonString);
                        }
                    }
                }

                else if (commandType == UserGameCommand.CommandType.MAKE_MOVE) {
                    System.out.println("make move messages");
                    // all the connections
                    for (var c : currConnections) {
                        if (c.session.isOpen()) {
                            var loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameName, null, null, chessGame);
                            System.out.println("sending message to authToken: " + c.authToken);
                            var jsonString = new Gson().toJson(loadMessage);
                            System.out.println(jsonString);
                            c.session.getRemote().sendString(jsonString);
                            if (!Objects.equals(c.authToken, exceptAuthToken)) {
                                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        null, msg, null, chessGame);
                                var notifJson = new Gson().toJson(notification);
                                System.out.println(notifJson);
                                c.session.getRemote().sendString(notifJson);
                            }
                        }
                    }


                } else if (commandType == UserGameCommand.CommandType.CONNECT) {
                    for (var c : currConnections) {
                        if (c.session.isOpen()) {
                            if (!Objects.equals(c.authToken, exceptAuthToken)) {
                                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        null, msg, null, chessGame);
                                var notifJson = new Gson().toJson(notification);
                                System.out.println(notifJson);

                                c.session.getRemote().sendString(notifJson);
                                // return notification to others
                            } else {

                                var loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameName, null, null, chessGame);
                                System.out.println("sending message to authToken: " + c.authToken);
                                var jsonString = new Gson().toJson(loadMessage);
                                System.out.println(jsonString);
                                c.session.getRemote().sendString(jsonString);
                            }
                        }
                    }

                } else if (commandType == UserGameCommand.CommandType.RESIGN) {
                    for (var c : currConnections) {
                        // send notification to everyone! someone resigned!
                        if (c.session.isOpen()) {
                            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, msg, null, chessGame);
                            var notifJson = new Gson().toJson(notification);
                            c.session.getRemote().sendString(notifJson);
                        }
                    }
                } else if (commandType == UserGameCommand.CommandType.LEAVE) {
                    System.out.println("broadcasting leave inside manager");
                    for (var c : currConnections) {
                        if (c.session.isOpen()) {
                            if (!Objects.equals(c.authToken, exceptAuthToken)) {
                                var notif = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, null, msg, null, chessGame);
                                var json = new Gson().toJson(notif);
                                c.session.getRemote().sendString(json);
                            } else {
                                c.session.close();
                            }
                        }
                    }
                }

                }
            }
        }

        }

    }
