/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xopro1;

import servergui.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import xopro1.XoDataBase;

/**
 *
 * @author black horse
 */
public class XoPro1 extends Application {
 
    XoDataBase db;
    int count;
    Socket socket;
    DataInputStream dis;
    PrintStream ps;
    Thread thread;
    private StackPane root1 = new StackPane();
    private StackPane root2 = new StackPane();
    private StackPane root3 = new StackPane();

    private Stage stage;
    //scene1 

    Scene scene = new Scene(root1, 300, 250);
    VBox vBox1 = new VBox();
    Label usrName1 = new Label("Username");
    TextField userText1 = new TextField();
    Label pass1 = new Label("Password");
    PasswordField passText1 = new PasswordField();
    Button logInBtn1 = new Button("Login");
    Label orLabel = new Label("OR");
    Button signupBtn1 = new Button("Sign Up");
    Alert alertEmptyLogIn1 = new Alert(Alert.AlertType.ERROR);

    //--------------------
    //scene2
    Scene scene2 = new Scene(root2, 300, 300);
    VBox vBox2 = new VBox();
    Label usrName2 = new Label("Username");
    TextField userText2 = new TextField();
    Label FirstName2 = new Label("First Name");
    TextField FirstText2 = new TextField();
    Label LastName2 = new Label("Last Name");
    TextField LastText2 = new TextField();
    Label pass2 = new Label("Password");
    PasswordField passText2 = new PasswordField();
    Button signupBtn2 = new Button("Sign Up");
    Button backBtn2 = new Button("Back");
    FlowPane flow2 = new FlowPane();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    Alert alertEmptySignUp1 = new Alert(Alert.AlertType.ERROR);
    Alert alertWrongLogIn1 = new Alert(Alert.AlertType.ERROR);
    Alert alertUserExists = new Alert(Alert.AlertType.ERROR);

    //--------------------------------
    //scene3 
    Scene scene3 = new Scene(root3, 300, 250);

    Button logOutBtn3 = new Button("Log out");
    VBox vBox3 = new VBox();
    Label UsernameLabel3 = new Label("Username");
    Text usernameDislay3;

    Label scoreLabel3 = new Label("Score");
    Text scoreDislay3 = new Text();
    Button singleBtn3 = new Button("Single Mode ");

    Button multiBtn3 = new Button("Multi Mode");
    GridPane grid3 = new GridPane();
    String text3;

    @Override
    public void init() {

        //scene1
        vBox1.setSpacing(8);
        vBox1.setPadding(new Insets(10, 10, 10, 10));
        vBox1.getChildren().addAll(
                usrName1,
                userText1,
                pass1,
                passText1,
                logInBtn1,
                orLabel,
                signupBtn1);
        root1.getChildren().addAll(vBox1);
        alertEmptyLogIn1.setTitle("sign in ");
        alertEmptyLogIn1.setHeaderText(null);
        alertEmptyLogIn1.setContentText("you let empty fields");

        alertWrongLogIn1.setTitle("LogIn ");
        alertWrongLogIn1.setHeaderText(null);
        alertWrongLogIn1.setContentText("Invalid User Name or Password");
        //-------------------------------
        //scene2

        flow2.setHgap(10);
        flow2.getChildren().addAll(signupBtn2, backBtn2);

        vBox2.setSpacing(8);
        vBox2.setPadding(new Insets(10, 10, 10, 10));
        vBox2.getChildren().addAll(usrName2, userText2, FirstName2, FirstText2,
                LastName2, LastText2, pass2, passText2, flow2);
        root2.getChildren().addAll(vBox2);

        alert.setTitle("SignUp");
        alert.setHeaderText(null);
        alert.setContentText("Your data saved successfully");

        alertEmptySignUp1.setTitle("signUp ");
        alertEmptySignUp1.setHeaderText(null);
        alertEmptySignUp1.setContentText("All fields are required");

        alertUserExists.setTitle("signUp ");
        alertUserExists.setHeaderText(null);
        alertUserExists.setContentText("User already exist choose another uername");

        // flow2.getChildren().addAll(signupBtn2,backBtn2) ; 
        //-------------------------------------------------
        //scene 3 
        //     userText1.getText() ;
        // System.out.println(userText1.getText());
//               usernameDislay3 = new TextField( );
        usernameDislay3 = new Text();

//             usernameDislay3.setEditable(false);
        grid3.setVgap(10);
        grid3.add(singleBtn3, 2, 1);
        grid3.add(multiBtn3, 2, 2);
        grid3.add(logOutBtn3, 3, 3);

        vBox3.setSpacing(8);
        vBox3.setPadding(new Insets(10, 10, 10, 10));
        vBox3.getChildren().addAll(
                UsernameLabel3,
                usernameDislay3,
                scoreLabel3,
                scoreDislay3,
                grid3);
        root3.getChildren().addAll(vBox3);
        try {
            socket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        /*
        Button btn = new Button("Request");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ps.println("");
            }
        });
         */

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String request = null;
                    try {
                        request = dis.readLine();
                        // handel request
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    public void start(Stage primaryStage) {

        db = new XoDataBase();
        count = 1;
        logInBtn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (userText1.getText().isEmpty() || passText1.getText().isEmpty()) {

                    alertEmptyLogIn1.showAndWait();

                } else {
                    int login = db.check_username_password(passText1.getText(), userText1.getText());
                    if (login == 1) {
                        usernameDislay3.setText(userText1.getText());
                        primaryStage.setScene(scene3);
                        ps.println(userText1.getText());
                    } else {
                        alertWrongLogIn1.showAndWait();
                        userText1.clear();
                        passText1.clear();

                    }

                }

            }
        });

        signupBtn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                userText2.clear();
                FirstText2.clear();
                LastText2.clear();
                passText2.clear();
                primaryStage.setScene(scene2);

            }

        });
        backBtn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                userText1.clear();
                passText1.clear();
                primaryStage.setScene(scene);

            }
        });
        signupBtn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (userText2.getText().isEmpty() || FirstText2.getText().isEmpty() || LastText2.getText().isEmpty() || passText2.getText().isEmpty()) {

                    alertEmptySignUp1.showAndWait();
                } else {
                    if (db.check_username(userText2.getText()) == 0) {

                        db.sign_up(FirstText2.getText(), LastText2.getText(), userText2.getText(), passText2.getText());
                        //sign up page                    
                        alert.showAndWait();
                        userText2.clear();
                        FirstText2.clear();
                        LastText2.clear();
                        passText2.clear();
                        //login page
                        userText1.clear();
                        passText1.clear();
                        count++;
                        primaryStage.setScene(scene);
                    } else {
                        alertUserExists.showAndWait();
                    }

                }

            }
        });
        logOutBtn3.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                userText1.clear();
                passText1.clear();
                System.out.println("User: "+usernameDislay3.getText());
                ClientHandler.logout(usernameDislay3.getText());
                primaryStage.setScene(scene);

//                ClientHandler.logout(text3);
            }
        });

        root1.setId("root1");
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle(" Tic Tac Toe game ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
