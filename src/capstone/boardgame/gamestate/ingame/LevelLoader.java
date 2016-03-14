package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.Elements.*;
import capstone.boardgame.GUI.Elements.Label;
import capstone.boardgame.GUI.GameGUIContainer;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.gamedata.GameObject.Player;
import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;
import capstone.boardgame.main.Main;

import java.awt.*;

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
        gui.add(board);
        //gui.add(tile);
        gui.add(dice);
        gui.add(player);
        gui.add(label);
        gui.add(tileManager);
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        gui.render(g);
        /*
        //g.drawString("Hello World", 200, 200);

        label.render(g);

        board.render(g);
        //tile.render(g);
        dice.render(g);
        tileManager.render(g);
        player.render(g);*/
    }
}
