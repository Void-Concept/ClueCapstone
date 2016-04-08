package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.BGContainer;
import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.Component.RadioButton;
import capstone.boardgame.GUI.Drawable.BGDrawable;
import capstone.boardgame.HTTP.WebSocket.PacketHandler;
import capstone.boardgame.main.Log;
import org.json.JSONArray;
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
        String command = (String) obj.get("Command");
        JSONArray params = (JSONArray)obj.get("Parameters");
        Player player = controller.getPlayerSession(session.getId());
        Log.d(tag, params.toString());

        try {
            switch (command) {
                case "onClick":
                    BGComponent playerToken = player.getViewByID("player");

                    String button = ((JSONObject)params.get(0)).get("view").toString();
                    switch (button) {
                        case "up arrow":
                            playerToken.setY(playerToken.getY() - 10);
                            break;
                        case "down arrow":
                            playerToken.setY(playerToken.getY() + 10);
                            break;
                        case "left arrow":
                            playerToken.setX(playerToken.getX() - 10);
                            break;
                        case "right arrow":
                            playerToken.setX(playerToken.getX() + 10);
                            break;
                    }

                    break;
                case "radioToggle":
                    Log.d(tag, "TODO: radioToggle");
                    try {
                        BGContainer remoteView = player.getRemoteView("old");
                        JSONObject radioParams = (JSONObject)params.get(0);
                        String radioButton = radioParams.get("view").toString();
                        String state = radioParams.get("state").toString();
                        ((RadioButton) remoteView.getViewByID(radioButton)).setChecked(state.equals("true"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "refresh":
                    Log.d(tag, "TODO: refresh");
                    break;
            }

        } catch (Exception e) {

        }
    }
}
