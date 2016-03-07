package capstone.boardgame.GUI.Elements;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/6/2016.
 */
public class Board extends BGDrawable {
    private int tilesX;
    private int tilesY;
    ArrayList<ArrayList<Integer>> tiles = new ArrayList<>();
    ArrayList<BufferedImage> stateTile = new ArrayList<>();

    public Board(int x, int y, int width, int height, int tilesX, int tilesY) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.tilesX = tilesX; this.tilesY = tilesY;
    }

    @Override
    public void init() {
        tiles.clear();
        for (int w = 0; w < tilesX; w++) {
            ArrayList<Integer> tile = new ArrayList<>();
            for (int h = 0; h < tilesY; h++) {
                tile.add(0);
            }
            tiles.add(tile);
        }
    }

    public void setState(int x, int y, int state) {
        ArrayList<Integer> tile = tiles.get(x);
        tile.add(y, state);
    }
    public int getState(int x, int y) {
        int state = 0;
        try {
            state = tiles.get(x).get(y);
        } catch (Exception e) {

        }
        return state;
    }

    public void setStateTile(int state, BufferedImage img) {
        try {
            stateTile.set(state, img);
        } catch (Exception e) {
            try {
                stateTile.add(state, img);
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    public void render(Graphics2D g) {
        int tileWidth = width / tilesX;
        int tileHeight = height / tilesY;

        int state;
        Color bg = new Color(0f, 0f, 0f, 0f);

        for (int w = 0; w < tilesX; w++) {
            for (int h = 0; h < tilesY; h++) {
                state = getState(w, h);
                try {
                    BufferedImage img = stateTile.get(state);
                    g.drawImage(img, x + tileWidth * w, y + tileHeight * h, bg, null);
                } catch (Exception e) {
                }
            }
        }
    }
}
