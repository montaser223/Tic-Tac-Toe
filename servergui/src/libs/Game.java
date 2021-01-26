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
    private String[] position;
    private boolean gameFlag;
    private String symbol;
    private String winner;
    private ArrayList<Integer>  x;
    private ArrayList<Integer> o;
    private String xAndo;
    private int location;

    public String getxAndo() {
        return xAndo;
    }

    public int getLocation() {
        return location;
    }

    public void setxAndo(String xAndo) {
        this.xAndo = xAndo;
    }

    public void setLocation(int location) {
        this.location = location;
    }
    

    public void setX(ArrayList<Integer> x) {
        this.x = x;
    }

    public void setO(ArrayList<Integer> o) {
        this.o = o;
    }

    public ArrayList<Integer> getX() {
        return x;
    }

    public ArrayList<Integer> getO() {
        return o;
    }

    
    public Game(){
        this.position = new String[9]; ;
        gameFlag = true;
        symbol = "X";
    }
    
    public Game(String[] position){
        this.position = position;
        gameFlag = true;
        symbol = "X";
    }
    public void setGamePosition(String[] position){
        this.position = position;
    }
    public String[] getGamePosition(){
        return position;
    }
    public void setGameFlag(boolean gameFlag){
        this.gameFlag = gameFlag;
    }
    public boolean getGameFlag(){
        return gameFlag;
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
    
}
