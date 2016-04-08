package capstone.boardgame.GUI.Component;

import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Token extends BGComponent {
    private static final String tag = "Token";
    public Token(int x, int y, int width, int height) {
        super(x, y, width, height);
        setBackgroundColor(new Color(0, 0, 0, 0));
    }

    @Override
    public void tick(double deltaTime) {

    }

    //Temporary code
    @Override
    public void renderComponent(Graphics2D g) {
        g.fillOval(x, y, width, height);
    }

    @Override
    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        return obj;
    }
}