package capstone.boardgame.gamestate;

import capstone.boardgame.HTTP.WebSocket.Packet;
import capstone.boardgame.HTTP.WebSocket.SocketEndpoint;
import capstone.boardgame.gamestate.ingame.GameController;
import capstone.boardgame.gamestate.menu.MainMenu;
import capstone.boardgame.main.Log;

import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameStateManager implements MouseListener {
    private static final String tag = "GameStateManager";
    public static Stack<GameState> states;
    protected SessionManager sessions;

    public GameStateManager() {
        states = new Stack<>();
        states.push(new MainMenu(this));
        sessions = new SessionManager();
        SocketEndpoint.manager = sessions;
    }

    public void loadGame(String gameid) {
        states.push(new GameController(this));
        states.peek().init();
    }

    public void addPlayer(Session session) {
        sessions.addPlayer(session);
    }
    public void removePlayer(Session session) {
        sessions.removePlayer(session);
    }
    public int getPlayerCount() {
        return sessions.getCount();
    }
    public ArrayList<Session> getPlayers() {
        return sessions.getPlayers();
    }

    private int ticks = 0;
    public void tick(double deltaTime) {
        ticks++;
        if (ticks >= 60) {
            ticks = 0;
            Packet packet = new Packet();
            packet.setCommand("ping");
            for (Session session : sessions.getPlayers()) {
                try {
                    session.getBasicRemote().sendText(packet.getJson());
                } catch (Exception e) {
                    removePlayer(session);
                    e.printStackTrace();
                }
            }
            sessions.disconnectInactiveSessions();
        }
        states.peek().tick(deltaTime);
    }

    public void render(Graphics2D g) {
        states.peek().render(g);
    }

    public void init() {
        states.peek().init();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        states.peek().mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        states.peek().mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        states.peek().mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        states.peek().mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        states.peek().mouseExited(e);
    }
}
