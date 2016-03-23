package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.Elements.*;
import capstone.boardgame.GUI.Elements.Button;
import capstone.boardgame.GUI.Elements.Label;
import capstone.boardgame.GUI.GameGUIContainer;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.gamedata.Game;
import capstone.boardgame.gamedata.GameObject.Player;
import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;
import capstone.boardgame.main.Log;
import capstone.boardgame.main.Main;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Kyle on 3/1/2016.
 */
public class LevelLoader extends GameState {
    private static final String tag = "LevelLoader";
    GameGUIContainer gui = new GameGUIContainer();
    Board board;
    WalkableTile tile;
    Dice dice;
    TileManager tileManager;
    Player player;
    Label label;
    public LevelLoader(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        BGDrawable.setDefaultColor(Color.cyan);

        board = new Board(320, 40, 640, 640, "ClueBoard.csv");
        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "spritesheet.png"));
        board.setStateTile(1, sheet.getTile(0, 0, 8, 8));

        dice = new Dice(200, 200, 25, 25, 20);
        dice.roll();

        tile = new WalkableTile(0, 0, 16, 16, sheet.getTile(0,0,8,8));
        tileManager = new TileManager();
        tileManager.addTile(tile);

        player = new Player(0);
        Token playerToken = new Token(50, 50, 10, 10);
        playerToken.setColor(Color.red);
        player.addDrawable(playerToken);

        label = new Label(200, 200, "Hello World");
        label.setColor(Color.gray);

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
                Log.d(tag, "Reloading assets");
                Game.loadAssets();
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

        gui.add(board);
        //gui.add(tile);
        gui.add(dice);
        gui.add(player);
        gui.add(label);
        gui.add(btn);
        gui.add(tileManager);
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        gui.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gui.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gui.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gui.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        gui.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        gui.mouseExited(e);
    }
}
