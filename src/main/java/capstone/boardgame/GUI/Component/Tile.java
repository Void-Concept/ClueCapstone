package capstone.boardgame.GUI.Component;

import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kyle on 3/7/2016.
 */
public class Tile extends BGComponent {
    private static final String tag = "Tile";
    private BufferedImage img;

    public Tile(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height);
        this.img = img;
    }

    @Override
    public void tick(double deltaTime) {
        //nothing to do
    }

    @Override
    public void renderComponent(Graphics2D g) {
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.drawRect(x, y, width, height);
        }
    }

    @Override
    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        return obj;
    }
}
