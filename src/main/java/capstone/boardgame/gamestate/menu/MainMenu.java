package capstone.boardgame.gamestate.menu;

import capstone.boardgame.GUI.Component.*;
import capstone.boardgame.GUI.Component.Button;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.GameGUIContainer;
import capstone.boardgame.HTTP.WebSocket.SocketEndpoint;
import capstone.boardgame.HTTP.WebSocket.SocketListener;
import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

        Button loadGame = new Button(500, 500, 200, 60, "Load Game");
        loadGame.setColor(Color.WHITE);
        loadGame.setMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gsm.getPlayerCount() > 1 && gsm.getPlayerCount() <= 6)
                    gsm.loadGame("Clue");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        gui.add(loadGame);

        Label gameLabel = new Label(540, 20, "Clue");
        gameLabel.setFont(gameLabel.getFont().deriveFont(50.0f));
        gameLabel.setColor(Color.CYAN);
        gui.add(gameLabel);

        Label gameRange = new Label(525, 200, "Players (2-6): ");
        gameRange.setId("GameRange");
        gameRange.setColor(Color.green);
        gui.add(gameRange);

        Label connections = new Label(655, 200, "0");
        connections.setId("connections");
        connections.setColor(Color.red);
        gui.add(connections);

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
    public boolean onOpen(Session session) throws IOException {
        gsm.addPlayer(session);
        Label connectionLabel = (Label)gui.getViewByID("connections");
        connectionLabel.setText(""+gsm.getPlayerCount());
        if (gsm.getPlayerCount() > 1 && gsm.getPlayerCount() <= 6) {
            connectionLabel.setColor(Color.green);
        } else {
            connectionLabel.setColor(Color.red);
        }

        return true;
    }

    @Override
    public void onClose(Session session, CloseReason reason) {
        gsm.removePlayer(session);
        Label connectionLabel = (Label)gui.getViewByID("connections");
        connectionLabel.setText(""+gsm.getPlayerCount());
        if (gsm.getPlayerCount() > 1 && gsm.getPlayerCount() <= 6) {
            connectionLabel.setColor(Color.green);
        } else {
            connectionLabel.setColor(Color.red);
        }
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
