package com.me.combate;

import com.me.combate.essentials.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;
    private static SceneManager sm;

    public static SceneManager getSceneManager() {
        return sm;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        sm = new SceneManager(stage);
        sm.addScene("menu", new Scene(sm.loadFXML("menu"), 600, 400));
        sm.addScene("inGame", new Scene(sm.loadFXML("inGame"), 900, 720));
        sm.addScene("about", new Scene(sm.loadFXML("about"), 600, 400));
        sm.addScene("finalScene", new Scene(sm.loadFXML("finalScene"),600,400));
        sm.setScene("menu");
        stage.setTitle("Combate");
        stage.show();
    }

}