package capstone.boardgame.gamedata.GameObject;

import capstone.boardgame.GUI.Elements.BGComponent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Player extends BGComponent {
    private static final String tag = "Player";
    ArrayList<BGComponent> drawables = new ArrayList<>();
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

    public void addDrawable(BGComponent drawable) {
        drawables.add(drawable);
    }


    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void renderComponent(Graphics2D g) {
        for (BGComponent drawable : drawables) {
            drawable.render(g);
        }
    }
}
