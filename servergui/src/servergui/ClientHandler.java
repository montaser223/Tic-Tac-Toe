/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xodatabase;
import libs.Status;
import libs.Respond;
import libs.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import xodatabase.game;

/**
 *
 * @author Eman
 


/**
 *
 * @author black horse
 */
public class ClientHandler extends Thread implements Serializable {

    private static Vector<ClientHandler> clients = new Vector<ClientHandler>();
    private ObjectInputStream readObj;
    private ObjectOutputStream writeObj;
    private XoDataBase database;
    private volatile boolean connected;
    private game play_game;
    private String [] Positions;
    

    public static HashMap<String, ClientHandler> connectedPlayers = new HashMap<String, ClientHandler>();

    ClientHandler(Socket socket) {
        try {
            database = new XoDataBase();
            writeObj = new ObjectOutputStream(socket.getOutputStream());
            readObj = new ObjectInputStream(socket.getInputStream());
            connected = true;
            start();
             

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Clinet socket is not work");
            connected = false;
        }
    }

    private void messageHandler(Player newPlayer) {
        switch (newPlayer.getRequest()) {
            case Request.LOGIN:
                login(newPlayer);
                break;
            case Request.LOGOUT:
                //logout(newPlayer);
                break;
                 case Request.GAMEINVITATION:
                 gameInvitation(newPlayer);
                break;
                 case Request.GAMERESPOND:
                gameResponse(newPlayer);
                break;
                
               //  case "playRequest":
               // playRequest();

        }
    }

    private void sendMsg(Player player) {

        try {
            this.writeObj.writeObject(player);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  /**
     * for player1
     * receive a new game request from player then check game type[old,new]
     * if there is an old game ask player1 to choose what he want to start [old,new]
     * if there is not an old game then send a new play request to player2
     */
    //game_invitation
    
    private void  gameInvitation(Player senderPlayer){
        //gui
       // newplayer2.setRequest(Request.GAMEINVITATION);
       //connectedPlayers.put("emansol", this);
      
       if((connectedPlayers.get(senderPlayer.getDestination())!=null))
         //(connectedPlayers.get(senderPlayer.getDestination()).getState().equals(Status.ONLINE)))
               {
                   //System.out.println("playe");
                    //senderPlayer.Requestgame();
                    senderPlayer.setRequest(Request.GAMEINVITATION);
                    
            connectedPlayers.get(senderPlayer.getDestination()).sendMsg(senderPlayer);
            //Player newPlay=new Player();
            //newPlay.setUsername(senderPlayer.getDestination());
            //newPlay.setRequest(Request.GAMEINVITATION);
            //sendMsg(newPlay);
            
            
             
            //sendMsg(senderPlayer);
      //thread.sleep(1000);
      
       }
       else{
            senderPlayer.setRespond(Respond.FAILURE);
            //enter play list again to chooce anther one to playe
            senderPlayer.setRespond(Request.GAMEINVITATION);
            System.out.println("no");
         
             
            sendMsg(senderPlayer);
       }
           
       // System.out.println(senderPlayer.Respondgame());
    }
             
//game invitition
//game respond
//    
    
      private void gameResponse(Player destinationPlayer){
          //gui
          //newplayer.setRequest(Request.respond_GAME);
         
         // newplayer.Requestgame();
          //System.out.println("enterrrrrrrr");
         // if(destinationPlayer.Respondgame()==JOptionPane.YES_OPTION){
              //destinationPlayer.setRespond(Respond.SUCCESS);
          //}
         // else if(destinationPlayer.Respondgame()==JOptionPane.NO_OPTION)
            //  destinationPlayer.setRespond(Respond.FAILURE);
          //else
              //destinationPlayer.setRespond(Respond.FAILURE);
          
          if(destinationPlayer.getRespond().equals(Respond.SUCCESS)){
               System.out.println("enter ah");
             //newplayer.setRespond(Respond.SUCCESS);
                 destinationPlayer.setRequest(Request.GAMERESPOND);
                  System.out.println("elgame");
                 //enter game
                 //newplayer.setRequest(Request.STARTGAME);
                 
      }
      else{
             System.out.println("not enter ");
            destinationPlayer.setRequest(Request.GAMERESPOND);
          destinationPlayer.setRespond(Respond.FAILURE);
         //newplayer.setRequest(Request.);
           
      }
           //gui get destinayion==> player obj.getusername()=> sender
            connectedPlayers.get(destinationPlayer.getUsername()).sendMsg(destinationPlayer);
      }
     private void sendNewGameRequest(Player newplayer1,Player newplayer2 ){
         //enter to new game frame 
        // play_game.game_record(Positions,newplayer1.getUsername(),newplayer2.getUsername());
                  
     
     }
      
   private void login(Player newPlayer) {
       Player newPlayerr=new Player();
     newPlayerr= database.check_username_password(newPlayer.getUsername(),newPlayer.getPassword());
        if (newPlayerr.getUsername()!=null) {
            connectedPlayers.put(newPlayer.getUsername(), this);
            newPlayer.setRespond(Respond.SUCCESS);
            newPlayer.setState(Status.ONLINE);
            
            System.out.println("login");
            sendMsg(newPlayer);

        } else {
            newPlayer.setRespond(Respond.FAILURE);
            System.out.println("not");
            sendMsg(newPlayer);
        }
    }

    private void logout(Player newPlayer) {
        Boolean isRemoved = connectedPlayers.remove(newPlayer.getUsername(), this);
        if (isRemoved) {
            try {
                newPlayer.setRespond(Respond.SUCCESS);
                newPlayer.setState(Status.OFFLINE);
                sendMsg(newPlayer);
                connected = false;
                readObj.close();
                writeObj.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
        }
    }

    private void sginUp(Player newPlayer) {
        
        int isExist = database.check_username(newPlayer.getUsername());
        
        if(isExist == 1){
            newPlayer.setRespond(Respond.FAILURE);
            sendMsg(newPlayer);
            
        }else{
           // database.sign_up(newPlayer.getFirstname, newPlayer.getLastname(), newPlayer.getUsername(),newPlayer.getPassword());
            newPlayer.setRespond(Respond.SUCCESS);
            sendMsg(newPlayer); 
        }
        
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Player p2 = (Player) readObj.readObject();
                messageHandler(p2);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}