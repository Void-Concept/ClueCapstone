package capstone.boardgame.main;

import capstone.boardgame.GUI.GameWindow;
import capstone.boardgame.HTTP.WebServer;
import capstone.boardgame.HTTP.WebSocketServer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by Kyle on 3/1/2016.
 */
public class Main {
    private static final String tag = "Main";
    public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static int width = 1280;//gd.getDisplayMode().getWidth();
    public static int height = 720;//gd.getDisplayMode().getHeight();
    public static GameWindow frame;

    public static void main(String[] args) {
        frame = new GameWindow("Board Games", width, height);
        frame.setFullScreen(1);
        frame.add(new GameLoop(width, height));
        frame.setVisible(true);
        frame.addWindowListener(windowListener);
        try {
            URL path = Main.class.getClassLoader().getResource("icon.png");
            Image img = ImageIO.read(path);
            frame.setIconImage(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebSocketServer.runServer();
        WebServer.start(80);
    }

    private static WindowListener windowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            WebSocketServer.stopServer();
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    };
}
