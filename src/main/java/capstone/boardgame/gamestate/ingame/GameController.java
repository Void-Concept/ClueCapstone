package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.GUI.Component.Label;
import capstone.boardgame.GUI.Component.Token;
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
    GamePacketHandler handler = new GamePacketHandler(this);

    private ArrayList<Player> players = new ArrayList<>();

    public GameController(GameStateManager gsm) {
        super(gsm);
    }

    public Player getPlayerSession(String id) {
        for (Player player : players) {
            if (player.getPid().equals(id)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void init() {
        BGComponent.setDefaultColor(Color.black);
        gui.setBackgroundColor(Color.gray);

        //add gui elements
        gui.addAll(LevelLoader.loadLevel(""));
        ((Label)gui.getViewByID("NumPlayers")).setText("" + gsm.getPlayerCount());

        //set up player objects
        ArrayList<Session> sessions = gsm.getPlayers();
        int number = 0;
        for (Session session : sessions) {
            Player player = new Player(session.getId(), session);
            players.add(player);

            Token token = null;
            switch (number) {
                case 0:
                    token = new Token(10, 10, 10, 10);
                    token.setColor(Color.magenta);
                    break;
                case 1:
                    token = new Token(20, 10, 10, 10);
                    token.setColor(Color.red);
                    break;
                case 2:
                    token = new Token(30, 10, 10, 10);
                    token.setColor(Color.yellow);
                    break;
                case 3:
                    token = new Token(40, 10, 10, 10);
                    token.setColor(Color.white);
                    break;
                case 4:
                    token = new Token(50, 10, 10, 10);
                    token.setColor(Color.green);
                    break;
                case 5:
                    token = new Token(60, 10, 10, 10);
                    token.setColor(Color.blue);
                    break;

            }
            if (token != null) {
                token.setId("player");
                player.add(token);
            }

            number++;
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
        for (Player player : players) {
            player.render(g);
        }
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
