package capstone.boardgame.HTTP;


import capstone.boardgame.HTTP.WebSocket.SocketEndpoint;
import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

/**
 * Created by Kyle on 3/14/2016.
 */

public class WebSocketServer {
    private static Server server;
    public static void runServer() {
        server = new Server("*", 8025, "/", SocketEndpoint.class);

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
}
