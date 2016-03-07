package capstone.boardgame.GOP;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameWindow extends JFrame {
    boolean fse = false;
    int fsm = 0;
    GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    public GameWindow(String title, int width, int height) {
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void setFullScreen() {
        switch (fsm) {
            case 0:
                setUndecorated(false);
                break;
            case 1:
                setUndecorated(true);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                break;
            case 2:
                setUndecorated(true);

                break;
        }
    }

    public void setFullScreen(int fsnm) {
        fse = true;
        if (fsm <= 2) {
            this.fsm = fsnm;
            setFullScreen();
        } else {
            System.err.print("Error: " + fsnm + " Not supported\n");
        }
    }
}

