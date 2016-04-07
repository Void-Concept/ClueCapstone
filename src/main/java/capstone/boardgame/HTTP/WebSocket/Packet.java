package capstone.boardgame.HTTP.WebSocket;

import capstone.boardgame.main.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kyle on 4/4/2016.
 */
public class Packet {
    private String command = "";
    private ArrayList<String[]> parameters = new ArrayList<>();

    public void addParameters(String param, String value) {
        parameters.add(new String[]{param, value});
    }

    public void setCommand(String command) {
        this.command = command;
    }
    public String getCommand() {
        return command;
    }

    public String getJson() {
        String json = "";
        json = json.concat("{\"Command\":\""+ this.command +"\",");
        json = json.concat("\"Parameters\":[");
        for (int i = 0; i < this.parameters.size(); i++) {
            json = json.concat("{" + this.parameters.get(i)[0] + ":\"" + this.parameters.get(i)[1] + "\"}");
            if (i != this.parameters.size() - 1) {
                json = json.concat(",");
            }
        }
        json = json.concat("]}");
        return json;
    }

    public static Packet parseJson(String json) {
        JSONObject obj = new JSONObject(json);
        Packet packet = new Packet();
        packet.setCommand((String)obj.get("Command"));

        JSONArray params = obj.getJSONArray("Parameters");
        for (int i = 0; i < params.length(); i++) {

        }

        return packet;
    }

    public static Packet createOpenPacket() {
        Packet packet = new Packet();

        packet.setCommand("onOpen");

        return packet;
    }
}
