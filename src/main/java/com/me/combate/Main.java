package com.me.combate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.me.combate.essentials.SceneManager;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;
    private static SceneManager sm;

    @Override
    public void start(Stage stage) throws IOException {
        sm = new SceneManager(stage);
        sm.addScene("menu", new Scene(sm.loadFXML("menu"),600,400));
        sm.addScene("inGame",new Scene(sm.loadFXML("inGame"),734,536));
        sm.addScene("about", new Scene(sm.loadFXML("about"),600,400));
        sm.setScene("menu");
        stage.setTitle("Combate");
        stage.show();
    }
    
    public static SceneManager getSceneManager(){
        return sm;
    }

    public static void main(String[] args) {
        launch();
    }

}