package capstone.boardgame.gamestate;

import javax.websocket.Session;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/28/2016.
 */
public class SessionManager {
    private ArrayList<Session> players = new ArrayList<>();

    public void addPlayer(Session session) {
        players.add(session);
    }
    public boolean removePlayer(Session session) {
        try {
            players.remove(session);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public int getCount() {
        return players.size();
    }
    public ArrayList<Session> getPlayers() {
        return players;
    }
}
