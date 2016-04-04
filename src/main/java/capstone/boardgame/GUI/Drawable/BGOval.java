package capstone.boardgame.GUI.Drawable;

import java.awt.*;

/**
 * Created by Kyle on 3/28/2016.
 */
public class BGOval extends BGDrawable {
    public enum MODE {fill, stroke}
    protected int width = 0, height = 0;
    protected MODE drawMode = MODE.stroke;

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
    public void setDrawMode(MODE drawMode) {
        this.drawMode = drawMode;
    }
    public MODE getDrawMode() {
        return this.drawMode;
    }

    @Override
    protected void render(Graphics2D g) {
        switch (drawMode) {
            case fill:
                g.fillOval(x, y, width, height);
                break;
            case stroke:
                g.drawOval(x, y, width, height);
                break;
        }
    }
}
