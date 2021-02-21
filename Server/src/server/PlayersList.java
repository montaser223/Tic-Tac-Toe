/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import libs.*;
import java.util.ArrayList;

/**
 *
 * @author black horse
 */
public abstract class PlayersList {

    static ArrayList<Player> players = new ArrayList<>();

    public static void setPlayerList(ArrayList<Player> _players) {
        players = _players;
    }

    public static  ArrayList<Player> getPlayersList() {
        return players;
    }
}
