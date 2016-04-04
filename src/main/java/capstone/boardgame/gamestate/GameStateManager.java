package capstone.boardgame.gamestate;

import capstone.boardgame.gamestate.ingame.GameController;
import capstone.boardgame.main.Log;

import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Stack;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameStateManager implements MouseListener {
    private static final String tag = "GameStateManager";
    public static Stack<GameState> states;
    protected SessionManager sessions;

    public GameStateManager() {
        states = new Stack<>();
        states.push(new GameController(this));
        sessions = new SessionManager();
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

    @Override
    public void mouseClicked(MouseEvent e) {
        states.peek().mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        states.peek().mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        states.peek().mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        states.peek().mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        states.peek().mouseExited(e);
    }
}
