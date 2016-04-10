package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.BGContainer;
import capstone.boardgame.GUI.Component.*;
import capstone.boardgame.GUI.Component.Button;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.Drawable.BGDrawable;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.main.Log;
import capstone.boardgame.main.Main;

import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Created by Kyle on 3/23/2016.
 */
public class LevelLoader {
    private static final String tag = "LevelLoader";
    public static ArrayList<BGComponent> loadLevel(String path) {
        //ignore path for now
        ArrayList<BGComponent> drawables = new ArrayList<>();

        SpriteSheet board = new SpriteSheet();
        board.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/boardbackground.png"));
        Tile boardBackground = new Tile(317, 77, 630, 631, board.getTile(0, 0, 194, 202));
        drawables.add(boardBackground);

        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/spritesheet.png"));
        drawables.add(createBoard(320, 80, 640, 640, sheet.getTile(0, 0, 8, 8)));

        Label label = new Label(380, 120, "Study");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(615, 155, "Hall");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(820, 140, "Lounge");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(370, 280, "Library");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(780, 380, "Dining Room");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(335, 430, "Billards Room");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(340, 610, "Conservatory");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(585, 580, "Ball Room");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(830, 610, "Kitchen");
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        label = new Label(580, 400, "Clue");
        label.setFont(label.getFont().deriveFont(40.0f));
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        Button turn = new Button(530, 10, 220, 40, "Colonel Mustard's Turn");
        turn.setBackgroundColor(Color.yellow);
        turn.setColor(Color.black);
        drawables.add(turn);

        Label players = new Label(1250, 5, "0");
        players.setId("NumPlayers");
        players.setColor(Color.GREEN);
        drawables.add(players);
        return drawables;
    }

