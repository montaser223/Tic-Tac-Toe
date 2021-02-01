/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import libs.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
            parser = new JSONParser();
            convert = new JsonConverter();
            connected = true;
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
                System.out.println("Line 77 " + obj);
                System.out.println("Line 78 " + obj.get("request"));
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
                login(message);
                break;
            case Request.LOGOUT:
                logout(message);
                break;
            case Request.SIGNUP:
                sginUp(message);
                break;
            case Request.START_GAME:
                startGame();
                break;
            case Request.END_GAME:
                endGame();
            case Request.GAME_MOVE:
            case Request.GAME_PLAYAGAIN:
            case Request.Chat_Message:
                System.out.println(message);
                sendRequestToGameHandler(message);
                break;
            case Request.RECORD_GAME:
                System.out.println(message);
                recordGamePosition(message);
                break;
            case Request.DISCONNECT:
                disconnect();
                break;
            
        }
    }

    private void startGame(){
        System.out.println("Sending start game to game handler");
        new GameHandler(this);
    }
    private void endGame(){
        
    }
    
    private void sendRequestToGameHandler(JSONObject message){
        
        Game newGame = convert.fromJsonToGame(message);
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
    
    private void recordGamePosition(JSONObject obj){
        
        Game game = convert.fromJsonWithArrayToGame(obj, convert.fromJsonArrayToRecordedGame( (JSONArray) obj.get("recordedPosition") ));
        System.out.println("send positions to database");
        
        if(isGamePositionEmpty(game.getRecordedGamePosition())){
            game.setRespond(Respond.FAILURE);
            game.setRecordedGamePosition(null);
            System.out.println("set respond to  " + game.getRespond());

            sendMsg(convert.fromGameToJson(game).toString());
        }else{
            /*
            will send to dataBase these information
            game.getPlayerX();
            game.getPlayerO();
            game.getRecordedGamePosition();
            */
            game.setRespond(Respond.SUCCESS);
            game.setRecordedGamePosition(null);
            System.out.println("set respond to  " + game.getRespond());

            sendMsg(convert.fromGameToJson(game).toString());
        }
    }
    
    private boolean isGamePositionEmpty(String[] gamePosition){
        boolean isEmpty = false;
        for(String position: gamePosition){
            if(position.equalsIgnoreCase("")){
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    /**
     * for player1
     * receive a new game request from player then check game type[old,new]
     * if there is an old game ask player1 to choose what he want to start [old,new]
     * if there is not an old game then send a new play request to player2
     */
    //game_invitation
    
//    private void  gameInvitation(Player senderPlayer){
//        //gui
//       // newplayer2.setRequest(Request.GAMEINVITATION);
//       //connectedPlayers.put("emansol", this);
//      
//       if((connectedPlayers.get(senderPlayer.getDestination())!=null))
//         //(connectedPlayers.get(senderPlayer.getDestination()).getState().equals(Status.ONLINE)))
//               {
//                   //System.out.println("playe");
//                    //senderPlayer.Requestgame();
//                    senderPlayer.setRequest(Request.GAMEINVITATION);
//                    
//            connectedPlayers.get(senderPlayer.getDestination()).sendMsg(senderPlayer);
//            //Player newPlay=new Player();
//            //newPlay.setUsername(senderPlayer.getDestination());
//            //newPlay.setRequest(Request.GAMEINVITATION);
//            //sendMsg(newPlay);
//            
//            
//             
//            //sendMsg(senderPlayer);
//      //thread.sleep(1000);
//      
//       }
//       else{
//            senderPlayer.setRespond(Respond.FAILURE);
//            //enter play list again to chooce anther one to playe
//            senderPlayer.setRespond(Request.GAMEINVITATION);
//            System.out.println("no");
//         
//             
//            sendMsg(senderPlayer);
//       }
//           
//       // System.out.println(senderPlayer.Respondgame());
//    }
//             
////game invitition
////game respond
////    
//    
//      private void gameResponse(Player destinationPlayer){
//          //gui
//          //newplayer.setRequest(Request.respond_GAME);
//         
//         // newplayer.Requestgame();
//          //System.out.println("enterrrrrrrr");
//         // if(destinationPlayer.Respondgame()==JOptionPane.YES_OPTION){
//              //destinationPlayer.setRespond(Respond.SUCCESS);
//          //}
//         // else if(destinationPlayer.Respondgame()==JOptionPane.NO_OPTION)
//            //  destinationPlayer.setRespond(Respondssssssssss.FAILURE);
//          //else
//              //destinationPlayer.setRespond(Respond.FAILURE);
//          
//          if(destinationPlayer.getRespond().equals(Respond.SUCCESS)){
//               System.out.println("enter ah");
//             //newplayer.setRespond(Respond.SUCCESS);
//                 destinationPlayer.setRequest(Request.GAMERESPOND);
//                  System.out.println("elgame");
//                 //enter game
//                 //newplayer.setRequest(Request.STARTGAME);
//                 
//            }
//            else{
//                    System.out.println("not enter ");
//                    destinationPlayer.setRequest(Request.GAMERESPOND);
//                destinationPlayer.setRespond(Respond.FAILURE);
//                //newplayer.setRequest(Request.);
//                
//            }
//                //gui get destinayion==> player obj.getusername()=> sender
//                    connectedPlayers.get(destinationPlayer.getUsername()).sendMsg(destinationPlayer);
//        }


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

    public void sendMsg(Player player){
        obj = convert.fromPlayerToJson(player, convert.fromPlayerListToJSONArray(player.getPlayersList()));
        System.out.println("asdasc : "+ obj);
        this.outStream.println(obj);
        System.out.println("object sennnnt");
    }
    
    public void sendMsg(Player player, String request){
        
        player.setRequest(request);
         obj = convert.fromPlayerToJson(player, convert.fromPlayerListToJSONArray(player.getPlayersList()));
        
        this.outStream.println(obj);
    }
    
    public void sendMsg(String game){
        
        this.outStream.println(game);
    }

   private void login(JSONObject message) {
        Player newPlayer = convert.fromJsonToPlayer(message);

        Player isExist = database.check_username_password(newPlayer.getUsername(), newPlayer.getPassword());
        if (isExist.getRespond().equals(Respond.SUCCESS)) {
            int isOnline = database.updateStatus(Status.ONLINE, newPlayer.getUsername());
            if (isOnline == 1) {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setState(Status.ONLINE);
                connectedPlayers.put(newPlayer.getUsername(), this);
                sendMsg(newPlayer,newPlayer.getRequest());
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

    private void updatePlayerList() {
        ArrayList<Player> unTrackedPlayers = new ArrayList<Player>();
        players.clear();
        players = database.selectplayer();
        PlayersList.setPlayerList(players);
        for (Player player : players) {
            System.out.println("Player :" + player.getUsername() + " Status= " + player.getState() + " score " + player.getScour());
            if (player.getState().equals(Status.ONLINE)) {
                player.setPlayerList(PlayersList.getPlayersList());
                ClientHandler user = connectedPlayers.get(player.getUsername());
                if (user != null) {
                    System.out.println("Line 332 : " + connectedPlayers.get(player.getUsername()));
                    user.sendMsg(player, Request.PlAYER_LIST);
//                    player
//                    players.indexOf(player);
//                    unTrackedPlayers.add();
                } 
                /*
                
                else {
                }
                */
            }
        }
//        logout(unTrackedPlayers);
    }

    private void logout(JSONObject message) {
        
        Player newPlayer = convert.fromJsonToPlayer(message);
        int isOffline = database.updateStatus(Status.OFFLINE, newPlayer.getUsername());
        if (isOffline == 1) {
            Boolean isRemoved = connectedPlayers.remove(newPlayer.getUsername(), this);
            if (isRemoved) {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setState(Status.OFFLINE);
                sendMsg(newPlayer,newPlayer.getRequest());
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
    private void disconnect()
    {
                    
        try {
            connected = false;
            inStream.close();
            outStream.close();
        } catch (IOException ex) {
//            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Client Close the application");
        }
                    
    
    }
    private void sginUp(JSONObject message) {
        
        Player newPlayer = convert.fromJsonToPlayer(message);
        int isExist = database.check_username(newPlayer.getUsername());

        if (isExist == 1) {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);

        } else {
           database.sign_up(newPlayer.getFirstname(), newPlayer.getLastname(), newPlayer.getUsername(),newPlayer.getPassword());
           newPlayer.setRespond(Respond.SUCCESS);
           sendMsg(newPlayer,newPlayer.getRequest()); 
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
