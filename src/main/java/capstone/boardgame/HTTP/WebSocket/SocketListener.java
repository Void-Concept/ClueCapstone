package capstone.boardgame.HTTP.WebSocket;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by Kyle on 3/23/2016.
 */
public interface SocketListener {
    void onOpen(Session session) throws IOException;
    void onClose(Session session, CloseReason reason);
    void onMessage(Session session, String message);
}
