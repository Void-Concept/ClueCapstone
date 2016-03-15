package capstone.boardgame.gamedata;

/**
 * Created by Kyle on 2/22/2016.
 */
public class GameAsset {
    private static final String tag = "GameAsset";
    private String assetPath = null;
    private byte[] assetBody = null;

    public GameAsset(String assetPath, byte[] assetBody) {
        this.assetPath = assetPath;
        this.assetBody = assetBody;
    }

    public GameAsset(String assetPath, GameAsset other) {
        this.assetPath = assetPath;
        this.assetBody = other.assetBody;
    }

    public byte[] getBody() {
        return assetBody;
    }

    public String getPath() {
        return assetPath;
    }
}
