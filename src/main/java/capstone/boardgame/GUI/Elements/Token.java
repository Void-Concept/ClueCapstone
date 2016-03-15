package capstone.boardgame.GUI.Elements;

import java.awt.*;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Token extends BGDrawable {
    private static final String tag = "Token";
    public Token(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void tick(double deltaTime) {

    }

    //Temporary code
    @Override
    public void renderComponent(Graphics2D g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
}
