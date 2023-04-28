/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.me.combate.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.IOException;
import com.me.combate.essentials.SceneManager;
import javafx.application.Platform;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author rafaelboeira
 */
public class MenuController implements Initializable {

    @FXML
    private Button bt_about;
    @FXML
    private Button bt_exit;
    @FXML
    private Button bt_restart;
    @FXML
    private Button bt_manual;
    @FXML
    private Button bt_random;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void startGame(ActionEvent event) throws IOException {
        SceneManager sm = Main.getSceneManager();
        
        sm.resetScenes();
        sm.setScene("inGame");
        
        Button clicked_button = (Button) event.getSource();
        if (clicked_button.getId().equals("bt_manual")){
            sm.getStage().setUserData(1);
        }
        else{
            sm.getStage().setUserData(2);
        }
    }

    @FXML
    private void goToAbout(ActionEvent event) throws IOException {
        SceneManager sm = Main.getSceneManager();
        
        sm.setScene("about");
    }
    
    public void showRestartButton(){
        bt_restart.setVisible(true);
    }

    @FXML
    private void closeGame(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void restartGame(ActionEvent event) {
        SceneManager sm = Main.getSceneManager();
        bt_restart.setVisible(false);
        sm.setScene("inGame");
    }
    
}
