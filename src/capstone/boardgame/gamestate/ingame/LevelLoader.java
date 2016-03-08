package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.Elements.Board;
import capstone.boardgame.GUI.Elements.Dice;
import capstone.boardgame.GUI.Elements.TileManager;
import capstone.boardgame.GUI.Elements.WalkableTile;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;
import capstone.boardgame.main.Main;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Kyle on 3/1/2016.
 */
public class LevelLoader extends GameState {
    Board board;
    WalkableTile tile;
    Dice dice;
    TileManager tileManager;
    public LevelLoader(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        //board = new Board(10, 10, 256, 256, 16, 16);
        //board.init();
        board = new Board(320, 40, 640, 640, "ClueBoard.csv");
        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "spritesheet.png"));
        board.setStateTile(1, sheet.getTile(0, 0, 8, 8));

        tile = new WalkableTile(0, 0, 16, 16, sheet.getTile(0,0,8,8));
        dice = new Dice(200, 200, 16, 16, 20);
        dice.roll();
        tileManager = new TileManager();
        tileManager.addTile(tile);
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        g.drawString("Hello World", 200, 200);
        board.render(g);
        //tile.render(g);
        dice.render(g);
        tileManager.render(g);
    }
}
