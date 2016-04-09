package capstone.boardgame.GUI.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import capstone.boardgame.GUI.BGContainer;

/**
 * Created by Kyle on 3/6/2016.
 */
public class Board extends BGContainer {
    private static final String tag = "Board";
    private int tilesX;
    private int tilesY;
    private Integer[][] board = null;
    //ArrayList<BufferedImage> stateTile = new ArrayList<>();
    private Map<Integer, BufferedImage> tileState = new HashMap<Integer, BufferedImage>();

    public Board(int x, int y, int width, int height, int tilesX, int tilesY) {
        super(x, y, width, height);
        this.tilesX = tilesX; this.tilesY = tilesY;
        init();
    }

    public Board(int x, int y, int width, int height, String filename) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        try {
            URL boardLoader = getClass().getClassLoader().getResource(filename);
            FileReader fr = new FileReader(boardLoader.getFile());
            BufferedReader br = new BufferedReader(fr);
            ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
            String line;
            ArrayList<String> sLine;
            ArrayList<Integer> aList;

            while ((line = br.readLine()) != null) {
                sLine = new ArrayList<>(Arrays.asList(line.split(",")));
                aList = new ArrayList<>();
                for (String str : sLine) {
                    aList.add(Integer.parseInt(str));
                }
                list.add(aList);
            }
            tilesX = list.get(0).size();
            tilesY = list.size();

            init();

            for (int w = 0; w < tilesX; w++) {
                for (int h = 0; h < tilesY; h++) {
                    board[w][h] = list.get(h).get(w);
                }
            }

            /*
            PrintWriter writer = new PrintWriter("./test.txt", "UTF-8");
            String bline;
            for (int h = 0; h < tilesY; h++) {
                bline = "";
                for (int w = 0; w < tilesX; w++) {
                    if (board[w][h] == 1) {
                        board[w][h] = 0;
                        board[w][h] |= w < tilesX-1 && board[w+1][h] > 0 ? 1 : 0;
                        board[w][h] |= w > 0 && board[w-1][h] > 0 ? 1<<1 : 0;
                        board[w][h] |= h < tilesY-1 && board[w][h+1] > 0 ? 1<<2 : 0;
                        board[w][h] |= h > 0 && board[w][h-1] > 0 ? 1<<3 : 0;
                    }
                    bline += String.format("%02d", board[w][h]) + ",";
                }
                writer.println(bline);
            }
            writer.close();*/
        } catch (Exception e) {
            e.printStackTrace();
            tilesX = 1;
            tilesY = 1;
        }
    }

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
    public void renderComponent(Graphics2D g) {
        int tileWidth = width / tilesX;
        int tileHeight = height / tilesY;

        int state;
        Color bg = new Color(0f, 0f, 0f, 0f);
        g.setColor(color);

        for (int w = 0; w < tilesX; w++) {
            for (int h = 0; h < tilesY; h++) {
                state = getState(w, h);
                try {
                    BufferedImage img =  tileState.get(state);

                    if (false) {
                        BufferedImage redversion = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = (Graphics2D) redversion.getGraphics();
                        g2d.setColor(Color.red);
                        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());

                        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                        g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);

                        g.drawImage(redversion, x + tileWidth * w, y + tileHeight * h, tileWidth, tileHeight, bg, null);
                    } else {
                        g.drawImage(img, x + tileWidth * w, y + tileHeight * h, tileWidth, tileHeight, bg, null);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
