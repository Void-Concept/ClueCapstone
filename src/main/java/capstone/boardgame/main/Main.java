package capstone.boardgame.main;

import capstone.boardgame.GUI.GameWindow;
import capstone.boardgame.HTTP.WebServer;
import capstone.boardgame.HTTP.WebSocketServer;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * Created by Kyle on 3/1/2016.
 */
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
        frame.addWindowListener(new WindowListener() {
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
        });
        //WebServer.start();
        WebSocketServer.runServer();
        //SpringApplication.run(Main.class, args);
    }
}
