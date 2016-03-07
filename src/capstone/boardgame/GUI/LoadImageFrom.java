package capstone.boardgame.GUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Kyle on 3/1/2016.
 */
public class LoadImageFrom {
    public static BufferedImage LoadImageFrom(Class<?> classFile, String path) {
        URL url = classFile.getResource(path);
        BufferedImage img = null;

        try {
            img = ImageIO.read(url);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return img;
    }
}
