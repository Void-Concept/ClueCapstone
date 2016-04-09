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

    private void movePlayer(BGComponent player, BGContainer board, int xoff, int yoff) {
        BGComponent moveTile = board.findViewsWithFlag("tilex", (Integer)player.getFlag("tilex") + xoff).findViewWithFlag("tiley", (Integer)player.getFlag("tiley") + yoff);

        player.setX(moveTile.getX() + moveTile.getWidth() / 2 - player.getWidth() / 2 + (Integer)player.getFlag("xoff"));
        player.setY(moveTile.getY() + moveTile.getHeight() / 2 - player.getHeight() / 2 + (Integer)player.getFlag("yoff"));

        player.setFlag("tilex", moveTile.getFlag("tilex"));
        player.setFlag("tiley", moveTile.getFlag("tiley"));
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
                    BGContainer board = (BGContainer)controller.getViewById("board");
                    BGComponent tile = board.findViewsWithFlag("tilex", playerToken.getFlag("tilex")).findViewWithFlag("tiley", playerToken.getFlag("tiley"));
                    switch (button) {
                        case "up arrow":
                            if (tile.getFlag("walkup").equals(true)) {
                                movePlayer(playerToken, board, 0, -1);
                            }
                            break;
                        case "down arrow":
                            if (tile.getFlag("walkdown").equals(true)) {
                                movePlayer(playerToken, board, 0, 1);
                            }
                            break;
                        case "left arrow":
                            if (tile.getFlag("walkleft").equals(true)) {
                                movePlayer(playerToken, board, -1, 0);
                            }
                            break;
                        case "right arrow":
                            if (tile.getFlag("walkright").equals(true)) {
                                movePlayer(playerToken, board, 1, 0);
                            }
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
            e.printStackTrace();
        }
    }
}
