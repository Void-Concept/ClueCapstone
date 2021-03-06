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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;

/**
 * Created by Kyle on 3/23/2016.
 */
public class LevelLoader {
    private static final String tag = "LevelLoader";


    public static final String[] suspects = {"Prof. Plum", "Ms. Scarlet", "Colonel Mustard", "Ms. White", "Mr. Green", "Ms. Peacock"};
//    public static final String[] suspects = {"Mr. Green", "Colonel Mustard", "Ms. Peacock", "Prof. Plum", "Ms. Scarlet", "Ms. White"};
    public static final String[] weapons = {"Candlestick", "Knife", "Lead Pipe", "Pistol", "Rope", "Wrench"};
    public static final String[] places = {"Hall", "Lounge", "Dining Room", "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study"};

    public static ArrayList<BGComponent> loadLevel(String path) {
        //ignore path for now
        ArrayList<BGComponent> drawables = new ArrayList<>();
        BGComponent.setDefaultBackgroundColor(new Color(0,0,0,0));
        Button.setDefaultBackgroundColor(new Color(0,0,0,0));

        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/spritesheet.png"));
        drawables.add(createBoard(320, 80, 640, 640, sheet.getTile(0, 0, 8, 8)));

        SpriteSheet board = new SpriteSheet();
        board.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/boardbackground.png"));
        Tile boardBackground = new Tile(317, 77, 630, 631, board.getTile(0, 0, 194, 202));
        drawables.add(boardBackground);

        Label label;

        drawables.add(createRoomDetails(615, 155, places[0]));
        drawables.add(createRoomDetails(820, 140, places[1]));
        drawables.add(createRoomDetails(780, 380, places[2]));
        drawables.add(createRoomDetails(830, 610, places[3]));
        drawables.add(createRoomDetails(585, 580, places[4]));
        drawables.add(createRoomDetails(340, 610, places[5]));
        drawables.add(createRoomDetails(335, 430, places[6]));
        drawables.add(createRoomDetails(370, 280, places[7]));
        drawables.add(createRoomDetails(380, 120, places[8]));

        label = new Label(580, 400, "Clue");
        label.setFont(label.getFont().deriveFont(40.0f));
        label.setColor(Color.white);
        label.applyEffect(2);
        drawables.add(label);

        Dice die = new Dice(200, 200, 60, 60, 6);
        die.setId("walk range");
        die.roll();
        die.setBackgroundColor(Color.blue);
        die.setColor(Color.white);
        drawables.add(die);

        Label range = new Label(800, 20, "Moves left: 0");
        range.setId("moves");
        range.setColor(Color.white);
        range.applyEffect(2);
        drawables.add(range);

        Button turn = new Button(530, 10, 220, 40, "Prof. Plum's Turn");
        turn.setId("current turn");
        turn.setBackgroundColor(Color.magenta);
        turn.setColor(Color.black);
        drawables.add(turn);

        Label players = new Label(1250, 5, "0");
        players.setId("NumPlayers");
        players.setColor(Color.GREEN);
        drawables.add(players);

        Button endGame = new Button(10, 5, 130, 35, "Restart Game");
        endGame.setId("EndGame");
        endGame.setColor(Color.black);
        //endGame.setBackgroundColor(Color.white);
        drawables.add(endGame);

        drawables.add(createBoardAccusation());
        return drawables;
    }

    private static BGContainer createBoardAccusation() {
        BGContainer container = new BGContainer();
        container.setId("accused");
        container.setVisibility(false);

        Label accuseTitle = new Label(1000, 100, "Accusing:");
        accuseTitle.setId("accuse title");
        container.add(accuseTitle);

        Label accusePlayer = new Label(1000, 150, "Suspect: Colonel Mustard");
        accusePlayer.setId("accuse suspect");
        container.add(accusePlayer);

        Label accuseWeapon = new Label(1000, 180, "Weapon: Candlestick");
        accuseWeapon.setId("accuse weapon");
        container.add(accuseWeapon);

        Label accusePlace = new Label(1000, 210, "Place: Billards Room");
        accusePlace.setId("accuse place");
        container.add(accusePlace);

        return container;
    }

