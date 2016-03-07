package capstone.boardgame.main;

import capstone.boardgame.GUI.AbsGameLoop;
import capstone.boardgame.gamestate.GameStateManager;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameLoop extends AbsGameLoop {
    GameStateManager gsm;
    public static Assets assets = new Assets();

    public GameLoop(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
        assets.init();

        gsm = new GameStateManager();
        gsm.init();

        super.init();

        System.out.print("GameLoop: " + getParent().getWidth() + " " + getParent().getHeight() + " " + ((float)getWidth()/width) + " " + ((float)getHeight()/height));

        scale((float)getWidth() / width, (float)getHeight() / height);
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
