/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import libs.*;
import java.io.DataInputStream;
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
    private GameHandler gameHandel;
    private String playerOneName;
    private String playerTwoName;

    ClientHandler(Socket socket) {

        try {
            database = new XoDataBase();
            outStream = new PrintStream(socket.getOutputStream());
            inStream = new DataInputStream(socket.getInputStream());
            parser = new JSONParser();
            parser = new JSONParser();
            convert = new JsonConverter();
            connected = true;
            start();

        } catch (IOException ex) {
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
                this.stop();

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
                endGame(message);
            case Request.GAME_MOVE:
            case Request.GAME_PLAYAGAIN:
            case Request.Chat_Message:
                sendRequestToGameHandler(message);
                break;
            case Request.RECORD_GAME:
                recordGamePosition(message);
                break;
            case Request.GET_RECORDEDGAME:
                sendBackOldRecordedPosition(message);
                break;

            case Request.DISCONNECT:
                disconnect(message);
                break;
            case Request.UPDATE_SCORE:
                updateScore(message);
                break;
            case Request.STOPGAME:
                gameHandel.disConnect();
                break;
                

        }
    }

    private void sendBackOldRecordedPosition(JSONObject message) {
        JSONArray positions = new JSONArray();

    }

    private void changePlayerState(){
       
       database.updateStatus(Status.ONLINE,playerOneName);
       database.updateStatus(Status.ONLINE,playerTwoName);
        
        updatePlayerList();
    }

    private void startGame() {
        gameHandel = new GameHandler(this);
    }

    private void endGame(JSONObject message) {
        sendRequestToGameHandler(message);
        changePlayerState();
//        gameHandel.disConnect();
        
    }

    private void sendRequestToGameHandler(JSONObject message) {

        Game newGame = convert.fromJsonToGame(message);
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

        if (isGamePositionEmpty(game.getRecordedGamePosition())) {
            game.setRespond(Respond.FAILURE);
            game.setRecordedGamePosition(null);

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
    
//     private int getPlayerScore(String username) {
//        int score = 0;
//        for (Player player : players) {
//            if (player.getUsername().equalsIgnoreCase(username)) {
//                score = player.getScour();
//            }
//        }
//
//        return score;
//    }
    
    private int getPlayerIndex(String username) {
        int plyerIndex = -1;
        for (int index = 0; index < players.size() ; index++) {

            if (players.get(index).getUsername().equals(username)) {

                return index;  
            }
            
        }

        return plyerIndex;
    }

    private void gameInvitation(JSONObject message) {

        Player senderPlayer = convert.fromJsonToPlayer(message);

        if (getPlayerStatus(senderPlayer.getDestination()).equalsIgnoreCase(Status.ONLINE) && 
                !senderPlayer.getDestination().equalsIgnoreCase(senderPlayer.getUsername()) ) {

            senderPlayer.setRequest(Request.GAME_INVITATION_RESPOND);
            connectedPlayers.get(senderPlayer.getDestination()).sendMsg(senderPlayer);
            playerOneName = senderPlayer.getUsername();
            playerTwoName = senderPlayer.getDestination();

        } else {

            senderPlayer.setRespond(Respond.FAILURE);
            senderPlayer.setRespond(Request.GAME_INVITATION);
            sendMsg(senderPlayer);
        }
    }

    private void gameResponse(JSONObject message) {

        Player destinationPlayer = convert.fromJsonToPlayer(message);

        if (destinationPlayer.getRespond().equals(Respond.SUCCESS)) {
            destinationPlayer.setRequest(Request.GAME_INVITATION);
            destinationPlayer.setRespond(Respond.SUCCESS);
            database.updateStatus(Status.PLAYING, destinationPlayer.getUsername());

            connectedPlayers.get(destinationPlayer.getUsername()).sendMsg(destinationPlayer);
            players.get(getPlayerIndex(destinationPlayer.getUsername())).setStatus(Status.PLAYING);

            try {
               
                this.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
//            destinationPlayer.setRequest(Request.GAME_INVITATION);
//            destinationPlayer.setRespond(Respond.SUCCESS);

            database.updateStatus(Status.PLAYING, destinationPlayer.getDestination());

            connectedPlayers.get(destinationPlayer.getDestination()).sendMsg(destinationPlayer);

            
            playerTwoName = destinationPlayer.getDestination();

            
            updatePlayerList();

        } else {
            destinationPlayer.setRequest(Request.GAME_INVITATION);
            destinationPlayer.setRespond(Respond.FAILURE);
            connectedPlayers.get(destinationPlayer.getUsername()).sendMsg(destinationPlayer);
        }

    }

    public void stopClientHandler() {

        this.connected = false;
        this.stop();
    }

    public void sendMsg(Player player) {

        obj = convert.fromPlayerToJson(player);

        this.outStream.println(obj);

    }

    public void sendMsgUpdateplayer(Player player, String request) {

        player.setRequest(request);
        obj = convert.fromPlayerToJson(player, convert.fromPlayerListToJSONArray(player.getPlayersList()));
        if (obj == null) {

        } else {
            this.outStream.println(obj);

        }

    }

    private void updateScore(JSONObject game) {
       
        Game newGame = convert.fromJsonToGame(game);
        if (newGame.getWinner().equalsIgnoreCase(Game.X_MOVE)) {
            
            int playerScore = players.get(getPlayerIndex(newGame.getPlayerX())).getScour();

            playerScore++;
            database.updateScore(playerScore, newGame.getPlayerX());
        } else if (newGame.getWinner().equalsIgnoreCase(Game.O_MOVE)) {

            int playerScore = players.get(getPlayerIndex(newGame.getPlayerO())).getScour();

            playerScore++;
            database.updateScore(playerScore, newGame.getPlayerO()
            );
        }
        updatePlayerList();
    }

    public void sendMsg(String game) {
        this.outStream.println(game);
    }

    private void login(JSONObject message) {
        Player newPlayer = convert.fromJsonToPlayer(message);
        Player isExist = database.check_username_password(newPlayer.getUsername(), newPlayer.getPassword());
        if (isExist.getRespond().equals(Respond.SUCCESS)) {

            int isOnline = database.updateStatus(Status.ONLINE, newPlayer.getUsername());
            if (isOnline == 1) {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setStatus(Status.ONLINE);
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

    private void updatePlayerList() {
        players.clear();
        players = database.selectplayer();
        PlayersList.setPlayerList(players);
        for (Player player : players) {
            if (player.getStatus().equals(Status.ONLINE)) {
                player.setPlayerList(PlayersList.getPlayersList());
                ClientHandler user = connectedPlayers.get(player.getUsername());
                if (user != null) {
                    user.sendMsgUpdateplayer(player, Request.PlAYER_LIST);
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
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setStatus(Status.OFFLINE);
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

    private void disconnect(JSONObject message) {
        Player newPlayer = convert.fromJsonToPlayer(message);
        database.updateStatus(Status.OFFLINE, newPlayer.getUsername());
        try {
            connected = false;
            inStream.close();
            outStream.close();
            updatePlayerList();
        } catch (IOException ex) {
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
            updatePlayerList();
        }

    }
}
