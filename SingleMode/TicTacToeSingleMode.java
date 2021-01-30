/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SingleMode;

import tictactoe.*;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author abobakr
 */
public class TicTacToeSingleMode extends Application {

    BorderPane border;
    Button[] buttos;   
    
    
    @Override
    public void start(Stage stage) {
        
        SingleModeGame.setPlayerName("abobakr");
        String[] p = new String[9];
        p[1]= "X";
        p[2]="O";
        SingleModeGame.setRecordedPosition(p);
        Parent root = new SingleModeGame();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
