/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;
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
        game = new Game();
        System.out.println(" game handler constructor");
        try {
            this.writeObj = new ObjectOutputStream(socket.getOutputStream());
            this.readObj =  new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
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
   
    @Override
    public void run() {
//        super.run(); //To change body of generated methods, choose Tools | Templates.
        while(connected){
            try {
                
                System.out.println("inside the thread");
                Game game = (Game) readObj.readObject();
                System.out.println("receved move from player");
                int location = game.getLocation();
                String xAndo = game.getxAndo();
                System.out.println("location of " + xAndo + " " +  location);
//                String [] arr = new String[9];
//                arr = game.getGamePosition();
//                for(String str: arr){
//                    System.out.println(str);
//                }
                sendGameMove(game);
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
                connected = false;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
                connected = false;
            }
            
        }
    }
    
    private void sendGameMove(Game game){
        for(GameHandler player: players){
            
            try {
                System.out.println("Broadcast to all player");
                player.writeObj.writeObject(game);
                
            } catch (IOException ex) {
                Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
