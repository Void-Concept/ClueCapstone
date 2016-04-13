package capstone.boardgame.gamestate.menu;

import capstone.boardgame.GUI.BGContainer;
import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.Component.RadioButton;
import capstone.boardgame.HTTP.WebSocket.PacketHandler;
import capstone.boardgame.main.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.Session;

/**
 * Created by Kyle on 4/8/2016.
 */
public class MenuPacketHandler implements PacketHandler {
    private MainMenu controller;

    public MenuPacketHandler(MainMenu controller) {
        this.controller = controller;
    }

    @Override
    public void handlePacket(Session session, String packet) {
        JSONObject obj = new JSONObject(packet);
        String command = (String) obj.get("Command");
        JSONArray params = (JSONArray)obj.get("Parameters");
        try {
            switch (command) {
                case "onClick":
                    String button = ((JSONObject) params.get(0)).get("view").toString();
                    switch (button) {
                        case "StartGame":
                            controller.loadGame();
                            break;
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
