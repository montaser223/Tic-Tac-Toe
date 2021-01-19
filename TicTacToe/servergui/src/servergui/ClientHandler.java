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

    DataInputStream dis;
    PrintStream ps;
    String UserName;
    private static Vector<ClientHandler> clients = new Vector<ClientHandler>();

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
        //printAllUser();
        System.out.println("52");
//        clients.forEach((t) -> {
//            System.out.println(t.UserName);
//        });
//        for(ClientHandler client: clients)
//         {
//             System.out.println("Line 55 :"+client.UserName);
//         }
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

//    static int getIndex(String _us) {
//        for (ClientHandler client : clients) {
//            System.out.println(client.UserName);
//        }
//        return 0;
//    }

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
    public void stopClientHandler() {
        this.stop();
    }

    @Override
    public void run() {
        while (true) {
            String msg = null;
            try {
                msg = dis.readLine();
                sendMsg(msg, this.UserName);

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

    static void printAllUser() {
        for (ClientHandler client : clients) {
            System.out.println(client.UserName);
        }
    }
}
