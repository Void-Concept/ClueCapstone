package capstone.boardgame.HTTP;


import capstone.boardgame.HTTP.WebSocket.SocketEndpoint;
import capstone.boardgame.main.Log;
import org.glassfish.tyrus.server.Server;

import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kyle on 3/14/2016.
 */

public class WebSocketServer {
    private static String tag = "WebSocketServer";
    private static Server server;
    public static void runServer() {
        Log.d(tag, "WebSocketServer starting");
        server = new Server("*", 8080, "/", SocketEndpoint.class);

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void stopServer() {
        if (server != null)
            server.stop();
    }

    private static ArrayList<Session> sessions = new ArrayList<>();

    public static void addSession(Session session) {
        sessions.add(session);
    }
    public static void removeSession(Session session) {
        sessions.remove(session);
    }

    public static Session getSession(int i) {
        return sessions.get(i);
    }

    public static void sendMessage(int session, String text) {
        try {
            getSession(session).getBasicRemote().sendText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
