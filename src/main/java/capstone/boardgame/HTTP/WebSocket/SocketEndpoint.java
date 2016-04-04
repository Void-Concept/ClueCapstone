package capstone.boardgame.HTTP.WebSocket;

import capstone.boardgame.HTTP.WebSocketServer;
import capstone.boardgame.main.Log;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/boardgame")
public class SocketEndpoint {
    private static final String tag = "SocketEndpoint";
    private static SocketListener listener = null;

    public static void setListener(SocketListener listener) {
        SocketEndpoint.listener = listener;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Log.d(tag, "onOpen");
        session.getBasicRemote().sendText("onOpen");
        WebSocketServer.addSession(session);
        if (listener != null) {
            listener.onOpen(session);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        Log.d(tag, "onClose");
        WebSocketServer.removeSession(session);
        if (listener != null) {
            listener.onClose(session, reason);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Log.d(tag, "onMessage: " + message);
        //session.getBasicRemote().sendText(message + " (from your server)");
        if (listener != null) {
            listener.onMessage(session, message);
            Packet.parseJson(message);
        }
    }

    @OnError
    public void onError(Throwable t) {
        Log.d(tag, "onError");
        t.printStackTrace();
    }
}
