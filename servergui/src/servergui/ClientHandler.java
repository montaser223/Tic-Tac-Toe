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
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author black horse
 */
public class ClientHandler extends Thread implements Serializable {

    private static ArrayList<Player> players = new ArrayList<Player>();
    private XoDataBase database;
    private volatile boolean connected;
    private JSONParser parser;
    private JsonConverter convert;
    private JSONObject obj;
    private DataInputStream inStream;
    private PrintStream outStream;
    public static HashMap<String, ClientHandler> connectedPlayers = new HashMap<String, ClientHandler>();
    public static Game gameHandlerRequest = null;
    

   
    

   
    
    ClientHandler(Socket socket) {
        
        try {
            
            System.out.println("Player connected");
            database = new XoDataBase();
            outStream = new PrintStream(socket.getOutputStream());
            inStream = new DataInputStream(socket.getInputStream());
            parser = new JSONParser();
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

    @Override
    public void run() {
        
        while (connected) {
            try {
                
                obj = (JSONObject) parser.parse(inStream.readLine());
                messageHandler(obj);
                
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ParseException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void messageHandler(JSONObject message) {
        
        switch ((String) message.get("request")) {
            case Request.LOGIN:
                
                login(JsonConverter.fromJsonToPlayer(message));
                break;
            case Request.LOGOUT:
                logout(JsonConverter.fromJsonToPlayer(message));
                break;
            case Request.START_GAME:
                startGame();
                break;
               
            case Request.GAME_MOVE:
            case Request.GAME_PLAYAGAIN:
            case Request.Chat_Message:
                System.out.println(message);
                sendRequestToGameHandler(JsonConverter.fromJsonToGame(message));
                break;
            case Request.RECORD_GAME:
                recordGamePosition(JsonConverter.fromJsonToGame(message));
                break;
            case Request.SIGNUP:
                sginUp(newPlayer);
                break;
        }
    }

    private void startGame(){
        System.out.println("Sending start game to game handler");
        new GameHandler(this);
    }
    
    private void sendRequestToGameHandler(Game newGame){
        
        System.out.println("Sending  gameMove to game handler");
        gameHandlerRequest = newGame;
        
    }
    
    public static Game getGameHandlerRequest(){
        Game tmp = gameHandlerRequest;
        gameHandlerRequest = null;
        return tmp;
    }
    
    public static void setGameHandlerRequest(Game game){
        gameHandlerRequest = game;
    }
    
    private void recordGamePosition(Game game){
        
        if(game.getRecordedGamePosition().equals(null)){
            game.setRespond(Respond.FAILURE);
            sendMsg(JsonConverter.fromGameToJson(game).toString());
        }else{
            /*
            will send to dataBase these information
            game.getPlayerX();
            game.getPlayerO();
            game.getRecordedGamePosition();
            */
            game.setRespond(Respond.SUCCESS);
            sendMsg(JsonConverter.fromGameToJson(game).toString());
        }
    }
    
    void logOut(String _un) {
//        int index = getIndex(_un);
//        if (index > -1) {
//            ClientHandler client = clients.elementAt(index);
//            System.out.println(client);
//            clients.removeElementAt(clients.indexOf(client));
//        } else {
//            System.out.println("Cannot remove client at index 0");
//        }
    }

    int getIndex(String _un) {
//        int index = -1;
//        for (ClientHandler client : clients) {
//            if (client.UserName == _un) {
//                index = clients.indexOf(client);
//            }
//        }
        return 0;
    }

    public void stopClientHandler() {

//        startFlag = false;
//        for (ClientHandler client : clients) {
//            logout(client.UserName);
//        }
    }

    private void sendMsg(Player player){
        
        
        JSONObject obj = convert.fromPlayerToJson(player, convert.fromPlayerListToJSONArray(player.getPlayersList()));

        this.outStream.println(player);
    }
    
    public void sendMsg(String game){
        
        this.outStream.println(game);
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
           database.sign_up(newPlayer.getFirstname(), newPlayer.getLastname(), newPlayer.getUsername(),newPlayer.getPassword());
           newPlayer.setRespond(Respond.SUCCESS);
           sendMsg(newPlayer); 
        }

    }

//     @Override
//     public void run() {
//         while (connected) {

//             try {
//                 try {
//                     JSONObject obj = (JSONObject) parser.parse(inStream.readUTF());
//                  , convert.jSONArrayToPlayerList((JSONArray) obj.get("playersList"))
//                     Player newPlayer = convert.fromJsonToPlayer(obj);
//                     messageHandler(newPlayer);
//                 } catch (IOException ex) {
//                     System.out.println(ex.getMessage());
//                 }
//             } catch (ParseException ex) {
//                 System.out.println(ex.getMessage());
//             }
//         }
//     }
}
