package ui;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import server.ServerFacade;


public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;



    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


}
