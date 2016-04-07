package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.HTTP.WebSocket.PacketHandler;
import capstone.boardgame.main.Log;
import org.json.JSONObject;

/**
 * Created by Kyle on 4/4/2016.
 */
public class GamePacketHandler implements PacketHandler {
    private static String tag = "GamePacketHandler";
    @Override
    public void handlePacket(String packet) {
        JSONObject obj = new JSONObject(packet);

        try {
            String command = (String) obj.get("Command");
            switch (command) {
                case "onClick":
                    Log.d(tag, "TODO: onClick");
                    break;
                case "refresh":
                    Log.d(tag, "TODO: refresh");
                    break;
            }

        } catch (Exception e) {

        }
    }
}
