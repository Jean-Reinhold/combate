package com.me.combate.controllers;

import com.me.combate.Main;
import com.me.combate.controllers.WinnerScreenController;
import com.me.combate.essentials.SceneManager;
import com.me.combate.exceptions.IllegalMovement;
import com.me.combate.exceptions.ItemOutOfBounds;
import com.me.combate.models.GameBoardModel.GameBoard;
import com.me.combate.models.GameSettings;
import com.me.combate.models.ItemModel.Item;
import com.me.combate.models.ItemModel.ItemFactory;
import com.me.combate.models.ItemModel.StaticItems.Bomb;
import com.me.combate.models.ItemModel.StaticItems.Lake;
import com.me.combate.models.ItemModel.TroopItemModel.Troop;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class InGameController implements Initializable {
    /*------------------------Falta--------------------------
          Movimentação PC
          Tela de Fim de Jogo

          IA do computador
    */
    private static boolean gameMode = true;
    private final Object lock = new Object();
    private final Map<Integer, String> indexMap = Map.of(0, "Soldado", 1, "Espião", 2, "Cabo Armeiro", 3, "Marechal", 4, "Bandeira", 5, "Bomba");
    private final Map<String, String> nameMap = Map.of("Soldado", "soldier", "Espião", "spy", "Cabo Armeiro", "gunsmith", "Marechal", "marshal", "Bandeira", "flag", "Bomba", "bomb");
    private final int[] individualQuantities = IntStream.of(6, 2, 4, 2, 2, 4).toArray();
    private int counterOfHints = 0;
    private GamePlayState state = GamePlayState.POSITIONING_PIECES;
    private int[] positionedPieces = new int[GameSettings.PLAYER_TOTAL_NUMBER_OF_PIECES];
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

    public static void setGameMode(boolean state) {
        gameMode = state;
    }
    @FXML
    private Label lb_hint;

    public Object getLock() {
        return lock;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bt_back.getStyleClass().setAll("bt");
        bt_debug.getStyleClass().setAll("bt");
        bt_begin.getStyleClass().setAll("bt");
        // Inicializa código depois da stage
        Platform.runLater(() -> {
            initializeVariables();
            initializeGrid();
            createRandomPieces(0, 2, "machine");
            for (Item piece : gameBoard.getMachineItems())
                setVisibility(piece, false);
            if (!gameMode) {
                createRandomPieces(3, 5, "user");
                bt_begin.setDisable(false);
            }
            refreshGrid();
        });

    }

    private void refreshLabels(String team) {
        HashMap<String, Integer> countValues = gameBoard.itemCount(team);

        for (String className : nameMap.values()) {
            Integer quantity = countValues.get(className);
            String id = "#lb_" + team + "_" + className;
            try {
                Label lb = (Label) getNode(id, "inGame");
                lb.setText(lb.getText().replaceFirst("\\d+", quantity.toString()));
            } catch (NullPointerException npe) {
                continue;
            }
        }

    }

    private void refreshGrid() {
        for (int i = 0; i < GameSettings.GRID_SIZE; i++) {
            for (int j = 0; j < GameSettings.GRID_SIZE; j++) {
                Item piece = gameBoard.getAt(i, j);

                String id = "#bt_" + i + j;
                Button bt = (Button) getNode(id, "inGame");

                if (bt == null)
                    return;

                if (i == gameBoard.getLakeRow() && j == gameBoard.getLakeCol()) {
                    bt.getStyleClass().setAll("piece", "lake");
                    continue;
                }

                if (piece == null) {
                    bt.getStyleClass().setAll("piece", "ground", "neutral");
                    continue;
                }

                String team = piece.getTeam();
                String className = piece.getSubClass();

                if (piece.getVisibility())
                    bt.getStyleClass().setAll("piece", className, team);
                else
                    bt.getStyleClass().setAll("piece", team);
            }
        }
    }

    private void inGameButtonBehavior(Button bt_clicked) {
        if (gameBoard.getWhoIsPlaying().equals("machine"))
            return;

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

        if (targetPiece == null) {
            try {
                applyMove(selectedPiece, row, col);
            } catch (IllegalMovement | ItemOutOfBounds e) {
                return;
            }
        } else {
            try {
                if (targetPiece.getTeam().equals("user") && targetPiece instanceof Troop) {
                    selectedPiece = (Troop) targetPiece;
                    return;
                }
                if (targetPiece.getTeam().equals("machine")) {
                    if (selectedPiece == null) {
                        return;
                    }
                    applyAttack(selectedPiece, row, col);
                }
            } catch (ItemOutOfBounds | IllegalMovement e) {
                return;
            }
        }
        selectedPiece = null;
        render();

        checkForDraw();
        checkForOnlyMachinePieces();
        gameBoard.setWhoIsPlaying("machine");
        machineTurn();

        render();
        gameBoard.setWhoIsPlaying("user");
    }

    private void render() {
        refreshGrid();
        refreshLabels("user");
        refreshLabels("machine");
    }

    private void machineTurn() {
        Random random = new Random();
        ArrayList<Troop> machineTroops = gameBoard.getMachineTroops();
        Collections.shuffle(machineTroops);

        for (Troop troop : machineTroops) {
            ArrayList<ArrayList<Integer>> attacks = gameBoard.getTroopPossibleAttacks(troop);
            ArrayList<ArrayList<Integer>> moves = gameBoard.getTroopPossibleMoves(troop);

            if (moves.isEmpty() && attacks.isEmpty()) {
                continue;
            }
            if (moves.isEmpty()) {
                ArrayList<Integer> coordinates = attacks.get(0);
                int x = coordinates.get(0);
                int y = coordinates.get(1);

                applyAttack(troop, x, y);
                return;
            }
            if (attacks.isEmpty()) {
                ArrayList<Integer> coordinates = moves.get(0);
                int x = coordinates.get(0);
                int y = coordinates.get(1);

                applyMove(troop, x, y);
                return;
            }

            if (random.nextBoolean()) {
                ArrayList<Integer> coordinates = attacks.get(0);
                int x = coordinates.get(0);
                int y = coordinates.get(1);

                applyAttack(troop, x, y);
                return;
            }
            ArrayList<Integer> coordinates = moves.get(0);
            int x = coordinates.get(0);
            int y = coordinates.get(1);

            applyMove(troop, x, y);
            return;

        }
    }

    private void inGameButtonConfiguration() {
        ContextMenu cm = new ContextMenu();
        cm.getStyleClass().setAll("context_menu");

        MenuItem mi_hint = new MenuItem("Pedir dica");
        mi_hint.setId("mi_hint");
        mi_hint.getStyleClass().setAll("popup");
        mi_hint.setOnAction(eh -> {
            Button bt_clicked = (Button) getFocusedNode("inGame");

            int col = GridPane.getColumnIndex(bt_clicked);

            giveHint(col);
            render();
        });

        cm.getItems().add(mi_hint);

        for (int i = 0; i < GameSettings.GRID_SIZE; i++) {
            for (int j = 0; j < GameSettings.GRID_SIZE; j++) {
                String id = "#bt_" + i + j;

                Button bt_piece = (Button) getNode(id, "inGame");
                bt_piece.setContextMenu(cm);

                if (i == gameBoard.getLakeRow() && j == gameBoard.getLakeCol())
                    continue;

                bt_piece.setOnMouseClicked(eh -> {
                    inGameButtonBehavior((Button) eh.getSource());
                });
            }
        }
    }

    @FXML
    private void startGame(ActionEvent event) throws NullPointerException {
        inGameButtonConfiguration();

        gameBoard.setWhoIsPlaying("user");
        bt_begin.setDisable(true);
        state = GamePlayState.IN_GAME;
        goToWinScreen("machine");
    }

    @FXML
    private void showDebugView(ActionEvent event) {
        for (Item piece : gameBoard.getMachineItems())
            setVisibility(piece, !gameBoard.isDebugging());
        gameBoard.setIsDebugging(!gameBoard.isDebugging());
    }

    private void setVisibility(Item piece, boolean visibility) {
        try {
            String pieceTeam = piece.getTeam();
            String pieceName = piece.getSubClass();
            String id = "#bt_" + piece.getX() + piece.getY();

            Button bt_target = (Button) getNode(id, "inGame");
            piece.setVisibility(visibility);

            if (visibility)
                bt_target.getStyleClass().setAll("piece", pieceTeam, pieceName);
            else
                bt_target.getStyleClass().setAll("piece", pieceTeam);

        } catch (NullPointerException e) {
        }
    }

    private void applyMove(Troop troop, int x, int y) {
        troop.move(gameBoard, x, y);
    }

    @FXML
    private void insert(MouseEvent event) {
    }

    private void applyAttack(Troop troop, int x, int y) {
        Troop.AttackResult battleResult = troop.attack(gameBoard, x, y);

        setVisibility(troop, true);
        setVisibility(gameBoard.getAt(x, y), true);

        if (battleResult == Troop.AttackResult.FINISHED_GAME) {
            goToWinScreen(troop.getTeam());
        }
    }

    private void goToWinScreen(String team) {
        SceneManager sm = Main.getSceneManager();
        WinnerScreenController.setWinner(team);
        sm.setScene("winnerScreen");
    }

    private boolean isThereATroop() {
        for (int i = 0; i < gameBoard.getGameBoardSize(); i++) {
            for (int j = 0; j < gameBoard.getGameBoardSize(); j++)
                if (gameBoard.getAt(i, j) instanceof Troop)
                    return true;
        }
        return false;
    }

    private boolean isThereAUserTroop() {
        for (int i = 0; i < gameBoard.getGameBoardSize(); i++) {
            for (int j = 0; j < gameBoard.getGameBoardSize(); j++)
                if (gameBoard.getAt(i, j) instanceof Troop && gameBoard.getAt(i, j).getTeam().equals("user"))
                    return true;
        }
        return false;
    }

    private void checkForOnlyMachinePieces() {
        if (!isThereATroop())
            goToWinScreen("machine");
    }

    private void checkForDraw() {
        if (!isThereATroop())
            goToWinScreen("draw");
    }

    @FXML
    private void goToMenu(ActionEvent event) {
        SceneManager sm = Main.getSceneManager();

        if (state == GamePlayState.IN_GAME) {
            Button bt_restart = (Button) getNode("#bt_restart", "menu");
            bt_restart.setVisible(true);
        }

        sm.setScene("menu");
    }

    private Object getNode(String id, String fxml) {
        SceneManager sm = Main.getSceneManager();
        Object node = null;
        try {
            node = sm.getScene(fxml).lookup(id);
        } catch (NullPointerException npe) {
            return null;
        }

        return node;
    }

    private Object getFocusedNode(String fxml) {
        SceneManager sm = Main.getSceneManager();

        return sm.getScene(fxml).getFocusOwner();
    }

    private ContextMenu createContextMenu() {
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

        for (int k = 0; k < GameSettings.PLAYER_TOTAL_NUMBER_OF_PIECES; k++) {
            mi.get(k).setId("mi_" + k);
            mi.get(k).getStyleClass().setAll("popup");
            mi.get(k).setOnAction(eh -> {
                Button bt_clicked = (Button) getFocusedNode("inGame");

                MenuItem mi_source = (MenuItem) eh.getSource();
                int index = mi.indexOf(mi_source);

                if (insertPiece(bt_clicked, mi_source.getText(), "user"))
                    positionedPieces[index]++;

                if (2 * positionedPieces[index] == individualQuantities[index])
                    cm.getItems().remove(mi_source);

                if (isFull()) {
                    bt_begin.setDisable(false);
                    bt_begin.getStyleClass().setAll("bt");
                }

            });
        }

        return cm;
    }

    private boolean isFull() {
        return 2 * getTotalQuantityOfPieces(positionedPieces) == getTotalQuantityOfPieces(individualQuantities);
    }

    private boolean insertPiece(Button bt_target, String pieceName, String team) {
        int row;
        int col;
        try {
            row = GridPane.getRowIndex(bt_target);
            col = GridPane.getColumnIndex(bt_target);
        } catch (Exception e) {
            return false;
        }

        if (team.equals("user") && row != GameSettings.USER_FLAG_Y && pieceName.equals("Bandeira"))
            return false;

        if (gameBoard.getAt(row, col) == null) {
            String className = nameMap.get(pieceName);

            Item piece = ItemFactory.createPiece(className, team);
            piece.setVisibility(true);
            gameBoard.insertItem(piece, row, col);

            bt_target.setText(pieceName);
            bt_target.getStyleClass().setAll("piece", className, team);

            return true;
        }
        return false;

    }

    private void initializeVariables() {
        gameBoard = new GameBoard();
        positionedPieces = IntStream.of(0, 0, 0, 0, 0, 0).toArray();

        Random rand = new Random();
        gameBoard.setLakeRow(2);
        gameBoard.setLakeCol(rand.nextInt(GameSettings.GRID_SIZE));
    }

    private void initializeGrid() {
        ContextMenu cm = createContextMenu();

        for (int i = 0; i < GameSettings.GRID_SIZE; i++) {
            for (int j = 0; j < GameSettings.GRID_SIZE; j++) {
                Button bt = new Button();
                bt.setId("bt_" + i + j);

                if (i >= GameSettings.USER_MIN_Y) {
                    bt.setOnContextMenuRequested(eh -> {
                        if (state == GamePlayState.IN_GAME)
                            return;

                        if (!bt_begin.isDisable()) {
                            Button bt_clicked = (Button) eh.getSource();
                            bt_clicked.getContextMenu().hide();
                        }
                    });
                    bt.setContextMenu(cm);
                }

                if ((i == gameBoard.getLakeRow()) && (j == gameBoard.getLakeCol())) {
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

    private int getTotalQuantityOfPieces(int[] v) {
        return IntStream.of(v).sum();
    }

    private void giveHint(int col) {
        Alert hintDialog = new Alert(Alert.AlertType.INFORMATION);
        hintDialog.setTitle("Dica");
        hintDialog.setHeaderText("");

        if (counterOfHints == GameSettings.HINT_MAX){
            hintDialog.setContentText("Limite de dicas excedido!");
            hintDialog.show();
            return;
        }
        boolean hintFlag = false;
        this.counterOfHints++;
        for (int i = 0; i < GameSettings.GRID_SIZE; i++) {
            Item unknownPiece = gameBoard.getAt(i, col);

            if (unknownPiece == null)
                continue;

            String unknownPieceTeam = unknownPiece.getTeam();
            if (unknownPiece instanceof Bomb && unknownPieceTeam.equals("machine")) {
                hintFlag = true;
                break;
            }
        }
        if (hintFlag) {
            hintDialog.setContentText("Bomba encontrada na coluna!");
            hintDialog.show();
            return;
        }

        hintDialog.setContentText("Nenhuma bomba encontrada!");
        hintDialog.show();
        
        String hintText = "Dicas Utilizadas: "+counterOfHints;
        lb_hint.setText(hintText);
    }

    private void createRandomPieces(int start, int end, String team) {
        int index;
        int flagRow;
        String className;
        Button bt_target;
        String id;

        if (team.equals("user"))
            flagRow = GameSettings.USER_FLAG_Y;
        else
            flagRow = GameSettings.MACHINE_FLAG_Y;

        Random rand = new Random();

        int flagCol = rand.nextInt(GameSettings.GRID_SIZE);
        positionedPieces[4]++;

        for (int i = start; i < end; i++) {
            for (int j = 0; j < GameSettings.GRID_SIZE; j++) {
                id = "#bt_" + i + j;
                bt_target = (Button) getNode(id, "inGame");
                if (i == flagRow && j == flagCol) {
                    insertPiece(bt_target, "Bandeira", team);
                    continue;
                }
                do {
                    index = rand.nextInt(6);
                    className = indexMap.get(index);
                } while (2 * positionedPieces[index] == individualQuantities[index]);

                insertPiece(bt_target, className, team);
                positionedPieces[index]++;
            }
        }

        positionedPieces = IntStream.of(0, 0, 0, 0, 0, 0).toArray();
    }

    private enum GamePlayState {
        POSITIONING_PIECES, IN_GAME, DEBUG
    }

}