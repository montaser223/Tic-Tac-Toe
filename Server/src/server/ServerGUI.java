/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server.*;
import libs.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
class Server  {

    private static ClientHandler handler;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static volatile boolean startServerflag;
    private static Thread thread;
    
    public static void startServerX(){
        try {
            serverSocket = new ServerSocket(8888);
            startServerflag = true;
            System.out.println(startServerflag);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        thread = new Thread(new Runnable(){
            @Override
            public void run() {
                
                while (startServerflag) {
                        System.out.println("inside thread" + startServerflag);
                    try {

                        socket = serverSocket.accept();
                        System.out.println("Request Recived");
                        new ClientHandler(socket);
                        
                    } catch (IOException ex) {
                        System.out.println("socket closed line 60");
                        // here we should add something to the client in case the server is down
                    }
                }
                
            }
        });
        
        thread.start();
    }
    
    public static void stopServer(){
        
        System.out.println(startServerflag);
        if(startServerflag){
            
            startServerflag = false;
            
            
            try {
                serverSocket.close();
            } catch (IOException ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket.close();
            } catch (NullPointerException ex) {
                System.out.println("Server is down");
                // will send to all the online player meesge to say the serve is down

            } catch (IOException ex) {

            }
            
            thread.stop();
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

                if (startFlag) {
                    Server.startServerX();
                    System.out.println("The server is runnig");
                    startAndStopButton.setText("Stop");
                    startFlag = false;
                } else {
                    Server.stopServer();
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
    public void stop() {
        /* this function will handle case if user close the app from the close btn (X)
            it will close Server and thread*/
        Server.stopServer();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
