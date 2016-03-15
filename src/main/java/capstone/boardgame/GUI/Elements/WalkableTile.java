package capstone.boardgame.GUI.Elements;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Kyle on 3/7/2016.
 */
public class WalkableTile extends Tile {
    private static final String tag = "WalkableTile";
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private HashMap<Integer, WalkableTile> walkDir = new HashMap<Integer, WalkableTile>();

    public WalkableTile(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
    }

    public void addDirection(int dir, WalkableTile tile) {
        walkDir.put(dir, tile);
    }

    public WalkableTile getDirection(int dir) {
        return walkDir.get(dir);
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
    }

    @Override
    public void renderComponent(Graphics2D g) {
        super.renderComponent(g);
    }
}
