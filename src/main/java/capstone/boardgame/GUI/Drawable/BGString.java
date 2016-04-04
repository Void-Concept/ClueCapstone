package capstone.boardgame.GUI.Drawable;

import java.awt.*;

/**
 * Created by Kyle on 3/28/2016.
 */
public class BGString extends BGDrawable {
    protected String text = "";
    public BGString() {

    }

    public BGString(int x, int y, String text) {
        setX(x);
        setY(y);
        setText(text);
    }

    public BGString(String text) {
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }

    @Override
    protected void render(Graphics2D g) {
        g.drawString(text, x, y);
    }
}
