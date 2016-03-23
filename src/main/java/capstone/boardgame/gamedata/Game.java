package capstone.boardgame.gamedata;

import capstone.boardgame.main.Log;

import java.io.File;
import java.net.URL;
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
    ClassLoader resources;

    public static void loadAssets() {
        //if instance is initialized, clear it
        if (instance != null) {
            Log.d(tag, "Setting game instance to null");
            instance.assets.clear();
            instance = null;
        }
        //regenerate through constructor
        getInstance();
        Log.d(tag, "Reloaded assets");
    }
    //game rules
    //player rules

    //recursive function that loads all files from basePath+innerPath,
    // calling again if a directory is found (appending to innerPath)
    private void addAssetFromPath(String basePath, String innerPath) {
        try {
            File folder = new File(basePath+innerPath);
            File[] listOfFiles = folder.listFiles();

            for (File listOfFile : listOfFiles) {
                try {
                    //if found file is a directory
                    if (listOfFile.isDirectory()) {
                        //recursive call to add assets inside
                        addAssetFromPath(basePath, innerPath + listOfFile.getName() + "/");
                    } else {
                        //otherwise read and add the file
                        Path file = Paths.get(listOfFile.getAbsolutePath());
                        byte[] contents = Files.readAllBytes(file);
                        if (listOfFile.getName().equals("index.html")) {
                            assets.add(new GameAsset(innerPath, contents));
                        }
                        assets.add(new GameAsset(innerPath + listOfFile.getName(), contents));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private Game() {
        assets = new ArrayList<>();
        resources = getClass().getClassLoader();

        addAssetFromPath(resources.getResource("webassets").getPath(), "/");
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
