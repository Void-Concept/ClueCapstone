package capstone.boardgame.GUI.Elements;

import java.awt.*;
import java.util.Random;

/**
 * Created by Kyle on 3/6/2016.
 */
public class Dice extends BGDrawable {
    private int maxRoll = 6;
    private int currRoll = 1;
    private Random rand = new Random();

    public Dice(int x, int y, int width, int height, int maxRoll) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.maxRoll = maxRoll;
    }

    public void roll() {
        currRoll = rand.nextInt(maxRoll) + 1;
    }

    public int getLast() {
        return currRoll;
    }

    @Override
    public void init() {

    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        g.drawRect(x, y, width, height);
        int fontSize = g.getFont().getSize();
        int yOff = (int)((fontSize*.75 + height)/2);
        g.drawString(""+currRoll, x + 3, y + yOff);
    }
}
