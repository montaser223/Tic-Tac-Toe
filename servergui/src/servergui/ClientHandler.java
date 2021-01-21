/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author black horse
 */
public class ClientHandler extends Thread {

    static boolean startFlag;
    DataInputStream dis;
    PrintStream ps;
    String UserName;
    static Vector<ClientHandler> clients = new Vector<ClientHandler>();

    ClientHandler(Socket socket) {
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            String msg = dis.readLine();
            login(this, msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void login(ClientHandler client, String _UserName) {
        client.UserName = _UserName;
        clients.add(client);
        start();
        printAllUser();
    }

    public static void logout(String _userName) {
        System.out.println("47");
        printAllUser();
        System.out.println(clients);
        System.out.println("49");
        System.out.println(clients.indexOf(_userName));;
        for (ClientHandler client : clients) {
            System.out.println("Line 55 :" + client.UserName);
        }
        System.out.println("55");
        //        clients.forEach((t) -> {
//            System.out.println(t.UserName);
//        });
//
//        int len = clients.capacity();
//        System.out.println(len);
//        System.out.println("57");
//        System.out.println(_userName);
//        int i = getIndex(_userName);
//        System.out.println("Line 59 " + i);
        //clients.remove(i);
//       clients.re
//       ClientHandler c = clients.get(getIndex(_userName));
//       c.dis.close();
//       c.ps.close();
//       c.stop();
    }

    void logOut(String _un) {
        int index = getIndex(_un);
        if (index > -1) {
            try {
                ClientHandler client = clients.elementAt(index);
                System.out.println(client);
                clients.removeElementAt(clients.indexOf(client));
                client.dis.close();
                client.ps.close();
                client.stop();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Cannot remove client at index 0");
        }
    }

    int getIndex(String _un) {
        int index = -1;
        for (ClientHandler client : clients) {
            if (client.UserName == _un) {
                index = clients.indexOf(client);
            }
        }
        return index;
    }

//    static int getIndex(String _userName)
//    {
//        System.out.println("getIndex : "+_userName);
//        int index =0 ;
//        for(ClientHandler client:clients)
//        {
//            System.out.println(client.UserName+" Line 64 "+_userName);
////            if(client.UserName == _userName)
////            {
//                System.out.println("Line 65"+clients.indexOf(client));
//                //clients.
//                index = clients.indexOf(client);
//            //}
//        }
//        return index;
//    }
    //@Override
    public static void stopClientHandler() {
//        this.stop();
        startFlag = false;
        for (ClientHandler client : clients) {
            logout(client.UserName);
        }
    }

    @Override
    public void run() {
        startFlag= true;
        while (startFlag) {
            String msg = null;
            try {
                msg = dis.readLine();
                if (msg.equals("logout")) {
                    logOut(this.UserName);
                }
                //getIndex(this.UserName, msg);

                // logOut / request 
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void sendMsg(String msg, String SenderName) {
        for (ClientHandler ml : clients) {
            ml.ps.println(SenderName + " Says " + msg);
        }
    }

    public static void printAllUser() {
        for (ClientHandler client : clients) {
            System.out.println(client.UserName);
        }
    }
}
