package capstone.boardgame.gamestate;

import javax.websocket.Session;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/28/2016.
 */
public class SessionManager {
    private ArrayList<Session> players = new ArrayList<>();
    private ArrayList<Session> other = new ArrayList<>();

    public void addSession(Session session) {
        other.add(session);
    }
    public boolean removeSession(Session session) {
        try {
            other.remove(session);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void promoteSession(Session session) {
        removeSession(session);
        players.add(session);
    }


}
