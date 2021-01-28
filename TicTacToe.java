/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import libs.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author hebaa
 */
public class TicTacToe extends Application implements Serializable {
    /************************************socket***********************************************************************/
    Socket socket;
    ObjectInputStream readObj;
    ObjectOutputStream writeObj;
    Thread thread;
    /****************************Player class******************************************************************************/
    Player p;
    /******************************Single Mode Game**********************************************************************************/
    int count = 0 ;
    SingleModeGame single ;
    /*************************************Alert**************************************************************************/
    Alert alertEmptyLogIn1 = new Alert(Alert.AlertType.ERROR);
    Alert alertWrongLogIn1 = new Alert(Alert.AlertType.ERROR);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert alertUserExists = new Alert(Alert.AlertType.ERROR);
    Alert alertEmptySignUp1 = new Alert(Alert.AlertType.ERROR);
    Alert alertWrongLogout1;
    Alert unSelected4 = new Alert(Alert.AlertType.ERROR);
    /*********************************AnchorPane Screens***********************************************************************************/
    AnchorPane ScreenOne;
    AnchorPane ScreenTwo;
    AnchorPane ScreenThree;
    AnchorPane ScreenFour;
    AnchorPane ScreenSingleMode;
    /***********************************Screen one variables**********************************************************************/
    public ImageView LoginImg;
    public Label LoginLabel;
    public Label usrName1;
    public TextField userText1;
    public Label pass1;
    public TextField passText1;
    public Button logInBtn1;
    public Button signupBtn1;
    String userName3;
    Text usernameDislay3 = new Text();
    ImageView imageView;
    InputStream stream;
    /***************************************Screen two variables********************************************************************/
    public GridPane GridOfImageAndForm;
    public ColumnConstraints columnConstraints;
    public ColumnConstraints columnConstraints0;
    public RowConstraints rowConstraints;
    public ImageView SignUpImg;
    public AnchorPane SignUpFormPanel;
    public Label FirstName2;
    public TextField userText2;
    public Label usrName2;
    public Label label;
    public TextField FirstText2;
    public Label LastName2;
    public TextField LastText2;
    public Label pass2;
    public PasswordField passText2;
    public Button signupBtn2;
    public Button backBtn2;
    /*****************************************Screen three variables*******************************************************************/
    public GridPane GridOfPlay;
    public ColumnConstraints tableColumnConstraints;
    public RowConstraints tableRowConstraints;
    public AnchorPane PlayPanel;
    public ImageView PlayImg;
    public Label PlayLable;
    public Button singleBtn3;
    public Button multiBtn3;
    public Button logOutBtn3;
    /*******************************************Screen four variables*****************************************************************************/
    ImageView TableImg;
    TableView PlayerTable;
    TableColumn PlayerName;
    TableColumn Score;
    TableColumn Status;
    Label TableLabel;
    Button playBtn4;
    Button backBtn4;
    /***********************************************ScreenSingleMode******************************************************************/
     ImageView GameImg;
     Button exit;
     Button recordGame;
     Button bt1;
     Button bt2;
     Button bt3;
     Button bt4;
     Button bt5;
     Button bt6;
     Button bt7;
     Button bt8;
     Button bt9;
     Label playerScore;
     Label computerScore;
     
