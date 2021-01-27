/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author abobakr
 */
public class Game implements Serializable {
    
    public static final String X_MOVE = "X";
    public static final String O_MOVE = "O";
    public static final String DRAW = "draw";
    public static final String GAME_OVER = "gameOver";
    private String gameRequest;

    private String[] gamePosition;
    private String nextMove;
    private String winner;
    private String playedMove;
    private int position;
   

    
    

  
    public Game(){
        this.gamePosition = new String[9]; ;
        nextMove = Game.X_MOVE;
    }
    
    public Game(String[] position){
        this.gamePosition = position;
        nextMove = Game.X_MOVE;
    }
    public void setGamePosition(String[] position){
        this.gamePosition = position;
    }
    public String[] getGamePosition(){
        return gamePosition;
    }
    
    public String getNextMove() {
        return nextMove;
    }

    public void setNextMove(String nextMove) {
        this.nextMove = nextMove;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
    
    
    public String getGameRequest() {
        return gameRequest;
    }

    public void setGameRequest(String gameRequest) {
        this.gameRequest = gameRequest;
    }
    
    

    public String getPlayedMove() {
        return playedMove;
    }

    
    public void setPlayedMove(String playedMove) {
        this.playedMove = playedMove;
    }
    
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
}
