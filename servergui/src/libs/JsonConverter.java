/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.*;

/**
 *
 * @author abobakr
 */

public class JsonConverter {
    private DataInputStream input;
    private DataOutputStream output;
    private static JSONObject obj;
    private static JSONParser parser;
    
    public static JSONObject playerToJson(Player player){
        
        obj = new JSONObject();
        obj.put("username", player.getUsername());
        obj.put("password", player.getPassword());
        return obj;
    }
    
    public static JSONObject gameToJson(Game game){
        
        obj  = new JSONObject();
        obj.put("position", game.getLocation());
        obj.put("xAndo", game.getxAndo());
        obj.put("symbol", game.getSymbol());
        return obj;
    }
    
}
