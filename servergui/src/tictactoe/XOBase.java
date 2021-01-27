package tictactoe;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
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
    
    private String currentMove; //symbol
    private Button[] buttons;
    private String[] recordedPositions;
    
    
    Game myGame;
    private String mySymbol;
    private volatile boolean connected;
    
    
    private DataInputStream inStream;
    private PrintStream outStream;
    
    
    
    private String checkWinner(){
        String winner = "";
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
                winner = Game.X_MOVE;
                 playerXpositions.clear();
                 playerXpositions.addAll(winningComp);
            }
            else if(playerOpositions.containsAll(winningComp)){
                winner = Game.O_MOVE;
                 playerOpositions.clear();
                 playerOpositions.addAll(winningComp);
            }
            else if(playerXpositions.size() + playerOpositions.size() == 9)
                winner =  Game.DRAW;
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
    
    private void drawPositions(int position,String playedMove)
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
        
        buttons[position-1].setText(playedMove);
        buttons[position-1].setDisable(true);
        
        if(playedMove.equalsIgnoreCase(Game.X_MOVE)){
            
            playerXpositions.add(position);
            
        }else{
            
            playerOpositions.add(position);
        }
        
        
        if(checkWinner().equalsIgnoreCase(Game.X_MOVE)){
            
            highLightWinner(playerXpositions);
            
        }else if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
            
             highLightWinner(playerOpositions);
             
        }else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
            drawTie();
        }
    }
    
    private void drawTie(){
        
        for(Button btn: buttons){
            btn.setText(Game.DRAW);
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
    
   
    
    private void sendGameMove(){
        
        
        int position = myGame.getPosition();
        String playedMove = myGame.getPlayedMove(); //myGame.getSymbol();
        System.out.println("Sending->" + playedMove + " to position " + position);
        this.outStream.println(new Gson().toJson(myGame));
    }
    
    
    private void startGame(){
        
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                
                try {
                    
                    Socket socket = new Socket("127.0.0.1", 5005);
                    outStream = new PrintStream(socket.getOutputStream());
                    inStream = new DataInputStream(socket.getInputStream());
                    
                    // receve the symbol 
                    myGame =  new Gson().fromJson(inStream.readLine(), Game.class);
                    
                    mySymbol = myGame.getNextMove();
                    connected = true;
                    
                    System.out.println("reciving game symbol " + mySymbol + " from gmae handler");
                    System.out.println("and this is my  symbol " + currentMove);
                    
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                while (connected) {
                    
                    try {
                        
                        
                        System.out.println("While: waiting for obj from gameHandler");
                        String obj = inStream.readLine();
                        myGame =  new Gson().fromJson(obj, Game.class);

                        System.out.println("receving game obj from game Handler");
                        
                        currentMove = myGame.getNextMove();
                        System.out.println(" this is " + currentMove + " turn");
                        
                        int position = myGame.getPosition();
                        String xAndo = myGame.getPlayedMove();
                        System.out.println("location of " + xAndo + " " +  position);

                          Platform.runLater(() -> {
                              drawPositions(position, xAndo );
                          });
                        
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        th.start();
        
    }
    
    public XOBase() {
        
        myGame = new Game();
        startGame();
        currentMove = Game.X_MOVE;
        
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
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
//                    bt1.setText(currentMove);
//                    bt1.setDisable(true);
//                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(1);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER); 
                            myGame.setPosition(1);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(1);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(1);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(1);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(1);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(1);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(1);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove());
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        });
        bt2.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
//                    bt2.setText(currentMove);
//                    bt2.setDisable(true);
//                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(2);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(2);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(2);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(2);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(2);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(2);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(2);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(2);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
            
        
        
        });
        bt3.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt3.setText(currentMove);
                    bt3.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(3);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(3);
                            myGame.setNextMove(Game.GAME_OVER);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(3);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(3);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(3);
                        
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(3);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(3);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(3);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        
        });
        bt4.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt4.setText(currentMove);
                    bt4.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(4);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(4);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(4);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(4);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(4);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(4);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(4);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(4);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        
        });
        bt5.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt5.setText(currentMove);
                    bt5.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(5);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(5);
                            myGame.setNextMove(Game.GAME_OVER);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(5);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(5);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(5);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(5);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(5);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(5);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove());
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        
        });
        bt6.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt6.setText(currentMove);
                    bt6.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(6);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(6);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(6);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(6);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(6);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(6);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(6);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(6);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        
        });
        bt7.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt7.setText(currentMove);
                    bt7.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(7);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(7);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(7);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(7);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(7);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(7);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(7);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(7);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        
        });
        bt8.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt8.setText(currentMove);
                    bt8.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(8);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(8);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(8);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(8);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(8);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(8);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(8);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(8);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                        
                        
                    }   
                }
            }
        
        
        });
        bt9.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                System.out.println("is  game turn " + currentMove + " == " + mySymbol);
                
                if(mySymbol.equalsIgnoreCase(currentMove)){
                    bt9.setText(currentMove);
                    bt9.setDisable(true);
                    
                    if(currentMove.equalsIgnoreCase(Game.X_MOVE)){
                        
                        playerXpositions.add(9);
                        if(checkWinner() == Game.X_MOVE){
                            highLightWinner(playerXpositions);
                            myGame.setWinner(Game.X_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(9);
                            sendGameMove();
                        }else if(checkWinner() == Game.DRAW){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(9);
                            sendGameMove();
                            
                        }else{                        
                            myGame.setPlayedMove(currentMove); 
                            myGame.setPosition(9);  
                            myGame.setNextMove(Game.O_MOVE); 
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
                            sendGameMove();
                        }
                    }
                    else{
                        playerOpositions.add(9);
                        if(checkWinner().equalsIgnoreCase(Game.O_MOVE)){
                            highLightWinner(playerOpositions);
                            myGame.setWinner(Game.O_MOVE);
                            myGame.setPlayedMove(currentMove);
                            myGame.setNextMove(Game.GAME_OVER);
                            myGame.setPosition(9);
                            sendGameMove();
                        }
                        else if(checkWinner().equalsIgnoreCase(Game.DRAW)){
                            drawTie();
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(9);
                            sendGameMove();
                        }else{
                            myGame.setPlayedMove(currentMove);
                            myGame.setPosition(9);
                            myGame.setNextMove(Game.X_MOVE);
                            System.out.println("Sending move the next turn is " + myGame.getNextMove() );
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
