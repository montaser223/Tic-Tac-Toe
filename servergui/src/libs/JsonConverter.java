/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import org.json.simple.JSONObject;

/**
 *
 * @author abobakr
 */
public class JsonConverter {
    
    private static JSONObject obj;
    private static Player player;
    private static Game game;
    
    public static Player fromJsonToPlayer(JSONObject obj){
        player = new Player();
        return player;
    }
    
    public static Game fromJsonToGame(JSONObject obj){
        game = new Game();
        game.setRecordedGamePosition((String[])obj.get("recordedgamePosition"));
        game.setWinner((String) obj.get("sinner"));
        game.setRequest((String) obj.get("request"));
        game.setPlayedMove((String) obj.get("playedMove"));
        game.setPosition((Long) obj.get("position"));
        game.setNextMove((String) obj.get("nextMove"));
        game.setMessage((String) obj.get("message"));
        
        
        
        return game;
    }
    
    public static JSONObject fromGameToJson(Game game){
        
        obj = new JSONObject();
//        obj.put("recordedgamePosition", game.getRecordedGamePosition());
        obj.put("winner", game.getWinner());
        obj.put("request", game.getRequest());
        obj.put("playedMove", game.getPlayedMove());
        obj.put("position", game.getPosition());
        obj.put("nextMove", game.getNextMove());
        obj.put("message", game.getMessage());
        
        return obj;
        
        
        
        
    }
    
}
