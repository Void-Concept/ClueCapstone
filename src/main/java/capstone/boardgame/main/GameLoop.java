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

    @Override
    public void init() {
        assets.init();

        gsm = new GameStateManager();
        gsm.init();

        super.init();

        float scaleX = (float)getWidth() / width;
        float scaleY = (float)getHeight() / height;
        float scale = scaleX > scaleY ? scaleY : scaleX;
        Log.d(tag, getParent().getWidth() + " " + getParent().getHeight() + " " + (scaleX) + " " + (scaleY) + " " + scale);
        scale(scaleX, scaleY);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                WebSocketServer.sendMessage(0, "Hello World");
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

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
