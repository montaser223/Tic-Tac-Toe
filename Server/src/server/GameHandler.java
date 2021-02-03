/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import java.io.DataInputStream;
import libs.*;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;
/**
 *
 * @author abobakr
 */
public class GameHandler extends Thread implements Serializable {

    private XoDataBase database;

    private static Vector<ClientHandler> players = new Vector<ClientHandler>();

    private volatile Game gameRequest;

    private volatile boolean isGameRunning;

    private static String symbol = Game.X_MOVE;

    private Socket socket;

    private DataInputStream inStream;
    private PrintStream outStream;
    private JsonConverter convert;

    GameHandler(ClientHandler player) {

        gameRequest = new Game();
        convert = new JsonConverter();
//        

        if (symbol.equalsIgnoreCase(Game.X_MOVE)) {

            // first player will take the symbol x
            gameRequest.setNextMove(symbol);
            players.add(player);  // adding player to the list

            gameRequest.setRequest(Request.GAME_MOVE);
            gameRequest.setPlayedMove("");
            player.sendMsg(new Gson().toJson(gameRequest));
            symbol = Game.O_MOVE;

        } else {
            gameRequest.setNextMove(symbol);
            gameRequest.setPlayedMove("");
            players.add(player);

           
            gameRequest.setRequest(Request.GAME_MOVE);
            player.sendMsg(new Gson().toJson(gameRequest));
            symbol = Game.X_MOVE;

        }

        isGameRunning = true;
        gameRequest = null;
        start();
    }

    @Override
    public void run() {
//        super.run(); //To change body of generated methods, choose Tools | Templates.

        while (isGameRunning) {

            System.out.print("");
            gameRequest = ClientHandler.getGameHandlerRequest();
            if (gameRequest != null) {
                sendGameMove(convert.fromGameToJson(gameRequest).toString());
//                    resetGameRequests();
            }
        }

    }

    private void getGameRequest() {

        gameRequest = ClientHandler.getGameHandlerRequest();

    }

    public static void setGameHandlerRequest(Game newGame) {

    }

    private void resetGameRequests() {
        while (gameRequest != null) {
            ClientHandler.setGameHandlerRequest(null);
            gameRequest = null;
        }
    }

    public void disConnect() {
        this.isGameRunning = false;
        this.stop();
    }

    public void sendGameMove(String obj) {

        for (ClientHandler player : players) {
            player.sendMsg(obj);

        }
    }

}
