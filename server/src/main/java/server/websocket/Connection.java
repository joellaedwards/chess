package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;


public class Connection {
    // need to use this bc the session isn't comparable like
    // it doesn't stay the same
    public String authToken;
    public Session session;

    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }
}
