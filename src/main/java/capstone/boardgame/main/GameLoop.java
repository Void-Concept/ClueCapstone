package capstone.boardgame.main;

import capstone.boardgame.GUI.AbsGameLoop;
import capstone.boardgame.HTTP.WebSocketServer;
import capstone.boardgame.gamestate.GameStateManager;

import javax.websocket.Session;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameLoop extends AbsGameLoop {
    private static final String tag = "GameLoop";
    GameStateManager gsm;
    public static Assets assets = new Assets();

    public GameLoop(int width, int height) {
        super(width, height);
    }

    public MouseEvent scaleMouseEvent(MouseEvent e) {
        float scaleX = (float)getWidth() / width;
        float scaleY = (float)getHeight() / height;
        int x = (int)(e.getX() / scaleX);
        int y = (int)(e.getY() / scaleY);
        return new MouseEvent(GameLoop.this, e.getID(), e.getWhen(), e.getModifiers(), x, y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }
    @Override
    public void init() {
        assets.init();

        gsm = new GameStateManager();
        gsm.init();

        super.init();

        float scaleX = (float)getWidth() / width;
        float scaleY = (float)getHeight() / height;
        final float scale = scaleX > scaleY ? scaleY : scaleX;
        Log.d(tag, getParent().getWidth() + " " + getParent().getHeight() + " " + (scaleX) + " " + (scaleY) + " " + scale);
        scale(scaleX, scaleY);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gsm.mouseClicked(scaleMouseEvent(e));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                gsm.mousePressed(scaleMouseEvent(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                gsm.mouseReleased(scaleMouseEvent(e));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                gsm.mouseEntered(scaleMouseEvent(e));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                gsm.mouseExited(scaleMouseEvent(e));
            }
        });
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
