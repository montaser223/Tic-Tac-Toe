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

    private String[] gamePosition;
    private String nextMove;
    private String winner;
    private String playedMove;
    private int position;
    private ArrayList<Integer>  xPosition;
    private ArrayList<Integer> oPosition;
    
    

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
    

    public void setXposition(ArrayList<Integer> xposition) {
        this.xPosition = xposition;
    }

    public void setOposition(ArrayList<Integer> oposition) {
        this.oPosition = oposition;
    }

    public ArrayList<Integer> getXposition() {
        return xPosition;
    }

    public ArrayList<Integer> getOposition() {
        return oPosition;
    }

    
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
    
}
