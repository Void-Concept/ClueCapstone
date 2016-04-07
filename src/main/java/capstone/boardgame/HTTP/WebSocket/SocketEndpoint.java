package capstone.boardgame.HTTP.WebSocket;

import capstone.boardgame.HTTP.WebSocketServer;
import capstone.boardgame.gamestate.SessionManager;
import capstone.boardgame.main.Log;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/boardgame")
public class SocketEndpoint {
    private static final String tag = "SocketEndpoint";
    private static SocketListener listener = null;
    private static PacketHandler handler = new PacketHandler() {
        @Override
        public void handlePacket(String packet) {
            //default packet handler
        }
    };

    public static void setListener(SocketListener listener) {
        SocketEndpoint.listener = listener;
    }
    public static void setPacketHandler(PacketHandler handler) { SocketEndpoint.handler = handler; }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Log.d(tag, "onOpen");

        SessionManager.addSession(session);
        session.getBasicRemote().sendText(Packet.createOpenPacket().getJson());
        if (listener != null) {
            listener.onOpen(session);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        Log.d(tag, "onClose");
        SessionManager.removeSession(session);
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
            //Packet.parseJson(message);
            handler.handlePacket(message);
        }
    }

    @OnError
    public void onError(Throwable t) {
        Log.d(tag, "onError");
        t.printStackTrace();
    }
}
