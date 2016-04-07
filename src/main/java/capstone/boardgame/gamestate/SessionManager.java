package capstone.boardgame.gamestate;

import javax.websocket.Session;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/28/2016.
 */
public class SessionManager {
    private static ArrayList<Session> players = new ArrayList<>();
    private static ArrayList<Session> sessions = new ArrayList<>();

    public static void addSession(Session session) {
        sessions.add(session);
    }
    public static boolean removeSession(Session session) {
        try {
            sessions.remove(session);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void addPlayer(Session session) {
        players.add(session);
    }
    public static boolean removePlayer(Session session) {
        try {
            players.remove(session);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static void promoteSession(Session session) {
        removeSession(session);
        addPlayer(session);
    }

    public static void demoteSession(Session session) {
        removePlayer(session);
        addSession(session);
    }
}
