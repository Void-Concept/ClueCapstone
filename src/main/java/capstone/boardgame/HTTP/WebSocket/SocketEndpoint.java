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
    private static SocketListener listener = new SocketListener() {
        @Override
        public boolean onOpen(Session session) throws IOException {
            return false;
        }

        @Override
        public void onClose(Session session, CloseReason reason) {

        }

        @Override
        public void onMessage(Session session, String message) {

        }
    };
    private static PacketHandler handler = new PacketHandler() {
        @Override
        public void handlePacket(String packet) {
            //default packet handler
        }
    };

    public static void setListener(SocketListener listener) {
        if (listener != null ) SocketEndpoint.listener = listener;
    }
    public static void setPacketHandler(PacketHandler handler) { if (handler != null) SocketEndpoint.handler = handler; }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Log.d(tag, "onOpen");
        //SessionManager.addSession(session);
        if (listener.onOpen(session)) {
            session.getBasicRemote().sendText(Packet.createOpenPacket().getJson());
        } else {
            session.getBasicRemote().sendText(Packet.createRejectPacket().getJson());
            session.close();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        Log.d(tag, "onClose");
        //SessionManager.removeSession(session);
        listener.onClose(session, reason);

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Log.d(tag, "onMessage: " + message);
        //session.getBasicRemote().sendText(message + " (from your server)");
        listener.onMessage(session, message);
        //Packet.parseJson(message);
        handler.handlePacket(message);
    }

    @OnError
    public void onError(Throwable t) {
        Log.d(tag, "onError");
        t.printStackTrace();
    }
}
