/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servergui;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author black horse
 */
class Server extends Thread {

    ClientHandler handler;
    ServerSocket serverSocket;
    Socket socket;
    volatile boolean startflag;

    Server() {
        try {
            serverSocket = new ServerSocket(5005);
            startflag = true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.start();
    }

    @Override
    public void run() {
        //super.run(); //To change body of generated methods, choose Tools | Templates.

        while (startflag) {
            try {

                socket = serverSocket.accept();
                System.out.println("Request Recived");
                new ClientHandler(socket);
                // here the server should send the sockt to the login function in clientHandler class
                // or create an object of a clinetHandler class this Constructor taking socket
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                // here we should add something to the client in case the server is down
            }
        }

    }

    public void stopServer() {
        // stop server and close  the server socket 
        startflag = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

public class ServerGUI extends Application {

    Server s;
    Button startAndStopButton;
    boolean startFlag;
    ImageView serverImg = new ImageView();
    AnchorPane root;

    @Override
    public void init() throws Exception {

        root = new AnchorPane();
        startAndStopButton = new Button("Start");
        serverImg.setFitHeight(540.0);
        serverImg.setFitWidth(377.0);
        serverImg.setPickOnBounds(true);
        try {
            FileInputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\ProjectImg\\server.jpg");
            Image image = new Image(stream);
            serverImg.setImage(image);

        } catch (FileNotFoundException ex) {
            System.out.println("Cann't load server background image");
        };
        startAndStopButton.setLayoutX(389.0);
        startAndStopButton.setLayoutY(192.0);
        startAndStopButton.setMnemonicParsing(false);
        startAndStopButton.setPrefHeight(53.0);
        startAndStopButton.setPrefWidth(213.0);
        startAndStopButton.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
        startAndStopButton.setText("Start");
        startAndStopButton.setTextFill(javafx.scene.paint.Color.WHITE);
        startAndStopButton.setFont(new Font("Lucida Calligraphy Italic", 18.0));
        
        startFlag = true;
        
        startAndStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (startFlag) {
                    s = new Server();
                    System.out.println("The server is runnig");
                    startAndStopButton.setLayoutX(389.0);
                    startAndStopButton.setLayoutY(192.0);
                    startAndStopButton.setMnemonicParsing(false);
                    startAndStopButton.setPrefHeight(53.0);
                    startAndStopButton.setPrefWidth(213.0);
                    startAndStopButton.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
                    startAndStopButton.setText("Start");
                    startAndStopButton.setTextFill(javafx.scene.paint.Color.WHITE);
                    startAndStopButton.setFont(new Font("Lucida Calligraphy Italic", 18.0));
                    startAndStopButton.setText("Stop");
                    startFlag = false;
                } else {
                    s.stopServer();
                    startFlag = true;
                    startAndStopButton.setText("Start");
                }

            }
        });
        root.setStyle("-fx-background-color: linear-gradient(to right, #5c258d, #4389a2);;");
        root.getChildren().add(serverImg);
        root.getChildren().add(startAndStopButton);

    }

    @Override
    public void start(Stage stage) throws Exception {

        try {
            //creating the image object
            InputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\xo.png");
            Image image = new Image(stream);
            //Creating the image view
            ImageView imageView = new ImageView();
            //Setting image to the image view
            stage.getIcons().add(image);
        } catch (FileNotFoundException ex) {

            System.out.println("Cann't load Icon image in start method");
        }

        Scene scene = new Scene(root, 642, 540);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tic Tac Toe Server");
        stage.show();
    }

    @Override
    public void stop() {
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
