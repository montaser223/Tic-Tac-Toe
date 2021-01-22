/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import java.io.Serializable;


/**
 *
 * @author abobakr
 */

public class Player implements Serializable{
     private  String username;
     private  String password;
     private  String state;
     private  String scour;
     private  String request;
     private  String respond;
     
   
     
     

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
