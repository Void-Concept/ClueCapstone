package capstone.boardgame.HTTP.WebSocket;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class SocketEndpoint {

    @OnOpen
    public void onOpen(Session session) throws IOException {
        session.getBasicRemote().sendText("onOpen");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {

    }

    @OnMessage
    public void echo(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message + " (from your server)");
        session.close();
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
