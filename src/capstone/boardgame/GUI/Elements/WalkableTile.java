package capstone.boardgame.GUI.Elements;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Kyle on 3/7/2016.
 */
public class WalkableTile extends BGDrawable {
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private HashMap<Integer, WalkableTile> walkDir = new HashMap<>();
    private BufferedImage img;

    public WalkableTile(int x, int y, int width, int height, BufferedImage img) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.img = img;
    }

    public void addDirection(int dir, WalkableTile tile) {
        walkDir.put(dir, tile);
    }

    public WalkableTile getDirection(int dir) {
        return walkDir.get(dir);
    }

    @Override
    public void init() {
        //nothing to do
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
