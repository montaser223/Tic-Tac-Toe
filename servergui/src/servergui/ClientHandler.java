/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import libs.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author black horse
 */
public class ClientHandler extends Thread implements Serializable {

    static ArrayList<Player> players = new ArrayList<Player>();
    private XoDataBase database;
    private volatile boolean connected;
    DataOutputStream outStream;
    DataInputStream inStream;
    JSONParser parser;
    JsonConverter convert;
    public static HashMap<String, ClientHandler> connectedPlayers = new HashMap<String, ClientHandler>();

    ClientHandler(Socket socket) {
        try {

            database = new XoDataBase();
            outStream = new DataOutputStream(socket.getOutputStream());
            inStream = new DataInputStream(socket.getInputStream());
            connected = true;
            parser = new JSONParser();
            convert = new JsonConverter();
            start();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Clinet socket is not work");
            connected = false;
        }
    }

    private void messageHandler(Player newPlayer) {
        switch (newPlayer.getRequest()) {
            case Request.LOGIN:
                login(newPlayer);
                break;
            case Request.LOGOUT:
                logout(newPlayer);
                break;
            case Request.SIGNUP:
                sginUp(newPlayer);
                break;
        }
    }

    private void sendMsg(Player player) {
        try {
            JSONObject obj = convert.fromPlayerToJson(player, convert.fromPlayerListToJSONArray(player.getPlayersList()));
            outStream.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void login(Player newPlayer) {
        Player isExist = database.check_username_password(newPlayer.getUsername(), newPlayer.getPassword());
        if (isExist.getRespond().equals(Respond.SUCCESS)) {
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
            System.out.println("Player :" + player.getUsername() + " Status= " + player.getState());
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
                    updatePlayerList();
                    connected = false;
                    inStream.close();
                    outStream.close();
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
                try {
                    JSONObject obj = (JSONObject) parser.parse(inStream.readUTF());
//                 , convert.jSONArrayToPlayerList((JSONArray) obj.get("playersList"))
                    Player newPlayer = convert.fromJsonToPlayer(obj);
                    messageHandler(newPlayer);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
