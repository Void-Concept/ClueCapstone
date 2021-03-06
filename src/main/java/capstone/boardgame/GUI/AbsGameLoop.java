package capstone.boardgame.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Kyle on 3/1/2016.
 */
public class AbsGameLoop extends JPanel implements Runnable {
    private static final String tag = "AbsGameLoop";
    private Thread thread;
    private boolean running;

    private int fps;
    private int tps;

    protected int width;
    protected int height;

    public Graphics2D g2d;
    private BufferedImage img;

    public static double currFps = 60d;

    public AbsGameLoop(int width, int height) {
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setFocusable(false);
        requestFocus();
    }

    protected void setScaledSize(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setFocusable(false);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        init();

        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / currFps;

        int frames = 0;
        int ticks = 0;

        long lastTimer = System.currentTimeMillis();
        double deltaTime = 0;

        while (running) {
            long now = System.nanoTime();
            deltaTime += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (deltaTime >= 1) {
                ticks++;
                tick(deltaTime);
                deltaTime -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                frames++;
                try {
                    render();
                } catch (Exception e) {

                }
            }

            try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}

            if (System.currentTimeMillis() - lastTime >= 1000) {
                lastTimer += 1000;
                tps = ticks;
                fps = frames;
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void init() {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D)img.getGraphics();
        running = true;
    }

    public void tick(double deltaTime) {

    }

    public void render() {
        g2d.clearRect(0, 0, width, height);
    }

    public void clear() {
        Graphics g2 = getGraphics();
        if (img != null) {
            g2.drawImage(img, 0, 0, null);
        }
        g2.dispose();
    }

    public void scale(float x, float y) {
        img = new BufferedImage((int)(width * x), (int)(height * y), BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D)img.getGraphics();
        g2d.scale(x, y);
    }


}

















