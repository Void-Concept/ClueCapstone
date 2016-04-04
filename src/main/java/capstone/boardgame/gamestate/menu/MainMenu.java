package capstone.boardgame.gamestate.menu;

import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.GameGUIContainer;
import capstone.boardgame.HTTP.WebSocket.SocketEndpoint;
import capstone.boardgame.HTTP.WebSocket.SocketListener;
import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by Kyle on 3/28/2016.
 */
public class MainMenu extends GameState implements SocketListener {
    private static final String tag = "MainMenu";
    GameGUIContainer gui = new GameGUIContainer();

    public MainMenu(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        BGComponent.setDefaultColor(Color.cyan);

        //gui.addAll(LevelLoader.loadLevel(""));
        //TODO: Load main menu


        SocketEndpoint.setListener(this);
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        gui.render(g);
    }

    @Override
    public void onOpen(Session session) throws IOException {

    }

    @Override
    public void onClose(Session session, CloseReason reason) {

    }

    @Override
    public void onMessage(Session session, String message) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gui.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gui.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gui.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        gui.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        gui.mouseExited(e);
    }
}
