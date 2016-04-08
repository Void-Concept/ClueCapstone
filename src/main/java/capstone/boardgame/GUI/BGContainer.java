package capstone.boardgame.GUI;

import capstone.boardgame.GUI.Component.BGComponent;
import capstone.boardgame.main.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Kyle on 3/14/2016.
 */
public class BGContainer extends BGComponent {
    public BGContainer() { super(); }
    public BGContainer(int x, int y, int width, int height) { super(x, y, width, height); }

    protected ArrayList<BGComponent> drawables = new ArrayList<BGComponent>();

    public void add(BGComponent drawable) {
        drawables.add(drawable);
    }
    public BGComponent getViewByID(String id) {
        BGComponent found = null;
        for (BGComponent drawable : drawables) {
            //check this drawable
            if (drawable.getId().equals(id)) {
                found = drawable;
                break;
            }
            //check children drawables if container
            if (drawable instanceof BGContainer) {
                found = ((BGContainer)drawable).getViewByID(id);
                if (found != null) {
                    break;
                }
            }
        }

        return found;
    }
    @Override
    public void renderComponent(Graphics2D g) {
        for (BGComponent drawable : drawables) {
            drawable.render(g);
        }
    }

    @Override
    public void tick(double deltaTime) {
        for (BGComponent drawable : drawables) {
            drawable.tick(deltaTime);
        }
    }

    protected JSONObject convertJson() {
        JSONObject obj = new JSONObject();
        obj.put("type","container");

        JSONArray arr = new JSONArray();
        for (BGComponent drawable : drawables) {
            arr.put(drawable.toJson());
        }
        obj.put("components", arr);

        return obj;
    }
}
