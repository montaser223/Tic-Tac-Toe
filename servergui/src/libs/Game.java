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
public class Game  implements Serializable {
    
    public static final String X_MOVE = "X";
    public static final String O_MOVE = "O";
    public static final String DRAW = "draw";
    public static final String GAME_OVER = "gameOver";
    

    private String[] recordedgamePosition;
    private String nextMove;
    private String winner;
    private String playedMove;
    private Long position;
    private String request;
    private String message;
    
   

    
    

  
    public Game(){}
    
    public Game(String[] position){
        this.recordedgamePosition = position;
    }
    public void setRecordedGamePosition(String[] position){
        this.recordedgamePosition = position;
    }
    public String[] getRecordedGamePosition(){
        return recordedgamePosition;
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
    
    
    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
    
    

    public String getPlayedMove() {
        return playedMove;
    }

    
    public void setPlayedMove(String playedMove) {
        this.playedMove = playedMove;
    }
    
    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
    
    
}
