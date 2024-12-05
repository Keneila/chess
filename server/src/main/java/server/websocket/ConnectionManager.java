package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(Session session, String user, Integer gameID) {
        var connection = new Connection(user, session, gameID);
        connections.put(user, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String user, Integer gameID, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if(notification.getServerMessageType() != ServerMessage.ServerMessageType.ERROR) {
                    if (!c.visitorName.equals(user) && Objects.equals(c.gameID, gameID)) {
                        c.send(notification.toString());
                    }
                } else {
                    if (c.visitorName.equals(user)){
                        c.send(notification.toString());
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
