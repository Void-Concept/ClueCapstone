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
    public static ArrayList<BGComponent> loadLevel(String path) {
        //ignore path for now
        ArrayList<BGComponent> drawables = new ArrayList<>();


        /*Board board = new Board(320, 40, 640, 640, "clue/ClueBoard.csv");
        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/spritesheet.png"));
        board.setStateTile(1, sheet.getTile(0, 0, 8, 8));
        drawables.add(board);*/
        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/spritesheet.png"));
        drawables.add(createBoard(320, 40, 640, 640, sheet.getTile(0, 0, 8, 8)));

        Label label = new Label(200, 200, "Hello World");
        label.setColor(Color.white);
        drawables.add(label);

        Label players = new Label(1250, 5, "0");
        players.setId("NumPlayers");
        players.setColor(Color.GREEN);
        drawables.add(players);

        Button btn = new Button(0, 500, 100, 30, "Testing");
        btn.setColor(Color.green);
        btn.setMouseListener(new MouseListener() {
            private static final String tag = "MouseListener";
            @Override
            public void mouseClicked(MouseEvent e) {
                Log.d(tag, "Clicked");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Log.d(tag, "Pressed");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Log.d(tag, "Released");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Log.d(tag, "Entered");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Log.d(tag, "Exited");
            }
        });
        drawables.add(btn);

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
        player.setCurrentRemoteView(remoteView.getId());

        Token token = new Token(0, 0, 15, 15);
        BGComponent tile = null;
        switch (playerNumber) {
            case 0:
                tile = board.findViewWithFlag("person", 6);
                token.addFlag("xoff", -3);
                token.addFlag("yoff", 3);
                token.setColor(Color.magenta);
                remoteView.setBackgroundColor(Color.magenta);
                break;
            case 1:
                tile = board.findViewWithFlag("person", 1);
                token.addFlag("xoff", 3);
                token.addFlag("yoff", 3);
                token.setColor(Color.red);
                remoteView.setBackgroundColor(Color.red);
                break;
            case 2:
                tile = board.findViewWithFlag("person", 2);
                token.addFlag("xoff", 0);
                token.addFlag("yoff", 4);
                token.setColor(Color.yellow);
                remoteView.setBackgroundColor(Color.yellow);
                break;
            case 3:
                tile = board.findViewWithFlag("person", 3);
                token.addFlag("xoff", 3);
                token.addFlag("yoff", -3);
                token.setColor(Color.white);
                remoteView.setBackgroundColor(Color.white);
                break;
            case 4:
                tile = board.findViewWithFlag("person", 4);
                token.addFlag("xoff", -3);
                token.addFlag("yoff", -3);
                token.setColor(Color.green);
                remoteView.setBackgroundColor(Color.green);
                break;
            case 5:
                tile = board.findViewWithFlag("person", 5);
                token.addFlag("xoff", 0);
                token.addFlag("yoff", -4);
                token.setColor(Color.blue);
                remoteView.setBackgroundColor(Color.blue);
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