    private static BGContainer createBoard(int x, int y, int width, int height, BufferedImage img) {
        BGContainer board = new BGContainer();
        board.setId("board");

        try {
            URL boardLoader = LevelLoader.class.getClassLoader().getResource("clue/boardMovement.txt");
            FileReader fr = new FileReader(boardLoader.getFile());
            BufferedReader br = new BufferedReader(fr);
            ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
            String line;
            ArrayList<String> sLine;
            ArrayList<Integer> aList;

            while ((line = br.readLine()) != null) {
                sLine = new ArrayList<>(Arrays.asList(line.split(",")));
                aList = new ArrayList<>();
                for (String str : sLine) {
                    aList.add(Integer.parseInt(str));
                }
                list.add(aList);
            }

            int tilesX = list.get(0).size();
            int tilesY = list.size();
            int tileWidth = width / tilesX;
            int tileHeight = height / tilesY;

            for (int w = 0; w < tilesX; w++) {
                for (int h = 0; h < tilesY; h++) {
                    int value = list.get(h).get(w);
                    if (value != 0) {
                        WalkableTile tile = new WalkableTile(x + tileWidth * w, y + tileHeight * h, tileWidth, tileHeight, img);
                        tile.addFlag("walkright", (value & 1) != 0);
                        tile.addFlag("walkleft", (value & 1<<1) != 0);
                        tile.addFlag("walkdown", (value & 1<<2) != 0);
                        tile.addFlag("walkup", (value & 1<<3) != 0);
                        tile.addFlag("isdoor", (value & 1<<4) != 0);
                        if ((value & 1<<4) != 0) {
                            tile.addFlag("room", value >> 5 & 0xF);
                            tile.setVisibility(false);
                        }
                        tile.addFlag("isstart", (value & 1<<9) != 0);
                        if ((value & 1<<9) != 0) {
                            tile.addFlag("person", value >> 10 & 0x7);
                        }
                        tile.addFlag("tilex", w);
                        tile.addFlag("tiley", h);
                        board.add(tile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }

    public static Player setupPlayer(int playerNumber, Session session, BGContainer gui) {
        Player player = new Player(session.getId(), session);
        BGContainer board = (BGContainer)gui.getViewByID("board");

        BGContainer remoteView = createOldClueView();
        player.addRemoteView(remoteView);
        remoteView = createSuspectView();
        player.addRemoteView(remoteView);
        remoteView = createControllerView();
        player.addRemoteView(remoteView);
        remoteView = createAccusationView();
        player.addRemoteView(remoteView);
        player.setCurrentRemoteView(remoteView.getId());
        player.setFlag("suspect return", "main");

        Token token = new Token(0, 0, 15, 15);
        BGComponent tile = null;
        switch (playerNumber) {
            case 0:
                tile = board.findViewWithFlag("person", 6);
                token.addFlag("xoff", -3);
                token.addFlag("yoff", 3);
                token.setColor(Color.magenta);
                player.setRemoteViewBackground(Color.magenta);
                Log.d(tag, "Prof. Plum");
                break;
            case 1:
                tile = board.findViewWithFlag("person", 1);
                token.addFlag("xoff", 3);
                token.addFlag("yoff", 3);
                token.setColor(Color.red);
                player.setRemoteViewBackground(Color.red);
                Log.d(tag, "Ms. Scarlett");
                break;
            case 2:
                tile = board.findViewWithFlag("person", 2);
                token.addFlag("xoff", 0);
                token.addFlag("yoff", 4);
                token.setColor(Color.yellow);
                player.setRemoteViewBackground(Color.yellow);
                Log.d(tag, "Colonel Mustard");
                break;
            case 3:
                tile = board.findViewWithFlag("person", 3);
                token.addFlag("xoff", 3);
                token.addFlag("yoff", -3);
                token.setColor(Color.white);
                player.setRemoteViewBackground(Color.white);
                Log.d(tag, "Mrs. White");
                break;
            case 4:
                tile = board.findViewWithFlag("person", 4);
                token.addFlag("xoff", -3);
                token.addFlag("yoff", -3);
                token.setColor(Color.green);
                player.setRemoteViewBackground(Color.green);
                Log.d(tag, "Mr. Green");
                break;
            case 5:
                tile = board.findViewWithFlag("person", 5);
                token.addFlag("xoff", 0);
                token.addFlag("yoff", -4);
                token.setColor(Color.blue);
                player.setRemoteViewBackground(Color.blue);
                Log.d(tag, "Ms. Peacock");
                break;

        }
        if (tile != null) {
            token.setX(tile.getX() + tile.getWidth() / 2 - token.getWidth() / 2 + (Integer)token.getFlag("xoff"));
            token.setY(tile.getY() + tile.getHeight() / 2 - token.getHeight() / 2 + (Integer)token.getFlag("yoff"));
            token.addFlag("tilex", tile.getFlag("tilex"));
            token.addFlag("tiley", tile.getFlag("tiley"));
        }
        token.setId("player");
        player.add(token);

        return player;
    }

    private static BGContainer createAccusationView() {
        BGContainer remoteView = createSuspectView();
        remoteView.setId("accusation");
        remoteView.remove("return");

        Button ret = new Button(250, 580, 150, 50, "Submit");
        ret.setId("submit");
        ret.setFont(ret.getFont().deriveFont(30.0f));
        remoteView.add(ret);

        return remoteView;
    }

    private static BGContainer createControllerView() {
        BGContainer remoteView = new BGContainer();
        remoteView.setId("main");

        Label title = new Label(180, 10, "Clue");
        title.setFont(title.getFont().deriveFont(60.0f));
        remoteView.add(title);

        BGContainer controls = new BGContainer();
        controls.setId("controls");

        Button ubtn = new Button(165, 200, 150, 150, "^");
        ubtn.setId("up arrow");
        controls.add(ubtn);

        Button rbtn = new Button(315, 350, 150, 150, ">");
        rbtn.setId("right arrow");
        controls.add(rbtn);

        Button lbtn = new Button(15, 350, 150, 150, "<");
        lbtn.setId("left arrow");
        controls.add(lbtn);

        Button dbtn = new Button(165, 500, 150, 150, "v");
        dbtn.setId("down arrow");
        controls.add(dbtn);

        remoteView.add(controls);

        Button ss = new Button(115, 725, 250, 60, "Suspect Sheet");
        ss.setId("view suspects");
        ss.setFont(ss.getFont().deriveFont(30.0f));
        remoteView.add(ss);

        return remoteView;
    }

    private static BGContainer createSuspectView() {
        BGContainer remoteView = new BGContainer();
        remoteView.setId("suspect");
        BGComponent.setDefaultBackgroundColor(Color.white);

        Label title = new Label(180, 10, "Clue");
        title.setFont(title.getFont().deriveFont(60.0f));
        remoteView.add(title);

        String[] suspects = {"Mr. Green", "Colonel Mustard", "Ms. Peacock", "Prof. Plum", "Ms. Scarlet", "Ms. White"};
        String[] weapons = {"Candlestick", "Knife", "Lead Pipe", "Pistol", "Rope", "Wrench"};
        String[] places = {"Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study"};

        BGContainer suspectContainer = new BGContainer();
        suspectContainer.setId("suspects");

        Label suspect = new Label(20, 90, "Suspects");
        suspect.setId("suspect label");
        suspect.setFont(suspect.getFont().deriveFont(30.0f));
        suspectContainer.add(suspect);

        for (int i = 0; i < suspects.length; i++) {
            RadioButton button = new RadioButton(10, 130 + 40*i, 40, 40, 2);
            button.setId("checkbox-"+suspects[i]);
            button.addFlag("group", "suspect");
            suspectContainer.add(button);

            Label aLabel = new Label(55, 140 + 40*i, suspects[i]);
            aLabel.setFont(aLabel.getFont().deriveFont(25.0f));
            suspectContainer.add(aLabel);
        }
        remoteView.add(suspectContainer);


        BGContainer weaponContainer = new BGContainer();
        weaponContainer.setId("weapons");

        Label weapon = new Label(20, 400, "Weapons");
        weapon.setId("weapon label");
        weapon.setFont(weapon.getFont().deriveFont(30.0f));
        weaponContainer.add(weapon);

        for (int i = 0; i < weapons.length; i++) {
            RadioButton button = new RadioButton(10, 440 + 40*i, 40, 40, 2);
            button.setId("checkbox-"+weapons[i]);
            button.addFlag("group", "weapon");
            weaponContainer.add(button);

            Label aLabel = new Label(55, 450 + 40*i, weapons[i]);
            aLabel.setFont(aLabel.getFont().deriveFont(25.0f));
            weaponContainer.add(aLabel);
        }
        remoteView.add(weaponContainer);

        BGContainer placeContainer = new BGContainer();
        placeContainer.setId("places");

        Label place = new Label(260, 90, "Places");
        place.setId("places label");
        place.setFont(place.getFont().deriveFont(30.0f));
        placeContainer.add(place);

        for (int i = 0; i < places.length; i++) {
            RadioButton button = new RadioButton(250, 130 + 40*i, 40, 40, 2);
            button.setId("checkbox-"+places[i]);
            button.addFlag("group", "place");
            placeContainer.add(button);

            Label aLabel = new Label(295, 140 + 40*i, places[i]);
            aLabel.setFont(aLabel.getFont().deriveFont(25.0f));
            placeContainer.add(aLabel);
        }
        remoteView.add(placeContainer);

        Button ret = new Button(250, 580, 150, 50, "Back");
        ret.setId("return");
        ret.setFont(ret.getFont().deriveFont(30.0f));
        remoteView.add(ret);

        return remoteView;
    }

    private static BGContainer createOldClueView() {
        BGContainer remoteView = new BGContainer();
        remoteView.setId("old");
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
        return remoteView;
    }
}
