package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.util.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> sessionMap = new ConcurrentHashMap<>();

    public void add(int gameId, String authToken, Session session) {
        System.out.println("inside add with authToken: " + authToken);
        var connection = new Connection(authToken, session);
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
    }

    public void remove(int gameId) {
        sessionMap.remove(gameId);
    }

    public void broadcast(int gameId, String exceptAuthToken) throws IOException {
        System.out.println("inside broadcast");
        System.out.println("length of sessionMap: " + sessionMap.values());
        ArrayList<Integer> removeList = new ArrayList<>();
        // just tell it to the people in that game but not the one who is literally in this session
        // bc they alr know they made that move or that they're there etc

        System.out.println("gameid: " + gameId);
        for (var k : sessionMap.keySet()) {
            if (k == gameId) {
                // all sessions associated w the current gameid
                ArrayList<Connection> currConnections = sessionMap.get(k);

                for (var c : currConnections) {
                    System.out.println("checking conn: " + c.authToken);
                        if(c.session.isOpen()) {

//                            if (c.authToken != exceptAuthToken) {
//                                System.out.println("send notification");
//                                // return notification to others
//                            }
//                            else {
                                var loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "123");
                                System.out.println("sending message to authToken: " + c.authToken);
                                var jsonString = new Gson().toJson(loadMessage);
                                System.out.println(jsonString);
                                c.session.getRemote().sendString(jsonString);
//                            }
                        }
//                    if (!Objects.equals(c.authToken, exceptAuthToken)) {
//                        System.out.println("sending message to authToken: " + c.authToken);
//                        var jsonString = new Gson().toJson(message);
//                        c.session.getRemote().sendString(jsonString);
//                    }
                }
            }

        }

        // frankly idk what these do lol
//            else {
//                System.out.println("apparently it doesnt contain the gameId");
//                removeList.add(gameId);
//            }
//
//        for (int c : removeList) {
//            sessionMap.remove(c);
//        }

    }

    }

