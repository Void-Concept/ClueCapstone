package capstone.boardgame.GUI;

/**
 * Created by Kyle on 3/1/2016.
 */
public class Vector2F {
    private static final String tag = "Vector2F";
    public float xpos;
    public float ypos;

    public static float worldXPos;
    public static float worldYPos;

    public Vector2F() {
        this(0.0f,0.0f);
    }
    public Vector2F(float xpos, float ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public static Vector2F zero() {
        return new Vector2F();
    }

    public void normalize() {
        double length = Math.sqrt(xpos * xpos + ypos * ypos);

        if (length != 0.0) {
            float s = 1.0f / (float)length;
            xpos = xpos * s;
            ypos = ypos * s;
        }
    }

    public boolean equals(Vector2F v2f) {
        return (this.xpos == v2f.xpos) && (this.ypos == v2f.ypos);
    }

    public Vector2F copy(Vector2F v2f) {
        xpos = v2f.xpos;
        ypos = v2f.ypos;
        return new Vector2F(v2f.xpos, v2f.ypos);
    }

    public Vector2F add(Vector2F v2f) {
        xpos = xpos + v2f.xpos;
        ypos = ypos + v2f.ypos;
        return new Vector2F(xpos, ypos);
    }

    public static void setWorldVars(float wx, float wy) {
        worldXPos = wx;
        worldYPos = wy;
    }

    public Vector2F getScreenLocation() {
        return new Vector2F(xpos, ypos);
    }

    public Vector2F getWorldLocation() {
        return new Vector2F(xpos - worldXPos, ypos - worldYPos);
    }

    public static double getDistanceOnScreen(Vector2F vec1, Vector2F vec2) {
        float v1 = vec1.xpos - vec2.xpos;
        float v2 = vec1.ypos - vec2.ypos;
        return Math.sqrt(v1*v1 + v2*v2);
    }

    public double getDistanceBetweenWorldVectors(Vector2F vec) {
        float dx = Math.abs(getWorldLocation().xpos - vec.getWorldLocation().xpos);
        float dy = Math.abs(getWorldLocation().ypos - vec.getWorldLocation().ypos);

        return Math.abs(dx*dx - dy*dy);
    }
}
