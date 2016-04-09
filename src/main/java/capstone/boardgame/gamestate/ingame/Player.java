package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.BGContainer;
import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.Component.Button;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.Component.RadioButton;
import capstone.boardgame.GUI.Drawable.BGDrawable;
import capstone.boardgame.GUI.GameGUIContainer;
import capstone.boardgame.HTTP.WebSocket.Packet;
import capstone.boardgame.main.Log;
import org.json.JSONObject;

import javax.websocket.Session;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/23/2016.
 */
public class Player extends BGContainer {
    private String pid;
    private Session session;
    public boolean enabled = true;

    private ArrayList<BGContainer> remoteViewStates = new ArrayList<>();
    private String currentView = "old";

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

    public void sendView() {
        try {
            Packet packet = new Packet();
            packet.setCommand("draw");
            packet.addParameters(getRemoteView(currentView).toJson());

            sendPacket(packet);
        } catch (Exception e) {

        }

        Log.d("Player", getRemoteView(currentView).toJson().toString());
    }

    public void addRemoteView(BGContainer remoteView) {
        remoteViewStates.add(remoteView);
    }
    public void removeRemoteView(String id) {
        remoteViewStates.remove(getRemoteView(id));
    }
    public void setCurrentRemoteView(String tag) {
        currentView = tag;
    }
    public String getCurrentRemoteView() {
        return currentView;
    }

    public BGContainer getRemoteView(String id) {
        for (BGContainer container : remoteViewStates) {
            if (container.getId().equals(id)) {
                return container;
            }
        }
        return null;
    }
}
