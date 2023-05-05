/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import com.me.combate.Main;
import com.me.combate.essentials.SceneManager;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.ItemFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.util.HashMap;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.ItemFactory;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Flag;
import com.me.combate.models.ItemModel.TroopItemModel.Troop;
import com.me.combate.models.ItemModel.TroopItemModel.Soldier;
import com.me.combate.models.ItemModel.TroopItemModel.Gunsmith;
import com.me.combate.models.ItemModel.TroopItemModel.Marshal;
import com.me.combate.models.ItemModel.TroopItemModel.Spy;

public class InGameController implements Initializable {
    /*------------------------Falta--------------------------
          Peças aleatórias
          Debug
    */
    private final int GRID_SIZE = 5;
    private final int UNIQUE_PIECES = 6;
    private final int USER_MIN_Y = 3;
    private final int HINT_MAX = 2;
    private int lakeRow, lakeCol;
    private int counterOfHints = 0;
    private String state = "positioning";
    private int[] individualQuantities = new int[UNIQUE_PIECES];
    private int[] positionedPieces = new int[UNIQUE_PIECES];
    private HashMap<String,String> nameMap;
    
    private Troop selectedPiece = null;
    private GameBoard gameBoard;
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
        initializeVariables();
        initializeGrid();
        
        bt_back.getStyleClass().setAll("bt");
        bt_debug.getStyleClass().setAll("bt");
        bt_begin.getStyleClass().setAll("bt");
    }
    
    private void refreshGrid(){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                Item piece = gameBoard.getAt(i, j);
                String id = "#bt_"+i+""+j+"";
                Button bt = (Button) getNode(id,"inGame");
                
                if (bt == null)
                    return;
                
                if (i == lakeRow && j == lakeCol)
                    continue;
                
                if (piece == null){         
                    bt.getStyleClass().setAll("piece","ground", "neutral");
                    continue;
                }
                                
                String team = piece.getTeam();
                
                String className = piece.getClass().getName().toLowerCase();
                int startSubClassIndex = className.lastIndexOf('.') + 1;
                
                bt.getStyleClass().setAll("piece", className.substring(startSubClassIndex), team);
            }
        }
    }

    @FXML
    private void insert(MouseEvent event) {
    }
    
    private void inGameButtonBehavior(Button bt_clicked){
        if (gameBoard.getWhoIsPlaying().equals("machine"))
            return;

        boolean wasSuccessful = true;
        
        int row = GridPane.getRowIndex(bt_clicked);
        int col = GridPane.getColumnIndex(bt_clicked);

        Item targetPiece = gameBoard.getAt(row, col);
        
        if (selectedPiece == null && targetPiece == null)
            return;

        if (selectedPiece == null && targetPiece != null) {
            if (!(targetPiece instanceof Troop))
                return;
            if (targetPiece.getTeam().equals("user")) {
                selectedPiece = (Troop) targetPiece;
                return;
            }
        }

        if (targetPiece == null)
            wasSuccessful = move(row, col);
        else {
            if (targetPiece instanceof Troop){
                selectedPiece = (Troop) targetPiece;
                return;
            }
            
            wasSuccessful = attack(row, col);
        }      

        if (wasSuccessful) {
            selectedPiece = null;
            refreshGrid();

            gameBoard.setWhoIsPlaying("machine");
            // TURNO DA MÁQUINA
            gameBoard.setWhoIsPlaying("user");
        }
    }
    
    private void machineTurn(){
        
    }
    
    private void inGameButtonConfiguration(){
        ContextMenu cm = new ContextMenu();
        cm.getStyleClass().setAll("context_menu");
        
        MenuItem mi_hint = new MenuItem("Pedir dica");
        mi_hint.getStyleClass().setAll("popup");
        mi_hint.setOnAction(eh -> {
            SceneManager sm = Main.getSceneManager();
            Button bt_clicked = (Button) sm.getScene("inGame").getFocusOwner();
            
            int col = GridPane.getColumnIndex(bt_clicked);
            
            giveHint(col);
        });
        
        cm.getItems().add(mi_hint);

        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                String id = "#bt_"+i+""+j+"";
                
                Button bt_piece = (Button) getNode(id,"inGame");
                
                bt_piece.setContextMenu(cm);
                
                if (i == lakeRow && j == lakeCol)
                    continue;
                
                bt_piece.setOnMouseClicked(eh -> {
                    inGameButtonBehavior((Button) eh.getSource());
                });  
            }
        }
    }

    @FXML
    private void startGame(ActionEvent event) throws NullPointerException{        
        inGameButtonConfiguration();
        
        gameBoard.setWhoIsPlaying("user");
        bt_begin.setVisible(false);
        state = "inGame";
    }

    @FXML
    private void showDebugView(ActionEvent event) {
        if (state.equals("inGame"))
            return;
        
    }
    
    private boolean move(int x, int y){
        if (selectedPiece.move(gameBoard, x, y) == 1)
            return true;
        
        return false;
    }
    
    private boolean attack(int x, int y){
        return true;
    }

    @FXML
    private void goToMenu(ActionEvent event) {
        SceneManager sm = Main.getSceneManager();
        
        if (state.equals("inGame")){
            Button bt_restart = (Button) getNode("#bt_restart", "menu");
            
            bt_restart.setVisible(true);
        }
        
        sm.setScene("menu");
    }
    
    private Object getNode(String id, String fxml){
        SceneManager sm = Main.getSceneManager();
        
        return sm.getScene(fxml).lookup(id);
    }
    
    private ContextMenu createContextMenu(){
        ContextMenu cm = new ContextMenu();
        cm.getStyleClass().setAll("context_menu");
        
        ArrayList<MenuItem> mi = new ArrayList();
        mi.add(new MenuItem("Soldado"));
        mi.add(new MenuItem("Espião"));
        mi.add(new MenuItem("Cabo Armeiro"));
        mi.add(new MenuItem("Marechal"));
        mi.add(new MenuItem("Bandeira"));
        mi.add(new MenuItem("Bomba"));
        cm.getItems().addAll(mi);
        
        for (int k=0; k<UNIQUE_PIECES; k++){
            mi.get(k).getStyleClass().setAll("popup");
            mi.get(k).setOnAction(eh -> {
                SceneManager sm = Main.getSceneManager();
                Button bt_clicked = (Button) sm.getScene("inGame").getFocusOwner();
                
                int[] n_pieces = individualQuantities;
                            
                MenuItem mi_source = (MenuItem) eh.getSource();
                int index = mi.indexOf(mi_source);
                            
                if (insertPiece(bt_clicked,mi_source.getText(), "user"))
                    positionedPieces[index]++;
                
                if (positionedPieces[index] == n_pieces[index])
                    cm.getItems().remove(mi_source);
                
            });
        }
        
        return cm;
    }
    
    private boolean insertPiece(Button bt_target, String pieceName, String team) {
        if (bt_target.getText().isBlank()){
            String className = nameMap.get(pieceName);
           
            bt_target.getStyleClass().setAll("user", className, "piece");
            bt_target.setText(pieceName);

            int row = GridPane.getRowIndex(bt_target);
            int col = GridPane.getColumnIndex(bt_target);
            
            Item piece = ItemFactory.createPiece(className, team);
            piece.setX(row);
            piece.setY(col);
            gameBoard.setAt(row, col, piece);        
        } else {
            return false;
        }        
        
        int sum = getAbsoluteQuantity(positionedPieces);
        
        if (sum == getAbsoluteQuantity(individualQuantities) - 1){
            bt_begin.setDisable(false);
            bt_begin.getStyleClass().setAll("bt");
        }
        
        return true;
    }
    
    private void initializeVariables(){
        gameBoard = new GameBoard();
        
        createNameMap();
        
        IntStream sizes = IntStream.of(3,1,2,1,1,2);
        individualQuantities = fillVector(individualQuantities, sizes);
        
        IntStream init = IntStream.of(0,0,0,0,0,0);
        positionedPieces = fillVector(positionedPieces,init);
        
        Random rand = new Random();   
        lakeRow = 2;
        lakeCol = rand.nextInt(5);
    }
    
    private void initializeGrid(){
        ContextMenu cm = createContextMenu();
        
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                Button bt = new Button();
                bt.setId("bt_"+i+""+j);
                
                if (i >= USER_MIN_Y){
                    // Para de exibir o popup após o início do jogo;
                    bt.setOnContextMenuRequested(eh -> { 
                        if (state.equals("inGame"))
                            return;
                        
                        if (!bt_begin.isDisable()){
                            Button bt_clicked = (Button) eh.getSource();
                            bt_clicked.getContextMenu().hide();
                        }
                    });
                    bt.setContextMenu(cm);
                }
                
                if ((i == lakeRow) && (j == lakeCol)){
                    bt.getStyleClass().setAll("piece", "lake");    
                } else {
                    bt.getStyleClass().setAll("piece", "ground", "neutral");
                }
                
                gd_table.add(bt, j, i);
            }
        }
    }
    
    private int[] fillVector(int[] v, IntStream values){
        v = values.toArray();
        
        return v;
    }
    
    private int getAbsoluteQuantity(int[] v) {
        int sum = 0;
        
        for (int n: v)
            sum += n;
                  
        return sum;   
    }
    
    private void createNameMap(){
        nameMap = new HashMap();
              
        nameMap.put("Soldado", "soldier");
        nameMap.put("Espião", "spy");
        nameMap.put("Cabo Armeiro", "gunsmith");
        nameMap.put("Marechal", "marshal");
        nameMap.put("Bandeira", "flag");
        nameMap.put("Bomba", "bomb");
    }

    private void giveHint(int col) {
        if (counterOfHints == HINT_MAX)
            return;
        
        Alert hintDialog = new Alert(Alert.AlertType.INFORMATION);
        hintDialog.setTitle("Dica");
        hintDialog.setHeaderText("");
       
        counterOfHints++;
        for (int i=0; i<GRID_SIZE; i++){
            Item unknownPiece = gameBoard.getAt(i,col);
        
            if (unknownPiece == null)
                continue;
            
            String unknownPieceTeam = unknownPiece.getTeam();
            
            if (unknownPiece instanceof Bomb && unknownPieceTeam.equals("machine")){
                hintDialog.setContentText("Bombas foram encontradas!");
                hintDialog.show();
                break;
            } else {
                hintDialog.setContentText("Nenhuma bomba encontrada!");
                hintDialog.show();
                break;
            }
        }
    }
}