    private static BGContainer createRoomDetails(int x, int y, String room) {
        BGContainer area = new BGContainer();
        area.setId("Room " + room);
        area.addFlag("room",room);

        Label label = new Label(x, y, room);
        label.setColor(Color.white);
        label.applyEffect(2);
        area.add(label);

        Tile tile;
        int xOff, yOff;
        int xoffoff = 5 * room.length() - 25;
        for (int i = 0; i < suspects.length; i++) {
            xOff = ((i % 3)) * 50 - 25 + xoffoff;
            yOff = ((i % 2)) * 50 - 20;
            tile = new Tile(x + xOff, y + yOff, 0, 0, null);
            tile.setVisibility(false);
            tile.addFlag("room", room);
            tile.addFlag("suspect", suspects[i]);
            area.add(tile);
        }

        return area;
    }

    private static BGContainer createBoard(int x, int y, int width, int height, BufferedImage img) {
        BGContainer board = new BGContainer();
        board.setId("board");

        try {
            URL boardLoader = LevelLoader.class.getClassLoader().getResource("clue/boardMovement.txt");
            //FileReader fr = new FileReader(boardLoader.getFile());
            BufferedReader br = new BufferedReader(new InputStreamReader(boardLoader.openStream()));
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

    private static Token setupToken(int number, BGContainer board) {
        Token token = new Token(0, 0, 15, 15);
        token.setFlag("enteredRoom", false);
        token.applyEffect(2);
        BGComponent tile = null;
        switch (number) {
            case 0:
                tile = board.findViewWithFlag("person", 6);
                token.addFlag("xoff", -3);
                token.addFlag("yoff", 3);
                token.addFlag("suspect", suspects[0]);
                //token.setColor(new Color(0xA8, 0x50, 0xA8));
                token.setColor(Color.magenta);
                Log.d(tag, "Prof. Plum");
                break;
            case 1:
                tile = board.findViewWithFlag("person", 1);
                token.addFlag("xoff", 3);
                token.addFlag("yoff", 3);
                token.addFlag("suspect", suspects[1]);
                //token.setColor(new Color(0x8B, 0, 0));
                token.setColor(Color.RED);
                Log.d(tag, "Ms. Scarlett");
                break;
            case 2:
                tile = board.findViewWithFlag("person", 2);
                token.addFlag("xoff", 0);
                token.addFlag("yoff", 4);
                token.addFlag("suspect", suspects[2]);
                //token.setColor(new Color(0xF5, 0xD0, 0x4C));
                token.setColor(Color.YELLOW);
                Log.d(tag, "Colonel Mustard");
                break;
            case 3:
                tile = board.findViewWithFlag("person", 3);
                token.addFlag("xoff", 3);
                token.addFlag("yoff", -3);
                token.addFlag("suspect", suspects[3]);
                token.setColor(Color.white);
                Log.d(tag, "Mrs. White");
                break;
            case 4:
                tile = board.findViewWithFlag("person", 4);
                token.addFlag("xoff", -3);
                token.addFlag("yoff", -3);
                token.addFlag("suspect", suspects[4]);
                token.setColor(new Color(0x22, 0x8B, 0x22));
                //token.setColor(Color.green);
                Log.d(tag, "Mr. Green");
                break;
            case 5:
                tile = board.findViewWithFlag("person", 5);
                token.addFlag("xoff", 0);
                token.addFlag("yoff", -4);
                token.addFlag("suspect", suspects[5]);
                //token.setColor(new Color(0x41, 0x69, 0xE1));
                token.setColor(Color.blue);
                Log.d(tag, "Ms. Peacock");
                break;

        }
        if (tile != null) {
            token.setX(tile.getX() + tile.getWidth() / 2 - token.getWidth() / 2 + (Integer)token.getFlag("xoff"));
            token.setY(tile.getY() + tile.getHeight() / 2 - token.getHeight() / 2 + (Integer)token.getFlag("yoff"));
            token.addFlag("tilex", tile.getFlag("tilex"));
            token.addFlag("tiley", tile.getFlag("tiley"));
        }
        return token;
    }

    public static BGComponent setupNPC(int npcNumber, BGContainer gui) {
        return setupToken(npcNumber, (BGContainer)gui.getViewByID("board"));
    }

    public static Player setupPlayer(int playerNumber, Session session, BGContainer gui) {
        Player player = new Player(session.getId(), session);
        BGContainer board = (BGContainer)gui.getViewByID("board");

        BGContainer remoteView = createOldClueView();
        player.addRemoteView(remoteView);

        remoteView = createSuspectView();
        player.addRemoteView(remoteView);

        remoteView = createAccusationView();
        player.addRemoteView(remoteView);

        remoteView = createRoomView();
        player.addRemoteView(remoteView);

        remoteView = createChoiceView();
        player.addRemoteView(remoteView);

        BGContainer notTurnView = createNotTurnView();
        player.addRemoteView(notTurnView);

        remoteView = createControllerView();
        player.addRemoteView(remoteView);

        player.setCurrentRemoteView(remoteView.getId());
        player.setNotTurnView(notTurnView.getId());
        player.setFlag("suspect return " + player.isTurn(), "main");

        Token token = setupToken(playerNumber, board);
        player.setRemoteViewBackground(token.getColor());

        token.setId("player");
        player.add(token);

        player.setFlag("lost game", false);
        player.setFlag("player number", playerNumber);

        return player;
    }

    private static BGContainer createNotTurnView() {
        BGContainer remoteView = new BGContainer();
        remoteView.setId("not turn");

        Label title = new Label(180, 10, "Clue");
        title.setFont(title.getFont().deriveFont(60.0f));
        remoteView.add(title);

        Button descript = new Button(65, 250, 350, 60, "It's Not Your Turn");
        descript.setBackgroundColor(Color.white);
        descript.setFont(descript.getFont().deriveFont(40.0f));
        descript.setId("turn");
        remoteView.add(descript);

        Button ss = new Button(115, 400, 250, 60, "Suspect Sheet");
        ss.setId("view suspects");
        ss.setBackgroundColor(Color.gray);
        ss.setFont(ss.getFont().deriveFont(30.0f));
        remoteView.add(ss);

        return remoteView;
    }

    private static BGContainer createChoiceView() {
        BGContainer remoteView = new BGContainer();
        remoteView.setId("choice");

        Label title = new Label(180, 10, "Clue");
        title.setFont(title.getFont().deriveFont(60.0f));
        remoteView.add(title);

        Button descript = new Button(40, 170, 400, 40, "");
        descript.setBackgroundColor(new Color(0, 0, 0, 0));
        descript.setId("decription");
        remoteView.add(descript);

        BGContainer choices = new BGContainer();
        choices.setId("choices");
        remoteView.add(choices);

        return remoteView;
    }

    private static BGContainer createRoomView() {
        BGContainer remoteView = new BGContainer();
        remoteView.setId("room");

        Label title = new Label(180, 10, "Clue");
        title.setFont(title.getFont().deriveFont(60.0f));
        remoteView.add(title);

        Button suggest = new Button(20, 200, 200, 90, "Suggest");
        suggest.setFont(suggest.getFont().deriveFont(30.0f));
        suggest.setId("suggest");
        remoteView.add(suggest);

        Button sheet = new Button(260, 200, 200, 90, "Suspect Sheet");
        sheet.setFont(sheet.getFont().deriveFont(30.0f));
        sheet.setId("view suspects");
        remoteView.add(sheet);

        Button accuse = new Button(20, 400, 200, 90, "Accuse");
        accuse.setFont(accuse.getFont().deriveFont(30.0f));
        accuse.setId("accuse");
        remoteView.add(accuse);


        Button exit = new Button(260, 400, 200, 90, "Exit room");
        exit.setFont(exit.getFont().deriveFont(30.0f));
        exit.setId("exit");
        remoteView.add(exit);

        Button passage = new Button(130, 600, 220, 90, "Secret Passage");
        passage.setFont(passage.getFont().deriveFont(30.0f));
        passage.setId("secret passage");
        remoteView.add(passage);
        passage.setVisibility(false);

        return remoteView;
    }

    private static BGContainer createAccusationView() {
        BGContainer remoteView = createSuspectView();
        remoteView.setId("accusation");
        remoteView.remove("return");

        Button submit = new Button(250, 540, 150, 50, "Submit");
        submit.setId("submit");
        submit.setFont(submit.getFont().deriveFont(30.0f));
        remoteView.add(submit);

        Button ret = new Button(250, 630, 150, 50, "Back");
        ret.setId("return");
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

        BGContainer suspectContainer = new BGContainer();
        suspectContainer.setId("suspects");

        Label suspect = new Label(20, 90, "Suspects");
        suspect.setId("suspect label");
        suspect.setFont(suspect.getFont().deriveFont(30.0f));
        suspectContainer.add(suspect);

        for (int i = 0; i < suspects.length; i++) {
            RadioButton button = new RadioButton(10, 130 + 40*i, 40, 40, 2);
            button.setId(suspects[i]);
            button.addFlag("group", "suspect");
            button.setFlag("hasCard", false);
            button.setFlag("seenCard", false);
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
            button.setId(weapons[i]);
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
            button.setId(places[i]);
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

    public static void changeTurn(GameController controller, int playerNum) {
        Button currTurn = (Button) controller.getViewById("current turn");
        Player player = controller.getPlayer(playerNum);

        //if player lost, skip turn
        if ((Boolean)player.getFlag("lost game")) {
            controller.nextTurn();
            return;
        }

        Dice die = (Dice)controller.getViewById("walk range");
        die.roll();
        die.setBackgroundColor(Color.white);
        die.setColor(Color.black);
        player.setFlag("walkRange", die.getLast());
        ((Label)controller.getViewById("moves")).setText("Moves left: " + player.getFlag("walkRange"));

        player.getRemoteView("room").getViewByID("exit").setVisibility(true);

        switch (playerNum) {
            case 0:
                currTurn.setText("Prof. Plum's Turn");
                currTurn.setBackgroundColor(Color.magenta);
                currTurn.setColor(Color.black);
                Log.d(tag, "Prof. Plum");
                break;
            case 1:
                currTurn.setText("Ms. Scarlett's Turn");
                currTurn.setBackgroundColor(Color.red);
                currTurn.setColor(Color.black);
                Log.d(tag, "Ms. Scarlett");
                break;
            case 2:
                currTurn.setText("Colonel Mustard's Turn");
                currTurn.setBackgroundColor(Color.yellow);
                currTurn.setColor(Color.black);
                Log.d(tag, "Colonel Mustard");
                break;
            case 3:
                currTurn.setText("Mrs. White's Turn");
                currTurn.setBackgroundColor(Color.white);
                currTurn.setColor(Color.black);
                Log.d(tag, "Mrs. White");
                break;
            case 4:
                currTurn.setText("Mr. Green's Turn");
                currTurn.setBackgroundColor(Color.green);
                currTurn.setColor(Color.black);
                Log.d(tag, "Mr. Green");
                break;
            case 5:
                currTurn.setText("Ms. Peacock's Turn");
                currTurn.setBackgroundColor(Color.blue);
                currTurn.setColor(Color.white);
                Log.d(tag, "Ms. Peacock");
                break;
        }
    }

    public static void finishSetup(GameController controller) {
        //deal cards randomly
        Random random = new Random(System.currentTimeMillis());

        int usedCards = 0, card;
        int totalCards = suspects.length + weapons.length + places.length ; //all except used cards
        boolean[] usedCard = new boolean[totalCards+3];

        //get one of each card
        card = random.nextInt(suspects.length);
        usedCard[card] = true;
        controller.gui.setFlag("final suspect", suspects[card]);
        Log.d("final suspect", suspects[card]);

        card = random.nextInt(weapons.length);
        usedCard[card+suspects.length] = true;
        controller.gui.setFlag("final weapon", weapons[card]);
        Log.d("final weapon", weapons[card]);

        card = random.nextInt(places.length);
        usedCard[card+suspects.length+weapons.length] = true;
        controller.gui.setFlag("final place", places[card]);
        Log.d("final place", places[card]);

        while (usedCards < totalCards-3) {
            for (int i = 0; i < controller.playerCount() && usedCards < totalCards-3; i++) {
                Player player = controller.getPlayer(i);
                BGContainer suspectView = player.getRemoteView("suspect");
                BGContainer accuseView = player.getRemoteView("accusation");

                //while the next random card hasn't been used
                while ( usedCard[(card = random.nextInt(totalCards))] );
                usedCard[card] = true;

                if (card < suspects.length) {
                    //is suspect
                    BGContainer suspectList = suspectView.findViewsWithFlag("group", "suspect");
                    RadioButton rb = (RadioButton)suspectList.get(card);
                    rb.setChecked(false);
                    rb.setEnabled(false);
                    rb.setBackgroundColor(Color.green);
                    rb.setColor(Color.green);
                    rb.setFlag("hasCard", true);
                    player.setFlag("card suspect " + suspects[card], true);

                    RadioButton rb2 = (RadioButton)accuseView.findViewsWithFlag("group", "suspect").get(card);
                    rb2.setBackgroundColor(Color.green);
                    rb2.setFlag("hasCard", true);

                    Log.d(tag, "Giving player " + suspects[i] + " card " + suspects[card]);
                } else if (card < suspects.length + weapons.length) {
                    //is weapon
                    BGContainer suspectList = suspectView.findViewsWithFlag("group", "weapon");
                    RadioButton rb = (RadioButton)suspectList.get(card-suspects.length);
                    rb.setChecked(false);
                    rb.setEnabled(false);
                    rb.setBackgroundColor(Color.green);
                    rb.setColor(Color.green);
                    rb.setFlag("hasCard", true);
                    player.setFlag("card weapon " + weapons[card-suspects.length], true);

                    RadioButton rb2 = (RadioButton)accuseView.findViewsWithFlag("group", "weapon").get(card-suspects.length);
                    rb2.setBackgroundColor(Color.green);
                    rb2.setFlag("hasCard", true);

                    Log.d(tag, "Giving player " + suspects[i] + " card " + weapons[card-suspects.length]);
                } else {
                    //is place
                    BGContainer suspectList = suspectView.findViewsWithFlag("group", "place");
                    RadioButton rb = (RadioButton)suspectList.get(card-suspects.length-weapons.length);
                    rb.setChecked(false);
                    rb.setEnabled(false);
                    rb.setBackgroundColor(Color.green);
                    rb.setColor(Color.green);
                    rb.setFlag("hasCard", true);
                    player.setFlag("card place " + places[card-suspects.length-weapons.length], true);

                    RadioButton rb2 = (RadioButton)accuseView.findViewsWithFlag("group", "place").get(card-suspects.length-weapons.length);
                    rb2.setBackgroundColor(Color.green);
                    rb2.setFlag("hasCard", true);

                    Log.d(tag, "Giving player " + suspects[i] + " card " + places[card-weapons.length-suspects.length]);
                }

                usedCards++;
            }
        }
    }
}
