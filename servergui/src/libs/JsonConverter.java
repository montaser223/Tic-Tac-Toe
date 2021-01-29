/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author black horse
 */
public class JsonConverter {

    public Player fromJsonToPlayer(JSONObject obj) {
        Player newPlayer = new Player();
        newPlayer.setUsername((String) obj.get("username"));
        newPlayer.setFirstname((String) obj.get("firstname"));
        newPlayer.setLastname((String) obj.get("lastname"));
        newPlayer.setRequest((String) obj.get("request"));
        newPlayer.setState((String) obj.get("status"));
        newPlayer.setRespond((String) obj.get("respond"));
        newPlayer.setPassword((String) obj.get("password"));
        return newPlayer;
    }
    
    public Player fromJsonToPlayer(JSONObject obj,ArrayList<Player> players) {
        Player newPlayer = new Player();
        newPlayer.setUsername((String) obj.get("username"));
        newPlayer.setFirstname((String) obj.get("firstname"));
        newPlayer.setLastname((String) obj.get("lastname"));
        newPlayer.setRequest((String) obj.get("request"));
        newPlayer.setState((String) obj.get("status"));
        newPlayer.setRespond((String) obj.get("respond"));
        newPlayer.setPassword((String) obj.get("password"));
        newPlayer.setPlayerList(players);
        return newPlayer;
    }

    public JSONObject fromPlayerToJson(Player newPlayer) {
        JSONObject obj = new JSONObject();
        obj.put("username", newPlayer.getUsername());
        obj.put("request", newPlayer.getRequest());
        obj.put("status", newPlayer.getState());
        obj.put("respond", newPlayer.getRespond());
        obj.put("password", newPlayer.getPassword());
        return obj;
    }
    
    public JSONObject fromPlayerToJson(Player newPlayer,JSONArray players) {
        JSONObject obj = new JSONObject();
        obj.put("username", newPlayer.getUsername());
        obj.put("request", newPlayer.getRequest());
        obj.put("status", newPlayer.getState());
        obj.put("respond", newPlayer.getRespond());
        obj.put("password", newPlayer.getPassword());
        obj.put("playersList", players);
        return obj;
    }

    public ArrayList<Player> fromJSONArrayToPlayerList(JSONArray playerList) {
        ArrayList<Player> players = new ArrayList<Player>();
        playerList.forEach((playerInfo) -> {
            players.add(fromJsonToPlayer((JSONObject) playerInfo));
        });
        return players;
    }

    public JSONArray fromPlayerListToJSONArray(ArrayList<Player> playerList) {
        //Construct json array
        JSONArray jsonPlayersList = new JSONArray();
        playerList.forEach((playerInfo) -> {
            jsonPlayersList.add(fromPlayerToJson(playerInfo));
        });

        return jsonPlayersList;
    }
}
