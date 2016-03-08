package capstone.boardgame.HTTP;

/**
 * Created by Kyle on 2/22/2016.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.rmi.server.ExportException;

import capstone.boardgame.main.Log;
import capstone.boardgame.gamedata.Game;
import capstone.boardgame.gamedata.GameAsset;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    private static String tag = "WebServer";
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Game assets = Game.getInstance();

            GameAsset asset = assets.getAssetFromPath(t.getRequestURI().getPath());
            if (asset == null) {
                Log.d(tag, "Bad path: " + t.getRequestURI().getPath());
                t.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                OutputStream os = t.getResponseBody();
                os.write("".getBytes());
                os.close();
            } else {
                Log.d(tag, "Good path: " + t.getRequestURI().getPath());
                //String response = asset.getBody();
                byte[] response = asset.getBody();
                t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
                OutputStream os = t.getResponseBody();
                os.write(response);
                os.close();
            }
        }
    }

    public static void start() {
        try {
            Log.d(tag, "WebServer starting");
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            Log.d(tag, "Server failed to start");
        }
    }
}
