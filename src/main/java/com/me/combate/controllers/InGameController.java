/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.me.combate.controllers;

import com.me.combate.Main;
import com.me.combate.essentials.SceneManager;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.GameSettings;
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
import java.util.Set;
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
import com.me.combate.models.ItemModel.StaticItems.Lake;
import com.me.combate.models.ItemModel.TroopItemModel.Troop;
import com.me.combate.models.ItemModel.TroopItemModel.Soldier;
import com.me.combate.models.ItemModel.TroopItemModel.Gunsmith;
import com.me.combate.models.ItemModel.TroopItemModel.Marshal;
import com.me.combate.models.ItemModel.TroopItemModel.Spy;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class InGameController implements Initializable {
    /*------------------------Falta--------------------------
          Movimentação PC
          Tela de Fim de Jogo 

          IA do computador
    */
    private static boolean gameMode = true;
    private GameSettings gameSettings = new GameSettings();
    private int lakeRow, lakeCol;
    private int counterOfHints = 0;
    private String state = "positioning";
    private int[] individualQuantities = new int[gameSettings.UNIQUE_PIECES];
    private int[] positionedPieces = new int[gameSettings.UNIQUE_PIECES];
    private HashMap<Integer,String> indexMap = new HashMap();
    private HashMap<String,String> nameMap = new HashMap();
    
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
    @FXML
    private Label lb_machine_soldier;
    @FXML
    private Label lb_machine_gunsmith;
    @FXML
    private Label lb_machine_spy;
    @FXML
    private Label lb_machine_marshal;
    @FXML
    private Label lb_machine_flag;
    @FXML
    private Label lb_machine_bomb;
    @FXML
    private Label lb_user_soldier;
    @FXML
    private Label lb_user_gunsmith;
    @FXML
    private Label lb_user_spy;
    @FXML
    private Label lb_user_marshal;
    @FXML
    private Label lb_user_flag;
    @FXML
    private Label lb_user_bomb;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        bt_back.getStyleClass().setAll("bt");
        bt_debug.getStyleClass().setAll("bt");
        bt_begin.getStyleClass().setAll("bt");
        // Executa após o load do controller. Motivo: má formação dos dados caso contrário
        Platform.runLater(()->{ 
            initializeVariables();
            initializeGrid();
            createRandomPieces(0,2,"machine");
            if (!gameMode){
                createRandomPieces(3,5,"user");
                bt_begin.setDisable(false);
            }
            showPieces(0,2,false);
            refreshGrid();
        });
        
         
    }
    
    private void refreshLabels(String team){
        HashMap<String, Integer> countValues = gameBoard.itemCount(team);
        
        for(String className: nameMap.values()){
            Integer quantity = countValues.get(className);
            String id = "#lb_"+team+"_"+className;
            try{
                Label lb = (Label) getNode(id, "inGame");
                lb.setText(lb.getText().replaceFirst("\\d+", quantity.toString()));
            } catch(NullPointerException npe){return;}
        }
        
    }
    
    private void refreshGrid(){
        for (int i=0; i<gameSettings.GRID_SIZE; i++){
            for (int j=0; j<gameSettings.GRID_SIZE; j++){
                Item piece = gameBoard.getAt(i, j);

                String id = "#bt_"+i+""+j+"";
                Button bt = (Button) getNode(id,"inGame");
                
                if (bt == null)
                    return;
                
                if (i == lakeRow && j == lakeCol){                    
                    bt.getStyleClass().setAll("piece", "lake");
                    continue;
                }
                
                if (piece == null){      
                    bt.getStyleClass().setAll("piece","ground", "neutral");
                    continue;
                }
                                
                String team = piece.getTeam();               
                String className = piece.getSubClass();

                if(piece.getVisibility())
                    bt.getStyleClass().setAll("piece", className, team);
                else
                    bt.getStyleClass().setAll("piece", team);
            }
        }
    }
    
    public static void setGameMode(boolean state){
        gameMode = state;
    }
    
    private void inGameButtonBehavior(Button bt_clicked){
        if (gameBoard.getWhoIsPlaying().equals("machine"))
            return;

        boolean wasSuccessful = false;
        
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
            if (targetPiece.getTeam().equals("user") && targetPiece instanceof Troop){
                selectedPiece = (Troop) targetPiece;
                return;
            }
            if (targetPiece.getTeam().equals("machine"))
                wasSuccessful = attack(row, col);
        }      

        if (wasSuccessful) {
            selectedPiece = null;
            checkWhoWon();
            gameBoard.setWhoIsPlaying("machine");
            // TURNO DA MÁQUINA
            checkWhoWon();
            gameBoard.setWhoIsPlaying("user");
            refreshGrid();
            refreshLabels("user");
            refreshLabels("machine");
        }
    }
    
    private void machineTurn(){
        
    }
    
    private void inGameButtonConfiguration(){
        ContextMenu cm = new ContextMenu();
        cm.getStyleClass().setAll("context_menu");
        
        MenuItem mi_hint = new MenuItem("Pedir dica");
        mi_hint.setId("mi_hint");
        mi_hint.getStyleClass().setAll("popup");
        mi_hint.setOnAction(eh -> {
            Button bt_clicked = (Button) getFocusedNode("inGame");
            
            int col = GridPane.getColumnIndex(bt_clicked);
            
            giveHint(col);
        });
        
        cm.getItems().add(mi_hint);

        for (int i=0; i<gameSettings.GRID_SIZE; i++){
            for (int j=0; j<gameSettings.GRID_SIZE; j++){
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
        
        if (gameBoard.isDebugging())
            showPieces(0,gameSettings.MACHINE_MAX_Y,false);
        
        gameBoard.setWhoIsPlaying("user");
        bt_begin.setVisible(false);
        bt_debug.setDisable(true);
        state = "inGame";
    }

    @FXML
    private void showDebugView(ActionEvent event) {
        if (state.equals("inGame"))
            return;
        
        showPieces(0,gameSettings.MACHINE_MAX_Y,!gameBoard.isDebugging());
        gameBoard.setIsDebugging(!gameBoard.isDebugging());
    }
    
    private void showPieces(int start,int end, boolean visibility){            
        for (int i=start; i<end; i++)
            for (int j=0; j<gameSettings.GRID_SIZE; j++)
                toggleVisibleState(i,j,visibility);
    }
    
    private void toggleVisibleState(int row, int col, boolean visibility){
        Item piece = gameBoard.getAt(row, col);

        if (piece == null)
            return;
        
        String pieceTeam = piece.getTeam();
        String pieceName = piece.getSubClass();
        String id = "#bt_"+row+""+col+"";
        
        Button bt_target = (Button) getNode(id, "inGame");
        piece.setVisibility(visibility);
        
        if (visibility)
            bt_target.getStyleClass().setAll("piece", pieceTeam, pieceName);
        else
            bt_target.getStyleClass().setAll("piece", pieceTeam);
    }
    
    private boolean move(int x, int y){
        selectedPiece.move(gameBoard,x,y);

        return true;
    }
    
    @FXML
    private void insert(MouseEvent event) {
    }
    
    private boolean attack(int x, int y){
        Troop.AttackResult battleResult = selectedPiece.attack(gameBoard, x, y);
        
        if (battleResult == Troop.AttackResult.LOST){
            gameBoard.getAt(x, y).setVisibility(true);
        }
        
        if (battleResult == Troop.AttackResult.FINISHED_GAME){
            goToWinScreen("user");
        }
        
        return true;
    }

    private void goToWinScreen(String team){
        SceneManager sm = Main.getSceneManager();
    }
    
    private boolean isThereAnyPieces(String team){
        HashMap<String, Integer> countValues = gameBoard.itemCount(team);
        int quantity;
        
        for(String className: nameMap.values()){
            if (className.equals("flag"))
                continue;

            quantity = countValues.get(className);
            if (quantity > 0){
                return true;
            }
        }
        System.out.println("4");
        return false;
    }
    
    private void checkWhoWon(){
        boolean userState = isThereAnyPieces("user");
        boolean machineState = isThereAnyPieces("machine");
        
        if (!userState && !machineState)
            goToWinScreen("draw");
        
        if (userState && !machineState)
            goToWinScreen("user");
        
        if (!userState && machineState)
            goToWinScreen("machine");
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
        Object node = null;
        try{
            node = sm.getScene(fxml).lookup(id);
        } catch(NullPointerException npe){return null;}
        
        return node;
    }
    
    private Object getFocusedNode(String fxml){
        SceneManager sm = Main.getSceneManager();
        
        return sm.getScene(fxml).getFocusOwner();
    }
    
    private ContextMenu createContextMenu(){
        ContextMenu cm = new ContextMenu();
        cm.setId("cm_positioning");
        cm.getStyleClass().setAll("context_menu");
        
        ArrayList<MenuItem> mi = new ArrayList();
        mi.add(new MenuItem("Soldado"));
        mi.add(new MenuItem("Espião"));
        mi.add(new MenuItem("Cabo Armeiro"));
        mi.add(new MenuItem("Marechal"));
        mi.add(new MenuItem("Bandeira"));
        mi.add(new MenuItem("Bomba"));
        cm.getItems().addAll(mi);
        
        for (int k=0; k<gameSettings.UNIQUE_PIECES; k++){
            mi.get(k).setId("mi_"+k);
            mi.get(k).getStyleClass().setAll("popup");
            mi.get(k).setOnAction(eh -> {
                Button bt_clicked = (Button) getFocusedNode("inGame");
                
                int[] n_pieces = individualQuantities;
                            
                MenuItem mi_source = (MenuItem) eh.getSource();
                int index = mi.indexOf(mi_source);
                            
                if (insertPiece(bt_clicked,mi_source.getText(), "user"))
                    positionedPieces[index]++;
                
                if (2*positionedPieces[index] == n_pieces[index])
                    cm.getItems().remove(mi_source);
                
                if (isFull()){
                    bt_begin.setDisable(false);
                    bt_begin.getStyleClass().setAll("bt");
                }
                
            });
        }
        
        return cm;
    }
    
    private boolean isFull(){
        if (2*getAbsoluteQuantity(positionedPieces) == getAbsoluteQuantity(individualQuantities))
            return true;
        return false;
    }
    
    private boolean insertPiece(Button bt_target, String pieceName, String team) {
        int row;
        int col;
        try{
            row = GridPane.getRowIndex(bt_target);
            col = GridPane.getColumnIndex(bt_target);
        } catch(Exception e){return false;}
        
        if (team.equals("user") && row != gameSettings.USER_FLAG_Y && pieceName.equals("Bandeira"))
            return false;
        
        if (gameBoard.getAt(row, col) == null){
            String className = nameMap.get(pieceName);
            
            Item piece = ItemFactory.createPiece(className, team);
            piece.setVisibility(true);
            gameBoard.insertItem(piece, row, col);
            
            bt_target.setText(pieceName);
            bt_target.getStyleClass().setAll("piece",className,team);
        } else {
            return false;
        }        
        
        return true;
    }
    
    private void initializeVariables(){
        gameBoard = new GameBoard();
        
        createNameMap();
        createIndexMap();
        
        IntStream sizes = IntStream.of(6,2,4,2,2,4);
        individualQuantities = fillVector(individualQuantities, sizes);
        
        IntStream init = IntStream.of(0,0,0,0,0,0);
        positionedPieces = fillVector(positionedPieces,init);
        
        Random rand = new Random();   
        lakeRow = 2;
        lakeCol = rand.nextInt(5);
    }
    
    private void initializeGrid(){
        ContextMenu cm = createContextMenu();
        
        for (int i=0; i<gameSettings.GRID_SIZE; i++){
            for (int j=0; j<gameSettings.GRID_SIZE; j++){
                Button bt = new Button();
                bt.setId("bt_"+i+""+j);
                
                if (i >= gameSettings.USER_MIN_Y){
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
                    Item lake = new Lake("neutral");
                    gameBoard.insertItem(lake, i, j);
                    bt.getStyleClass().setAll("piece", "lake");    
                } else {
                    gameBoard.insertItem(null, i, j);
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
    
    private void createIndexMap(){        
        indexMap.put(0,"Soldado");
        indexMap.put(1,"Espião");
        indexMap.put(2,"Cabo Armeiro");
        indexMap.put(3,"Marechal");
        indexMap.put(4,"Bandeira");
        indexMap.put(5,"Bomba");
    }
    
    private void createNameMap(){ 
        nameMap.put("Soldado", "soldier");
        nameMap.put("Espião", "spy");
        nameMap.put("Cabo Armeiro", "gunsmith");
        nameMap.put("Marechal", "marshal");
        nameMap.put("Bandeira", "flag");
        nameMap.put("Bomba", "bomb");
    }

    private void giveHint(int col) {
        if (counterOfHints == gameSettings.HINT_MAX)
            return;
        boolean hintFlag = false;
        Alert hintDialog = new Alert(Alert.AlertType.INFORMATION);
        hintDialog.setTitle("Dica");
        hintDialog.setHeaderText("");
       
        counterOfHints++;
        for (int i=0; i<gameSettings.GRID_SIZE; i++){
            Item unknownPiece = gameBoard.getAt(i,col);
        
            if (unknownPiece == null)
                continue;
            
            String unknownPieceTeam = unknownPiece.getTeam();
            System.out.println(unknownPiece);
            
            if (unknownPiece instanceof Bomb && unknownPieceTeam.equals("machine")){
                hintFlag = true;
                break;
            }
        }
        if (hintFlag){
            hintDialog.setContentText("Bombas foram encontradas!");
            hintDialog.show();
            return;
        }
        
        hintDialog.setContentText("Nenhuma bomba encontrada!");
        hintDialog.show();
    }
    
    private void createRandomPieces(int start, int end, String team) {
        int index;
        int flagRow;
        String className;
        Button bt_target;
        String id;
        
        if (team.equals("user"))
            flagRow = gameSettings.USER_FLAG_Y;
        else
            flagRow = gameSettings.MACHINE_FLAG_Y;
        
        Random rand = new Random();
        
        int flagCol = rand.nextInt(gameSettings.GRID_SIZE);
        positionedPieces[4]++;
        
        for (int i=start; i<end; i++){
            for (int j=0; j<gameSettings.GRID_SIZE; j++){
                id = "#bt_"+i+""+j+"";
                bt_target = (Button) getNode(id,"inGame");
                if (i == flagRow && j == flagCol){
                    insertPiece(bt_target,"Bandeira",team);
                    continue;
                }
                do{
                    index = rand.nextInt(6);
                    className = indexMap.get(index);   
                }while(2*positionedPieces[index] == individualQuantities[index]);
                
                insertPiece(bt_target, className, team);
                positionedPieces[index]++;
            }
        }
        
        IntStream init = IntStream.of(0,0,0,0,0,0);
        positionedPieces = fillVector(positionedPieces,init);
    }
    
}