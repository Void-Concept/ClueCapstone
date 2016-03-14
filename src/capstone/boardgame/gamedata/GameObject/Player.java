package capstone.boardgame.gamedata.GameObject;

import capstone.boardgame.GUI.Elements.BGDrawable;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Player extends BGDrawable {
    private static final String tag = "Player";
    ArrayList<BGDrawable> drawables = new ArrayList<>();
    int playerNum;

    /*
    * Play area
    * token
    * current tile
    *
    * */

    public Player(int playerNum) {
        super();
        this.playerNum = playerNum;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void addDrawable(BGDrawable drawable) {
        drawables.add(drawable);
    }


    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void renderComponent(Graphics2D g) {
        for (BGDrawable drawable : drawables) {
            drawable.render(g);
        }
    }
}
