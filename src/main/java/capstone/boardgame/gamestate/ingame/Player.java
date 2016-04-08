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

/**
 * Created by Kyle on 3/23/2016.
 */
public class Player extends BGContainer {
    private String pid;
    private Session session;
    public boolean enabled = true;
    public BGContainer remoteView = new BGContainer();

    public Player(String pid, Session session) {
        this.pid = pid;
        this.session = session;
        createDefaultClueView();
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
        Packet packet = new Packet();
        packet.setCommand("draw");
        packet.addParameters(remoteView.toJson());
        try {
            sendPacket(packet);
        } catch (Exception e) {

        }

        Log.d("Player", remoteView.toJson().toString());
    }

    private void createDefaultClueView() {
        remoteView.setId("root");
        BGComponent.setDefaultBackgroundColor(Color.white);

        Label title = new Label(200, 10, "Clue");
        title.setFont(title.getFont().deriveFont(30.0f));
        remoteView.add(title);

        String[] suspects = {"Mr. Green", "Colonel Mustard", "Ms. Peacock", "Prof. Plum", "Ms. Scarlet", "Ms. White"};
        String[] weapons = {"Candlestick", "Knife", "Lead Pipe", "Pistol", "Rope", "Wrench"};
        String[] places = {"Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study"};

        BGContainer suspectContainer = new BGContainer();
        suspectContainer.setId("suspects");
        for (int i = 0; i < suspects.length; i++) {
            RadioButton button = new RadioButton(10, 80 + 30*i, 30, 30, 2);
            button.setId("checkbox-"+suspects[i]);

            suspectContainer.add(button);
            Label aLabel = new Label(50, 85 + 30*i, suspects[i]);
            aLabel.setFont(aLabel.getFont().deriveFont(20.0f));

            suspectContainer.add(aLabel);
        }
        remoteView.add(suspectContainer);

        BGContainer weaponContainer = new BGContainer();
        weaponContainer.setId("weapons");
        for (int i = 0; i < weapons.length; i++) {
            RadioButton button = new RadioButton(10, 310 + 30*i, 30, 30, 2);
            button.setId("checkbox-"+weapons[i]);

            weaponContainer.add(button);
            Label aLabel = new Label(50, 315 + 30*i, weapons[i]);
            aLabel.setFont(aLabel.getFont().deriveFont(20.0f));

            weaponContainer.add(aLabel);
        }
        remoteView.add(weaponContainer);

        BGContainer placeContainer = new BGContainer();
        placeContainer.setId("places");
        for (int i = 0; i < places.length; i++) {
            RadioButton button = new RadioButton(10, 540 + 30*i, 30, 30, 2);
            button.setId("checkbox-"+places[i]);

            placeContainer.add(button);
            Label aLabel = new Label(50, 545 + 30*i, places[i]);
            aLabel.setFont(aLabel.getFont().deriveFont(20.0f));

            placeContainer.add(aLabel);
        }
        remoteView.add(placeContainer);

        BGContainer controls = new BGContainer();
        controls.setId("controls");

        Button ubtn = new Button(290, 400, 90, 90, "^");
        ubtn.setId("up arrow");
        controls.add(ubtn);

        Button rbtn = new Button(380, 490, 90, 90, ">");
        rbtn.setId("right arrow");
        controls.add(rbtn);

        Button lbtn = new Button(200, 490, 90, 90, "<");
        lbtn.setId("left arrow");
        controls.add(lbtn);

        Button dbtn = new Button(290, 580, 90, 90, "v");
        dbtn.setId("down arrow");
        controls.add(dbtn);

        remoteView.add(controls);
    }
}
