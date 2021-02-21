package tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import libs.*;
import java.io.IOException;
import java.io.InputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author hebaa
 */
public class Client extends Application implements Serializable {
    
     Alert alertForRespond = new Alert(Alert.AlertType.CONFIRMATION);
          ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("no", ButtonBar.ButtonData.NO);
    
        

    ArrayList<Player> ASD;
    /**
     * ***********socket***********************
     */
    private JSONObject obj;

    JSONParser parser = new JSONParser();
    JsonConverter convert = new JsonConverter();
//      PrintStream  outStream  ;
    DataInputStream inStream;

    Socket socket;
//    ObjectInputStream readObj;
//    DataInputStream   readObj;

//    ObjectOutputStream writeObj;
    PrintStream outStream;
    Thread thread;
    /**
     * *******Player class**************************
     */
    Player p;
    /**
     * *********Single Mode Game**************************
     */
    int count = 0;
//    SingleModeGame single ;
    /**
     * **********Alert************************
     */
    Alert alertEmptyLogIn1 = new Alert(Alert.AlertType.ERROR);
    Alert alertWrongLogIn1 = new Alert(Alert.AlertType.ERROR);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert alertUserExists = new Alert(Alert.AlertType.ERROR);
    Alert alertEmptySignUp1 = new Alert(Alert.AlertType.ERROR);
    Alert alertWrongLogout1;
    Alert unSelected4 = new Alert(Alert.AlertType.ERROR);
    /**
     * **********AnchorPane Screens***************************
     */
    
    AnchorPane ScreenOne;
    AnchorPane ScreenTwo;
    AnchorPane ScreenThree;
    AnchorPane ScreenFour;
    AnchorPane ScreenSingleMode;
    /**
     * **********Screen one variables**********************
     */
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
    /**
     * ************Screen two variables**********************
     */
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
    /**
     * ************Screen three variables*********************
     */
    public GridPane GridOfPlay;
    public ColumnConstraints tableColumnConstraints;
    public RowConstraints tableRowConstraints;
    public AnchorPane PlayPanel;
    public ImageView PlayImg;
    public Label PlayLable;
    public Button singleBtn3;
    public Button multiBtn3;
    public Button logOutBtn3;
    /**
     * ************Screen four variables*************************
     */
    ImageView TableImg;
    TableView PlayerTable;
    TableColumn PlayerName;
    TableColumn Score;
    TableColumn Status;
    Label TableLabel;
    Button playBtn4;
    Button backBtn4;
    /**
     * **************ScreenSingleMode**********************
     */
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

    ArrayList<Integer> playerXpositions = new ArrayList<Integer>();
    ArrayList<Integer> playerOpositions = new ArrayList<Integer>();
    Label playerName;
    Label computerName;
    Label playerScore;
    Label computerScore;
    Label scoreSeperator;//remove
    Button[] buttons;
    String[] recordedPositions;
    String symbol;
    boolean gameFlag;
    int move;
    int playerScoreCounter;
    int computerScoreCounter;
    int index;

    private static String username;

