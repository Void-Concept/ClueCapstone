package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.Component.*;
import capstone.boardgame.GUI.Component.Button;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.main.Log;
import capstone.boardgame.main.Main;

import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/23/2016.
 */
public class LevelLoader {
    public static ArrayList<BGComponent> loadLevel(String path) {
        //ignore path for now
        ArrayList<BGComponent> drawables = new ArrayList<>();


        Board board = new Board(320, 40, 640, 640, "clue/ClueBoard.csv");
        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "clue/spritesheet.png"));
        board.setStateTile(1, sheet.getTile(0, 0, 8, 8));
        drawables.add(board);

        Label label = new Label(200, 200, "Hello World");
        label.setColor(Color.white);
        drawables.add(label);

        Label players = new Label(1200, 5, "0");
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

    public static Player setupPlayer(int playerNumber, Session session) {
        Player player = new Player(session.getId(), session);

        Token token = null;
        switch (playerNumber) {
            case 0:
                token = new Token(10, 10, 10, 10);
                token.setColor(Color.magenta);
                break;
            case 1:
                token = new Token(20, 10, 10, 10);
                token.setColor(Color.red);
                break;
            case 2:
                token = new Token(30, 10, 10, 10);
                token.setColor(Color.yellow);
                break;
            case 3:
                token = new Token(40, 10, 10, 10);
                token.setColor(Color.white);
                break;
            case 4:
                token = new Token(50, 10, 10, 10);
                token.setColor(Color.green);
                break;
            case 5:
                token = new Token(60, 10, 10, 10);
                token.setColor(Color.blue);
                break;

        }
        if (token != null) {
            token.setId("player");
            player.add(token);
        }

        return player;
    }
}
