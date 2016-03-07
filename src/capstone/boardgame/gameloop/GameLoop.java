package capstone.boardgame.gameloop;

import capstone.boardgame.GOP.AbsGameLoop;
import capstone.boardgame.gamestate.GameStateManager;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameLoop extends AbsGameLoop {
    GameStateManager gsm;

    public GameLoop(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
        gsm = new GameStateManager();

        gsm.init();

        super.init();
    }

    @Override
    public void tick(double deltaTime) {
        gsm.tick(deltaTime);
    }

    @Override
    public void render() {
        super.render();
        gsm.render(g2d);
        clear();
    }

    @Override
    public void clear() {
        super.clear();
    }
}
