package capstone.boardgame.gamestate;

import capstone.boardgame.main.Log;

import javax.websocket.Session;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/28/2016.
 */
public class SessionManager {
    private static final String tag = "SessionManager";
    private ArrayList<TimeoutSession> players = new ArrayList<>();
    public static long timeout = 2000;

    public class TimeoutSession {
        public Session session;
        public long timeout;
        public TimeoutSession(Session session, long timeout) {
            this.session = session;
            this.timeout = timeout;
        }
        @Override
        public boolean equals(Object other) {
            boolean equal = false;
            if (this == other) {
                equal = true;
            } else if (other instanceof Session) {
                equal = other.equals(this.session);
            } else if (other instanceof TimeoutSession) {
                equal = ((TimeoutSession) other).session.equals(this.session);
            }
            return equal;
        }
    }

    public void resetTimeout(Session session) {
        for (TimeoutSession ts : players) {
            if (ts.equals(session)) {
                ts.timeout = System.currentTimeMillis();
            }
        }
    }

    public void disconnectInactiveSessions() {
        ArrayList<TimeoutSession> copy = new ArrayList<>(players);
        for (TimeoutSession ts : copy) {
            if (!ts.session.isOpen()) {
                removePlayer(ts.session);
            }
            if (System.currentTimeMillis() - ts.timeout > timeout) {
                try {
                    ts.session.close();
                    removePlayer(ts.session);
                } catch (Exception e) {}
            }
        }
    }

    public void addPlayer(Session session) {
        players.add(new TimeoutSession(session, System.currentTimeMillis()));
    }
    public boolean removePlayer(Session session) {
        ArrayList<TimeoutSession> sessions = new ArrayList<>(players);
        for (TimeoutSession ts : sessions) {
            if (ts.equals(session)) {
                Log.d(tag, "Removing player");
                return players.remove(ts);
            }
        }
        return false;
    }
    public int getCount() {
        return players.size();
    }
    public ArrayList<Session> getPlayers() {
        ArrayList<Session> sessions = new ArrayList<>();
        for (TimeoutSession session : players) {
            sessions.add(session.session);
        }
        return sessions;
    }
}
