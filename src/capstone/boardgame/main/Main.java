package capstone.boardgame.main;

import capstone.boardgame.GUI.GameWindow;

import java.awt.*;

/**
 * Created by Kyle on 3/1/2016.
 */
public class Main {
    public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static int width = 1280;//gd.getDisplayMode().getWidth();
    public static int height = 720;//gd.getDisplayMode().getHeight();

    public static void main(String[] args) {
        GameWindow frame = new GameWindow("Board Games", width, height);
        frame.setFullScreen(1);
        frame.add(new GameLoop(width, height));
        frame.setVisible(true);
    }
}
