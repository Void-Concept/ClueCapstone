package capstone.boardgame.GUI.Component;

import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Label extends BGComponent {
    private static final String tag = "Label";
    protected String text;
    private int drawEffect = 0;

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
    public String getText() { return this.text; }

    public void applyEffect(int effect) {
        this.drawEffect = effect;
    }

    @Override
    protected void renderComponent(Graphics2D g) {
        int fontSize = getFont().getSize();
        int yOff = y + (int)(fontSize*.75);

        if (drawEffect > 0) {
            switch (drawEffect) {
                case 1:
                    //shadow
                    g.setColor(new Color(50, 50, 50));
                    g.drawString(text, x + 2, yOff + 2);
                    g.setColor(this.color);
                    break;
                case 2:
                    //outline
                    g.setColor(new Color(50, 50, 50));
                    g.drawString(text, x + 1, yOff + 1);
                    g.drawString(text, x + 1, yOff - 1);
                    g.drawString(text, x - 1, yOff + 1);
                    g.drawString(text, x - 1, yOff - 1);
                    g.setColor(this.color);
                    break;
            }
        }
        g.drawString(text, x, yOff);
    }

    @Override
    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        obj.put("type","label");

        obj.put("text", text);
        return obj;
    }
}
