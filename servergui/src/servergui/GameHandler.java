/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import libs.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    
    private  static String symbol = "X";
    
    private Socket socket;
    private JSONObject obj;
    private JSONParser parser;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    
    
    
    GameHandler(ObjectInputStream readObj, ObjectOutputStream writeObj) {
        System.out.println(" game handler constructor");
        this.readObj = readObj;
        this.writeObj = writeObj;
        game = new Game();
        
        if(symbol == "X"){
            
            // first player will take the symbol x
            game.setSymbol(symbol);
            players.add(this);  // adding player to the list
            
            System.out.println(" game handler: adding the first player" );
            try {
                
                // send the sybol for the player to use it for the rest of the game
                System.out.println(" sending symbol" + symbol + " to first player");
                writeObj.writeObject(game);
                symbol = "O";
                
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }else{
            // second player will take O
            System.out.println(" game handler: adding the second player" );
            game.setSymbol(symbol);
            players.add(this);
            
            try {
                // send the sybol for the player to use it for the rest of the game

                System.out.println(" sending symbol" + symbol + " to socend player");

                writeObj.writeObject(game);
                symbol = "X";
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        connected = true;
        System.out.println("start thread");
        start();
    }
    
    
    
    
    
    GameHandler(Socket socket) {
        this.socket = socket;
        obj = new JSONObject();
        parser = new JSONParser();
        System.out.println(" game handler constructor");
        try {
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        if(symbol == "X"){
            
            // first player will take the symbol x
//            game.setSymbol(symbol);
            obj.put("symbol", symbol);
            players.add(this);  // adding player to the list
            
            System.out.println(" game handler: adding the first player" );
            try {
                
                // send the sybol for the player to use it for the rest of the game
                System.out.println(" sending symbol" + symbol + " to first player");
                outStream.writeUTF(obj.toString());
//                writeObj.writeObject(game);
                symbol = "O";
                
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }else{
            // second player will take O
            System.out.println(" game handler: adding the second player" );
//            game.setSymbol(symbol);
            obj.put("symbol", symbol);
            players.add(this);
            
            try {
                // send the sybol for the player to use it for the rest of the game

                System.out.println(" sending symbol" + symbol + " to socend player");
                outStream.writeUTF(obj.toString());
//                writeObj.writeObject(game);
                symbol = "X";
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        connected = true;
        System.out.println("start thread");
        start();
    }
   
    @Override
    public void run() {
//        super.run(); //To change body of generated methods, choose Tools | Templates.
        while(connected){
            try {
                
                System.out.println("inside the thread waiting move ");
                System.out.println("receved move from player");
                String x = inStream.readUTF();
                System.out.println(x);
                obj = new JSONObject();
                obj = (JSONObject) parser.parse(x);
                System.out.println("After parseing");
                
//                int location = (int)obj.get("position");
//                String xAndo = (String) obj.get("xAndo");
//                System.out.println("location of " + xAndo + " " +  location);
                sendGameMove(obj);
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
                connected = false;
            } catch (ParseException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    private void sendGameMove(JSONObject obj){
        
        for(GameHandler player: players){
            try {
                System.out.println("Broadcast to all player");
                player.outStream.writeUTF(obj.toString());
                
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
