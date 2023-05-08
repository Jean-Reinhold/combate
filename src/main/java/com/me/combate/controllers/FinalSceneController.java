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
import com.me.combate.essentials.SceneManager;
/**
 * FXML Controller class
 *
 * @author rafaelboeira
 */
public class FinalSceneController implements Initializable {

    @FXML
    private Label lb_winner;
    @FXML
    private Button bt_winner;
    @FXML
    private Button bt_image;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }    

    @FXML
    private void goToMenu(ActionEvent event) {
        SceneManager sm = Main.getSceneManager();
        
        sm.setScene("menu");
    }
    
}
