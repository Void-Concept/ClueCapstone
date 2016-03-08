package capstone.boardgame.GUI.Elements;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kyle on 3/7/2016.
 */
public class Tile extends BGDrawable {

    private BufferedImage img;

    public Tile(int x, int y, int width, int height, BufferedImage img) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.img = img;
    }

    @Override
    public void tick(double deltaTime) {
        //nothing to do
    }

    @Override
    public void render(Graphics2D g) {
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }
}
