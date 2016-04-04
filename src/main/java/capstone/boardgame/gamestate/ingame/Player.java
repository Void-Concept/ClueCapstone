package capstone.boardgame.gamestate.ingame;

import capstone.boardgame.GUI.BGContainer;

/**
 * Created by Kyle on 3/23/2016.
 */
public class Player extends BGContainer {
    private int pid;

    public Player(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    @Override
    public void tick(double deltaTime) {

    }
}
