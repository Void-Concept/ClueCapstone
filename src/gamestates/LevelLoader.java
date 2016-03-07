package gamestates;

import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;

import java.awt.*;

/**
 * Created by Kyle on 3/1/2016.
 */
public class LevelLoader extends GameState {
    public LevelLoader(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {

    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        g.drawString("Hello World", 200, 200);
    }
}
