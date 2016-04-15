package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.BGContainer;
import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.Component.Button;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.Component.RadioButton;
import capstone.boardgame.HTTP.WebSocket.PacketHandler;
import capstone.boardgame.main.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.Session;
import java.awt.*;
import java.util.ArrayList;

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
        BGComponent moveTile = board.findViewsWithFlag("tilex", (Integer) player.getFlag("tilex") + xoff).findViewWithFlag("tiley", (Integer) player.getFlag("tiley") + yoff);

        player.setX(moveTile.getX() + moveTile.getWidth() / 2 - player.getWidth() / 2 + (Integer) player.getFlag("xoff"));
        player.setY(moveTile.getY() + moveTile.getHeight() / 2 - player.getHeight() / 2 + (Integer) player.getFlag("yoff"));

        player.setFlag("tilex", moveTile.getFlag("tilex"));
        player.setFlag("tiley", moveTile.getFlag("tiley"));

        if (moveTile.getFlag("isdoor").equals(true)) {
            String[] places = {"Study", "Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library"};
            player.setFlag("currRoom", places[(Integer) moveTile.getFlag("room") - 1]);
            player.setFlag("currRoomVal", moveTile.getFlag("room"));
            player.setFlag("enteredRoom", true);
        }
    }

    @Override
    public void handlePacket(Session session, String packet) {
        JSONObject obj = new JSONObject(packet);
        String command = (String) obj.get("Command");
        JSONArray params = (JSONArray) obj.get("Parameters");

        Player player = controller.getPlayerSession(session.getId());

        switch (player.getCurrentVisibleView()) {
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
            case "choice":
                handleChoicePacket(command, params, player);
                break;
            case "not turn":
                handleNotTurnPacket(command, params, player);
                break;
        }
    }

    private void transitionToRoom(Player player) {
        String room = (String) player.getFlag("currRoom");
        BGContainer remoteView = player.getRemoteView("room");
        player.getRemoteView("room").getViewByID("exit").setVisibility(false); //will be set visible on next turn
        player.getViewByID("player").setFlag("enteredRoom", false);

        switch (room) {
            case "Study":
            case "Lounge":
            case "Kitchen":
            case "Conservatory":
                remoteView.getViewByID("secret passage").setVisibility(true);
                break;
            default:
                remoteView.getViewByID("secret passage").setVisibility(false);
                break;
        }

        sendCurrentView(player, "room");
    }

    private void gotoChoiceView(Player player, String[] choices, String handleTag, String descript) {
        BGContainer choiceView = player.getRemoteView("choice");
        player.setFlag("handler", handleTag);
        BGContainer choiceList = (BGContainer) choiceView.getViewByID("choices");
        choiceList.clear();

        Button description = (Button) choiceView.getViewByID("decription");
        description.setText(descript);

        String choice;
        Button btn;
        for (int i = 0; i < choices.length; i++) {
            choice = choices[i];

            btn = new Button(140, 270 + 60 * i, 200, 50, choice);
            btn.setId(choice);
            choiceList.add(btn);
        }
    }

    private void sendCurrentView(Player player, String view) {
        player.setCurrentRemoteView(view);
        player.sendView();
    }

    private void sendNotTurnView(Player player, String view) {
        player.setNotTurnView(view);
        player.sendView();
    }

    private void handleNotTurnPacket(String command, JSONArray params, Player player) {
        try {
            switch (command) {
                case "onClick":
                    String button = ((JSONObject)params.get(0)).get("view").toString();
                    switch (button) {
                        case "view suspects":
                            Log.d(tag, "Setting view");
                            player.setFlag("suspect return", "not turn");
                            sendNotTurnView(player, "suspect");
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleChoicePacket(String command, JSONArray params, Player player) {
        try {
            switch ((String)player.getFlag("handler")) {
                case "exit room":
                    switch (command) {
                        case "onClick":
                            String button = ((JSONObject)params.get(0)).get("view").toString();
                            switch (button) {
                                case "Next Door":
                                    BGContainer board = (BGContainer)controller.getViewById("board");
                                    BGContainer doors = board.findViewsWithFlag("room", player.getFlag("currRoomVal"));

                                    int index = ((Integer)player.getFlag("exiting room") + 1) % doors.size();
                                    Log.d(tag, "Going to index " + index);
                                    BGComponent door = doors.get(index);
                                    player.setFlag("exiting room", index);

                                    BGComponent playerToken = player.getViewByID("player");
                                    playerToken.setFlag("tilex", door.getFlag("tilex"));
                                    playerToken.setFlag("tiley", door.getFlag("tiley"));
                                    movePlayer(playerToken, board, 0, 0);
                                    break;
                                case "Exit":
                                    player.getViewByID("player").setFlag("enteredRoom", false);
                                    sendCurrentView(player, "main");
                                    break;
                            }
                            break;
                    }
                    break;
                case "rebuttal":
                    switch (command) {
                        case "onClick":
                            String button = ((JSONObject)params.get(0)).get("view").toString();
                            Player currPlayer = controller.getPlayer(controller.getCurrentTurn());
                            if (button.equals("None")) {
                                Log.d(tag, "None, exiting");
                                int nextPlayer = controller.getNextPlayerNumber((Integer)currPlayer.getFlag("rebuttal player"));
                                gotoPlayerRebuttal(nextPlayer);

                                sendNotTurnView(player, (String)player.getFlag("rebuttal return"));
                                break;
                            }
                            Log.d(tag, button);


                            BGContainer suspectView = currPlayer.getRemoteView("suspect");
                            RadioButton rb = (RadioButton)suspectView.getViewByID(button);
                            rb.setFlag("seenCard", true);
                            rb.setBackgroundColor(Color.yellow);
                            rb.setColor(Color.yellow);
                            rb.setChecked(false);
                            rb.setEnabled(false);

                            gotoChoiceView(currPlayer, new String[]{button}, "rebuttal confirm", "You were shown");
                            sendCurrentView(currPlayer, "choice");

                            sendNotTurnView(player, (String)player.getFlag("rebuttal return"));
                            break;
                    }
                    break;
                case "rebuttal confirm":
                    player.setCurrentRemoteView("room");
                    controller.nextTurn();
                    break;
                case "rebuttal no entry":
                    switch (command) {
                        case "onClick":
                            String button = ((JSONObject)params.get(0)).get("view").toString();
                            Log.d(tag, button);
                            switch (button) {
                                case "Open case folder":
                                    checkCaseFolder(player);
                                    break;
                                case "End turn":
                                    player.setCurrentRemoteView("room");
                                    controller.nextTurn();
                                    break;
                            }
                            break;
                    }
                    break;
                case "player lost":
                    player.setCurrentRemoteView("room");
                    controller.nextTurn();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRoomPacket(String command, JSONArray params, Player player) {
        try {
            BGComponent playerToken = player.getViewByID("player");
            switch (command) {
                case "onClick":
                    String button = ((JSONObject)params.get(0)).get("view").toString();

                    switch (button) {
                        case "accuse":
                            player.setFlag("isAccusing", true);
                            transitionToAccusation(player, true);
                            break;
                        case "suggest":
                            player.setFlag("isAccusing", false);
                            transitionToAccusation(player, false);
                            break;
                        case "view suspects":
                            player.setFlag("suspect return", "room");
                            sendCurrentView(player, "suspect");
                            break;
                        case "exit":
                            BGContainer board = (BGContainer)controller.getViewById("board");
                            BGContainer doors = board.findViewsWithFlag("room", player.getFlag("currRoomVal"));

                            BGComponent door = doors.get(0);
                            player.setFlag("exiting room", 0);


                            playerToken.setFlag("tilex", door.getFlag("tilex"));
                            playerToken.setFlag("tiley", door.getFlag("tiley"));
                            movePlayer(playerToken, board, 0, 0);

                            gotoChoiceView(player, new String[]{"Next Door", "Exit"}, "exit room", "Choose a door to exit from");
                            sendCurrentView(player, "choice");
                            break;
                        case "secret passage":
                            String[] places = {"Study", "Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library"};
                            switch ((String)player.getFlag("currRoom")) {
                                case "Study":
                                    playerToken.setFlag("currRoom", places[4]);
                                    playerToken.setFlag("currRoomVal", 5);
                                    break;
                                case "Lounge":
                                    playerToken.setFlag("currRoom", places[6]);
                                    playerToken.setFlag("currRoomVal", 7);
                                    break;
                                case "Kitchen":
                                    playerToken.setFlag("currRoom", places[0]);
                                    playerToken.setFlag("currRoomVal", 1);
                                    break;
                                case "Conservatory":
                                    playerToken.setFlag("currRoom", places[2]);
                                    playerToken.setFlag("currRoomVal", 3);
                                    break;
                            }
                            moveIntoRoom(player);
                            break;
                    }

                    break;
                case "radioToggle":
                    break;
                case "refresh":
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIndex(String[] coll, String val) {
        int i = 0;
        for (String str : coll) {
            if (str.equals(val)) {
                return i;
            }
            i++;
        }
        return -1;
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
        sendCurrentView(player, "accusation");
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

    private void setPlayerWon(Player player) {
        String finalSuspect = (String)controller.gui.getFlag("final suspect"),
                finalWeapon = (String)controller.gui.getFlag("final weapon"),
                finalPlace = (String)controller.gui.getFlag("final place");

        gotoChoiceView(player, new String[]{finalSuspect, finalWeapon, finalPlace}, "player won", "You win!");
        sendCurrentView(player, "choice");
        sendNotTurnView(player, "choice");

        Button currTurn = (Button) controller.getViewById("current turn");

        String playerLabel = (String)player.getViewByID("player").getFlag("suspect");
        currTurn.setText(playerLabel + " won the game!");
        currTurn.setColor(Color.black);
        currTurn.setBackgroundColor(Color.green);
        switch ((Integer)player.getFlag("player number")) {
            case 0:
                currTurn.setText("Prof. Plum won!");
                currTurn.setBackgroundColor(Color.magenta);
                currTurn.setColor(Color.black);
                Log.d(tag, "Prof. Plum");
                break;
            case 1:
                currTurn.setText("Ms. Scarlett won!");
                currTurn.setBackgroundColor(Color.red);
                currTurn.setColor(Color.black);
                Log.d(tag, "Ms. Scarlett");
                break;
            case 2:
                currTurn.setText("Colonel Mustard won!");
                currTurn.setBackgroundColor(Color.yellow);
                currTurn.setColor(Color.black);
                Log.d(tag, "Colonel Mustard");
                break;
            case 3:
                currTurn.setText("Mrs. White won!");
                currTurn.setBackgroundColor(Color.white);
                currTurn.setColor(Color.black);
                Log.d(tag, "Mrs. White");
                break;
            case 4:
                currTurn.setText("Mr. Green won!");
                currTurn.setBackgroundColor(Color.green);
                currTurn.setColor(Color.black);
                Log.d(tag, "Mr. Green");
                break;
            case 5:
                currTurn.setText("Ms. Peacock won!");
                currTurn.setBackgroundColor(Color.blue);
                currTurn.setColor(Color.white);
                Log.d(tag, "Ms. Peacock");
                break;
        }
    }

    private void checkCaseFolder(Player player) {
        String suspect = (String)player.getFlag("accuse-suspect"),
                weapon = (String)player.getFlag("accuse-weapon"),
                place = (String)player.getFlag("accuse-place");

        String finalSuspect = (String)controller.gui.getFlag("final suspect"),
                finalWeapon = (String)controller.gui.getFlag("final weapon"),
                finalPlace = (String)controller.gui.getFlag("final place");
        if (suspect.equals(finalSuspect) &&
                weapon.equals(finalWeapon) &&
                place.equals(finalPlace)) {
            Log.d(tag, "Win condition");
            setPlayerWon(player);
        } else {
            Log.d(tag, "Player loses");
            player.setFlag("lost game", true);

            //set player lost
            gotoChoiceView(player, new String[]{finalSuspect, finalWeapon, finalPlace}, "player lost", "Case file incorrect. You lose");
            sendCurrentView(player, "choice");

            //check if last remaining other player
            int goodPlayer = 0;
            Player lastGood = null;
            for (int i = 0; i < controller.playerCount(); i++) {
                Player testPlayer = controller.getPlayer(i);
                if (!(Boolean)testPlayer.getFlag("lost game")) {
                    goodPlayer++;
                    lastGood = testPlayer;
                }
                if (goodPlayer >= 2) {
                    break;
                }
            }
            if (goodPlayer < 2) {
                setPlayerWon(lastGood);
                return;
            }
        }
    }

    private void gotoPlayerRebuttal(int playerNum) {
        Player currPlayer = controller.getPlayer(controller.getCurrentTurn());
        String suspect = (String)currPlayer.getFlag("accuse-suspect"),
               weapon = (String)currPlayer.getFlag("accuse-weapon"),
               place = (String)currPlayer.getFlag("accuse-place");
        currPlayer.setFlag("rebuttal player", playerNum);

        Player player = controller.getPlayer(playerNum);
        player.setFlag("rebuttal return", player.getCurrentVisibleView());
        if (playerNum == controller.getCurrentTurn()) {
            //made it around w/o card being found
            gotoChoiceView(currPlayer, new String[]{"Open case folder", "End turn"}, "rebuttal no entry", "Nobody could show you a card");
            sendCurrentView(currPlayer, "choice");
            return;
        }

        ArrayList<String> cards = new ArrayList<>();
        Log.d(tag, "Looking for cards");
        Object suspectCard = player.getFlag("card suspect " + suspect);
        if (suspectCard != null) {
            Log.d(tag, "Suspect card found");
            cards.add(suspect);
        }

        Object weaponCard = player.getFlag("card weapon " + weapon);
        if (weaponCard != null) {
            Log.d(tag, "Weapon card found");
            cards.add(weapon);
        }

        Object placeCard = player.getFlag("card place " + place);
        if (placeCard != null) {
            Log.d(tag, "place card found");
            cards.add(place);
        }

        if (cards.size() == 0) {
            cards.add("None");
        }

        gotoChoiceView(player, cards.toArray(new String[]{}), "rebuttal", "Choose a card to disprove suggestion");
        sendNotTurnView(player, "choice");
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

                            if ((Boolean)player.getFlag("isAccusing")) {
                                checkCaseFolder(player);
                                return;
                            }

                            gotoPlayerRebuttal(controller.getNextPlayerNumber());
                            gotoChoiceView(player, new String[]{}, "", "Waiting for other players");
                            sendCurrentView(player, "choice");

                            break;
                        case "return":
                            remoteView = player.getRemoteView(player.getCurrentRemoteView());
                            group = remoteView.findViewsWithFlag("group", "suspect");
                            resetCheckboxes(group);
                            group = remoteView.findViewsWithFlag("group", "weapon");
                            resetCheckboxes(group);
                            group = remoteView.findViewsWithFlag("group", "place");
                            resetCheckboxes(group);

                            sendCurrentView(player, (String)player.getFlag("accuse return"));
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
                            if (player.isTurn()) {
                                sendCurrentView(player, (String) player.getFlag("suspect return"));
                            } else {
                                sendNotTurnView(player, (String) player.getFlag("suspect return"));
                            }
                            break;
                    }

                    break;
                case "radioToggle":
                    try {
                        BGContainer remoteView = player.getRemoteView(player.getCurrentVisibleView());
                        JSONObject radioParams = (JSONObject)params.get(0);
                        String radioButton = radioParams.get("view").toString();
                        String state = radioParams.get("state").toString();
                        ((RadioButton) remoteView.getViewByID(radioButton)).setChecked(state.equals("true"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "refresh":
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

                    int newRange = (Integer)player.getFlag("walkRange");
                    switch (button) {
                        case "up arrow":
                            if (tile.getFlag("walkup").equals(true)) {
                                movePlayer(playerToken, board, 0, -1);
                                newRange--;
                            }
                            break;
                        case "down arrow":
                            if (tile.getFlag("walkdown").equals(true)) {
                                movePlayer(playerToken, board, 0, 1);
                                newRange--;
                            }
                            break;
                        case "left arrow":
                            if (tile.getFlag("walkleft").equals(true)) {
                                movePlayer(playerToken, board, -1, 0);
                                newRange--;
                            }
                            break;
                        case "right arrow":
                            if (tile.getFlag("walkright").equals(true)) {
                                movePlayer(playerToken, board, 1, 0);
                                newRange--;
                            }
                            break;
                        case "view suspects":
                            player.setFlag("suspect return", "main");
                            sendCurrentView(player, "suspect");
                            break;
                    }
                    try {
                        //int newRange = (Integer)player.getFlag("walkRange") - 1;
                        player.setFlag("walkRange", newRange);
                        ((Label)controller.getViewById("moves")).setText("Moves left: " + newRange);
                        if (playerToken.getFlag("enteredRoom").equals(true)) {
                            moveIntoRoom(player);
                            transitionToRoom(player);
                        } else if (newRange <= 0) {
                            controller.nextTurn();
                        }
                    } catch (Exception e) {
                        playerToken.setFlag("enteredRoom", false);
                        e.printStackTrace();
                    }

                    break;
                case "radioToggle":
                    break;
                case "refresh":
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveIntoRoom(Player player) {
        BGComponent playerToken = player.getViewByID("player");
        player.setFlag("currRoom", playerToken.getFlag("currRoom"));
        player.setFlag("currRoomVal", playerToken.getFlag("currRoomVal"));
        playerToken.setFlag("enteredRoom", false);
        BGContainer room = (BGContainer)controller.getViewById("Room " + player.getFlag("currRoom"));
        BGComponent inRoom = room.findViewWithFlag("suspect", playerToken.getFlag("suspect"));
        playerToken.setX(inRoom.getX());
        playerToken.setY(inRoom.getY());
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
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
