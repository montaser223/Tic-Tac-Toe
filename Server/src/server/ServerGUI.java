/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import libs.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author black horse
 */
class Server {

    private  ClientHandler handler;
    private  ArrayList <ClientHandler>  user = new ArrayList <ClientHandler> ();
    private  ServerSocket serverSocket;
    private  Socket socket;
    private  volatile boolean startServerflag;
    private  Thread thread;

    public  void startServerX() {
        try {
            serverSocket = new ServerSocket(5005);
            startServerflag = true;
        } catch (IOException ex) {
            startServerflag = false;
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (startServerflag) {
                    try {

                        socket = serverSocket.accept();
                        user.add(new ClientHandler(socket));

                    } catch (IOException ex) {
                        startServerflag = false;
                    }
                }

            }
        });

        thread.start();
    }

    public void stopServer() {

        if (startServerflag) {

            startServerflag = false;
            try {
                serverSocket.close();
            } catch (IOException ex) {
            }
            try {
                socket.close();
            } catch (NullPointerException ex) {
            } catch (IOException ex) {
            }
            for(int index = 0 ; index < user.size(); index++){
                user.get(index).stopClientHandler();
            }
            user.clear();
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
    ObservableList<Player> playerData;
    TableColumn playerNameServer;
    TableColumn scoreServer;
    TableColumn statusServer;
    AnchorPane root;
    ArrayList<Player> players;
    private XoDataBase database;
    Thread timer;

    @Override
    public void init() {

        root = new AnchorPane();
        s = new Server();
        startAndStopButton = new Button("Start");
        database = new XoDataBase();
        tableView = new TableView();
        playerNameServer = new TableColumn();
        scoreServer = new TableColumn();
        statusServer = new TableColumn();

        serverImg.setFitHeight(540.0);
        serverImg.setFitWidth(259.0);
        serverImg.setPickOnBounds(true);

            Image image = new Image(getClass().getClassLoader().getResourceAsStream("server.jpeg"));
            serverImg.setImage(image);
        /**
         * *************************Button text
         * flag******************************************************************
         */

        startFlag = true;
        /**
         * ****************************Table
         * Data*********************************************************
         */
        tableView = new TableView<Player>();
        playerData = FXCollections.observableArrayList();

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
                    
                    s.startServerX();
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
                    startTimer();
                    startFlag = false;
                } else {
                    s.stopServer();
                    startFlag = true;
                    stopTimer();
                    startAndStopButton.setText("Start");
                }

            }
        });
        PlayersList.setPlayerList(database.selectplayer());
        updateScreen();
        tableView.getColumns()
                .add(playerNameServer);
        tableView.getColumns()
                .add(scoreServer);
        tableView.getColumns()
                .add(statusServer);
        root.getChildren()
                .add(tableView);
        root.setStyle(
                "-fx-background-color: linear-gradient(to right, #5c258d, #4389a2);;");
        root.getChildren()
                .add(serverImg);
        root.getChildren()
                .add(startAndStopButton);

    }

    public void startTimer() {
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        updateScreen();
                        timer.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            int updated = database.serverDown();
            if (updated != 0) {
                PlayersList.setPlayerList(database.selectplayer());
                updateScreen();
            }
            timer.stop();
        }
    }

    @Override
    public void start(Stage stage) {

            
            Image image = new Image(getClass().getClassLoader().getResourceAsStream("index.jpeg"));
            stage.getIcons().add(image);
        
        ImageView imageView = new ImageView();

        Scene scene = new Scene(root, 642, 540);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Tic Tac Toe Server");
        stage.show();
    }

    public void updateScreen() {
        updatePlayerList();
        playerData.clear();
//        playerData.addAll(players);
        for (int i=0 ; i< players.size();i++)
        {
            if (!(players.get(i).getUsername().equals("Computre")))
            {
                playerData.add(players.get(i));
            }
           
        }
        playerNameServer
                .setCellValueFactory(
                        new PropertyValueFactory<Player, String>("username")
                );
        scoreServer.setCellValueFactory(
                new PropertyValueFactory<Player, String>("scour")
        );
        statusServer.setCellValueFactory(
                new PropertyValueFactory<Player, String>("status")
        );

        tableView.setItems(playerData);
    }

    public void updatePlayerList() {
        players = PlayersList.getPlayersList();
    }

    @Override
    public void stop() {
        /* this function will handle case if user close the app from the close btn (X)
            it will close Server and thread*/
        s.stopServer();
        stopTimer();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
