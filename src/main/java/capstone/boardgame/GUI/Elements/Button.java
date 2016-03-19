package capstone.boardgame.GUI.Elements;

import capstone.boardgame.main.Log;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

/**
 * Created by Kyle on 3/18/2016.
 */
public class Button extends BGDrawable implements MouseListener {
    private static final String tag = "Button";
    protected String text = "";

    private MouseListener listener = null;

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    protected void renderComponent(Graphics2D g) {
        g.drawRect(x, y, width, height);
        int fontSize = g.getFont().getSize();
        int yOff = (int)((fontSize*.75 + height)/2);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r2d = fm.getStringBounds(text, g);

        g.drawString(text, x + (int)(width/2 - r2d.getWidth()/2), y + yOff);
    }

    public void setMouseListener(MouseListener listener) {
        this.listener = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null) {
            listener.mouseClicked(e);
        }
        Log.d(tag, "Clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (listener != null) {
            listener.mousePressed(e);
        }
        Log.d(tag, "Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (listener != null) {
            listener.mouseReleased(e);
        }
        Log.d(tag, "Released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (listener != null) {
            listener.mouseEntered(e);
        }
        Log.d(tag, "Entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (listener != null) {
            listener.mouseExited(e);
        }
        Log.d(tag, "Exited");
    }
}
