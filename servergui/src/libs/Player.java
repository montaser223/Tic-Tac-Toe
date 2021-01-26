/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.Serializable;
import java.util.ArrayList;
import static libs.PlayersList.players;

/**
 *
 * @author black horse
 */
public class Player implements Serializable {

    ArrayList<Player> players = new ArrayList<>();
    String username, password, firstname, lastname, status, request, respond;
    int scour;

    public Player(String _username, String _password) {
        username = _username;
        password = _password;
        request = Request.LOGIN;
        // status = Status.ONLINE;
    }

    public Player(String _username, int _scour, String _status) {
        username = _username;
        scour = _scour;
        status = _status;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Player(String _username, String _password, String _firstname, String _lastname) {
        username = _username;
        password = _password;
        firstname = _firstname;
        lastname = _lastname;
        request = Request.SIGNUP;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(String state) {
        this.status = state;
    }

    public void setScour(int scour) {
        this.scour = scour;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setRespond(String respond) {
        this.respond = respond;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getState() {
        return status;
    }

    public int getScour() {
        return scour;
    }

    public String getRequest() {
        return request;
    }

    public String getRespond() {
        return respond;
    }

    public void setPlayerList(ArrayList<Player> _players) {
        players = _players;
    }

    public ArrayList<Player> getPlayersList() {
        return players;
    }
}
