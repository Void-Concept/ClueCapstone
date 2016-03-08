package capstone.boardgame.gamestate;

import capstone.boardgame.GUI.Elements.Board;
import capstone.boardgame.GUI.Elements.Dice;
import capstone.boardgame.GUI.Elements.WalkableTile;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.main.Main;

import java.awt.*;

/**
 * Created by Kyle on 3/1/2016.
 */
public class LevelLoader extends GameState {
    Board board;
    WalkableTile tile;
    Dice dice;
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
        //board.setStateTile(2, sheet.getTile(1, 1, 8, 8));
        tile = new WalkableTile(0, 0, 16, 16, sheet.getTile(0,0,8,8));
        dice = new Dice(0, 200, 16, 16, 6);
        dice.roll();
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        g.drawString("Hello World", 200, 200);
        board.render(g);
        tile.render(g);
        dice.render(g);
    }
}
