/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import libs.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author abobakr
 */

public class GameHandler extends Thread  implements Serializable{
    
    
    private XoDataBase database;

    private static Vector<ClientHandler> players = new Vector<ClientHandler>();
    
    private  volatile Game gameRequest;
    
   
    
    private volatile boolean isGameRunning;
    
    private  static String symbol = Game.X_MOVE;
    
    private Socket socket;
    
    private DataInputStream inStream;
    private PrintStream outStream;
    private JsonConverter convert;
    
    
    
    GameHandler(ClientHandler player) {
        
        gameRequest = new Game();
        convert = new JsonConverter();
        System.out.println(" game handler constructor");
//        
        
        
        if(symbol == Game.X_MOVE){
            
            // first player will take the symbol x
            gameRequest.setNextMove(symbol);
            players.add(player);  // adding player to the list
            
            System.out.println(" game handler: adding the first player" );
            // send the sybol for the player to use it for the rest of the game
            System.out.println(" sending symbol" + symbol + " to first player");
            player.sendMsg(new Gson().toJson(gameRequest));
            symbol = Game.O_MOVE;
            
            
            
        }else{
            // second player will take O
            System.out.println(" game handler: adding the second player" );
            System.out.println("The symol is = " + symbol);
            gameRequest.setNextMove(symbol);
            System.out.println("The symol inside the object  = " + gameRequest.getNextMove());
            players.add(player);
            
            // send the sybol for the player to use it for the rest of the game
            
            System.out.println(" sending symbol" + symbol + " to socend player");
            player.sendMsg(new Gson().toJson(gameRequest));
            symbol = Game.X_MOVE;
            
            
        }
        
        isGameRunning = true;
        gameRequest = null;
        System.out.println("start thread");
        start();
    }
    
    @Override
    public void run() {
//        super.run(); //To change body of generated methods, choose Tools | Templates.

        while(isGameRunning){
            
            
                System.out.print("");
                gameRequest = ClientHandler.getGameHandlerRequest();
                if(gameRequest != null){
                    System.out.println(" Game Handler receved a move ");
                    sendGameMove(convert.fromGameToJson(gameRequest).toString());
//                    resetGameRequests();
                }
        }
       
    }
    
    private void getGameRequest(){
        
        gameRequest = ClientHandler.getGameHandlerRequest();
        
    }
    
    public static void setGameHandlerRequest(Game newGame){
        
    }
     private void resetGameRequests(){
        while(gameRequest != null){
            ClientHandler.setGameHandlerRequest(null);
            gameRequest = null;
        }
    }
    
    
    private void disConnect(){
        // this function not finished yet, 
        // we need to handle if one of the player lost the connecttion or left the game
        System.out.println("breaking the loop");
        isGameRunning = false;
        this.stop();
    }
    
    public void sendGameMove(String obj){
        
        for(ClientHandler player: players){
            
            System.out.println("Broadcast to all player");
            player.sendMsg(obj);
            
        }
    }
    
    
}
