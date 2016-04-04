package capstone.boardgame.GUI.Drawable;

import java.awt.*;

/**
 * Created by Kyle on 3/28/2016.
 */
public class BGBorder extends BGDrawable {
    protected int width = 0, height = 0;

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() {
        return width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return height;
    }

    @Override
    protected void render(Graphics2D g) {
        g.drawRect(x, y, width, height);
    }
}
