package capstone.boardgame.GUI.Component;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created by Kyle on 3/6/2016.
 */
public class Dice extends BGComponent {
    private static final String tag = "Dice";
    private int maxRoll = 6;
    private int currRoll = 1;
    private Random rand = new Random();

    public Dice(int x, int y, int width, int height, int maxRoll) {
        super(x, y, width, height);
        this.maxRoll = maxRoll;
    }

    public void roll() {
        currRoll = rand.nextInt(maxRoll) + 1;
    }

    public int getLast() {
        return currRoll;
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void renderComponent(Graphics2D g) {
        g.drawRect(x, y, width, height);
        int fontSize = g.getFont().getSize();
        int yOff = (int)((fontSize*.75 + height)/2);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r2d = fm.getStringBounds(""+currRoll, g);

        g.drawString(""+currRoll, x + (int)(width/2 - r2d.getWidth()/2), y + yOff);
    }
}