     @Override
   public void init() {
         p=new Player("","");
         alertWrongLogIn1.setTitle("LogIn ");
         alertWrongLogIn1.setHeaderText(null);
         alertWrongLogIn1.setContentText("Invalid User Name or Password");
         
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    socket = new Socket("127.0.0.1", 5005);
                    writeObj = new ObjectOutputStream(socket.getOutputStream());
                    readObj = new ObjectInputStream(socket.getInputStream());                      
                } 
                catch (IOException ex)
                {
                    System.out.println(ex.getMessage());
                }
                System.out.println("hoooo");
                while (true) {
                    try
                    {
                        p = (Player) readObj.readObject();
                        System.out.println("Line 196: " + p.getRespond());
                        messageHandelr(p);
                        System.out.println("Line 98: " + p.getRespond());
                        System.out.println("Line 196: " + p.getUsername() + p.getPassword());
                    } 
                    catch (IOException ex)
                    {
                         System.out.println("Line 100: " + p.getRespond());
                        System.out.println(ex.getMessage());
                    } 
                    catch (ClassNotFoundException ex)
                    {
                         System.out.println("Line 106: " + p.getRespond());
                        System.out.println(ex.getMessage());
                    }
                    catch (NullPointerException ex)
                    {
                        System.out.println("Line 111: " + p.getRespond());
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        thread.start();

    }
    /************************************Screens methods********************************************************************************/
        public AnchorPane ScreenOne()
    {

        LoginImg = new ImageView();
        LoginLabel = new Label();
        usrName1 = new Label();
        userText1 = new TextField();
        pass1 = new Label();
        passText1 = new PasswordField();
        logInBtn1 = new Button();
        signupBtn1 = new Button();

        ScreenOne.setMaxHeight(USE_PREF_SIZE);
        ScreenOne.setMaxWidth(USE_PREF_SIZE);
        ScreenOne.setMinHeight(USE_PREF_SIZE);
        ScreenOne.setMinWidth(USE_PREF_SIZE);
        ScreenOne.setPrefHeight(540.0);
        ScreenOne.setPrefWidth(642.0);
        ScreenOne.setStyle("-fx-background-color: linear-gradient(to right, #5c258d, #4389a2);;");

        LoginImg.setFitHeight(540.0);
        LoginImg.setFitWidth(307.0);
        LoginImg.setPickOnBounds(true);

        try {
            FileInputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\login.jpg");
            Image image = new Image(stream);
            LoginImg.setImage(image);
            
        } catch (FileNotFoundException ex) {
           
            System.out.println("Faild to load login image");
        }

        LoginLabel.setAlignment(javafx.geometry.Pos.CENTER);
        LoginLabel.setLayoutX(387.0);
        LoginLabel.setLayoutY(78.0);
        LoginLabel.setPrefHeight(17.0);
        LoginLabel.setPrefWidth(204.0);
        LoginLabel.setText("Login");
        LoginLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        LoginLabel.setFont(new Font("Javanese Text", 24.0));

        usrName1.setLayoutX(315.0);
        usrName1.setLayoutY(164.0);
        usrName1.setPrefHeight(51.0);
        usrName1.setPrefWidth(77.0);
        usrName1.setText("User Name");
        usrName1.setTextFill(javafx.scene.paint.Color.valueOf("#fffefe"));
        usrName1.setFont(new Font("Javanese Text", 15.0));

        userText1.setLayoutX(411.0);
        userText1.setLayoutY(177.0);
        userText1.setPrefHeight(34.0);
        userText1.setPrefWidth(213.0);

        pass1.setLayoutX(315.0);
        pass1.setLayoutY(256.0);
        pass1.setPrefHeight(51.0);
        pass1.setPrefWidth(77.0);
        pass1.setText("Password");
        pass1.setTextFill(javafx.scene.paint.Color.valueOf("#fffefe"));
        pass1.setFont(new Font("Javanese Text", 15.0));

        passText1.setAccessibleRole(javafx.scene.AccessibleRole.PASSWORD_FIELD);
        passText1.setLayoutX(411.0);
        passText1.setLayoutY(264.0);
        passText1.setPrefHeight(34.0);
        passText1.setPrefWidth(213.0);

        logInBtn1.setLayoutX(411.0);
        logInBtn1.setLayoutY(361.0);
        logInBtn1.setMnemonicParsing(false);
        logInBtn1.setPrefHeight(53.0);
        logInBtn1.setPrefWidth(213.0);
        logInBtn1.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
        logInBtn1.setText("Login");
        logInBtn1.setTextFill(javafx.scene.paint.Color.WHITE);
        logInBtn1.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        signupBtn1.setLayoutX(410.0);
        signupBtn1.setLayoutY(432.0);
        signupBtn1.setMnemonicParsing(false);
        signupBtn1.setPrefHeight(53.0);
        signupBtn1.setPrefWidth(213.0);
        signupBtn1.setText("Sign up");
        signupBtn1.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        ScreenOne.getChildren().add(LoginImg);
        ScreenOne.getChildren().add(LoginLabel);
        ScreenOne.getChildren().add(usrName1);
        ScreenOne.getChildren().add(userText1);
        ScreenOne.getChildren().add(pass1);
        ScreenOne.getChildren().add(passText1);
        ScreenOne.getChildren().add(logInBtn1);
        ScreenOne.getChildren().add(signupBtn1);
        
        userText1.clear();
        passText1.clear();
        alertEmptyLogIn1.setTitle("sign in ");
        alertEmptyLogIn1.setHeaderText(null);
        alertEmptyLogIn1.setContentText("All Fields are Required");
        
        /*************Screen One Button Action********************/
        signupBtn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                ScreenTwo().getChildren().clear();
                
                signupBtn1.getScene().setRoot(ScreenTwo());

            }

        });

        logInBtn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (userText1.getText().isEmpty() || passText1.getText().isEmpty())
                {
                    alertEmptyLogIn1.showAndWait();
                } 
                else
                {
                    Player newPlayer = new Player(userText1.getText(), passText1.getText());
                    SingleModeGame.setPlayerName( userText1.getText() );
                    usernameDislay3.setText(userName3);
                    single = new SingleModeGame () ;
                    getusername() ;
                    
                    try
                    {
                        writeObj.writeObject(newPlayer);
                        System.out.println("object sent!");
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                }

            }
        });
        
        return ScreenOne;
          
    }
        
    public String getusername()
   {
       System.out.println(userName3);
       return  userText1.getText()  ;
       
       
   }
    
     public AnchorPane ScreenTwo()
    {
        GridOfImageAndForm = new GridPane();
        columnConstraints = new ColumnConstraints();
        columnConstraints0 = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        SignUpImg = new ImageView();
        SignUpFormPanel = new AnchorPane();
        FirstName2 = new Label();
        userText2 = new TextField();
        usrName2 = new Label();
        label = new Label();
        FirstText2 = new TextField();
        LastName2 = new Label();
        LastText2 = new TextField();
        pass2 = new Label();
        passText2 = new PasswordField();
        signupBtn2 = new Button();
        backBtn2 = new Button();

        ScreenTwo.setMaxHeight(USE_PREF_SIZE);
        ScreenTwo.setMaxWidth(USE_PREF_SIZE);
        ScreenTwo.setMinHeight(USE_PREF_SIZE);
        ScreenTwo.setMinWidth(USE_PREF_SIZE);
        ScreenTwo.setPrefHeight(540.0);
        ScreenTwo.setPrefWidth(642.0);

        GridOfImageAndForm.setPrefHeight(540.0);
        GridOfImageAndForm.setPrefWidth(642.0);
        GridOfImageAndForm.setStyle("-fx-background-color: linear-gradient(to right, #5c258d, #4389a2);;");

        columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(100.0);

        columnConstraints0.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints0.setMinWidth(10.0);
        columnConstraints0.setPrefWidth(100.0);

        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        SignUpImg.setFitHeight(538.0);
        SignUpImg.setFitWidth(321.0);
        SignUpImg.setPickOnBounds(true);
        try {
            FileInputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\registration.jpg");
            Image image = new Image(stream);
            SignUpImg.setImage(image);
            
        } catch (FileNotFoundException ex) {
            System.out.println("Faild load signup image");
        }
        GridPane.setColumnIndex(SignUpFormPanel, 1);
        SignUpFormPanel.setPrefHeight(200.0);
        SignUpFormPanel.setPrefWidth(200.0);

        FirstName2.setLayoutX(4.0);
        FirstName2.setLayoutY(181.0);
        FirstName2.setPrefHeight(51.0);
        FirstName2.setPrefWidth(77.0);
        FirstName2.setText("First Name");
        FirstName2.setTextFill(javafx.scene.paint.Color.valueOf("#fffefe"));
        FirstName2.setFont(new Font("Javanese Text", 15.0));

        userText2.setLayoutX(94.0);
        userText2.setLayoutY(127.0);
        userText2.setPrefHeight(34.0);
        userText2.setPrefWidth(213.0);

        usrName2.setLayoutX(4.0);
        usrName2.setLayoutY(118.0);
        usrName2.setPrefHeight(51.0);
        usrName2.setPrefWidth(77.0);
        usrName2.setText("User Name");
        usrName2.setTextFill(javafx.scene.paint.Color.valueOf("#fffefe"));
        usrName2.setFont(new Font("Javanese Text", 15.0));

        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setLayoutX(23.0);
        label.setLayoutY(46.0);
        label.setPrefHeight(51.0);
        label.setPrefWidth(287.0);
        label.setText("SIGN UP");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new Font("Javanese Text", 24.0));

        FirstText2.setLayoutX(94.0);
        FirstText2.setLayoutY(190.0);
        FirstText2.setPrefHeight(34.0);
        FirstText2.setPrefWidth(213.0);

        LastName2.setLayoutX(4.0);
        LastName2.setLayoutY(245.0);
        LastName2.setPrefHeight(51.0);
        LastName2.setPrefWidth(77.0);
        LastName2.setText("Last Name");
        LastName2.setTextFill(javafx.scene.paint.Color.valueOf("#fffefe"));
        LastName2.setFont(new Font("Javanese Text", 15.0));

        LastText2.setLayoutX(94.0);
        LastText2.setLayoutY(254.0);
        LastText2.setPrefHeight(34.0);
        LastText2.setPrefWidth(213.0);

        pass2.setLayoutX(4.0);
        pass2.setLayoutY(307.0);
        pass2.setPrefHeight(51.0);
        pass2.setPrefWidth(77.0);
        pass2.setText("Password");
        pass2.setTextFill(javafx.scene.paint.Color.valueOf("#fffefe"));
        pass2.setFont(new Font("Javanese Text", 15.0));

        passText2.setLayoutX(92.0);
        passText2.setLayoutY(311.0);
        passText2.setPrefHeight(34.0);
        passText2.setPrefWidth(213.0);

        signupBtn2.setLayoutX(92.0);
        signupBtn2.setLayoutY(383.0);
        signupBtn2.setMnemonicParsing(false);
        signupBtn2.setPrefHeight(53.0);
        signupBtn2.setPrefWidth(173.0);
        signupBtn2.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
        signupBtn2.setText("sign up");
        signupBtn2.setTextFill(javafx.scene.paint.Color.WHITE);
        signupBtn2.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        backBtn2.setLayoutX(94.0);
        backBtn2.setLayoutY(452.0);
        backBtn2.setMnemonicParsing(false);
        backBtn2.setPrefHeight(53.0);
        backBtn2.setPrefWidth(173.0);
        backBtn2.setText("Back");
        backBtn2.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        GridOfImageAndForm.getColumnConstraints().add(columnConstraints);
        GridOfImageAndForm.getColumnConstraints().add(columnConstraints0);
        GridOfImageAndForm.getRowConstraints().add(rowConstraints);
        GridOfImageAndForm.getChildren().add(SignUpImg);
        SignUpFormPanel.getChildren().add(FirstName2);
        SignUpFormPanel.getChildren().add(userText2);
        SignUpFormPanel.getChildren().add(usrName2);
        SignUpFormPanel.getChildren().add(label);
        SignUpFormPanel.getChildren().add(FirstText2);
        SignUpFormPanel.getChildren().add(LastName2);
        SignUpFormPanel.getChildren().add(LastText2);
        SignUpFormPanel.getChildren().add(pass2);
        SignUpFormPanel.getChildren().add(passText2);
        SignUpFormPanel.getChildren().add(signupBtn2);
        SignUpFormPanel.getChildren().add(backBtn2);
        GridOfImageAndForm.getChildren().add(SignUpFormPanel);
        ScreenTwo.getChildren().add(GridOfImageAndForm);
        
        userText2.clear();
        FirstText2.clear();
        LastText2.clear();
        passText2.clear();
        alert.setTitle("SignUp");
        alert.setHeaderText(null);
        alert.setContentText("Your data saved successfully");

        alertEmptySignUp1.setTitle("signUp ");
        alertEmptySignUp1.setHeaderText(null);
        alertEmptySignUp1.setContentText("All fields are required");

        alertUserExists.setTitle("signUp ");
        alertUserExists.setHeaderText(null);
        alertUserExists.setContentText("User already exist choose another uername");
        
        signupBtn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (userText2.getText().isEmpty() || FirstText2.getText().isEmpty() || LastText2.getText().isEmpty() || passText2.getText().isEmpty()) {

                    alertEmptySignUp1.showAndWait();
                } 
                else
                {
                    
                    Player signUpPlayer = new Player(userText2.getText(),passText2.getText(), FirstText2.getText(), LastText2.getText());
                    
                    try
                    {
                        writeObj.writeObject(signUpPlayer);
                        System.out.println("New Player has sign up!");
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.getMessage());
                    }

                }

            }
        });
        backBtn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ScreenOne().getChildren().clear();
                backBtn2.getScene().setRoot(ScreenOne());
            }

        });
        return ScreenTwo;
    }
     
     public AnchorPane ScreenThree()
    {
        GridOfPlay = new GridPane();
        tableColumnConstraints = new ColumnConstraints();
        tableRowConstraints = new RowConstraints();
        PlayPanel = new AnchorPane();
        PlayImg = new ImageView();
        PlayLable = new Label();
        singleBtn3 = new Button();
        multiBtn3 = new Button();
        logOutBtn3 = new Button();

        ScreenThree.setMaxHeight(USE_PREF_SIZE);
        ScreenThree.setMaxWidth(USE_PREF_SIZE);
        ScreenThree.setMinHeight(USE_PREF_SIZE);
        ScreenThree.setMinWidth(USE_PREF_SIZE);
        ScreenThree.setPrefHeight(540.0);
        ScreenThree.setPrefWidth(642.0);

        GridOfPlay.setPrefHeight(540.0);
        GridOfPlay.setPrefWidth(642.0);

        tableColumnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        tableColumnConstraints.setMinWidth(10.0);
        tableColumnConstraints.setPrefWidth(100.0);

        tableRowConstraints.setMinHeight(10.0);
        tableRowConstraints.setPrefHeight(30.0);
        tableRowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        PlayPanel.setPrefHeight(200.0);
        PlayPanel.setPrefWidth(200.0);
        PlayPanel.setStyle("-fx-background-image: <?xml version='1.0' encoding='UTF-8'?><?import javafx.scene.control.Button?><?import javafx.scene.text.Font?><Button fx:id='signupBtn2' layoutX='226.0' layoutY='153.0' mnemonicParsing='false' prefHeight='53.0' prefWidth='173.0' style='-fx-background-color: linear-gradient(to right, #283048, #859398);;' text='Sign UP' textFill='WHITE' xmlns='http://javafx.com/javafx/8.0.171' xmlns:fx='http://javafx.com/fxml/1'>   <font>      <Font name='Lucida Calligraphy Italic' size='18.0' />   </font></Button>;");

        PlayImg.setFitHeight(540.0);
        PlayImg.setFitWidth(642.0);
        PlayImg.setPickOnBounds(true);
        try {
            FileInputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\rocket.jpg");
            Image image = new Image(stream);
            PlayImg.setImage(image);
            
        } catch (FileNotFoundException ex) {
            System.out.println("tictactoe.TicTacToe.ScreenThree()");
        }

        PlayLable.setAlignment(javafx.geometry.Pos.CENTER);
        PlayLable.setLayoutX(145.0);
        PlayLable.setLayoutY(48.0);
        PlayLable.setPrefHeight(67.0);
        PlayLable.setPrefWidth(339.0);
        PlayLable.setText("Let's Play");
        PlayLable.setTextFill(javafx.scene.paint.Color.WHITE);
        PlayLable.setFont(new Font("Javanese Text", 36.0));

        singleBtn3.setLayoutX(226.0);
        singleBtn3.setLayoutY(153.0);
        singleBtn3.setMnemonicParsing(false);
        singleBtn3.setPrefHeight(53.0);
        singleBtn3.setPrefWidth(173.0);
        singleBtn3.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
        singleBtn3.setText("Signle Mode");
        singleBtn3.setTextFill(javafx.scene.paint.Color.WHITE);
        singleBtn3.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        multiBtn3.setLayoutX(226.0);
        multiBtn3.setLayoutY(244.0);
        multiBtn3.setMnemonicParsing(false);
        multiBtn3.setPrefHeight(53.0);
        multiBtn3.setPrefWidth(173.0);
        multiBtn3.setText("Multi Mode");
        multiBtn3.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        logOutBtn3.setLayoutX(226.0);
        logOutBtn3.setLayoutY(346.0);
        logOutBtn3.setMnemonicParsing(false);
        logOutBtn3.setPrefHeight(53.0);
        logOutBtn3.setPrefWidth(173.0);
        logOutBtn3.setStyle("-fx-background-color: linear-gradient(to right, #283048, #859398);;");
        logOutBtn3.setText("Logout");
        logOutBtn3.setTextFill(javafx.scene.paint.Color.WHITE);
        logOutBtn3.setFont(new Font("Lucida Calligraphy Italic", 18.0));

        GridOfPlay.getColumnConstraints().add(tableColumnConstraints);
        GridOfPlay.getRowConstraints().add(tableRowConstraints);
        PlayPanel.getChildren().add(PlayImg);
        PlayPanel.getChildren().add(PlayLable);
        PlayPanel.getChildren().add(singleBtn3);
        PlayPanel.getChildren().add(multiBtn3);
        PlayPanel.getChildren().add(logOutBtn3);
        GridOfPlay.getChildren().add(PlayPanel);
        ScreenThree.getChildren().add(GridOfPlay);
        
        logOutBtn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                Player logOutPlayer = new Player(userText1.getText(), passText1.getText());
                logOutPlayer.setRequest(Request.LOGOUT);
                System.out.println(logOutPlayer.getRequest());
                    try
                    {
                        writeObj.writeObject(logOutPlayer);
                        System.out.println("User send logout Request!");
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.getMessage());
                    }
            }

        });
        
        multiBtn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ScreenFour().getChildren().clear();
                multiBtn3.getScene().setRoot(ScreenFour());

            }

        });
        
        singleBtn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                SingleModeGame singleNew = new  SingleModeGame();
                singleBtn3.getScene().setRoot(singleNew.SingleMode());
                
            }

        });
        
        return ScreenThree;
    }
     
     public AnchorPane ScreenFour()
     {
        TableImg = new ImageView();
//        PlayerTable = new TableView();
        TableView<Player> PlayerTable = new TableView<Player>();
        ObservableList<Player> playerData = FXCollections.observableArrayList(
        new Player("MOHAMED" , "ON","13"),
        new Player("ALI","OFF","25"),
        new Player("HOSSAM","ON","42"),
        new Player("gala","OFF","42"),
        new Player("heba","ON","34")
    );
        PlayerName = new TableColumn();
        Score = new TableColumn();
        Status = new TableColumn();
        TableLabel = new Label();
        playBtn4 = new Button();
        backBtn4 = new Button();
        ScreenFour.setMaxHeight(USE_PREF_SIZE);
        ScreenFour.setMaxWidth(USE_PREF_SIZE);
        ScreenFour.setMinHeight(USE_PREF_SIZE);
        ScreenFour.setMinWidth(USE_PREF_SIZE);
        ScreenFour.setPrefHeight(540.0);
        ScreenFour.setPrefWidth(642.0);
//        ScreenFour.setStyle("-fx-background-color: linear-gradient(to right, #0f0c29, #302b63, #24243e);");

        TableImg.setFitHeight(540.0);
        TableImg.setFitWidth(642.0);
        TableImg.setPickOnBounds(true);
//        TableImg.setImage(new Image(getClass().getResource("../../../Project/Img/image.jpg").toExternalForm()));
        try {
            FileInputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\image.jpg");
            Image image = new Image(stream);
            TableImg.setImage(image);
            
        } catch (FileNotFoundException ex) {
//            Logger.getLogger(FXMLScreenOne.class.getName()).log(Level.SEVERE, null, ex);
        }

        PlayerTable.setLayoutX(89.0);
        PlayerTable.setLayoutY(109.0);
        PlayerTable.setPrefHeight(287.0);
        PlayerTable.setPrefWidth(480.0);

        PlayerName.setPrefWidth(178.0);
        PlayerName.setText("Player Name");

        Score.setPrefWidth(132.0);
        Score.setText("Score");

        Status.setPrefWidth(169.0);
        Status.setText("Status");

        TableLabel.setAlignment(javafx.geometry.Pos.CENTER);
        TableLabel.setLayoutX(167.0);
        TableLabel.setLayoutY(28.0);
        TableLabel.setPrefHeight(46.0);
        TableLabel.setPrefWidth(309.0);
        TableLabel.setText("Player List");
        TableLabel.setTextFill(javafx.scene.paint.Color.valueOf("#fffbfb"));
        TableLabel.setFont(new Font("Javanese Text", 24.0));
        playBtn4.setAlignment(javafx.geometry.Pos.CENTER);
        playBtn4.setLayoutX(379.0);
        playBtn4.setLayoutY(456.0);
        playBtn4.setMnemonicParsing(false);
        playBtn4.setPrefHeight(44.0);
        playBtn4.setPrefWidth(173.0);
        playBtn4.setStyle("-fx-background-color: linear-gradient(to right, #000428, #004e92);;");
        playBtn4.setText("Play");
        playBtn4.setTextFill(javafx.scene.paint.Color.valueOf("#fffdfd"));
        playBtn4.setFont(new Font("Lucida Calligraphy Italic", 20.0));

        backBtn4.setAlignment(javafx.geometry.Pos.CENTER);
        backBtn4.setLayoutX(98.0);
        backBtn4.setLayoutY(456.0);
        backBtn4.setMnemonicParsing(false);
        backBtn4.setPrefHeight(44.0);
        backBtn4.setPrefWidth(173.0);
        backBtn4.setStyle("-fx-background-color: linear-gradient(to right, #000428, #004e92);;");
        backBtn4.setText("Back");
        backBtn4.setTextFill(javafx.scene.paint.Color.valueOf("#fffdfd"));
        backBtn4.setFont(new Font("Lucida Calligraphy Italic", 20.0));

        ScreenFour.getChildren().add(TableImg);
        PlayerTable.getColumns().add(PlayerName);
        PlayerTable.getColumns().add(Score);
        PlayerTable.getColumns().add(Status);
        ScreenFour.getChildren().add(PlayerTable);
        ScreenFour.getChildren().add(TableLabel);
        ScreenFour.getChildren().add(playBtn4);
        ScreenFour.getChildren().add(backBtn4);
        PlayerName.setCellValueFactory(
        new PropertyValueFactory<Player,String>("username")
    );
    Score.setCellValueFactory(
        new PropertyValueFactory<Player,String>("scour")
    );
    Status.setCellValueFactory(
        new PropertyValueFactory<Player,String>("state")
    );
        
        PlayerTable.setItems(playerData);
        
        unSelected4.setTitle("player list");
        unSelected4.setHeaderText(null);
        unSelected4.setContentText("please select player ");

    backBtn4.setOnAction(new EventHandler<ActionEvent>() {

            @Override 
            public void handle(ActionEvent event) {
                   
                ScreenThree().getChildren().clear();
                backBtn4.getScene().setRoot(ScreenThree());   


            }
        });
    playBtn4.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Player pl = PlayerTable.getSelectionModel().getSelectedItem();
                    Platform.runLater(()->{ScreenOne().getChildren().clear();});
                     Platform.runLater(()->{playBtn4.getScene().setRoot(ScreenOne());
                             });

                   
                
                } catch (NullPointerException q) {

                    unSelected4.showAndWait();
                }

            }
        });
    
        return ScreenFour; 
        
     }
     
    public AnchorPane ScreenSingleMode()
    {
         GameImg = new ImageView();
        exit = new Button();
        recordGame = new Button();
        bt1 = new Button();
        bt2 = new Button();
        bt3 = new Button();
        bt4 = new Button();
        bt5 = new Button();
        bt6 = new Button();
        bt7 = new Button();
        bt8 = new Button();
        bt9 = new Button();
        playerScore = new Label();
        computerScore = new Label();

        ScreenSingleMode.setMaxHeight(USE_PREF_SIZE);
        ScreenSingleMode.setMaxWidth(USE_PREF_SIZE);
        ScreenSingleMode.setMinHeight(USE_PREF_SIZE);
        ScreenSingleMode.setMinWidth(USE_PREF_SIZE);
        ScreenSingleMode.setPrefHeight(540.0);
        ScreenSingleMode.setPrefWidth(642.0);
        ScreenSingleMode.setStyle("-fx-background-color: linear-gradient(to right, #0f0c29, #302b63, #24243e);");

        GameImg.setFitHeight(540.0);
        GameImg.setFitWidth(642.0);
        GameImg.setPickOnBounds(true);
//        GameImg.setImage(new Image(getClass().getResource("../../../Project/Img/image123.jpg").toExternalForm()));
        try {
            FileInputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\image.jpg");
            Image image = new Image(stream);
            GameImg.setImage(image);
            
        } catch (FileNotFoundException ex) {
//            Logger.getLogger(FXMLScreenOne.class.getName()).log(Level.SEVERE, null, ex);
        }

        exit.setAlignment(javafx.geometry.Pos.CENTER);
        exit.setLayoutX(331.0);
        exit.setLayoutY(456.0);
        exit.setMnemonicParsing(false);
        exit.setPrefHeight(46.0);
        exit.setPrefWidth(179.0);
        exit.setStyle("-fx-background-color: linear-gradient(to right, #616161, #9bc5c3);;");
        exit.setText("Exit");
        exit.setTextFill(javafx.scene.paint.Color.WHITE);
        exit.setFont(new Font("Lucida Calligraphy Italic", 20.0));

        recordGame.setAlignment(javafx.geometry.Pos.CENTER);
        recordGame.setLayoutX(93.0);
        recordGame.setLayoutY(456.0);
        recordGame.setMnemonicParsing(false);
        recordGame.setPrefHeight(46.0);
        recordGame.setPrefWidth(179.0);
        recordGame.setStyle("-fx-background-color: linear-gradient(to right, #616161, #9bc5c3);;");
        recordGame.setText("Record Game");
        recordGame.setTextFill(javafx.scene.paint.Color.valueOf("#fffdfd"));
        recordGame.setFont(new Font("Lucida Calligraphy Italic", 20.0));

        bt1.setLayoutX(120.0);
        bt1.setLayoutY(102.0);
        bt1.setMnemonicParsing(false);
        bt1.setPrefHeight(89.0);
        bt1.setPrefWidth(95.0);

        bt2.setLayoutX(249.0);
        bt2.setLayoutY(100.0);
        bt2.setMnemonicParsing(false);
        bt2.setPrefHeight(89.0);
        bt2.setPrefWidth(95.0);

        bt3.setLayoutX(376.0);
        bt3.setLayoutY(100.0);
        bt3.setMnemonicParsing(false);
        bt3.setPrefHeight(89.0);
        bt3.setPrefWidth(95.0);

        bt4.setLayoutX(120.0);
        bt4.setLayoutY(215.0);
        bt4.setMnemonicParsing(false);
        bt4.setPrefHeight(89.0);
        bt4.setPrefWidth(95.0);

        bt5.setLayoutX(249.0);
        bt5.setLayoutY(215.0);
        bt5.setMnemonicParsing(false);
        bt5.setPrefHeight(89.0);
        bt5.setPrefWidth(95.0);

        bt6.setLayoutX(377.0);
        bt6.setLayoutY(215.0);
        bt6.setMnemonicParsing(false);
        bt6.setPrefHeight(89.0);
        bt6.setPrefWidth(95.0);

        bt7.setLayoutX(120.0);
        bt7.setLayoutY(331.0);
        bt7.setMnemonicParsing(false);
        bt7.setPrefHeight(89.0);
        bt7.setPrefWidth(95.0);

        bt8.setLayoutX(249.0);
        bt8.setLayoutY(334.0);
        bt8.setMnemonicParsing(false);
        bt8.setPrefHeight(89.0);
        bt8.setPrefWidth(95.0);

        bt9.setLayoutX(377.0);
        bt9.setLayoutY(336.0);
        bt9.setMnemonicParsing(false);
        bt9.setPrefHeight(89.0);
        bt9.setPrefWidth(95.0);

        playerScore.setLayoutX(122.0);
        playerScore.setLayoutY(14.0);
        playerScore.setText("Player Name");
        playerScore.setTextFill(javafx.scene.paint.Color.WHITE);
        playerScore.setFont(new Font(18.0));

        computerScore.setLayoutX(390.0);
        computerScore.setLayoutY(14.0);
        computerScore.setText("Computer");
        computerScore.setTextFill(javafx.scene.paint.Color.WHITE);
        computerScore.setFont(new Font(18.0));

        ScreenSingleMode.getChildren().add(GameImg);
        ScreenSingleMode.getChildren().add(exit);
        ScreenSingleMode.getChildren().add(recordGame);
        ScreenSingleMode.getChildren().add(bt1);
        ScreenSingleMode.getChildren().add(bt2);
        ScreenSingleMode.getChildren().add(bt3);
        ScreenSingleMode.getChildren().add(bt4);
        ScreenSingleMode.getChildren().add(bt5);
        ScreenSingleMode.getChildren().add(bt6);
        ScreenSingleMode.getChildren().add(bt7);
        ScreenSingleMode.getChildren().add(bt8);
        ScreenSingleMode.getChildren().add(bt9);
        ScreenSingleMode.getChildren().add(playerScore);
        ScreenSingleMode.getChildren().add(computerScore);
        
        return ScreenSingleMode;
    }
     
    public void messageHandelr(Player p) {
        
        System.out.println("Line 212: " + p.getRespond());
        switch (p.getRequest()) {
            case Request.LOGIN:
                login(p);
                break;
            case Request.SIGNUP:
                signup(p);
                break;
            case Request.LOGOUT:
                System.out.println("Line 313: " + p.getRespond());
                logout(p);
                break;
        }
        
    }
    
    public void login(Player newPalyer)
    {
        System.out.println("Line 221: " + newPalyer.getRespond());
        
        if (newPalyer.getRespond().equals(Respond.SUCCESS))
        {
            
            System.out.println("Line 223: " + newPalyer.getRespond());
            
            Platform.runLater(()->{ScreenThree().getChildren().clear();});
            Platform.runLater(()->{logInBtn1.getScene().setRoot(ScreenThree());});

            System.out.println("Line 226: " + newPalyer.getRespond());
        } 
        else
        {
            System.out.println("Line 228: " + newPalyer.getRespond());
            Platform.runLater(()->alertWrongLogIn1.showAndWait());
            

        }
    }
    
    
    public void signup(Player p) {
        
        System.out.println("Line 343: " + p.getRespond());
        if (p.getRespond().equals(Respond.SUCCESS)) {
            System.out.println("Line 344: " + p.getRespond());
            Platform.runLater(()->{alert.showAndWait();});
            Platform.runLater(()->{ScreenOne().getChildren().clear();});
            Platform.runLater(()->{signupBtn2.getScene().setRoot(ScreenOne());});
            
            System.out.println("Line 348: " + p.getRespond());
        } 
        else
        {
            System.out.println("else"+p.getRespond());
            Platform.runLater(()->{alertUserExists.showAndWait();});
            System.out.println("Line 232: user already exsist" + p.getRespond());
           
            

        }
    }

    public void logout(Player p) {
        
        System.out.println("Logout function: " + p.getRespond());
        
        if (p.getRespond().equals(Respond.SUCCESS)) {
            
            Platform.runLater(()->{ScreenOne().getChildren().clear();});
            Platform.runLater(()->{logOutBtn3.getScene().setRoot(ScreenOne());});
            System.out.println("User logout successfully: " + p.getRespond());
        } 
        else
        {
            alertWrongLogout1=new Alert(Alert.AlertType.ERROR);
            alertWrongLogout1.setTitle("Logout ");
            alertWrongLogout1.setHeaderText(null);
            alertWrongLogout1.setContentText("Faild logout");
            Platform.runLater(()->alertWrongLogout1.showAndWait());
            System.out.println("Line 389 logout: " + p.getRespond());

        }
    }
    
   
    
    @Override
     public void start(Stage primaryStage) {

            try
        {
            Parent root = ScreenOne();
            Scene scene = new Scene(root);

            try {
                //creating the image object
                InputStream stream = new FileInputStream("F:\\ITI\\Java\\Project\\Img\\icon.png");
                Image image = new Image(stream);
                //Creating the image view
                ImageView imageView = new ImageView();
                //Setting image to the image view
                primaryStage.getIcons().add(image);
            } catch (FileNotFoundException ex) {
                
                System.out.println("Cann't load background image in start method");
            }
            
            
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.show();
        }
        catch(NullPointerException ex)
        {
            System.out.println("Error");
        }
    }
    
    @Override
    public void stop() {
        
        p.setRequest(Request.DISCONNECT);
        try
        {
            writeObj.writeObject(p);
            socket.close();
            readObj.close();
            writeObj.close();
            System.out.println("Sent a disconnection request to the server");
        }
        catch (IOException ex)
        {
                        System.out.println("problem in stop method ");
            
        }
        thread.stop();
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    
}
