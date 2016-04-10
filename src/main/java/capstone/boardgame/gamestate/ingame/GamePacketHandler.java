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

        if (moveTile.getFlag("isdoor").equals(true)) {
            String[] places = {"Study", "Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library"};
            player.setFlag("currRoom", places[(Integer)moveTile.getFlag("room") - 1]);
            player.setFlag("enteredRoom", true);
        }
    }

    @Override
    public void handlePacket(Session session, String packet) {
        JSONObject obj = new JSONObject(packet);
        String command = (String) obj.get("Command");
        JSONArray params = (JSONArray)obj.get("Parameters");

        Player player = controller.getPlayerSession(session.getId());

        switch (player.getCurrentRemoteView()) {
            case "old":
                handleOldPacket(command, params, player);
                break;
            case "main":
                handleMainPacket(command, params, player);
                break;
            case "suspect":
                handleSuspectPacket(command, params, player);
                break;
            case "accusation":
                handleAccusationPacket(command, params, player);
                break;
            case "room":
                handleRoomPacket(command, params, player);
                break;
        }
    }

    private void handleRoomPacket(String command, JSONArray params, Player player) {
        try {
            switch (command) {
                case "onClick":
                    String button = ((JSONObject)params.get(0)).get("view").toString();

                    switch (button) {
                        case "accuse":
                            transitionToAccusation(player, true);
                            break;
                        case "suggest":
                            transitionToAccusation(player, false);
                            break;
                        case "exit":
                            player.setCurrentRemoteView("main");
                            player.sendView();
                            break;
                    }

                    break;
                case "radioToggle":
                    Log.d(tag, "TODO: radioToggle");
                    break;
                case "refresh":
                    Log.d(tag, "TODO: refresh");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //parameter: true if accusing, false if suggesting
    private void transitionToAccusation(Player player, boolean accusation) {
        BGContainer remoteView = player.getRemoteView("accusation");

        //if a suggestion, disable everything and set room
        if (!accusation) {
            BGContainer group = remoteView.findViewsWithFlag("group", "place");
            for (int i = 0; i < group.size(); i++) {
                BGComponent view = group.get(i);
                if (view instanceof RadioButton) {
                    if (view.getId().equals(player.getFlag("currRoom")))
                        ((RadioButton) view).setChecked(true);
                    else
                        ((RadioButton) view).setChecked(false);
                    ((RadioButton) view).setEnabled(false);
                }
            }
        }
        player.setFlag("accuse return", player.getCurrentRemoteView());
        player.setCurrentRemoteView("accusation");
        player.sendView();
    }

    private boolean checkAccusation(BGContainer group, Player player, String flag) {
        boolean good = false;
        for (int i = 0; i < group.size(); i++) {
            BGComponent view = group.get(i);
            if (view instanceof RadioButton) {
                if (((RadioButton) view).isChecked()) {
                    good = true;
                    player.setFlag(flag, view.getId());
                    break;
                }
            }
        }
        return good;
    }

    private void resetCheckboxes(BGContainer group) {
        for (int i = 0; i < group.size(); i++) {
            BGComponent view = group.get(i);
            if (view instanceof RadioButton) {
                ((RadioButton) view).setEnabled(true);
                ((RadioButton) view).setChecked(false);
            }
        }
    }

    private void handleAccusationPacket(String command, JSONArray params, Player player) {
        try {
            switch (command) {
                case "onClick":
                    String button = ((JSONObject)params.get(0)).get("view").toString();

                    switch (button) {
                        case "submit":
                            //check to see if one of each is checked, as well as which
                            BGContainer remoteView = player.getRemoteView(player.getCurrentRemoteView());

                            BGContainer group = remoteView.findViewsWithFlag("group", "suspect");
                            if (!checkAccusation(group, player, "accuse-suspect"))
                                break;

                            group = remoteView.findViewsWithFlag("group", "weapon");
                            if (!checkAccusation(group, player, "accuse-weapon"))
                                break;

                            group = remoteView.findViewsWithFlag("group", "place");
                            if (!checkAccusation(group, player, "accuse-place"))
                                break;

                            group = remoteView.findViewsWithFlag("group", "suspect");
                            resetCheckboxes(group);
                            group = remoteView.findViewsWithFlag("group", "weapon");
                            resetCheckboxes(group);
                            group = remoteView.findViewsWithFlag("group", "place");
                            resetCheckboxes(group);

                            player.setCurrentRemoteView((String)player.getFlag("accuse return"));
                            player.sendView();
                            break;
                        case "return":
                            remoteView = player.getRemoteView(player.getCurrentRemoteView());
                            group = remoteView.findViewsWithFlag("group", "suspect");
                            resetCheckboxes(group);
                            group = remoteView.findViewsWithFlag("group", "weapon");
                            resetCheckboxes(group);
                            group = remoteView.findViewsWithFlag("group", "place");
                            resetCheckboxes(group);

                            player.setCurrentRemoteView((String)player.getFlag("accuse return"));
                            player.sendView();
                            break;
                    }

                    break;
                case "radioToggle":
                    try {
                        BGContainer remoteView = player.getRemoteView(player.getCurrentRemoteView());
                        JSONObject radioParams = (JSONObject)params.get(0);
                        String radioButton = radioParams.get("view").toString();
                        String state = radioParams.get("state").toString();

                        RadioButton rb = ((RadioButton) remoteView.getViewByID(radioButton));
                        BGContainer group = remoteView.findViewsWithFlag("group", rb.getFlag("group"));

                        if (state.equals("true")) {
                            for (int i = 0; i < group.size(); i++) {
                                BGComponent view = group.get(i);
                                if (view instanceof RadioButton) {
                                    ((RadioButton) view).setChecked(false);
                                }
                            }
                            rb.setChecked(true);
                        } else {
                            rb.setChecked(false);
                        }
                        player.sendView();
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

    private void handleSuspectPacket(String command, JSONArray params, Player player) {
        try {
            switch (command) {
                case "onClick":
                    String button = ((JSONObject)params.get(0)).get("view").toString();

                    switch (button) {
                        case "return":
                            player.setCurrentRemoteView((String)player.getFlag("suspect return"));
                            player.sendView();
                            break;
                    }

                    break;
                case "radioToggle":
                    Log.d(tag, "TODO: radioToggle");
                    try {
                        BGContainer remoteView = player.getRemoteView(player.getCurrentRemoteView());
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

    private void handleMainPacket(String command, JSONArray params, Player player) {
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
                        case "view suspects":
                            player.setFlag("suspect return", "main");
                            player.setCurrentRemoteView("suspect");
                            player.sendView();
                            break;
                    }
                    try {
                        if (playerToken.getFlag("enteredRoom").equals(true)) {
                            player.setFlag("currRoom", playerToken.getFlag("currRoom"));
                            playerToken.setFlag("enteredRoom", false);
                            player.setCurrentRemoteView("room");
                            player.sendView();
                        }
                    } catch (Exception e) {
                        playerToken.setFlag("enteredRoom", false);
                        e.printStackTrace();
                    }

                    break;
                case "radioToggle":
                    Log.d(tag, "TODO: radioToggle");
                    break;
                case "refresh":
                    Log.d(tag, "TODO: refresh");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleOldPacket(String command, JSONArray params, Player player) {
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
