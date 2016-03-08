package capstone.boardgame.GUI.Elements;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import capstone.boardgame.main.Log;

/**
 * Created by Kyle on 3/6/2016.
 */
public class Board extends BGDrawable {
    private static final String tag = "Board";
    private int tilesX;
    private int tilesY;
    private Integer[][] board = null;
    //ArrayList<BufferedImage> stateTile = new ArrayList<>();
    private Map<Integer, BufferedImage> tileState = new HashMap<>();

    public Board(int x, int y, int width, int height, int tilesX, int tilesY) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.tilesX = tilesX; this.tilesY = tilesY;
        init();
    }

    public Board(int x, int y, int width, int height, String filename) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<ArrayList<Integer>> list = new ArrayList<>();
            String line;
            ArrayList<String> sLine;
            ArrayList<Integer> aList;

            while ((line = br.readLine()) != null) {
                sLine = new ArrayList<>(Arrays.asList(line.split(",")));
                aList = new ArrayList<>();
                for (String str : sLine) {
                    //Log.d(tag, "Input: " + str);
                    aList.add(Integer.parseInt(str));
                }
                list.add(aList);
            }
            tilesX = list.get(0).size();
            tilesY = list.size();

            init();

            for (int w = 0; w < tilesX; w++) {
                for (int h = 0; h < tilesY; h++) {
                    Log.d(tag, "Output: " + list.get(h).get(w));
                    board[w][h] = list.get(h).get(w);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tilesX = 1;
            tilesY = 1;
        }
    }

    @Override
    public void init() {
        board = new Integer[tilesX][tilesY];
        for (int w = 0; w < tilesX; w++) {
            for (int h = 0; h < tilesY; h++) {
                board[w][h] = 0;
            }
        }
    }

    public void setState(int x, int y, int state) {
        board[x][y] = state;
    }
    public int getState(int x, int y) {
        return board[x][y];
    }

    public void setStateTile(int state, BufferedImage img) {
        tileState.put(state, img);
        /*try {
            stateTile.set(state, img);
        } catch (Exception e) {
            try {
                stateTile.add(state, img);
            } catch (Exception ex) {

            }
        }*/
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
                    //BufferedImage img = stateTile.get(state);
                    BufferedImage img =  tileState.get(state);
                    g.drawImage(img, x + tileWidth * w, y + tileHeight * h, tileWidth, tileHeight, bg, null);
                } catch (Exception e) {
                }
            }
        }
    }
}
