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
    private static Game game;

    public Player fromJsonToPlayer(JSONObject obj) {
        Player newPlayer = new Player();
        newPlayer.setUsername((String) obj.get("username"));
        newPlayer.setFirstname((String) obj.get("firstname"));
        newPlayer.setLastname((String) obj.get("lastname"));
        newPlayer.setRequest((String) obj.get("request"));
        newPlayer.setStatus((String) obj.get("status"));
        newPlayer.setRespond((String) obj.get("respond"));
        newPlayer.setPassword((String) obj.get("password"));
        newPlayer.setDestination((String) obj.get("destination"));
        newPlayer.setScour((int) (long) obj.get("scour"));
        newPlayer.setResumeOldGame((boolean) obj.get("resumeOldGame"));
        return newPlayer;
    }

    public Player fromJsonToPlayer(JSONObject obj, ArrayList<Player> players) {
        Player newPlayer = new Player();
        newPlayer.setUsername((String) obj.get("username"));
        newPlayer.setFirstname((String) obj.get("firstname"));
        newPlayer.setLastname((String) obj.get("lastname"));
        newPlayer.setRequest((String) obj.get("request"));
        newPlayer.setStatus((String) obj.get("status"));
        newPlayer.setRespond((String) obj.get("respond"));
        newPlayer.setPassword((String) obj.get("password"));
        newPlayer.setScour((int) (long) obj.get("scour"));
        newPlayer.setPlayerList(players);
        newPlayer.setResumeOldGame((boolean) obj.get("resumeOldGame"));
        return newPlayer;
    }

    public JSONObject fromPlayerToJson(Player newPlayer) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", newPlayer.getUsername());
        jsonObj.put("firstname", newPlayer.getFirstname());
        jsonObj.put("lastname", newPlayer.getLastname());
        jsonObj.put("request", newPlayer.getRequest());
        jsonObj.put("status", newPlayer.getStatus());
        jsonObj.put("respond", newPlayer.getRespond());
        jsonObj.put("password", newPlayer.getPassword());
        jsonObj.put("destination", newPlayer.getDestination());
        jsonObj.put("scour", newPlayer.getScour());
        jsonObj.put("resumeOldGame", newPlayer.isResumeOldGame());
        return jsonObj;
    }

    public JSONObject fromPlayerToJson(Player newPlayer, JSONArray players) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", newPlayer.getUsername());
        jsonObj.put("request", newPlayer.getRequest());
        jsonObj.put("status", newPlayer.getStatus());
        jsonObj.put("respond", newPlayer.getRespond());
        jsonObj.put("password", newPlayer.getPassword());
        jsonObj.put("playersList", players);
        jsonObj.put("scour", newPlayer.getScour());
        jsonObj.put("resumeOldGame", newPlayer.isResumeOldGame());
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
    } // done

    /**
     * ****************************game**************************************
     */
    public Game fromJsonToGame(JSONObject obj) {
        game = new Game();
        game.setWinner((String) obj.get("winner"));
        game.setRequest((String) obj.get("request"));
        game.setPlayedMove((String) obj.get("playedMove"));
        game.setPosition((Long) obj.get("position"));
        game.setNextMove((String) obj.get("nextMove"));
        game.setMessage((String) obj.get("message"));
        game.setRespond((String) obj.get("respond"));
        game.setPlayerX((String) obj.get("playerx"));
        game.setPlayerO((String) obj.get("playero"));
        
        return game;
    }

    public JSONObject fromGameToJson(Game game) {

        obj = new JSONObject();
        obj.put("winner", game.getWinner());
        obj.put("request", game.getRequest());
        obj.put("playedMove", game.getPlayedMove());
        obj.put("position", game.getPosition());
        obj.put("nextMove", game.getNextMove());
        obj.put("message", game.getMessage());
        obj.put("respond", game.getRespond());
        obj.put("playerx", game.getPlayerX());
        obj.put("playero", game.getPlayerO());

        return obj;
    }

    public JSONObject fromGameToJsonWithArray(Game game, JSONArray recordedArray) {

        obj = new JSONObject();
        obj.put("recordedPosition", recordedArray);
        obj.put("playerX", game.getPlayerX());
        obj.put("playerO", game.getPlayerO());
        obj.put("request", game.getRequest());

        obj.put("playedMove", game.getPlayedMove());
        obj.put("position", game.getPosition());
        obj.put("nextMove", game.getNextMove());
        obj.put("winner", game.getWinner());

        return obj;
    }

    public JSONArray fromRecordedGamePositionTOJsonArray(String[] positionToRecord) {
        JSONArray recordedList = new JSONArray();
        for (String position : positionToRecord) {
            recordedList.add(position);
        }
        return recordedList;
    }

    public Game fromJsonWithArrayToGame(JSONObject obj, String[] recordedGamePosition) {
        game = new Game();

        game.setRecordedGamePosition(recordedGamePosition);
        game.setWinner((String) obj.get("winner"));
        game.setRequest((String) obj.get("request"));
        game.setPlayedMove((String) obj.get("playedMove"));
        game.setPosition((Long) obj.get("position"));
        game.setNextMove((String) obj.get("nextMove"));
        game.setMessage((String) obj.get("message"));

        return game;
    }

    public String[] fromJsonArrayToRecordedGame(JSONArray recordedArray) {
        String[] recordedPosition = new String[9];
        for (int index = 0; index < 9; index++) {
            recordedPosition[index] = (String) recordedArray.get(index);
        }
        return recordedPosition;
    }
}
