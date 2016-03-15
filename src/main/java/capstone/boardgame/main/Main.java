package capstone.boardgame.main;

import capstone.boardgame.GUI.GameWindow;
import capstone.boardgame.HTTP.WebServer;

import java.awt.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Kyle on 3/1/2016.
 */
@SpringBootApplication
public class Main {
    private static final String tag = "Main";
    public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static int width = 1280;//gd.getDisplayMode().getWidth();
    public static int height = 720;//gd.getDisplayMode().getHeight();

    public static void main(String[] args) {
        GameWindow frame = new GameWindow("Board Games", width, height);
        frame.setFullScreen(1);
        frame.add(new GameLoop(width, height));
        frame.setVisible(true);
        //WebServer.start();
        SpringApplication.run(Main.class, args);
    }
}