    /**
     * ***********************************
     */
    @Override
    public void init() {
        
       


alertForRespond.getButtonTypes().setAll(okButton, noButton);
                      
     
  

        p = new Player("", "");
        alertWrongLogIn1.setTitle("LogIn ");
        alertWrongLogIn1.setHeaderText(null);
        alertWrongLogIn1.setContentText("Invalid User Name or Password");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket("127.0.0.1", 5005);

//               outStream = new PrintStream(socket.getOutputStream());
                    inStream = new DataInputStream(socket.getInputStream());

//                    outStream = new ObjectOutputStream(socket.getOutputStream());
                    outStream = new PrintStream(socket.getOutputStream());

//               readObj =  new DataInputStream(socket.getInputStream());                   
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("hoooo");
                while (true) {
                    try {


                        obj = (JSONObject) parser.parse(inStream.readLine());
                        
//                        Player p2 = convert.fromJsonToPlayer(obj,
//                                convert.fromJSONArrayToPlayerList((JSONArray) obj.get("playersList")));

                        System.out.println("line 225 " + obj);
                            

                        messageHandelr(obj);
                    System.out.println("line 250 ");

//                        System.out.println("Line 98: " + p.getRespond());
//                        System.out.println("Line 196: " + p.getUsername() + p.getPassword());
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (NullPointerException ex) {
                        System.out.println(ex.getMessage());
                    } catch (ParseException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        thread.start();

    }

    /**
     * ***********Screens methods**************************
     */
    public AnchorPane ScreenOne() {
        ScreenOne = new AnchorPane();
        LoginImg = new ImageView();
        LoginLabel = new Label();
        usrName1 = new Label();
        userText1 = new TextField();
        pass1 = new Label();
        passText1 = new PasswordField();
        logInBtn1 = new Button();
        signupBtn1 = new Button();

//        ScreenOne.setMaxHeight(USE_PREF_SIZE);
//        ScreenOne.setMaxWidth(USE_PREF_SIZE);
//        ScreenOne.setMinHeight(USE_PREF_SIZE);
//        ScreenOne.setMinWidth(USE_PREF_SIZE);
        ScreenOne.setPrefHeight(540.0);
        ScreenOne.setPrefWidth(642.0);
        ScreenOne.setStyle("-fx-background-color: linear-gradient(to right, #5c258d, #4389a2);;");

        LoginImg.setFitHeight(540.0);
        LoginImg.setFitWidth(307.0);
        LoginImg.setPickOnBounds(true);

        try {
            FileInputStream stream = new FileInputStream("/media/mohamed/4CEA8043EA802B72/ITI/java/pro2021/Tic-Tac-Toe/XoPro1/src/xopro1/login.jpg");
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

        /**
         * **Screen One Button Action******
         */
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

                if (userText1.getText().isEmpty() || passText1.getText().isEmpty()) {
                    alertEmptyLogIn1.showAndWait();
                } else {
                    try {
                        Player newPlayer = new Player(userText1.getText(), passText1.getText());
//                           setPlayerName( userText1.getText() );
                      System.out.println("line 402" + newPlayer);

                        obj = convert.fromPlayerToJson(newPlayer);
                      System.out.println("line 404" + obj);

                        outStream.println(obj.toString());
//                           outStream.println(obj.toString()) ;
                        System.out.println("object sent!");

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }

            }
        });

        return ScreenOne;

    }

    public String getusername() {
        System.out.println(userName3);
        return userText1.getText();

    }

    public AnchorPane ScreenTwo() {
        ScreenTwo = new AnchorPane();
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
//
//        ScreenTwo.setMaxHeight(USE_PREF_SIZE);
//        ScreenTwo.setMaxWidth(USE_PREF_SIZE);
//        ScreenTwo.setMinHeight(USE_PREF_SIZE);
//        ScreenTwo.setMinWidth(USE_PREF_SIZE);
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
            FileInputStream stream = new FileInputStream("/media/mohamed/4CEA8043EA802B72/ITI/java/pro2021/Tic-Tac-Toe/XoPro1/src/xopro1/registration.jpg");
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
                } else {

                    Player signUpPlayer = new Player(userText2.getText(), passText2.getText(), FirstText2.getText(), LastText2.getText());

                    obj = convert.fromPlayerToJson(signUpPlayer);
                    outStream.println(obj.toString());
                    System.out.println("New Player has sign up!");

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

    public AnchorPane ScreenThree() {
        ScreenThree = new AnchorPane();
        GridOfPlay = new GridPane();
        tableColumnConstraints = new ColumnConstraints();
        tableRowConstraints = new RowConstraints();
        PlayPanel = new AnchorPane();
        PlayImg = new ImageView();
        PlayLable = new Label();
        singleBtn3 = new Button();
        multiBtn3 = new Button();
        logOutBtn3 = new Button();

//        ScreenThree.setMaxHeight(USE_PREF_SIZE);
//        ScreenThree.setMaxWidth(USE_PREF_SIZE);
//        ScreenThree.setMinHeight(USE_PREF_SIZE);
//        ScreenThree.setMinWidth(USE_PREF_SIZE);
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
            FileInputStream stream = new FileInputStream("/media/mohamed/4CEA8043EA802B72/ITI/java/pro2021/Tic-Tac-Toe/XoPro1/src/xopro1/rocket.jpg");
            Image image = new Image(stream);
            PlayImg.setImage(image);

        } catch (FileNotFoundException ex) {
            System.out.println("Fsild to load menu image");
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
                try {
                    System.out.println(logOutPlayer.getRequest());

                    obj = convert.fromPlayerToJson(logOutPlayer);
                    outStream.println(obj.toString());
                    System.out.println("User send logout Request!");

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        });

        multiBtn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               
//                    
//                        Player p3 = new Player()  ;
//                        p3.setRequest(Request.USERS);
//                      obj = convert.fromPlayerToJson(p3) ;
//                        writeObj.println(obj.toString()) ;
//
//                    for (Player player : ASD) {
//
////                        System.out.println("Player :" + player.getUsername() + " Status= " + player.getState());
//                    }

                    Platform.runLater(() -> {
                      
                              ScreenFour().getChildren().clear();
                             multiBtn3.getScene().setRoot(ScreenFour());
                            
                        
                        
                      

                    });
                    
//                    
            

            }

        });

