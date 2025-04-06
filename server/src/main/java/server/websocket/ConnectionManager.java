package server.websocket;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import dataaccess.DataAccess;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import passoff.model.TestListResult;
import server.Server;
import server.ServerFacade;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.lang.reflect.Array;
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

    public void remove(int gameId) {
        sessionMap.remove(gameId);
    }

    public void broadcast(int gameId, String exceptAuthToken, Session currSession, boolean success, String msg, UserGameCommand.CommandType commandType) throws IOException {
        System.out.println("inside broadcast");

        System.out.println("gameid: " + gameId);
        if (!success) {
            var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, null, msg);
            var errorJson = new Gson().toJson(errorMessage);
            currSession.getRemote().sendString(errorJson);
        } else {
        for (var k : sessionMap.keySet()) {
            if (k == gameId) {
                // all sessions associated w the current gameid
                ArrayList<Connection> currConnections = sessionMap.get(k);

                if (commandType == UserGameCommand.CommandType.MAKE_MOVE) {
                    System.out.println("make move messages");
                    // all the connections
                    for (var c : currConnections) {
                        if (c.session.isOpen()) {
                            var loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "game name", null, null);
                            System.out.println("sending message to authToken: " + c.authToken);
                            var jsonString = new Gson().toJson(loadMessage);
                            System.out.println(jsonString);
                            c.session.getRemote().sendString(jsonString);
                            if (!Objects.equals(c.authToken, exceptAuthToken)) {
                                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        null, msg, null);
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
                                System.out.println("send notification");
                                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                                        null, msg, null);
                                var notifJson = new Gson().toJson(notification);
                                System.out.println(notifJson);

                                c.session.getRemote().sendString(notifJson);
                                // return notification to others
                            } else {
                                var loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "game name", null, null);
                                System.out.println("sending message to authToken: " + c.authToken);
                                var jsonString = new Gson().toJson(loadMessage);
                                System.out.println(jsonString);
                                c.session.getRemote().sendString(jsonString);
                            }
                        }
                    }


                }

                }
            }
        }

        }

    }

