package tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import libs.*;
import org.json.simple.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

public class XOBase extends AnchorPane implements Serializable{

    protected final SplitPane splitPane;
    protected final AnchorPane anchorPane;
    protected final ImageView imageView;
    protected final GridPane gridPane;
    protected final ColumnConstraints columnConstraints;
    protected final ColumnConstraints columnConstraints0;
    protected final ColumnConstraints columnConstraints1;
    protected final RowConstraints rowConstraints;
    protected final RowConstraints rowConstraints0;
    protected final RowConstraints rowConstraints1;
    protected final Button bt1;
    protected final Button bt2;
    protected final Button bt3;
    protected final Button bt4;
    protected final Button bt5;
    protected final Button bt6;
    protected final Button bt7;
    protected final Button bt8;
    protected final Button bt9;
    protected final AnchorPane anchorPane0;
    protected final ImageView imageView0;
    protected final TextArea chatArea;
    protected final TextField messageField;
    protected final Button sendMsg;
    protected final Button recordGame;
    protected final Button exitGame;
    
    private  ArrayList<Integer> playerXpositions = new ArrayList<Integer>();
    private  ArrayList<Integer> playerOpositions = new ArrayList<Integer>();
    
    private String symbol;
    Button[] buttons;
    private boolean gameFlag;
    String[] recordedPositions;
    private int index;
    
    ObjectInputStream readObj;
    ObjectOutputStream writeObj;
    Game myGame;
    private String mySymbol;
    private volatile boolean connected;
    private JSONObject obj;
    private JSONParser parser;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    
    private String checkWinner(){
        String winner = null;
        List topRow = Arrays.asList(1,2,3);
        List midRow = Arrays.asList(4,5,6);
        List botRow = Arrays.asList(7,8,9);
        
        List liftCol = Arrays.asList(1,4,7);
        List midtCol = Arrays.asList(2,5,8);
        List righCol = Arrays.asList(3,6,9);
        
        List cross1 = Arrays.asList(1,5,9);
        List cross2 = Arrays.asList(3,5,7);
        
        List<List> winning = new ArrayList<List>();
        
        winning.add(topRow);
        winning.add(midRow);
        winning.add(botRow);
        winning.add(liftCol);
        winning.add(midtCol);
        winning.add(righCol);
        winning.add(cross1);
        winning.add(cross2);
        
        for(List winningComp: winning){
           
            if(playerXpositions.containsAll(winningComp)){
                winner = "X";
                 playerXpositions.clear();
                 playerXpositions.addAll(winningComp);
            }
            else if(playerOpositions.containsAll(winningComp)){
                winner = "O";
                 playerOpositions.clear();
                 playerOpositions.addAll(winningComp);
            }
            else if(playerXpositions.size() + playerOpositions.size() == 9)
                winner =  "tie";
        }
        return winner;
    }
    private void highLightWinner(ArrayList<Integer> winner){
        // will take the winner position and highlight each button equl to this position
        
        for(int i=0; i<9; i++){
            if( i+1 == (int) winner.get(0) || i+1 == (int) winner.get(1) || i+1 == (int) winner.get(2))
                buttons[i].setStyle("-fx-background-color: #90EE90");
        }         
    }
    
    private void recordPositions(){
        
        // save the value of each buttons of the 9'th, X or O or bull
        for(int index = 0; index <9; index++){
            recordedPositions[index] = buttons[index].getText();
        }
    }
    
    private void drawPositions(int position,String xAndo)
    {
        // will append symbol, and disable  buttons according to  last saved
//        for(int index = 0; index <9; index++){
//            System.out.println("inside draw position: " + recordedPositions[index]);
//            if(positions[index].equalsIgnoreCase("X") || positions[index].equalsIgnoreCase("O")){
//                // recordedPositions[index].equalsIgnoreCase("X") || recordedPositions[index].equalsIgnoreCase("O")  
//                // positions[index].equalsIgnoreCase("X") || positions[index].equalsIgnoreCase("O") 
////                System.out.println("inside draw position: " + recordedPositions[index]);
//                buttons[index].setText(positions[index]);
//                buttons[index].setDisable(true);
//            }
//        }
//        for(Integer i: x){
//            buttons[i-1].setText("X");
//            buttons[i-1].setDisable(true);
//            
//        }
//        for(Integer i: o){
//            buttons[i-1].setText("O");
//            buttons[i-1].setDisable(true);
//            
//        }

        buttons[position-1].setText(xAndo);
        buttons[position-1].setDisable(true);
    }
    
