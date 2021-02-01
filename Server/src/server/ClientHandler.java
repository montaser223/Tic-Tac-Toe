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
import java.util.Map;
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
                System.out.println("line 68 : " + obj);
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
            case Request.GAME_INVITATION:
                gameInvitation(message);
                break;
            case Request.GAME_INVITATION_RESPOND:
                gameResponse(message);
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
            case Request.GET_RECORDEDGAME:
                break;

        }
    }

    private void startGame() {
        System.out.println("Sending start game to game handler");
        new GameHandler(this);
    }

    private void endGame() {

    }

    private void sendRequestToGameHandler(JSONObject message) {

        Game newGame = convert.fromJsonToGame(message);
        System.out.println("134: Sending  gameMove to game handler");
        gameHandlerRequest = newGame;

    }

    public static Game getGameHandlerRequest() {
        Game tmp = gameHandlerRequest;
        gameHandlerRequest = null;
        return tmp;
    }

    public static void setGameHandlerRequest(Game game) {
        gameHandlerRequest = game;
    }

    private void recordGamePosition(JSONObject obj) {

        Game game = convert.fromJsonWithArrayToGame(obj, convert.fromJsonArrayToRecordedGame((JSONArray) obj.get("recordedPosition")));
        System.out.println("send positions to database");

        if (isGamePositionEmpty(game.getRecordedGamePosition())) {
            game.setRespond(Respond.FAILURE);
            game.setRecordedGamePosition(null);
            System.out.println("set respond to  " + game.getRespond());

            sendMsg(convert.fromGameToJson(game).toString());
        } else {
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

    private boolean isGamePositionEmpty(String[] gamePosition) {
        boolean isEmpty = false;
        for (String position : gamePosition) {
            if (position.equalsIgnoreCase("")) {
                isEmpty = true;
            }
        }
        return isEmpty;
    }

    private String getPlayerStatus(String username) {
        String status = "";
        for (Player player : players) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                status = player.getStatus();
            }
        }

        return status;
    }

    private int getPlayerIndex(String username) {
        int index = -1;
        for (Player player : players) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                index++;
            }
        }

        return index;
    }

    private void gameInvitation(JSONObject message) {

        Player senderPlayer = convert.fromJsonToPlayer(message);

        if (getPlayerStatus(senderPlayer.getDestination()).equalsIgnoreCase(Status.ONLINE)) {

            senderPlayer.setRequest(Request.GAME_INVITATION);
            connectedPlayers.get(senderPlayer.getDestination()).sendMsg(senderPlayer);

        } else {

            senderPlayer.setRespond(Respond.FAILURE);
            senderPlayer.setRespond(Request.GAME_INVITATION);
            sendMsg(senderPlayer);
        }
    }

    private void gameResponse(JSONObject message) {
        // this is the same object send from the gameInvitation sender,
        // so the playerObj.getUsername -> the sender Username
        // and the playerObj.getDestination -> the destination player

        Player destinationPlayer = convert.fromJsonToPlayer(message);

        if (destinationPlayer.getRespond().equals(Respond.SUCCESS)) {
            destinationPlayer.setRequest(Request.GAME_INVITATION);
            destinationPlayer.setRespond(Respond.SUCCESS);
         destinationPlayer.setStatus(Status.PLAYING);
          System.out.println("send back to SENDER "+ convert.fromPlayerToJson(destinationPlayer).toString());


            connectedPlayers.get(destinationPlayer.getUsername()).sendMsg(destinationPlayer);
            players.get(getPlayerIndex(destinationPlayer.getUsername())).setStatus(Status.PLAYING);
//            updatePlayerList();
            // we need to send again for the destinationPlayer a Game Invitation Respond == success
            // to join the game with
            destinationPlayer.setRequest(Request.GAME_INVITATION);
            destinationPlayer.setRespond(Respond.SUCCESS);
            destinationPlayer.setStatus(Status.PLAYING);
            System.out.println("send back to destination "+ convert.fromPlayerToJson(destinationPlayer).toString());
        
          connectedPlayers.get(destinationPlayer.getDestination()).sendMsg(destinationPlayer); 

            players.get(getPlayerIndex(destinationPlayer.getDestination())).setStatus(Status.PLAYING);
            updatePlayerList();

        } else {
            destinationPlayer.setRequest(Request.GAME_INVITATION);
            destinationPlayer.setRespond(Respond.FAILURE);
            connectedPlayers.get(destinationPlayer.getUsername()).sendMsg(destinationPlayer);
        }

    }

    public void stopClientHandler() {

//        startFlag = false;
//        for (ClientHandler client : clients) {
//            logout(client.UserName);
//        }
    }

    public void sendMsg(Player player) {
       
//        obj = convert.fromPlayerToJson(player);
        System.out.println("player inside send meesage = "+ player);
        obj = convert.fromPlayerToJson(player); 

        System.out.println("line 282 clinet handeler send message "+obj); 
        
        this.outStream.println(obj);
        
    }

    public void sendMsgUpdateplayer(Player player, String request) {

        player.setRequest(request);
        obj = convert.fromPlayerToJson(player, convert.fromPlayerListToJSONArray(player.getPlayersList()));
        System.out.println("line 291 "+ obj);
            if (obj == null )
            {
                
                System.out.println("tring to send null obj ");
            }
            else
            {
                        this.outStream.println(obj);

            }

    }

    public void sendMsg(String game) {

        this.outStream.println(game);
    }

    private void login(JSONObject message) {
        Player newPlayer = convert.fromJsonToPlayer(message);
            System.out.println("line 283" +newPlayer.getRequest());
        Player isExist = database.check_username_password(newPlayer.getUsername(), newPlayer.getPassword());
        if (isExist.getRespond().equals(Respond.SUCCESS)) {
               

            int isOnline = database.updateStatus(Status.ONLINE, newPlayer.getUsername());
            if (isOnline == 1) {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setStatus(Status.ONLINE);
                connectedPlayers.put(newPlayer.getUsername(), this);
                sendMsgUpdateplayer(newPlayer,newPlayer.getRequest());
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
        players.clear();
        players = database.selectplayer();
        PlayersList.setPlayerList(players);
        for (Player player : players) {
            if (player.getStatus().equals(Status.ONLINE)) {
                player.setPlayerList(PlayersList.getPlayersList());
                ClientHandler user = connectedPlayers.get(player.getUsername());
                if (user != null) {
                    user.sendMsg(player, Request.PlAYER_LIST);
                } else {
                    database.updateStatus(Status.OFFLINE, player.getUsername());
                }
            }

        }
    }

    private void logout(JSONObject message) {
        Player newPlayer = convert.fromJsonToPlayer(message);
        int isOffline = database.updateStatus(Status.OFFLINE, newPlayer.getUsername());
        if (isOffline == 1) {
            Boolean isRemoved = connectedPlayers.remove(newPlayer.getUsername(), this);
            if (isRemoved) {
                try {
                    newPlayer.setRespond(Respond.SUCCESS);
                    newPlayer.setStatus(Status.OFFLINE);
                    sendMsg(newPlayer);
                    updatePlayerList();
                    connected = false;
                    this.inStream.close();
                    this.outStream.close();
                    this.stop();
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

    private void sginUp(JSONObject message) {

        Player newPlayer = convert.fromJsonToPlayer(message);
        int isExist = database.check_username(newPlayer.getUsername());

        if (isExist == 1) {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);

        } else {
            database.sign_up(newPlayer.getFirstname(), newPlayer.getLastname(), newPlayer.getUsername(), newPlayer.getPassword());
            newPlayer.setRespond(Respond.SUCCESS);
            sendMsg(newPlayer);
        }

    }
}
