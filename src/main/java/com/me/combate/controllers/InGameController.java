/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import com.me.combate.Main;
import com.me.combate.essentials.SceneManager;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InGameController implements Initializable {
    private final int GRID_SIZE = 5;
    private final int USER_MAX_Y = 1;
    private boolean in_debug_mode = false;
    private GameBoard gameBoard;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gameBoard = new GameBoard();
    }
}