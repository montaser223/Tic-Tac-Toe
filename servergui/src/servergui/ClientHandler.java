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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.global;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONObject;

/**
 *
 * @author black horse
 */
public class ClientHandler extends Thread implements Serializable {

    static ArrayList<Player> players = new ArrayList<Player>();
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
//            writeObj.wr
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
            int isOnline = database.updateStatus(Status.ONLINE, newPlayer.getUsername());
            if (isOnline == 1) {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setState(Status.ONLINE);
                connectedPlayers.put(newPlayer.getUsername(), this);
                sendMsg(newPlayer);
                updatePlayerList();
            } else {
                newPlayer.setRespond(Respond.FAILURE);
                sendMsg(newPlayer);
            }
        } else {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
        }
    }

    public void updatePlayerList() {
        players = database.selectplayer();
        PlayersList.setPlayerList(players);
        for (Player player : players) {
            if (player.getState().equals(Status.ONLINE)) {
                player.setRequest(Request.USERS);
                player.setPlayerList(PlayersList.getPlayersList());
                ClientHandler user = connectedPlayers.get(player.getUsername());
                user.sendMsg(player);
            }

        }
    }

    private void logout(Player newPlayer) {
        int isOffline = database.updateStatus(Status.OFFLINE, newPlayer.getUsername());
        if (isOffline == 1) {
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
        } else {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
        }
    }

    private void sginUp(Player newPlayer) {

        int isExist = database.check_username(newPlayer.getUsername());

        if (isExist == 1) {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);

        } else {
//            database.sign_up(newPlayer.getFirstname(), newPlayer.getLastname(), newPlayer.getUsername(),newPlayer.getPassword());
//            newPlayer.setRespond(Respond.SUCCESS);
//            sendMsg(newPlayer); 
        }

    }

    @Override
    public void run() {
        while (connected) {
            try {
                Player p2 = (Player) readObj.readObject();
//                JSONParser p3 = new JSONParser(p2, global,true);
//                JSONO p3 = new JSONWriter(p2
//                System.out.println("Line 180 : " + p2);
//                System.out.println("Line 18 : " + p2.get(p2.keySet()));
                messageHandler(p2);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
