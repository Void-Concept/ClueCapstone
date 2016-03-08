package capstone.boardgame.GUI.Elements;

import java.awt.*;

/**
 * Created by Kyle on 3/6/2016.
 */
public abstract class BGDrawable {
    private static final String tag = "BGDrawable";
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected Color color;
    protected static Color defaultColor = Color.white;

    protected Font font;
    protected static Font defaultfont = new Font("Arial", Font.PLAIN, 20);

    public static void setDefaultFont(Font font) {
        defaultfont = font;
    }
    public static void setDefaultColor(Color color) {
        defaultColor = color;
    }
    public static Font getDefaultfont() {
        return defaultfont;
    }
    public static Color getDefaultColor() {
        return defaultColor;
    }

    public BGDrawable() {
        font = defaultfont;
        color = defaultColor;
    }
    public BGDrawable(int x, int y, int width, int height) {
        this();
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setColor(Color color) { this.color = color; }
    public void setFont(Font font) { this.font = font; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Color getColor() { return color; }
    public Font getFont() { return font; }

    public abstract void tick(double deltaTime);
    public abstract void render(Graphics2D g);
}
