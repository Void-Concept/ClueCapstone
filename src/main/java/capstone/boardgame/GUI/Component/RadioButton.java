package capstone.boardgame.GUI.Component;

import capstone.boardgame.GUI.Drawable.BGDrawable;
import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Kyle on 4/7/2016.
 */
public class RadioButton extends BGComponent {
    private boolean checked = false;
    private int lineWidth = 5;

    public RadioButton(int x, int y, int width, int height, int lineWidth) {
        super(x, y, width, height);
        this.lineWidth = lineWidth;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void tick(double deltaTime) {

    }

    @Override
    protected void renderComponent(Graphics2D g) {
        g.drawRect(x, y, width, height);
        if (checked) {
            Stroke stroke = g.getStroke();
            g.setStroke(new BasicStroke(lineWidth));
            g.drawLine(x + (int)(width*.1), this.y + height/2, x + width/2, (int)(this.y + height*.9));
            g.drawLine(x + width/2, (int)(this.y + height*.9), x + (int)(width*.9), y + (int)(height*.05));
            g.setStroke(stroke);
        }
    }

    @Override
    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        obj.put("type","radiobutton");

        obj.put("checked", checked);
        obj.put("lineWidth", lineWidth);

        return obj;
    }
}
