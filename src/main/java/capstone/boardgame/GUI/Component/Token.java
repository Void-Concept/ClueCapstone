package capstone.boardgame.GUI.Component;

import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Kyle on 3/8/2016.
 */
public class Token extends BGComponent {
    private static final String tag = "Token";
    private int drawEffect = 0;

    public Token(int x, int y, int width, int height) {
        super(x, y, width, height);
        setBackgroundColor(new Color(0, 0, 0, 0));
    }

    @Override
    public void tick(double deltaTime) {

    }

    public void applyEffect(int effect) {
        this.drawEffect = effect;
    }

    //Temporary code
    @Override
    public void renderComponent(Graphics2D g) {
        if (drawEffect > 0) {
            switch (drawEffect) {
                case 1:
                    //shadow
                    g.setColor(new Color(50, 50, 50));
                    g.fillOval(x + 2, y + 2, width, height);
                    g.setColor(this.color);
                    break;
                case 2:
                    //outline
                    g.setColor(new Color(50, 50, 50));
                    g.fillOval(x-2, y-2, width + 4, height + 4);
                    //g.fillOval(x-1, y-1, width + 2, height + 2);
                    g.setColor(this.color);
                    break;
            }
        }
        g.fillOval(x, y, width, height);
    }

    @Override
    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        return obj;
    }
}
