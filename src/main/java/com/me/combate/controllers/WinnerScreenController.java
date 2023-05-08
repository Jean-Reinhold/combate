/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import com.me.combate.Main;

/**
 * FXML Controller class
 *
 * @author rafaelboeira
 */
public class WinnerScreenController implements Initializable {
    private static String winner = "user";
    @FXML
    private Label lb_winner;
    @FXML
    private ImageView img_winner;
    @FXML
    private Button bt_back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (winner.equals("user"))
            lb_winner.setText("VOCÊ VENCEU!!!");
        if (winner.equals("machine"))
            lb_winner.setText("VOCÊ PERDEU...");
        if (winner.equals("draw"))
            lb_winner.setText("EMPATE...");
            
        img_winner.getStyleClass().setAll(winner);
        bt_back.getStyleClass().setAll("bt");
    }    
    
    public static void setWinner(String result){
        System.out.println(result);
        winner = result;
    }

    @FXML
    private void goToMenu(ActionEvent event) {
        try{
            Main.getSceneManager().resetScenes();
        }catch(Exception e){
            return;
        }
        Main.getSceneManager().setScene("menu");
    }
    
}
