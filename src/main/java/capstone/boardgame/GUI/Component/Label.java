package capstone.boardgame.GUI.Component;

import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Label extends BGComponent {
    private static final String tag = "Label";
    protected String text;
    public Label(int x, int y, String text) {
        super(x, y, 0, 0);
        this.text = text;
    }

    @Override
    public void tick(double deltaTime) {

    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void renderComponent(Graphics2D g) {
        int fontSize = getFont().getSize();
        int yOff = (int)(fontSize*.75);

        g.drawString(text, x, y + yOff);
    }

    @Override
    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        obj.put("type","label");

        obj.put("text", text);
        return obj;
    }
}
