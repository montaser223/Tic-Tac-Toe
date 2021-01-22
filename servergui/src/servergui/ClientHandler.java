/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author black horse
 */

public class ClientHandler extends Thread implements Serializable {
    
    ObjectInputStream readObj;
    ObjectOutputStream writeObj;
   
    public static HashMap<String,ClientHandler> connectedPlayers = new HashMap<String,ClientHandler>();
    
    ClientHandler(Socket socket) {
        try {
            writeObj = new ObjectOutputStream(socket.getOutputStream());
            readObj= new ObjectInputStream(socket.getInputStream());
            start();

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    private void messageHandler(Player p){
        switch(p.getRequest()){
            case "login":
                login(p);
                break;
            case "logout":
                logout(p);
                break;
                
        }
    }
    
    private void login(Player newPlayer){
        // will connect with data base and check of this user
        // will add  player user name with the playe object to HashMap
        // will send back a message with respod  == loged in using the this.writeObject.writeObject(player.getRespond())
        newPlayer.setRespond("logedIn");
        try {
            this.writeObj.writeObject(newPlayer);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void logout(Player newPlayer){
        
    }
    private void sginUp(Player newPlayer){
        
    }
   
    public void stopClientHandler() {
        this.stop();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Player p2 = (Player) readObj.readObject();
                messageHandler(p2);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                                System.out.println(ex.getMessage());

            }
        }
    }
}
   