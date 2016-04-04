package capstone.boardgame.GUI.Component;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/7/2016.
 */
public class TileManager extends BGComponent {
    private static final String tag = "TileManager";
    ArrayList<Tile> tiles = new ArrayList<Tile>();

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    @Override
    public void tick(double deltaTime) {
        for (Tile tile : tiles) {
            tile.tick(deltaTime);
        }
    }

    @Override
    public void renderComponent(Graphics2D g) {
        for (Tile tile : tiles) {
            tile.render(g);
        }
    }
}
