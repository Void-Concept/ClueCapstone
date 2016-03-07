package capstone.boardgame.GOP;

import java.awt.image.BufferedImage;

/**
 * Created by Kyle on 3/1/2016.
 */
public class SpriteSheet {
    private BufferedImage spriteSheet;

    public SpriteSheet() {

    }

    public void setSpriteSheet(BufferedImage spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public BufferedImage getTile(int xTile, int yTile, int width, int height) {
        BufferedImage sprite = spriteSheet.getSubimage(xTile, yTile, width, height);
        return sprite;
    }
}
