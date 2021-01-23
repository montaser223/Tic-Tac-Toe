/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import libs.*;

/**
 *
 * @author abobakr
 */
public class ServerController {

    public static void Login(Player newPlayer) {
        // will call the login function is the server and send object
        // user name and password, will send throu the stream ???
        String msg = newPlayer.getRequest();

    }
}
