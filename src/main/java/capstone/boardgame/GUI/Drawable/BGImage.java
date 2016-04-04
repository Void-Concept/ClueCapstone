package capstone.boardgame.GUI.Drawable;

import java.awt.*;

/**
 * Created by Kyle on 3/28/2016.
 */
public class BGImage extends BGDrawable {
    protected int width = 0, height = 0;
    protected Image img = null;

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
    public void setImage(Image img) {
        this.img = img;
    }
    public Image getImage() {
        return img;
    }

    @Override
    protected void render(Graphics2D g) {
        g.drawImage(img, x, y, width, height, null);
    }
}
