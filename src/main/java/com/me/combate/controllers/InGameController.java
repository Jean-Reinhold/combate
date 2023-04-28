/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.me.combate.Main;
import com.me.combate.essentials.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.Event;
import javafx.scene.text.TextAlignment;

public class InGameController implements Initializable {
    private Button selected_button = null;
    private boolean in_debug_mode = false;
    private int positioned_pieces = 0;
    private int GRID_SIZE = 5;
    private int USER_MAX_Y = 1;

    @FXML
    private GridPane gd_table;
    @FXML
    private Button bt_begin;
    @FXML
    private Button bt_debug;
    @FXML
    private Button bt_back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createEmptyGrid();
    }

    private void createEmptyGrid(){
        for (int i=0; i<this.getGridSize(); i++){
            for (int j=0; j<this.getGridSize(); j++){
                Button bt = new Button();
                bt.setText("");
                bt.setId("bt_"+i+""+j);
                bt.setWrapText(true);
                bt.setTextAlignment(TextAlignment.CENTER);

                bt.setOnAction(eh -> {
                    if (this.selected_button != null && this.bt_begin.isDisabled()){
                        this.insertPiece(eh);
                    }
                    if (this.bt_begin.isVisible() == false)
                        this.selectButton(eh);
                });

                this.gd_table.add(bt, j, i);
                bt.setPrefSize(107, 82);
            }
        }

    }

    public int getGridSize(){
        return this.GRID_SIZE;
    }
    private void insertMachinePieces(){
    
    }

    private void insertPiece(Event eh) {
        Button bt_destiny = (Button) eh.getSource();
        
        int row = Integer.parseInt(bt_destiny.getId().substring(3, 4));
        
        if (row > USER_MAX_Y)
            return;
        
        if (bt_destiny.getText().isBlank()){
            bt_destiny.setText(this.selected_button.getText());
            this.selected_button = null;
            this.positioned_pieces++;
        }
        
        if (positioned_pieces == 10)
            bt_begin.setDisable(false);
    }
    
    private void movePiece(Event eh){
        
    }
    @FXML
    private void selectButton(ActionEvent event) {
        if (selected_button == null){
            selected_button = (Button) event.getSource();
            selected_button.setDisable(true);
        }
    }

    @FXML
    private void goToMenu(ActionEvent event) throws IOException {
        SceneManager sm = Main.getSceneManager();
        
        if (!bt_begin.isVisible()){
            Scene sc_menu = sm.getScene("menu");
            Button bt_restart = (Button) sc_menu.lookup("#bt_restart");
            
            bt_restart.setVisible(true);
        }
        
        sm.setScene("menu");
    }

    @FXML
    private void insert(MouseEvent event) {
    }

    @FXML
    private void startGame(ActionEvent event) {
        bt_begin.setVisible(false);
    }

    @FXML
    private void showDebugView(ActionEvent event) {
        if(!in_debug_mode){
            in_debug_mode = true;
            // mostra peças inimigas
            return;
        }
    }
    
}
