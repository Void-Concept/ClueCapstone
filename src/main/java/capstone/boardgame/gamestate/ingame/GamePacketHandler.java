package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.HTTP.WebSocket.PacketHandler;
import capstone.boardgame.main.Log;
import org.json.JSONObject;

import javax.websocket.Session;

/**
 * Created by Kyle on 4/4/2016.
 */
public class GamePacketHandler implements PacketHandler {
    private static String tag = "GamePacketHandler";
    private GameController controller;

    public GamePacketHandler(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void handlePacket(Session session, String packet) {
        JSONObject obj = new JSONObject(packet);
        Player player = controller.getPlayerSession(session.getId());

        try {
            String command = (String) obj.get("Command");
            switch (command) {
                case "onClick":
                    Log.d(tag, "TODO: onClick");

                    break;
                case "radioToggle":
                    Log.d(tag, "TODO: radioToggle");
                    break;
                case "refresh":
                    Log.d(tag, "TODO: refresh");
                    break;
            }

        } catch (Exception e) {

        }
    }
}
