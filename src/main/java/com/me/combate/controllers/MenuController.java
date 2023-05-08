package com.me.combate.controllers;

import com.me.combate.Main;
import com.me.combate.essentials.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        String style = "bt_menu";

        bt_about.getStyleClass().setAll(style);
        bt_manual.getStyleClass().setAll(style);
        bt_restart.getStyleClass().setAll(style);
        bt_exit.getStyleClass().setAll(style);
        bt_random.getStyleClass().setAll(style);
    }

    @FXML
    private void startGame(ActionEvent event) throws IOException {
        SceneManager sm = Main.getSceneManager();

        Button clicked_button = (Button) event.getSource();
        InGameController.setGameMode(clicked_button.getText().equals("Manual"));

        sm.resetScenes();
        sm.setScene("inGame");
        
        Alert startDialog = new Alert(Alert.AlertType.INFORMATION);
        startDialog.setTitle("Como Jogar");
        startDialog.setHeaderText("Posicionando peças e recebendo dicas");
        startDialog.setContentText("Para selecionar a peça desejada e posicioná-la corretamente, é necessário clicar com o botão direito nas peças presentes nas duas fileiras inferiores. Após posicionar todas as peças adequadamente, a opção de iniciar o jogo estará disponível para você.\n\n\nDurante o jogo, você terá direito a duas dicas. Uma das dicas informará se há bombas presentes na coluna selecionada. Para obter essa informação, basta clicar na coluna desejada.");
        startDialog.show();
    }

    @FXML
    private void goToAbout(ActionEvent event) throws IOException {
        SceneManager sm = Main.getSceneManager();

        sm.setScene("about");
    }

    public void showRestartButton() {
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
