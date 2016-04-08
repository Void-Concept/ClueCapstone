package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.BGContainer;
import capstone.boardgame.HTTP.WebSocket.Packet;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by Kyle on 3/23/2016.
 */
public class Player extends BGContainer {
    private String pid;
    private Session session;
    public boolean enabled = true;

    public Player(String pid, Session session) {
        this.pid = pid;
        this.session = session;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getPid() {
        return pid;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void tick(double deltaTime) {

    }

    public void sendPacket(Packet packet) throws IOException {
        this.session.getBasicRemote().sendText(packet.getJson());
    }
}
