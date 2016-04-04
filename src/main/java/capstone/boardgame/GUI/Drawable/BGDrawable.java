package capstone.boardgame.GUI.Drawable;

import java.awt.*;

/**
 * Created by Kyle on 3/28/2016.
 */
public abstract class BGDrawable {
    protected int x = 0, y = 0;

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }

    protected abstract void render(Graphics2D g);
}
