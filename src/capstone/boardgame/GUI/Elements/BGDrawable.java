package capstone.boardgame.GUI.Elements;

import java.awt.*;

/**
 * Created by Kyle on 3/6/2016.
 */
public abstract class BGDrawable {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }


    abstract void init();
    abstract void tick(double deltaTime);
    abstract void render(Graphics2D g);
}
