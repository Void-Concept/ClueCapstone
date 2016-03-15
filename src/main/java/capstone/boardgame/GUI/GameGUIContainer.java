package capstone.boardgame.GUI;

import capstone.boardgame.GUI.Elements.*;

import java.awt.*;


/**
 * Created by Kyle on 3/14/2016.
 */
public class GameGUIContainer extends BGContainer {
    @Override
    public void tick(double deltaTime) {

    }
    @Override
    public void renderComponent(Graphics2D g) {
        g.setFont(BGDrawable.getDefaultfont());
        g.setColor(BGDrawable.getDefaultColor());
        super.renderComponent(g);
    }
}
