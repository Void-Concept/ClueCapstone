package capstone.boardgame.HTTP.WebSocket;

import javax.websocket.Session;

/**
 * Created by Kyle on 4/4/2016.
 */
public interface PacketHandler {
    void handlePacket(Session session, String packet);
}
