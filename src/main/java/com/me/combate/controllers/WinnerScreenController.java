/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.me.combate.essentials.SceneManager;
import javafx.application.Platform;
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
    private static Label lb_winner = new Label();

    @FXML
    private ImageView img_winner;

    @FXML
    private Button bt_back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateWinnerLabel();
    }

    public static void setWinner(String result) {
        winner = result;
        Platform.runLater(() -> updateWinnerLabel());
    }

    private static void updateWinnerLabel() {
        if (winner.equals("user")) {
            lb_winner.setText("VOCÊ VENCEU!!!");
        }
        if (winner.equals("machine")) {
            lb_winner.setText("VOCÊ PERDEU...");
        }
        if (winner.equals("draw")) {
            lb_winner.setText("EMPATE...");
        }
    }

    @FXML
    private void goToMenu(ActionEvent event) {
        try {
            Main.getSceneManager().resetScenes();
        } catch (Exception e) {
            // Do nothing
        }
        Main.getSceneManager().setScene("menu");
    }

}
