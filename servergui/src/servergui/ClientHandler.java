/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import libs.*;
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

    static Vector<ClientHandler> clients = new Vector<ClientHandler>();
    ObjectInputStream readObj;
    ObjectOutputStream writeObj;
    XoDataBase database;

    private static Vector<ClientHandler> clients = new Vector<ClientHandler>();
    private ObjectInputStream readObj;
    private ObjectOutputStream writeObj;
    private XoDataBase database;
    private volatile boolean connected;

    public static HashMap<String, ClientHandler> connectedPlayers = new HashMap<String, ClientHandler>();

    ClientHandler(Socket socket) {
        try {
            database = new XoDataBase();
            writeObj = new ObjectOutputStream(socket.getOutputStream());
            readObj = new ObjectInputStream(socket.getInputStream());
            connected = true;
            start();

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Clinet socket is not work");
            connected = false;
        }
    }

    private void messageHandler(Player newPlayer) {
        switch (newPlayer.getRequest()) {
            case "login":
                login(newPlayer);
                break;
            case "logout":
                logout(newPlayer);
                break;

        }
    }

    private void sendMsg(Player player) {

        try {
            this.writeObj.writeObject(player);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void login(Player newPlayer) {
        int isCorrect = database.check_username_password(newPlayer.getPassword(), newPlayer.getUsername());
        if (isCorrect == 1) {
            newPlayer.setRespond(Respond.SUCCESS);
            newPlayer.setState(Status.ONLINE);
            connectedPlayers.put(newPlayer.getUsername(), this);
            sendMsg(newPlayer);

        } else {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
        }
    }

    private void logout(Player newPlayer) {
        Boolean isRemoved = connectedPlayers.remove(newPlayer.getUsername(), this);
        if (isRemoved) {
            try {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setState(Status.OFFLINE);
                sendMsg(newPlayer);
                connected = false;
                readObj.close();
                writeObj.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
        }
    }

    private void sginUp(Player newPlayer) {
        
        int isExist = database.check_username(newPlayer.getUsername());
        
        if(isExist == 1){
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
            
        }else{
            database.sign_up(newPlayer.getFirstname(), newPlayer.getLastname(), newPlayer.getUsername(),newPlayer.getPassword());
            newPlayer.setRespond(Respond.SUCCESS);
            sendMsg(newPlayer); 
        }
        
    }

    @Override
    public void run() {
        while (connected) {
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
