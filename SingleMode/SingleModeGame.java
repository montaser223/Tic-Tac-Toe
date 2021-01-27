package SingleMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SingleModeGame extends AnchorPane {

    protected final Button bt1;
    protected final Button bt2;
    protected final Button bt3;
    protected final Button bt4;
    protected final Button bt5;
    protected final Button bt6;
    protected final Button bt7;
    protected final Button bt8;
    protected final Button bt9;
    protected final Button recordGame;
    protected final Button exit;
    protected final Label playerName;
    protected final Label computerName;
    protected final Label playerScore;
    protected final Label scoreSeperator;
    protected final Label computerScore;

    private ArrayList<Integer> playerXpositions = new ArrayList<Integer>();
    private ArrayList<Integer> playerOpositions = new ArrayList<Integer>();

    private String symbol;
    private int move;
    Button[] buttons;
    private boolean gameFlag;
    String[] recordedPositions;
    
    private int playerScoreCounter;
    private int computerScoreCounter;
    private int index;
    private static String username;
    
    private String checkWinner() {
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

    private void highLightWinner(ArrayList<Integer> winner) {
        // will take the winner position and highlight each button equl to this position

        for (int i = 0; i < 9; i++) {
            if (i + 1 == (int) winner.get(0) || i + 1 == (int) winner.get(1) || i + 1 == (int) winner.get(2)) {
                buttons[i].setStyle("-fx-background-color: #90EE90");
            }
        }
    }

    private String[] recordPositions() {
        // save the value of each buttons of the 9'th, X or O or bull
        recordedPositions = new String[9];
        for (int index = 0; index < 9; index++) {
            recordedPositions[index] = buttons[index].getText();
        }
        return recordedPositions;
    }

    private void drawOldPositions(String[] positions) {
        // will append symbol, and disable  buttons according to  last saved
        for (int index = 0; index < 9; index++) {

            if (positions[index] != null) {
                buttons[index].setText(positions[index]);
                buttons[index].setDisable(true);
                if(positions[index] == "X")
                    playerXpositions.add(index+1);
                else
                    playerOpositions.add(index+1);
            }
        }
        //change the symbol to the right turn
        if(playerXpositions.size() > playerOpositions.size()){
            symbol = "O";
            playComputerMove();
        }

    }

    private void drawTie() {
        for (Button btn : buttons) {
            btn.setText("Tie");
        }
    }

    private void drawXO() {
        for (String str : recordedPositions) {
            System.out.println(str);
        }
    }

    private int computerMove() {
        Random random = new Random();
        int cpuPosition = random.nextInt(9)+ 1;
        // make sure that this position is not taken before
        while(playerXpositions.contains(cpuPosition)|| playerOpositions.contains(cpuPosition)){
            cpuPosition = random.nextInt(9) +1;
        }
        return cpuPosition;
    }
    
    private void drawCpuMove(int movePosition) {
        movePosition--; // to indecate the index of the button
        for (int index = 0; index < 9; index++) {
            if (index == movePosition) {
                buttons[index].setText(symbol);
                buttons[index].setDisable(true);
            }
        }
    }
    
    private void playComputerMove() {
        if(gameFlag){
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

    private void playAgain(){
        recordGame.setText("Record Game");
        gameFlag = true;
        symbol = "X";
        playerXpositions.clear();
        playerOpositions.clear();
        for(Button btn: buttons){
            
            btn.setText("");
            btn.setDisable(false);
            
            // reset the old style
            Button tmpButton = new Button();
            btn.setStyle(tmpButton.getStyle());
            
        }
    }
    
    private void updateScore(){
        playerScore.setText(String.valueOf(playerScoreCounter));
        computerScore.setText(String.valueOf(computerScoreCounter));
        recordGame.setText("Play Again!");
    }
    
    public static void setPlayerName(String playerName){
        
        username = playerName;
    }
    public SingleModeGame() {
        
        playerScoreCounter = 0;
        computerScoreCounter = 0;
        recordedPositions = new String[9];
        gameFlag = true;
        symbol = "X";
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
        
        for (index=0; index<9; index++) {
            buttons[index].setFont(Font.font("MV Boli", FontWeight.BOLD, 24));
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
                        }else{
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
                        }else{
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
                        }
                        else{
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
                        }else{
                            playComputerMove();
                        }
                    }                 }
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
                        }else{
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
                        }else{
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
                        }else{
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
                        }else{
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
                            
                        }else{
                            playComputerMove();
                        }
                    }
                }
            }

        });

        recordGame = new Button();

        recordGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!gameFlag){
                    playAgain();
                }else{
                    recordPositions();
                    drawXO();
                }
                
            }

        });

        exit = new Button();
        playerName = new Label();
        computerName = new Label();
        playerScore = new Label();
        scoreSeperator = new Label();
        computerScore = new Label();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(537.0);
        setPrefWidth(652.0);

        bt1.setLayoutX(14.0);
        bt1.setLayoutY(14.0);
        bt1.setMnemonicParsing(false);
        bt1.setPrefHeight(103.0);
        bt1.setPrefWidth(117.0);

        bt2.setLayoutX(148.0);
        bt2.setLayoutY(14.0);
        bt2.setMnemonicParsing(false);
        bt2.setPrefHeight(103.0);
        bt2.setPrefWidth(117.0);

        bt3.setLayoutX(276.0);
        bt3.setLayoutY(14.0);
        bt3.setMnemonicParsing(false);
        bt3.setPrefHeight(103.0);
        bt3.setPrefWidth(117.0);

        bt4.setLayoutX(17.0);
        bt4.setLayoutY(137.0);
        bt4.setMnemonicParsing(false);
        bt4.setPrefHeight(103.0);
        bt4.setPrefWidth(117.0);

        bt5.setLayoutX(151.0);
        bt5.setLayoutY(137.0);
        bt5.setMnemonicParsing(false);
        bt5.setPrefHeight(103.0);
        bt5.setPrefWidth(117.0);

        bt6.setLayoutX(279.0);
        bt6.setLayoutY(137.0);
        bt6.setMnemonicParsing(false);
        bt6.setPrefHeight(103.0);
        bt6.setPrefWidth(117.0);

        bt7.setLayoutX(20.0);
        bt7.setLayoutY(270.0);
        bt7.setMnemonicParsing(false);
        bt7.setPrefHeight(103.0);
        bt7.setPrefWidth(117.0);

        bt8.setLayoutX(154.0);
        bt8.setLayoutY(270.0);
        bt8.setMnemonicParsing(false);
        bt8.setPrefHeight(103.0);
        bt8.setPrefWidth(117.0);

        bt9.setLayoutX(282.0);
        bt9.setLayoutY(270.0);
        bt9.setMnemonicParsing(false);
        bt9.setPrefHeight(103.0);
        bt9.setPrefWidth(117.0);

        recordGame.setLayoutX(50.0);
        recordGame.setLayoutY(484.0);
        recordGame.setMnemonicParsing(false);
        recordGame.setText("Record Game");

        exit.setLayoutX(356.0);
        exit.setLayoutY(484.0);
        exit.setMnemonicParsing(false);
        exit.setText("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
            
        });

        playerName.setLayoutX(421.0);
        playerName.setLayoutY(58.0);
        playerName.setText(username);

        computerName.setLayoutX(573.0);
        computerName.setLayoutY(58.0);
        computerName.setText("Computer");

        playerScore.setLayoutX(456.0);
        playerScore.setLayoutY(101.0);
        playerScore.setText(String.valueOf(playerScoreCounter));
        playerScore.setFont(new Font(19.0));

        scoreSeperator.setLayoutX(528.0);
        scoreSeperator.setLayoutY(47.0);
        scoreSeperator.setPrefWidth(28.0);
        scoreSeperator.setText(":");
        scoreSeperator.setFont(new Font(31.0));

        computerScore.setLayoutX(599.0);
        computerScore.setLayoutY(101.0);
        computerScore.setText(String.valueOf(computerScoreCounter));
        computerScore.setFont(new Font(19.0));

        getChildren().add(bt1);
        getChildren().add(bt2);
        getChildren().add(bt3);
        getChildren().add(bt4);
        getChildren().add(bt5);
        getChildren().add(bt6);
        getChildren().add(bt7);
        getChildren().add(bt8);
        getChildren().add(bt9);
        getChildren().add(recordGame);
        getChildren().add(exit);
        getChildren().add(playerName);
        getChildren().add(computerName);
        getChildren().add(playerScore);
        getChildren().add(scoreSeperator);
        getChildren().add(computerScore);

    }
}
