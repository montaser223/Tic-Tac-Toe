/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;
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
    

    private static Vector<GameHandler> players = new Vector<GameHandler>();
    
    private Game game;
    
    private ObjectInputStream readObj;
    private ObjectOutputStream writeObj;
    
    private XoDataBase database;
    private volatile boolean connected;
    
    private  static String symbol = Game.X_MOVE;
    
    private Socket socket;
    
    private DataInputStream inStream;
    private PrintStream outStream;
    
    
    
    GameHandler(ObjectInputStream readObj, ObjectOutputStream writeObj) {
        System.out.println(" game handler constructor");
        this.readObj = readObj;
        this.writeObj = writeObj;
        game = new Game();
        
        if(symbol == Game.X_MOVE){
            
            
            game.setNextMove(symbol); // first player will take the symbol x
            
            players.add(this);  // adding player to the list
            
            System.out.println(" game handler: adding the first player" );
            // send the sybol for the player to use it for the rest of the game
            System.out.println(" sending symbol" + symbol + " to first player");
            outStream.println(new Gson().toJson(game));
            symbol = Game.O_MOVE;
            
            
            
        }else{
            // second player will take O
            System.out.println(" game handler: adding the second player" );
            game.setNextMove(symbol);
            players.add(this);
            
            // send the sybol for the player to use it for the rest of the game
            
            System.out.println(" sending symbol" + symbol + " to socend player");
            outStream.println(new Gson().toJson(game));
            symbol = Game.X_MOVE;
            
            
        }
        
        connected = true;
        System.out.println("start thread");
        start();
    }
    
    
    
    
    
    GameHandler(Socket socket) {
        this.socket = socket;
        game = new Game();
        System.out.println(" game handler constructor");
        try {
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        if(symbol == Game.X_MOVE){
            
            // first player will take the symbol x
            game.setNextMove(symbol);
            players.add(this);  // adding player to the list
            
            System.out.println(" game handler: adding the first player" );
            // send the sybol for the player to use it for the rest of the game
            System.out.println(" sending symbol" + symbol + " to first player");
            outStream.println(new Gson().toJson(game));
            symbol = Game.O_MOVE;
            
            
            
        }else{
            // second player will take O
            System.out.println(" game handler: adding the second player" );
//            game.setSymbol(symbol);
            game.setNextMove(symbol);
            players.add(this);
            
            // send the sybol for the player to use it for the rest of the game
            
            System.out.println(" sending symbol" + symbol + " to socend player");
            outStream.println(new Gson().toJson(game));
            symbol = Game.X_MOVE;
            
            
        }
        
        connected = true;
        System.out.println("start thread");
        start();
    }
   
    @Override
    public void run() {
//        super.run(); //To change body of generated methods, choose Tools | Templates.

        while(connected){
            
                
                System.out.println("inside the thread waiting move ");
                String obj;
            try {
                
                
                obj = inStream.readLine();
                System.out.println("receved move from player");
                if(obj == null){
                    
                    System.out.println("breaking the loop");
                    connected = false;
                    inStream.close();
                    outStream.close();
                    outStream.println("wlecom");
                    break;
                }else{
                    System.out.println(obj);
                    sendGameMove(obj);
                }
                
                System.out.println("11111");
                
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
                connected = false;
            }
                
                

            
                
            
            
        }
    }
    
    private void sendGameMove(String obj){
        
        for(GameHandler player: players){
            
            System.out.println("Broadcast to all player");
            player.outStream.println(obj);
        }
    }
    
    
}
