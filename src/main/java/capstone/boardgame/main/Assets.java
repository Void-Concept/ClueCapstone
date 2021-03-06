package capstone.boardgame.main;

import capstone.boardgame.GUI.LoadImageFrom;
import capstone.boardgame.GUI.SpriteSheet;

import java.awt.image.BufferedImage;

/**
 * Created by Kyle on 3/6/2016.
 */
public class Assets {
    private static final String tag = "Assets";
    private SpriteSheet blocks = new SpriteSheet();

    private BufferedImage stone_1;

    public void init() {
        blocks.setSpriteSheet(LoadImageFrom.LoadImageFrom(this.getClass(), "clue/spritesheet.png"));

        stone_1 = blocks.getTile(0, 0, 8, 8);
    }
}
