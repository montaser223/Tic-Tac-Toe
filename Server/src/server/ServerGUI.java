/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server.*;
import libs.*;
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

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
            System.out.println("Server");
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
                        System.out.println("socket closed");
                        // here we should add something to the client in case the server is down
                    }
                }
                
            }
        });
        
        thread.start();
    }
    
    public static void stopServer(){
        
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
    ImageView serverImg = new ImageView();
    TableView tableView;
    TableColumn playerNameServer;
    TableColumn scoreServer;
    TableColumn statusServer;
    AnchorPane root;

    @Override
    public void init(){

        root = new AnchorPane();
        
        startAndStopButton = new Button("Start");
        
        tableView = new TableView();
        playerNameServer = new TableColumn();
        scoreServer = new TableColumn();
        statusServer = new TableColumn();
        
        serverImg.setFitHeight(540.0);
        serverImg.setFitWidth(259.0);
        serverImg.setPickOnBounds(true);
        try {
            FileInputStream stream = new FileInputStream("ProjectImg/server.jpeg");
            Image image = new Image(stream);
            serverImg.setImage(image);

        } catch (FileNotFoundException ex) {
            System.out.println("Cann't load server background image");
        };
        /***************************Button text flag*******************************************************************/
        
        startFlag = true;
        /******************************Table Data**********************************************************/
        TableView<Player> tableView = new TableView<Player>();
        ObservableList<Player> playerData = FXCollections.observableArrayList();
        
        
          playerNameServer.setCellValueFactory(new PropertyValueFactory<Player, String>("username")
        );
        scoreServer.setCellValueFactory(
                new PropertyValueFactory<Player, String>("scour")
        );
        statusServer.setCellValueFactory(
                new PropertyValueFactory<Player, String>("state")
        );

        tableView.setItems(playerData);

        playerNameServer = new TableColumn();
        scoreServer = new TableColumn();
        statusServer = new TableColumn();
        tableView.setLayoutX(273.0);
        tableView.setLayoutY(18.0);
        tableView.setPrefHeight(375.0);
        tableView.setPrefWidth(356.0);

        playerNameServer.setPrefWidth(152.0);
        playerNameServer.setText("Player Name");

        scoreServer.setPrefWidth(95.0);
        scoreServer.setText("Score");

        statusServer.setPrefWidth(108.0);
        statusServer.setText("Status");
        
        startAndStopButton.setLayoutX(357.0);
        startAndStopButton.setLayoutY(450.0);
        startAndStopButton.setMnemonicParsing(false);
        startAndStopButton.setPrefHeight(53.0);
        startAndStopButton.setPrefWidth(213.0);
        startAndStopButton.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
        startAndStopButton.setText("Start");
        startAndStopButton.setTextFill(javafx.scene.paint.Color.WHITE);
        startAndStopButton.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        startAndStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (startFlag) {
                    Server.startServerX();
                    System.out.println("The server is runnig");
                    startAndStopButton.setLayoutX(357.0);
                    startAndStopButton.setLayoutY(450.0);
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
                    Server.stopServer();
                    startFlag = true;
                    startAndStopButton.setText("Start");
                }

            }
        });
        
        tableView.getColumns().add(playerNameServer);
        tableView.getColumns().add(scoreServer);
        tableView.getColumns().add(statusServer);
        root.getChildren().add(tableView);
        root.setStyle("-fx-background-color: linear-gradient(to right, #5c258d, #4389a2);;");
        root.getChildren().add(serverImg);
        root.getChildren().add(startAndStopButton);

    }

    @Override
    public void start(Stage stage){

        try {
            //creating the image object

            Image image = new Image(new FileInputStream("ProjectImg/index.jpeg"));
        } catch (FileNotFoundException ex) {
                System.out.println("can't load icon img");
        }
        //Creating the image view
        ImageView imageView = new ImageView();
        //Setting image to the image view
//        stage.getIcons().add(image);

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
        Server.stopServer();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}