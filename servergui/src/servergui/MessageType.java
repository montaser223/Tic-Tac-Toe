/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abobakr
 */

public class MessageType implements Serializable {
    
    
    public static final String LOGEDIN = "logedin";
    public static final String LOGEDOUT = "logeout";
    
    /*public static void main(String args[]){
        Player p = new Player();
        p.setRequest(Request.LOGIN);
        Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;
        try {
            socket = new Socket("127.0.0.1", 5005);   
            
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(p);
            input = new ObjectInputStream(socket.getInputStream());
            
            try {
                Player p2 = (Player) input.readObject();
                System.out.println(p2.getRespond());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MessageType.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageType.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
}