        singleBtn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ScreenSingleMode().getChildren().clear();
                singleBtn3.getScene().setRoot(ScreenSingleMode());

            }

        });

        return ScreenThree;
    }

    public AnchorPane ScreenFour()  {
        ScreenFour = new AnchorPane();
        TableImg = new ImageView();

//        PlayerTable = new TableView();
        TableView<Player> PlayerTable = new TableView<Player>();
        ObservableList<Player> playerData =   FXCollections.observableArrayList();
                    playerData.clear(); 
                    playerData.addAll(ASD);
                    
        

        PlayerName = new TableColumn();
        Score = new TableColumn();
        Status = new TableColumn();
        TableLabel = new Label();
        playBtn4 = new Button();
        backBtn4 = new Button();
//        ScreenFour.setMaxHeight(USE_PREF_SIZE);
//        ScreenFour.setMaxWidth(USE_PREF_SIZE);
//        ScreenFour.setMinHeight(USE_PREF_SIZE);
//        ScreenFour.setMinWidth(USE_PREF_SIZE);
        ScreenFour.setPrefHeight(540.0);
        ScreenFour.setPrefWidth(642.0);
//        ScreenFour.setStyle("-fx-background-color: linear-gradient(to right, #0f0c29, #302b63, #24243e);");

        TableImg.setFitHeight(540.0);
        TableImg.setFitWidth(642.0);
        TableImg.setPickOnBounds(true);
//        TableImg.setImage(new Image(getClass().getResource("../../../Project/Img/image.jpg").toExternalForm()));
        try {
            FileInputStream stream = new FileInputStream("/media/mohamed/4CEA8043EA802B72/ITI/java/pro2021/Tic-Tac-Toe/XoPro1/src/xopro1/image.jpg");
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
        //--------------------------- tableview ------------------
        PlayerTable.getColumns().add(PlayerName);
        PlayerTable.getColumns().add(Score);
        PlayerTable.getColumns().add(Status);
        //--------------------------- tableview ------------------

        ScreenFour.getChildren().add(PlayerTable);
        ScreenFour.getChildren().add(TableLabel);
        ScreenFour.getChildren().add(playBtn4);
        ScreenFour.getChildren().add(backBtn4);
        PlayerName.setCellValueFactory(
                new PropertyValueFactory<Player, String>("username")
        );
        Score.setCellValueFactory(
                new PropertyValueFactory<Player, String>("scour")
        );
        Status.setCellValueFactory(
                new PropertyValueFactory<Player, String>("status")
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
           //     try {
                    
         
                    
                    String a= PlayerTable.getSelectionModel().getSelectedItem().getUsername() ;
                    
//                      System.out.println(a);
                  System.out.println(userText1.getText());
                   
                      Player  p  = new Player()  ;
                      p.setUsername(userText1.getText());
                      p.setRequest(Request.GAME_INVITATION);
                      p.setDestination(a);
                      
                   obj = convert.fromPlayerToJson(p);
                 outStream.println(obj.toString());
                 
                 
                        
                        
                        
                        
                      
                      
                      
                      
                    
//                    Player pl = PlayerTable.getSelectionModel().getSelectedItem();
//                    System.out.println( PlayerTable.getSelectionModel().getSelectedItem());     
//                    Platform.runLater(() -> {
//                        ScreenOne().getChildren().clear();
//                    });
//                    Platform.runLater(() -> {
//                        playBtn4.getScene().setRoot(ScreenOne());
//                    });
//
//                } catch (NullPointerException q) {
//
//                    unSelected4.showAndWait();
//                }

            }
        });

        return ScreenFour;

    }

    public AnchorPane ScreenSingleMode() {
        ScreenSingleMode = new AnchorPane();
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

        playerName = new Label();
        computerName = new Label();
        scoreSeperator = new Label();
        username = getusername();

//
//        ScreenSingleMode.setMaxHeight(USE_PREF_SIZE);
//        ScreenSingleMode.setMaxWidth(USE_PREF_SIZE);
//        ScreenSingleMode.setMinHeight(USE_PREF_SIZE);
//        ScreenSingleMode.setMinWidth(USE_PREF_SIZE);
        ScreenSingleMode.setPrefHeight(540.0);
        ScreenSingleMode.setPrefWidth(642.0);
        ScreenSingleMode.setStyle("-fx-background-color: linear-gradient(to right, #0f0c29, #302b63, #24243e);");

        GameImg.setFitHeight(540.0);
        GameImg.setFitWidth(642.0);
        GameImg.setPickOnBounds(true);
        try {
            FileInputStream stream = new FileInputStream("/media/mohamed/4CEA8043EA802B72/ITI/java/pro2021/Tic-Tac-Toe/XoPro1/src/xopro1/image.jpg");
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

        playerName.setLayoutX(122.0);
        playerName.setLayoutY(5.0);
        playerName.setText(username);
        playerName.setTextFill(javafx.scene.paint.Color.WHITE);
        playerName.setFont(Font.font("Javanese Text", FontWeight.BOLD, 20));

        computerName.setLayoutX(390.0);
        computerName.setLayoutY(5.0);
        computerName.setText("Computer");
        computerName.setTextFill(javafx.scene.paint.Color.WHITE);
        computerName.setFont(Font.font("Javanese Text", FontWeight.BOLD, 20));

        playerScore.setLayoutX(140.0);
        playerScore.setLayoutY(50.0);
        playerScore.setText(String.valueOf(playerScoreCounter));
        playerScore.setTextFill(javafx.scene.paint.Color.WHITE);
        playerScore.setFont(Font.font("Engravers MT", FontWeight.BOLD, 36));

        scoreSeperator.setLayoutX(300.0);
        scoreSeperator.setLayoutY(14.0);
        scoreSeperator.setPrefWidth(28.0);
        scoreSeperator.setText(":");
        scoreSeperator.setTextFill(javafx.scene.paint.Color.WHITE);
        scoreSeperator.setFont(Font.font("Engravers MT", FontWeight.BOLD, 36));

        computerScore.setLayoutX(410.0);
        computerScore.setLayoutY(50.0);
        computerScore.setText(String.valueOf(computerScoreCounter));
        computerScore.setFont(Font.font("Engravers MT", FontWeight.BOLD, 36));
        computerScore.setTextFill(javafx.scene.paint.Color.WHITE);

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
        ScreenSingleMode.getChildren().add(playerName);
        ScreenSingleMode.getChildren().add(computerName);
        ScreenSingleMode.getChildren().add(scoreSeperator);

        playerScoreCounter = 0;
        computerScoreCounter = 0;
        recordedPositions = new String[9];
        gameFlag = true;
        symbol = "X";
        buttons = new Button[9];

        buttons[0] = bt1;
        buttons[1] = bt2;
        buttons[2] = bt3;
        buttons[3] = bt4;
        buttons[4] = bt5;
        buttons[5] = bt6;
        buttons[6] = bt7;
        buttons[7] = bt8;
        buttons[8] = bt9;
        for (index = 0; index < 9; index++) {
            buttons[index].setFont(Font.font("Engravers MT", FontWeight.BOLD, 36));
        }

        bt1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt1.setText(symbol);
                    bt1.setDisable(true);
                    if (symbol == "X") {
                        playerXpositions.add(1);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt2.setText(symbol);
                    bt2.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(2);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt3.setText(symbol);
                    bt3.setDisable(true);
                    if (symbol == "X") {
                        playerXpositions.add(3);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt3.setText("Tie");
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt4.setText(symbol);
                    bt4.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(4);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt4.setText("Tie");
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt5.setText(symbol);
                    bt5.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(5);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt5.setText("Tie");
                            drawTie();
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt6.setText(symbol);
                    bt6.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(6);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt6.setText("Tie");
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt7.setText(symbol);
                    bt7.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(7);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt7.setText("Tie");
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt8.setText(symbol);
                    bt8.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(8);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt8.setText("Tie");
                            drawTie();
                            gameFlag = false;
                            updateScore();
                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });
        bt9.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFlag) {
                    bt9.setText(symbol);
                    bt9.setDisable(true);
                    if (symbol == "X") {

                        playerXpositions.add(9);
                        symbol = "O";
                        if (checkWinner() == "X") {
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                            playerScoreCounter++;
                            updateScore();
                        } else if (checkWinner() == "tie") {
                            bt9.setText("Tie");
                            drawTie();
                            gameFlag = false;
                            updateScore();

                        } else {
                            playComputerMove();
                        }
                    }
                }
            }

        });

        recordGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!gameFlag) {
                    playAgain();
                } else {
                    recordPositions();
                    drawXO();
                }

            }

        });

        exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ScreenThree().getChildren().clear();
                exit.getScene().setRoot(ScreenThree());
                ;
            }

        });

        playerName.setText(username);

        return ScreenSingleMode;
    }

    public void messageHandelr(JSONObject message) {
        
        System.out.println("hi from message handler 1453");
        
        System.out.println("line 1452 "+(String)message.get("request") );

//        try {
            switch ((String)message.get("request")) {
                case Request.LOGIN:
                    login(message);
                    break;
                case Request.SIGNUP:
                    signup(message);
                    break;
                case Request.LOGOUT:
                
                    logout(message);
                    break;
                case Request.PlAYER_LIST:
                    

                    playlist(message);
                    break;
                    
                    
               case Request.GAME_INVITATION:
                checkGameRespond(message);
                    break  ; 
                    
               case Request.GAME_INVITATION_RESPOND:
                   System.out.println("hi 1475  inside  generateAlertToAskUserForRespond  " );
                generateAlertToAskUserForRespond(message);
                break;

            }

//        } catch (Exception e) {
//
//            System.out.println("error in  messange handler 1475");
//            System.out.println(e.getMessage());
//        }

    }

    public void login(JSONObject message) {
        
        Player newPlayer = convert.fromJsonToPlayer(message,
                                convert.fromJSONArrayToPlayerList((JSONArray) message.get("playersList")));

        if (newPlayer.getRespond().equals(Respond.SUCCESS)) {


            Platform.runLater(() -> {
                ScreenThree().getChildren().clear();
            });
            Platform.runLater(() -> {
                logInBtn1.getScene().setRoot(ScreenThree());
            });

        } else {
            Platform.runLater(() -> alertWrongLogIn1.showAndWait());

        }
    }

    public void signup(JSONObject message) {
        Player p = convert.fromJsonToPlayer(message,
                                convert.fromJSONArrayToPlayerList((JSONArray) message.get("playersList")));

        if (p.getRespond().equals(Respond.SUCCESS)) {
            Platform.runLater(() -> {
                alert.showAndWait();
            });
            Platform.runLater(() -> {
                ScreenOne().getChildren().clear();
            });
            Platform.runLater(() -> {
                signupBtn2.getScene().setRoot(ScreenOne());
            });

        } else {
            System.out.println("else" + p.getRespond());
            Platform.runLater(() -> {
                alertUserExists.showAndWait();
            });

        }
    }

    public void logout(JSONObject message) {
         Player p = convert.fromJsonToPlayer(message,
                                convert.fromJSONArrayToPlayerList((JSONArray) message.get("playersList")));

        System.out.println("Logout function: " + p.getRespond());

        if (p.getRespond().equals(Respond.SUCCESS)) {

            Platform.runLater(() -> {
                ScreenOne().getChildren().clear();
            });
            Platform.runLater(() -> {
                logOutBtn3.getScene().setRoot(ScreenOne());
            });
            System.out.println("User logout successfully: " + p.getRespond());
        } else {
            alertWrongLogout1 = new Alert(Alert.AlertType.ERROR);
            alertWrongLogout1.setTitle("Logout ");
            alertWrongLogout1.setHeaderText(null);
            alertWrongLogout1.setContentText("Faild logout");
            Platform.runLater(() -> alertWrongLogout1.showAndWait());

        }
    }

    /**
     * ***********Game methods**************************
     */
    String checkWinner() {
        String winner = null;
        //winning conditions
        List topRow = Arrays.asList(1, 2, 3);
        List midRow = Arrays.asList(4, 5, 6);
        List botRow = Arrays.asList(7, 8, 9);

        List liftCol = Arrays.asList(1, 4, 7);
        List midtCol = Arrays.asList(2, 5, 8);
        List righCol = Arrays.asList(3, 6, 9);

        List cross1 = Arrays.asList(1, 5, 9);
        List cross2 = Arrays.asList(3, 5, 7);

        List<List> winning = new ArrayList<List>();

        winning.add(topRow);
        winning.add(midRow);
        winning.add(botRow);
        winning.add(liftCol);
        winning.add(midtCol);
        winning.add(righCol);
        winning.add(cross1);
        winning.add(cross2);

        for (List winningComp : winning) {

            if (playerXpositions.containsAll(winningComp)) {
                winner = "X";
                playerXpositions.clear();
                playerXpositions.addAll(winningComp);
            } else if (playerOpositions.containsAll(winningComp)) {
                winner = "O";
                playerOpositions.clear();
                playerOpositions.addAll(winningComp);
            } else if (playerXpositions.size() + playerOpositions.size() == 9) {
                winner = "tie";
            }
        }
        return winner;
    }

    void highLightWinner(ArrayList<Integer> winner) {
        // will take the winner position and highlight each button equl to this position

        for (int i = 0; i < 9; i++) {
            if (i + 1 == (int) winner.get(0) || i + 1 == (int) winner.get(1) || i + 1 == (int) winner.get(2)) {
                buttons[i].setStyle("-fx-background-color: #128dba");
            }
        }
    }

    String[] recordPositions() {
        // save the value of each buttons of the 9'th, X or O or bull
        recordedPositions = new String[9];
        for (int index = 0; index < 9; index++) {
            recordedPositions[index] = buttons[index].getText();
        }
        return recordedPositions;
    }

    void drawOldPositions(String[] positions) {
        // will append symbol, and disable  buttons according to  last saved
        for (int index = 0; index < 9; index++) {

            if (positions[index] != null) {
                buttons[index].setText(positions[index]);
                buttons[index].setDisable(true);
                if (positions[index] == "X") {
                    playerXpositions.add(index + 1);
                } else {
                    playerOpositions.add(index + 1);
                }
            }
        }

        //change the symbol to the right turn
        if (playerXpositions.size() > playerOpositions.size()) {
            symbol = "O";
            playComputerMove();
        }

    }

    void drawTie() {
        for (Button btn : buttons) {
            btn.setText("Tie");
        }
    }

    void drawXO() {
        for (String str : recordedPositions) {
            System.out.println(str);
        }
    }

    int computerMove() {
        Random random = new Random();
        int cpuPosition = random.nextInt(9) + 1;
        // make sure that this position is not taken before
        while (playerXpositions.contains(cpuPosition) || playerOpositions.contains(cpuPosition)) {
            cpuPosition = random.nextInt(9) + 1;
        }
        return cpuPosition;
    }

    void drawCpuMove(int movePosition) {
        movePosition--; // to indecate the index of the button
        for (int index = 0; index < 9; index++) {
            if (index == movePosition) {
                buttons[index].setText(symbol);
                buttons[index].setDisable(true);
            }
        }
    }

    void playComputerMove() {
        if (gameFlag) {
            move = computerMove();
            playerOpositions.add(move);
            drawCpuMove(move);
            symbol = "X";
            if (checkWinner() == "O") {
                highLightWinner(playerOpositions);
                gameFlag = false;
                computerScoreCounter++;
                updateScore();
            } else if (checkWinner() == "tie") {
                drawTie();
                updateScore();
            }
        }
    }

    private void playAgain() {
        recordGame.setText("Record Game");
        gameFlag = true;
        symbol = "X";
        playerXpositions.clear();
        playerOpositions.clear();
        for (Button btn : buttons) {
            btn.setText("");
            //btn.setStyle("-fx-background-color: #D2691E ");
            btn.setDisable(false);

            // reset the old style
            Button tmpButton = new Button();
            btn.setStyle(tmpButton.getStyle());
        }
    }

    public static void setPlayerName(String playerName) {

        username = playerName;
    }

    public static String getPlayerName() {

        return username;
    }

    private void updateScore() {
        playerScore.setText(String.valueOf(playerScoreCounter));
        computerScore.setText(String.valueOf(computerScoreCounter));
        recordGame.setText("Play Again!");
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root = ScreenOne();
            Scene scene = new Scene(root);

            try {
                //creating the image object
                InputStream stream = new FileInputStream("/media/mohamed/4CEA8043EA802B72/ITI/java/pro2021/Tic-Tac-Toe/XoPro1/src/xopro1/xo.png");
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
        } catch (NullPointerException ex) {
            System.out.println("Error");
        }
    }

    @Override
    public void stop() {

//        p.setRequest(Request.DISCONNECT);
        try {

            obj = convert.fromPlayerToJson(p);
            outStream.println(obj.toString());
//            writeObj.outStreamect(p);
            socket.close();
//            readObj.close();
            outStream.close();
            System.out.println("Sent a disconnection request to the server");
        } catch (IOException ex) {
            System.out.println("problem in stop method ");

        }
        thread.stop();

    }

    public void playlist(JSONObject message)  {
        Player p = convert.fromJsonToPlayer(message,
                                convert.fromJSONArrayToPlayerList((JSONArray) message.get("playersList")));
        
    
        ASD  =  p.getPlayersList()  ;
        
        
    }
    
    
    
    public void checkGameRespond(JSONObject message)
    {
        
       Player p = convert.fromJsonToPlayer(message,
                                convert.fromJSONArrayToPlayerList((JSONArray) message.get("playersList")));
        if (p.getRespond().equals(Respond.SUCCESS))    
        {
            System.out.println("start game function and run here");  
                //---------
        }
        else 
        {
            
           System.out.println(" request refused alert ");  

            
        }
        
        
        
    }
    
   public void  generateAlertToAskUserForRespond(JSONObject message)
           
   {
                           Player p3 = convert.fromJsonToPlayer(message) ; 
                           
       
                alertForRespond.setTitle("want to play with "  +  p3.getUsername());
            alertForRespond.setContentText("play?");
               Platform.runLater(() -> {
                         
    
    alertForRespond.showAndWait().ifPresent(type -> {
    
    if (type == okButton) 
    { System.out.println("play");
        System.out.println(" a message send with success resond ");
            p3.setRespond(Respond.SUCCESS);
            obj = convert.fromPlayerToJson(p3) ; 
            System.out.println( "line 1837" + obj);
            outStream.println(obj.toString()); 
            
    
    } 
    else if (type == noButton)
    { 
//         p3.setRespond(Respond.FAILURE);
//            obj = convert.fromPlayerToJson(p3) ; 
//            this.outStream.println(obj.toString()); 
//        System.out.println("refuse"); 
    // do something
    }
    else { 
        
        
        System.out.println("cancel"); 
    }
    
    });
 
   });
      
       
       
   }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
