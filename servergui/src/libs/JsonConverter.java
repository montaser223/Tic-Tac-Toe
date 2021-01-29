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

    private static JSONObject obj;
//    private static Game game;

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

    public Player fromJsonToPlayer(JSONObject obj, ArrayList<Player> players) {
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
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", newPlayer.getUsername());
        jsonObj.put("request", newPlayer.getRequest());
        jsonObj.put("status", newPlayer.getState());
        jsonObj.put("respond", newPlayer.getRespond());
        jsonObj.put("password", newPlayer.getPassword());
        return jsonObj;
    }

    public JSONObject fromPlayerToJson(Player newPlayer, JSONArray players) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", newPlayer.getUsername());
        jsonObj.put("request", newPlayer.getRequest());
        jsonObj.put("status", newPlayer.getState());
        jsonObj.put("respond", newPlayer.getRespond());
        jsonObj.put("password", newPlayer.getPassword());
        jsonObj.put("playersList", players);
        return jsonObj;
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

//    public static Game fromJsonToGame(JSONObject obj) {
//        game = new Game();
//        game.setRecordedGamePosition((String[]) obj.get("recordedgamePosition"));
//        game.setWinner((String) obj.get("sinner"));
//        game.setRequest((String) obj.get("request"));
//        game.setPlayedMove((String) obj.get("playedMove"));
//        game.setPosition((Long) obj.get("position"));
//        game.setNextMove((String) obj.get("nextMove"));
//        game.setMessage((String) obj.get("message"));
//
//        return game;
//    }
//
//    public static JSONObject fromGameToJson(Game game) {
//
//        obj = new JSONObject();
////        obj.put("recordedgamePosition", game.getRecordedGamePosition());
//        obj.put("winner", game.getWinner());
//        obj.put("request", game.getRequest());
//        obj.put("playedMove", game.getPlayedMove());
//        obj.put("position", game.getPosition());
//        obj.put("nextMove", game.getNextMove());
//        obj.put("message", game.getMessage());
//
//        return obj;
//    }
}
