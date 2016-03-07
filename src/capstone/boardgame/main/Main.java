package capstone.boardgame.main;

import capstone.boardgame.GOP.GameWindow;
import capstone.boardgame.gameloop.GameLoop;

import java.awt.*;

/**
 * Created by Kyle on 3/1/2016.
 */
public class Main {
    public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static int width = gd.getDisplayMode().getWidth();
    public static int height = gd.getDisplayMode().getHeight();

    public static void main(String[] args) {
        GameWindow frame = new GameWindow("Board Games", width, height);
        frame.setFullScreen(1);
        frame.add(new GameLoop(width, height));
        frame.setVisible(true);
    }
}
