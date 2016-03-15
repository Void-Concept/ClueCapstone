package capstone.boardgame.gamedata;

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
    //game rules
    //player rules
    private void addAssetFromPath(String basePath, String innerPath) {
        try {
            File folder = new File(basePath+innerPath);
            File[] listOfFiles = folder.listFiles();

            System.out.println("List length: " + listOfFiles.length);
            for (File listOfFile : listOfFiles) {
                try {
                    //if found file is a directory
                    if (listOfFile.isDirectory()) {
                        System.out.println("Going inside directory: " + innerPath+listOfFile.getName()+"/");
                        //recursive call to add assets inside
                        addAssetFromPath(basePath, innerPath + listOfFile.getName() + "/");
                    } else {
                        System.out.println("Adding asset: " + innerPath+listOfFile.getName());
                        //otherwise read and add the file
                        Path file = Paths.get(listOfFile.getAbsolutePath());
                        assets.add(new GameAsset(innerPath + listOfFile.getName(), Files.readAllBytes(file)));
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
        if (false) {
            assets.add(new GameAsset("/", ("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h2>Room</h2>\n" +
                    "<img src=\"ZeldaBackground.jpg\" alt=\"Room\">\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>").getBytes()));

            assets.add(new GameAsset("/test", ("<!DOCTYPE html>\n" +
                    " \n" +
                    "<html>\n" +
                    "    <head>\n" +
                    "        <title>Echo Chamber</title>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width\">\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "       \n" +
                    "        <div>\n" +
                    "            <input type=\"text\" id=\"messageinput\"/>\n" +
                    "        </div>\n" +
                    "        <div>\n" +
                    "            <button type=\"button\" onclick=\"openSocket();\" >Open</button>\n" +
                    "            <button type=\"button\" onclick=\"send();\" >Send</button>\n" +
                    "            <button type=\"button\" onclick=\"closeSocket();\" >Close</button>\n" +
                    "        </div>\n" +
                    "        <!-- Server responses get written here -->\n" +
                    "        <div id=\"messages\"></div>\n" +
                    "       \n" +
                    "        <!-- Script to utilise the WebSocket -->\n" +
                    "        <script type=\"text/javascript\">\n" +
                    "                       \n" +
                    "            var webSocket;\n" +
                    "            var messages = document.getElementById(\"messages\");\n" +
                    "           \n" +
                    "           \n" +
                    "            function openSocket(){\n" +
                    "                // Ensures only one connection is open at a time\n" +
                    "                if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){\n" +
                    "                   writeResponse(\"WebSocket is already opened.\");\n" +
                    "                    return;\n" +
                    "                }\n" +
                    "                // Create a new instance of the websocket\n" +
                    "                webSocket = new WebSocket(\"ws://localhost:8080/echo\");\n" +
                    "                 \n" +
                    "                /**\n" +
                    "                 * Binds functions to the listeners for the websocket.\n" +
                    "                 */\n" +
                    "                webSocket.onopen = function(event){\n" +
                    "                    // For reasons I can't determine, onopen gets called twice\n" +
                    "                    // and the first time event.data is undefined.\n" +
                    "                    // Leave a comment if you know the answer.\n" +
                    "                    if(event.data === undefined)\n" +
                    "                        return;\n" +
                    " \n" +
                    "                    writeResponse(event.data);\n" +
                    "                };\n" +
                    " \n" +
                    "                webSocket.onmessage = function(event){\n" +
                    "                    writeResponse(event.data);\n" +
                    "                };\n" +
                    " \n" +
                    "                webSocket.onclose = function(event){\n" +
                    "                    writeResponse(\"Connection closed\");\n" +
                    "                };\n" +
                    "            }\n" +
                    "           \n" +
                    "            /**\n" +
                    "             * Sends the value of the text input to the server\n" +
                    "             */\n" +
                    "            function send(){\n" +
                    "                var text = document.getElementById(\"messageinput\").value;\n" +
                    "                webSocket.send(text);\n" +
                    "            }\n" +
                    "           \n" +
                    "            function closeSocket(){\n" +
                    "                webSocket.close();\n" +
                    "            }\n" +
                    " \n" +
                    "            function writeResponse(text){\n" +
                    "                messages.innerHTML += \"<br/>\" + text;\n" +
                    "            }\n" +
                    "           \n" +
                    "        </script>\n" +
                    "       \n" +
                    "    </body>\n" +
                    "</html>").getBytes()));

            try {
                //File file = new File("./ZeldaBackground.jpg");
                Path file = Paths.get("./ZeldaBackground.jpg");

                assets.add(new GameAsset("/ZeldaBackground.jpg", Files.readAllBytes(file)));
            } catch (Exception e) {
                System.out.printf("Failed to load file");
            }
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
