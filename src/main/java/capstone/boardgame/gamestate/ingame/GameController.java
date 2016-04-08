package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.GameGUIContainer;
import capstone.boardgame.HTTP.WebSocket.Packet;
import capstone.boardgame.HTTP.WebSocket.SocketEndpoint;
import capstone.boardgame.HTTP.WebSocket.SocketListener;
import capstone.boardgame.gamestate.GameState;
import capstone.boardgame.gamestate.GameStateManager;
import capstone.boardgame.main.Log;
import org.json.JSONObject;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/1/2016.
 */
public class GameController extends GameState implements SocketListener {
    private static final String tag = "GameController";
    GameGUIContainer gui = new GameGUIContainer();
    GamePacketHandler handler = new GamePacketHandler();

    private ArrayList<Player> players = new ArrayList<>();

    public GameController(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        BGComponent.setDefaultColor(Color.cyan);

        //add gui elements
        gui.addAll(LevelLoader.loadLevel(""));
        ((Label)gui.getViewByID("NumPlayers")).setText("" + gsm.getPlayerCount());

        //set up player objects
        ArrayList<Session> sessions = gsm.getPlayers();
        int id = 0;
        for (Session session : sessions) {
            players.add(new Player(session.getId(), session));
            id++;
        }

        //set up packet handler
        SocketEndpoint.setPacketHandler(handler);

        for (Player player : players) {
            try {
                player.sendView();
            } catch (Exception e) {

            }
        }

        SocketEndpoint.setListener(this);
    }

    @Override
    public boolean onOpen(Session session) throws IOException {
        if (players.size() > gsm.getPlayerCount()) {
            Log.d(tag, "onOpen: Adding new session to replace old");
            gsm.addPlayer(session);
            for (Player player : players) {
                if (!player.enabled) {
                    Log.d(tag, "Replacing player " + player.getPid() + " with " + session.getId());
                    player.setPid(session.getId());
                    player.setSession(session);
                    player.enabled = true;
                    player.sendView();
                    break;
                }
            }
            ((Label)gui.getViewByID("NumPlayers")).setText("" + gsm.getPlayerCount());

            return true;
        } else {
            Log.d(tag, "onOpen: Rejecting new session");
            return false;
        }
    }

    @Override
    public void onClose(Session session, CloseReason reason) {
        gsm.removePlayer(session);
        Log.d(tag, "Closing session " + session.getId());

        for (Player player : players) {
            if (player.getPid().equals(session.getId())) {
                Log.d(tag, "Disabling player " + player.getPid());
                player.enabled = false;
                break;
            }
        }

        ((Label)gui.getViewByID("NumPlayers")).setText("" + gsm.getPlayerCount());
    }

    @Override
    public void onMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        gui.render(g);
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
