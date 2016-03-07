package capstone.boardgame.gamestate;

import capstone.boardgame.GUI.Elements.Board;
import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;
import capstone.boardgame.main.Main;

import java.awt.*;

/**
 * Created by Kyle on 3/1/2016.
 */
public class LevelLoader extends GameState {
    Board board;
    public LevelLoader(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        board = new Board(10, 10, 128, 128, 16, 16);
        board.init();
        SpriteSheet sheet = new SpriteSheet();
        sheet.setSpriteSheet(LoadImageFrom.LoadImageFrom(Main.class, "spritesheet.png"));

        board.setStateTile(0, sheet.getTile(0, 0, 8, 8));
        board.setState(1, 0, 1);
        board.setState(0, 1, 2);
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        g.drawString("Hello World", 200, 200);
        board.render(g);
    }
}
