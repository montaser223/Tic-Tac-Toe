/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
//import servergui.FXMLDocumentBase;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author black horse
 */
class Server extends Thread{

    ClientHandler handler;
    ServerSocket serverSocket;
    Socket socket; 
    volatile boolean startflag;
    
    Server()
    {
        try {
            serverSocket = new ServerSocket(5005);
            startflag  = true;
        } catch (IOException ex) {
            //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            
        }
        this.start();
    }

    @Override
    public void run() {
        //super.run(); //To change body of generated methods, choose Tools | Templates.
         
            while(startflag)
            {
                try {
                    
                    socket = serverSocket.accept();
                    handler = new ClientHandler(socket);
                    // here the server should send the sockt to the login function in clientHandler class
                    // or create an object of a clinetHandler class this Constructor taking socket
                    // new ClientHandler(scoket);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    // here we should add something to the client in case the server is down
                }
            }
    
    }
    
    public void stopServer()
    {
        // stop server and close  the server socket 
        
        startflag  = false;
        try {
            serverSocket.close();
            //handler.stopClientHandler();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}


public class ServerGUI extends Application {
    
    Server s;
    Button startAndStopButton;
    boolean startFlag;

    @Override
    public void init() throws Exception {
        
        startFlag = true;
        startAndStopButton = new Button("Start");
        startAndStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                if(startFlag)
                {
                    s = new Server();
                    System.out.println("The server is runnig");
                    startAndStopButton.setText("Stop");
                    startFlag = false;
                }
                else
                {
                    s.stopServer();
                    startFlag = true;
                    startAndStopButton.setText("Start");
                }
                
                
            }
        });
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        BorderPane root = new BorderPane();
        
        root.setCenter(startAndStopButton);
        Scene scene = new Scene(root, 300, 300);
        
        stage.setScene(scene);
        stage.show();
    }

    
    @Override
    public void stop()
    {
        /* this function will handle case if user close the app from the close btn (X)
            it will close Server and thread*/
        s.stopServer();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}