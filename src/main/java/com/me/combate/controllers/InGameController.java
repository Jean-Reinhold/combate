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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import javafx.event.Event;
/**
 * FXML Controller class
 *
 * @author rafaelboeira
 */
public class InGameController implements Initializable {

    private Button selected_button = null;
    @FXML
    private Button bt_flag;
    @FXML
    private Button bt_bomb1;
    @FXML
    private Button bt_bomb2;
    @FXML
    private Button bt_spy;
    @FXML
    private Button bt_soldier1;
    @FXML
    private Button bt_soldier2;
    @FXML
    private Button bt_soldier3;
    @FXML
    private Button bt_gunsmith1;
    @FXML
    private Button bt_gunsmith2;
    @FXML
    private Button bt_marshal;
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
        // TODO
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                Button bt = new Button();
                bt.setText("");
                bt.setId("bt_"+i+""+j);
                bt.setWrapText(true);
                
                bt.setOnAction(eh -> {
                    if (this.selected_button != null && this.bt_begin.isDisabled())
                        this.insertPiece(eh);   
                    if (this.bt_begin.isVisible() == false)
                        this.movePiece(eh);
                });
                
                this.gd_table.add(bt, j, i);
                bt.setPrefSize(107, 82);
            }
        }
    }    
    
    private void insertMachinePieces(){
    
    }

    private void insertPiece(Event eh) {
        Button bt_destiny = (Button) eh.getSource();
        
        int row = Integer.parseInt(bt_destiny.getId().substring(3, 4));
        
        if (row > 1)
            return;
        
        if (bt_destiny.getText().isBlank()){
            bt_destiny.setText(this.selected_button.getText());
            this.selected_button = null;
        }
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
        Main.setRoot("menu");
    }

    @FXML
    private void insert(MouseEvent event) {
    }
    
}
