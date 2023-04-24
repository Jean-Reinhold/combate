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

/**
 * FXML Controller class
 *
 * @author rafaelboeira
 */
public class MenuController implements Initializable {

    @FXML
    private Button bt_new_game;
    @FXML
    private Button bt_about;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void startGame(ActionEvent event) throws IOException {
        Main.setRoot("inGame");
    }

    @FXML
    private void goToAbout(ActionEvent event) throws IOException {
        Main.setRoot("about");
    }
    
}
