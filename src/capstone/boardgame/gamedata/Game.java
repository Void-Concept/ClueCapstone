package capstone.boardgame.gamedata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Kyle on 2/22/2016.
 */
public class Game {
    private static final String tag = "Game";
    private static Game instance = null;
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    ArrayList<GameAsset> assets;
    //game rules
    //player rules

    private Game() {
        assets = new ArrayList<>();
        assets.add(new GameAsset("/", ("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h2>Room</h2>\n" +
                "<img src=\"ZeldaBackground.jpg\" alt=\"Room\">\n" +
                "\n" +
                "</body>\n" +
                "</html>").getBytes()));

        try {
            //File file = new File("./ZeldaBackground.jpg");
            Path file = Paths.get("./ZeldaBackground.jpg");

            assets.add(new GameAsset("/ZeldaBackground.jpg",Files.readAllBytes(file)));
        } catch (Exception e) {
            System.out.printf("Failed to load file");
        }
    }

    public GameAsset getAssetFromPath(String assetPath) {
        for (GameAsset asset : assets) {
            if (asset.getPath().equals(assetPath)) {
                return asset;
            }
        }
        return null;
    }
}
