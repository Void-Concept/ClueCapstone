package capstone.boardgame.GUI.Component;

import capstone.boardgame.main.Log;
import org.json.JSONObject;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Kyle on 3/6/2016.
 */
public abstract class BGComponent {
    protected String bgTag = "";
    protected String label = "";
    private static int currLabel = 1;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    protected Color color;
    protected static Color defaultColor = Color.white;

    protected Color backgroundColor = defaultBackgroundColor;
    protected static Color defaultBackgroundColor = new Color(0,0,0,0);

    protected Font font;
    protected static Font defaultfont = new Font("Arial", Font.PLAIN, 20);

    protected boolean visible = true;

    public static void setDefaultFont(Font font) {
        defaultfont = font;
    }
    public static void setDefaultColor(Color color) {
        defaultColor = color;
    }
    public static void setDefaultBackgroundColor(Color color) {
        defaultBackgroundColor = color;
    }
    public static Font getDefaultfont() {
        return defaultfont;
    }
    public static Color getDefaultColor() {
        return defaultColor;
    }

    public BGComponent() {
        font = defaultfont;
        color = defaultColor;
        label = "Drawable"+currLabel;
        currLabel++;
    }
    public BGComponent(int x, int y, int width, int height) {
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
    public void setBackgroundColor(Color color) { this.backgroundColor = color; }
    public void setFont(Font font) { this.font = font; }

    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }
    public boolean isVisible() {
        return visible;
    }

    public void setId(String label) {
        this.label = label;
    }
    public String getId(){
        return label;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Color getColor() { return color; }
    public Color getBackgroundColor() { return backgroundColor; }
    public Font getFont() { return font; }

    public abstract void tick(double deltaTime);
    public void render(Graphics2D g) {
        if (visible) {
            //render background
            g.setColor(backgroundColor);
            g.fillRect(x, y, width, height);
            g.setColor(new Color(0,0,0,0xFF));

            g.setColor(color);
            g.setFont(font);
            renderComponent(g);
        }
    }
    protected abstract void renderComponent(Graphics2D g);

    protected String convertColor(Color color) {
        /*if (color.getAlpha() == 0xFF) {
            return "#" + String.format("%06X", color.getRGB() & 0xFFFFFF);
        }*/
        return "rgba(" + color.getRed() + "," + color.getBlue() + "," + color.getGreen() + "," + color.getAlpha() + ")";
    }

    public JSONObject toJson() {
        JSONObject obj = convertJson();
        if (obj == null) {
            obj = new JSONObject();
        }
        obj.put("x", x);
        obj.put("y", y);
        obj.put("width", width);
        obj.put("height", height);
        obj.put("label", label);
        Font font = getFont();
        obj.put("fontFamily", font.getFamily());
        obj.put("fontSize", font.getSize());
        obj.put("backgroundColor", convertColor(backgroundColor));
        obj.put("color", convertColor(color));

        return obj;
    }
    protected abstract JSONObject convertJson();
}

