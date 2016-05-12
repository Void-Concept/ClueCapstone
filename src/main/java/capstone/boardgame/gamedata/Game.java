package capstone.boardgame.gamedata;

import capstone.boardgame.main.GetResourceListing;
import capstone.boardgame.main.Log;
import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

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
        if (basePath.substring(0, 5).equals("file:")) {
            basePath = basePath.substring(5);
        }
        try {
            File folder = new File(basePath+innerPath);
            File[] listOfFiles = folder.listFiles();
            Log.d(tag, folder.getAbsolutePath());
            Log.d(tag, basePath + " ||| " + innerPath);

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

    private void addAssetsFromResources(String path, String addPath) {
        resources = getClass().getClassLoader();

        try {
            URL resource = resources.getResource(path + addPath);

            BufferedReader in = new BufferedReader(new InputStreamReader(resources.getResourceAsStream(path + addPath)));
            Log.d(tag, in.ready() + "");
            Log.d(tag, resource.toURI().toString());
            URL test = new URL(resource.toURI().toString() + "icon.png");
            Log.d(tag, "Test url: " + test);
            File tfile = new File(test.getFile());
            if (tfile != null) {
                Log.d(tag, "File worked?");
            }

            String line;
            while ((line = in.readLine()) != null) {
                URL newPath = new URL(resource.toURI().toString() + "/" + line);
                if (newPath.getProtocol().equals("file")) {
                    Log.d(tag, "Processing File: " + line);
                    File file = new File(newPath.getFile());
                    if (file.isDirectory()) {
                        Log.d(tag, "File is a directory; recursive call goes here");
                        addAssetsFromResources(path, addPath + file.getName() + "/");
                    } else {
                        Log.d(tag, "File is a file; add to assets");
                        Path apath = Paths.get(file.getAbsolutePath());
                        byte[] content = Files.readAllBytes(apath);
                        if (file.getName().equals("index.html")) {
                            assets.add(new GameAsset(addPath, content));
                        }
                        assets.add(new GameAsset(addPath + file.getName(), content));
                    }
                } else {
                    Log.d(tag, "Something has gone horribly wrong opening file: " + newPath.toURI().toString());
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
    }

    private void addAssetsFromResource(String path) {
        try {
            String[] resourceListing = GetResourceListing.getResourceListing(Game.class, path);

            for (String str : resourceListing) {
                if (str.equals("")) {
                    continue;
                }
                Log.d(tag, "Processing: " + str);
                URL res = resources.getResource(path + str);
                if (res.getProtocol().equals("file")) {
                    File file = new File(res.toURI());
                    Log.d(tag, "Regular file " + file.canRead());
                    Path apath = Paths.get(file.getAbsolutePath());
                    byte[] content = Files.readAllBytes(apath);
                    if (file.getName().equals("index.html")) {
                        String[] split = str.split("/");
                        String spath = "/";
                        for (int i = 0; i < split.length - 1; i++) {
                            spath += split[i] + "/";
                        }
                        assets.add(new GameAsset(spath, content));
                    }
                    assets.add(new GameAsset("/" + str, content));

                } else if (res.getProtocol().equals("jar")) {
                    byte[] buff = IOUtils.readFully(res.openStream(), -1, false);
                    Log.d(tag, "Jar file: " + str);
                    if (res.getFile().contains("index.html")) {
                        String[] split = str.split("/");
                        String spath = "/";
                        for (int i = 0; i < split.length - 1; i++) {
                            spath += split[i] + "/";
                        }
                        Log.d(tag, "Index path: " + spath);
                        assets.add(new GameAsset(spath, buff));
                    }
                    assets.add(new GameAsset("/" + str, buff));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Game() {
        assets = new ArrayList<>();
        resources = getClass().getClassLoader();

        //addAssetsFromResources("webassets", "/");
        addAssetsFromResource("webassets/");

        //addAssetFromURL(resources.getResource("webassets"), "/");
        /*try {
            Log.d(tag, resources.getResource("webassets").toURI().toString());
            addAssetFromPath(resources.getResource("webassets").getPath(), "/");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
