package capstone.boardgame.gamestate;

import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Created by Kyle on 3/1/2016.
 */
public abstract class GameState implements MouseListener {
    private static final String tag = "GameState";
    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public abstract void init();
    public abstract void tick(double deltaTime);
    public abstract void render(Graphics2D g);
}