    private void drawTie(){
        for(Button btn: buttons){
            btn.setText("Tie");
        }
    }
    
    private void drawXO(){
        for(String str: recordedPositions){
            System.out.println(str);
        }
    }
    private void drawXOmyGame(){
//        String[] arr = new String[9];
//        arr = myGame.getGamePosition();

        for(String str: recordedPositions){
            System.out.println(str);
        }
    }
    
    /*private void position(){
        for(Integer x: playerXpositions){
            System.out.print("X: "+ x );
        }
        System.out.println("new line");
         for(Integer o: playerOpositions){
            System.out.print("O " + o);
        }
    }*/
    
    private void activeBoard(){
        for(Button btn: buttons){
            btn.setDisable(false);
        }
    }
    private void disActiveBoard(){
        for(Button btn: buttons){
            btn.setDisable(true);
        }
    }
    
    private void sendGameMove(){
        try {
            
            obj = new JSONObject();
            obj  = JsonConverter.gameToJson(myGame);
            
            int location = (int) obj.get("position");
            String xAndo = (String) obj.get("xAndo");
            System.out.println("Sending->" + xAndo + " to position " + location);
            this.outStream.writeUTF(obj.toString());
        } catch (IOException ex) {
            Logger.getLogger(XOBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void startGame(){
        
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                
                try {
                    
                    Socket socket = new Socket("127.0.0.1", 5005);
                    outStream = new DataOutputStream(socket.getOutputStream());
                    inStream = new DataInputStream(socket.getInputStream());
                    
                    // receve the symbol 
                    
                    obj = (JSONObject) parser.parse(inStream.readUTF());
                    mySymbol =(String) obj.get("symbol");
                    connected = true;
                    
                    System.out.println("reciving game symbol " + mySymbol + " from gmae handler");
                    System.out.println("and this is my  symbol " + symbol);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } catch (ParseException ex) {
                    Logger.getLogger(XOBase.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (connected) {
                    try {
                        
                        myGame = new Game();
                        System.out.println("While: waiting for obj from gameHandler");
                        obj = (JSONObject) parser.parse(inStream.readUTF());

                        System.out.println("receving game obj from game Handler");
                        
                        symbol = (String) obj.get("symbol");
                        System.out.println(" this is " + symbol + "turn");
                        Long location =(Long) obj.get("position");
                        String xAndo = (String) obj.get("xAndo");
                        System.out.println("location of " + xAndo + " " +  location.intValue());

                          Platform.runLater(() -> {
                              drawPositions(location.intValue(), xAndo );
                          });
                        
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (ParseException ex) {
                        Logger.getLogger(XOBase.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        th.start();
        
    }
    public XOBase() {
        myGame = new Game();
        index = 1;
        startGame();
        symbol = "X";
        parser = new JSONParser();
        
        splitPane = new SplitPane();
        anchorPane = new AnchorPane();
        imageView = new ImageView();
        gridPane = new GridPane();
        columnConstraints = new ColumnConstraints();
        columnConstraints0 = new ColumnConstraints();
        columnConstraints1 = new ColumnConstraints();
        rowConstraints = new RowConstraints();
        rowConstraints0 = new RowConstraints();
        rowConstraints1 = new RowConstraints();
        
        recordedPositions = new String[9];
        gameFlag = true;
        buttons = new Button[9];
        
        bt1 = new Button();
        bt2 = new Button();
        bt3 = new Button();
        bt4 = new Button();
        bt5 = new Button();
        bt6 = new Button();
        bt7 = new Button();
        bt8 = new Button();
        bt9 = new Button();
        
        buttons[0] = bt1;
        buttons[1] = bt2;
        buttons[2] = bt3;
        buttons[3] = bt4;
        buttons[4] = bt5;
        buttons[5] = bt6;
        buttons[6] = bt7;
        buttons[7] = bt8;
        buttons[8] = bt9;
        for(Button btn: buttons){
            btn.setFont(Font.font("MV Boli", FontWeight.BOLD, 24));
            /*btn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    if(gameFlag){
                        Button bt =  (Button) event.getSource();
                    btn.setText(symbol);
                    btn.setDisable(true);
                    if(symbol == "X"){
                        playerXpositions.add();
                        symbol = "O";
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            gameFlag = false;
                        }else if(checkWinner() == "tie"){
                            drawTie();
                        }
                            

                    }
                    else{
                        playerOpositions.add();
                        index++;
                        symbol = "X";
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            gameFlag = false;
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                        }
                    }
                }
                    
                }
            });*/
            
        }
        
        
        bt1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + symbol + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(symbol)){
                    System.out.println("set btn txt");
                    bt1.setText(symbol);
                    bt1.setDisable(true);
                    
                    if(symbol.equalsIgnoreCase("X")){
                        
                        playerXpositions.add(1);
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                            
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(1);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                        
                    }
                    else{
                        playerOpositions.add(1);
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(1);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        });
        bt2.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                System.out.println("is game turn " + symbol + " == " + mySymbol);

                if(mySymbol.equalsIgnoreCase(symbol)){
                    bt2.setText(symbol);
                    bt2.setDisable(true);
                    
                    if(symbol.equalsIgnoreCase("X")){
                        
                        playerXpositions.add(2);
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(2);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                            

                    }
                    else{
                        playerOpositions.add(2);
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(2);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt3.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    
                    bt3.setText(symbol);
                    bt3.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        playerXpositions.add(3);
                        
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(3);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();

                        }
                            

                    }
                    else{
                        playerOpositions.add(3);
                        
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(3);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt4.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    
                    bt4.setText(symbol);
                    bt4.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        
                        playerXpositions.add(4);
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(4);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                            

                    }
                    else{
                        playerOpositions.add(4);
                        
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(4);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt5.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    
                    bt5.setText(symbol);
                    bt5.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        playerXpositions.add(5);
                        
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(5);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                            

                    }
                    else{
                        playerOpositions.add(5);
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(5);
                             myGame.setSymbol("X");
                             System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                             sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt6.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    
                    bt6.setText(symbol);
                    bt6.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        playerXpositions.add(6);
                       
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
//                            gameFlag = false;
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(6);
                             myGame.setSymbol("O");
                             System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                             sendGameMove();
                        }
                            

                    }
                    else{
                        playerOpositions.add(6);
                        
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
//                            gameFlag = false;
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(6);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt7.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    
                    bt7.setText(symbol);
                    bt7.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        playerXpositions.add(7);
                        
                        
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
//                            gameFlag = false;
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(7);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                            
                            

                    }
                    else{
                        playerOpositions.add(7);
                        
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(7);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt8.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    
                    bt8.setText(symbol);
                    bt8.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        playerXpositions.add(8);
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(8);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                            

                    }
                    else{
                        playerOpositions.add(8);
                        
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                            
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(8);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        bt9.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(mySymbol.equalsIgnoreCase(symbol)){
                    bt9.setText(symbol);
                    bt9.setDisable(true);
                    if(symbol.equalsIgnoreCase("X")){
                        playerXpositions.add(9);
                        if(checkWinner() == "X"){
                            highLightWinner(playerXpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("X");
                            sendGameMove();
                        }else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(9);
                            myGame.setSymbol("O");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                            

                    }
                    else{
                        playerOpositions.add(9);
                        
                        if(checkWinner() == "O"){
                            highLightWinner(playerOpositions);
                            myGame.setGameFlag(false);
                            myGame.setWinner("O");
                            sendGameMove();
                        }
                        else if(checkWinner() == "tie"){
                            drawTie();
                            sendGameMove();
                        }else{
                            myGame.setxAndo(symbol);
                            myGame.setLocation(9);
                            myGame.setSymbol("X");
                            System.out.println("Sending move the next turn is " + myGame.getSymbol() );
                            sendGameMove();
                        }
                    }
                }
            }
        
        
        });
        
        anchorPane0 = new AnchorPane();
        imageView0 = new ImageView();
        chatArea = new TextArea();
        messageField = new TextField();
        sendMsg = new Button();
        recordGame = new Button();
        exitGame = new Button();

        setId("AnchorPane");
        setPrefHeight(603.0);
        setPrefWidth(857.0);

        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setDividerPositions(0.5614035087719298);
        splitPane.setLayoutX(188.0);
        splitPane.setLayoutY(125.0);
        splitPane.setPrefHeight(500.0);
        splitPane.setPrefWidth(543.0);

        anchorPane.setMinHeight(0.0);
        anchorPane.setMinWidth(0.0);
        anchorPane.setPrefHeight(160.0);
        anchorPane.setPrefWidth(100.0);

        imageView.setPickOnBounds(true);
        imageView.setImage(new Image(getClass().getResource("2.jpeg").toExternalForm()));

        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);
        AnchorPane.setTopAnchor(gridPane, 0.0);
        gridPane.setFocusTraversable(true);
        gridPane.setGridLinesVisible(true);
        gridPane.setLayoutX(101.0);
        gridPane.setLayoutY(159.0);
        gridPane.setPrefHeight(498.0);
        gridPane.setPrefWidth(389.0);

        columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints.setMinWidth(10.0);
        columnConstraints.setPrefWidth(100.0);

        columnConstraints0.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints0.setMinWidth(10.0);
        columnConstraints0.setPrefWidth(100.0);

        columnConstraints1.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        columnConstraints1.setMinWidth(10.0);
        columnConstraints1.setPrefWidth(100.0);

        rowConstraints.setMinHeight(10.0);
        rowConstraints.setPrefHeight(30.0);
        rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints0.setMinHeight(10.0);
        rowConstraints0.setPrefHeight(30.0);
        rowConstraints0.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        rowConstraints1.setMinHeight(10.0);
        rowConstraints1.setPrefHeight(30.0);
        rowConstraints1.setVgrow(javafx.scene.layout.Priority.SOMETIMES);

        bt1.setMnemonicParsing(false);
        bt1.setPrefHeight(232.0);
        bt1.setPrefWidth(159.0);
        bt1.setStyle("-fx-background-color: 0");

        GridPane.setColumnIndex(bt2, 1);
        bt2.setMnemonicParsing(false);
        bt2.setPrefHeight(223.0);
        bt2.setPrefWidth(271.0);
        bt2.setStyle("-fx-background-color: 0");

        GridPane.setColumnIndex(bt3, 2);
        bt3.setMnemonicParsing(false);
        bt3.setPrefHeight(235.0);
        bt3.setPrefWidth(254.0);
        bt3.setStyle("-fx-background-color: 0");

        GridPane.setRowIndex(bt4, 1);
        bt4.setMnemonicParsing(false);
        bt4.setPrefHeight(210.0);
        bt4.setPrefWidth(168.0);
        bt4.setStyle("-fx-background-color: 0");

        GridPane.setColumnIndex(bt5, 1);
        GridPane.setRowIndex(bt5, 1);
        bt5.setMnemonicParsing(false);
        bt5.setPrefHeight(199.0);
        bt5.setPrefWidth(271.0);
        bt5.setStyle("-fx-background-color: 0");

        GridPane.setColumnIndex(bt6, 2);
        GridPane.setRowIndex(bt6, 1);
        bt6.setMnemonicParsing(false);
        bt6.setPrefHeight(256.0);
        bt6.setPrefWidth(239.0);
        bt6.setStyle("-fx-background-color: 0");

        GridPane.setRowIndex(bt7, 2);
        bt7.setMnemonicParsing(false);
        bt7.setPrefHeight(296.0);
        bt7.setPrefWidth(159.0);
        bt7.setStyle("-fx-background-color: 0");

        GridPane.setColumnIndex(bt8, 1);
        GridPane.setRowIndex(bt8, 2);
        bt8.setMnemonicParsing(false);
        bt8.setPrefHeight(276.0);
        bt8.setPrefWidth(159.0);
        bt8.setStyle("-fx-background-color: 0");

        GridPane.setColumnIndex(bt9, 2);
        GridPane.setRowIndex(bt9, 2);
        bt9.setMnemonicParsing(false);
        bt9.setPrefHeight(284.0);
        bt9.setPrefWidth(158.0);
        bt9.setStyle("-fx-background-color: 0");

        anchorPane0.setMinHeight(0.0);
        anchorPane0.setMinWidth(0.0);
        anchorPane0.setPrefHeight(160.0);
        anchorPane0.setPrefWidth(100.0);

        imageView0.setLayoutY(8.0);
        imageView0.setPickOnBounds(true);
        imageView0.setImage(new Image(getClass().getResource("2.jpeg").toExternalForm()));

        AnchorPane.setLeftAnchor(chatArea, 0.0);
        AnchorPane.setRightAnchor(chatArea, 0.0);
        AnchorPane.setTopAnchor(chatArea, 0.0);
        chatArea.setEditable(false);
        chatArea.setLayoutX(1.0);
        chatArea.setLayoutY(8.0);
        chatArea.setPrefHeight(320.0);
        chatArea.setPrefWidth(371.0);

        AnchorPane.setBottomAnchor(messageField, 254.0);
        AnchorPane.setLeftAnchor(messageField, 0.0);
        AnchorPane.setRightAnchor(messageField, 50.0);
        AnchorPane.setTopAnchor(messageField, 321.0);
        messageField.setLayoutX(1.0);
        messageField.setLayoutY(321.0);
        messageField.setPrefHeight(26.0);
        messageField.setPrefWidth(223.0);

        AnchorPane.setBottomAnchor(sendMsg, 254.0);
        AnchorPane.setLeftAnchor(sendMsg, 321.0);
        AnchorPane.setRightAnchor(sendMsg, -1.0);
        AnchorPane.setTopAnchor(sendMsg, 321.0);
        sendMsg.setLayoutX(321.0);
        sendMsg.setLayoutY(321.0);
        sendMsg.setMnemonicParsing(false);
        sendMsg.setText("Send");

        recordGame.setLayoutX(14.0);
        recordGame.setLayoutY(545.0);
        recordGame.setMnemonicParsing(false);
        recordGame.setText("Record Game");
        recordGame.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                recordPositions();
                drawXO();
            }
        
        });

        exitGame.setLayoutX(293.0);
        exitGame.setLayoutY(545.0);
        exitGame.setMnemonicParsing(false);
        exitGame.setPrefHeight(26.0);
        exitGame.setPrefWidth(64.0);
        exitGame.setText("Exit");

        anchorPane.getChildren().add(imageView);
        gridPane.getColumnConstraints().add(columnConstraints);
        gridPane.getColumnConstraints().add(columnConstraints0);
        gridPane.getColumnConstraints().add(columnConstraints1);
        gridPane.getRowConstraints().add(rowConstraints);
        gridPane.getRowConstraints().add(rowConstraints0);
        gridPane.getRowConstraints().add(rowConstraints1);
        gridPane.getChildren().add(bt1);
        gridPane.getChildren().add(bt2);
        gridPane.getChildren().add(bt3);
        gridPane.getChildren().add(bt4);
        gridPane.getChildren().add(bt5);
        gridPane.getChildren().add(bt6);
        gridPane.getChildren().add(bt7);
        gridPane.getChildren().add(bt8);
        gridPane.getChildren().add(bt9);
        anchorPane.getChildren().add(gridPane);
        splitPane.getItems().add(anchorPane);
        anchorPane0.getChildren().add(imageView0);
        anchorPane0.getChildren().add(chatArea);
        anchorPane0.getChildren().add(messageField);
        anchorPane0.getChildren().add(sendMsg);
        anchorPane0.getChildren().add(recordGame);
        anchorPane0.getChildren().add(exitGame);
        splitPane.getItems().add(anchorPane0);
        getChildren().add(splitPane);

    }
    
    
}
