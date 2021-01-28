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
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author black horse
 */
public class ClientHandler extends Thread implements Serializable {


    private static Vector<ClientHandler> clients = new Vector<ClientHandler>();
    private DataInputStream inStream;
    private PrintStream outStream;
    private XoDataBase database;
    private volatile boolean connected;
    private JSONObject obj;
    private JSONParser parser;

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
            start();
            
            

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Clinet socket is not work");
            connected = false;
        }
    }

    @Override
    public void run() {
        
        while (connected) {
            try {
                
                obj = (JSONObject) parser.parse(inStream.readLine());
                System.out.println("Clinet hander receved a move");
                messageHandler(obj);
                
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ParseException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void messageHandler(JSONObject message) {
        
        System.out.println("Message Hanlder receve: " + (String) message.get("request"));
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
    
//    public JSONObject getForwardedRequest(){
//        JSONObject result = forwardedRequest;
//        forwardedRequest = null;
//        return result;
//    }
    
    public static Game getGameHandlerRequest(){
        Game tmp = gameHandlerRequest;
        gameHandlerRequest = null;
        return tmp;
    }
    
    public static void setGameHandlerRequest(Game game){
        gameHandlerRequest = game;
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
        
        this.outStream.println(player);
    }
    
    public void sendMsg(String game){
        
        this.outStream.println(game);
    }
    
    private void login(Player newPlayer) {
        
        int isCorrect = database.check_username_password(newPlayer.getPassword(),newPlayer.getUsername());
        
        if(isCorrect == 1){
            newPlayer.setRespond(Respond.SUCCESS);
            newPlayer.setState(Status.ONLINE);
            newPlayer.setScour(database.getPlayerScore(newPlayer.getUsername()));
            connectedPlayers.put(newPlayer.getUsername(), this);
            sendMsg(newPlayer);
        }else{
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
        }
    }

    private void logout(Player newPlayer) {
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

    
}
