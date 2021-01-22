/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xopro1;

import java.io.Serializable;


/**
 *
 * @author black horse
 */
public class Player implements Serializable{

    String username, password, firstname, lastname, state, scour, request, respond;

    Player(String _username, String _password) {
        username = _username;
        password = _password;
        request = Request.LOGIN;
    }

    Player(String _username, String _password, String _firstname, String _lastname) {
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
        this.state = state;
    }

    public void setScour(String scour) {
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
        return state;
    }

    public String getScour() {
        return scour;
    }

    public String getRequest() {
        return request;
    }

    public String getRespond() {
        return respond;
    }

}
