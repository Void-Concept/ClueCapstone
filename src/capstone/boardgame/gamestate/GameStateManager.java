package capstone.boardgame.gamestate;

import capstone.boardgame.gamestate.ingame.LevelLoader;

import java.awt.*;
import java.util.Stack;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameStateManager {
    private static final String tag = "GameStateManager";
    public static Stack<GameState> states;

    public GameStateManager() {
        states = new Stack<>();
        states.push(new LevelLoader(this));
    }

    public void tick(double deltaTime) {
        states.peek().tick(deltaTime);
    }

    public void render(Graphics2D g) {
        states.peek().render(g);
    }

    public void init() {
        states.peek().init();
    }
}
