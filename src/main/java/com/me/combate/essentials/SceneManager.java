/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.me.combate.essentials;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.util.HashMap;
import java.util.Set;
import com.me.combate.Main;
import java.io.IOException;
import java.util.LinkedList;
/**
 *
 * @author rafaelboeira
 */
public class SceneManager {
    private Stage stage;
    private HashMap<String,Scene> scenes;
    
    public SceneManager(Stage stage){
        this.stage = stage;
        scenes = new HashMap();
    }
    
    public Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public Stage getStage(){
        return stage;
    }
    
    public void addScene(String fxml, Scene sc){
        scenes.put(fxml, sc);
    }
    
    public void removeScene(String fxml){
        scenes.remove(fxml);
    }
    
    public Scene getScene(String fxml){
        return scenes.get(fxml);
    }
    
    public void setScene(String fxml){
        stage.setScene(scenes.get(fxml));
    }
    
    public void resetScenes() throws IOException {
        Set<String> scenes = this.scenes.keySet();
        for(String key: scenes){
            Scene scene = this.scenes.get(key);
            scene.setRoot(this.loadFXML(key));
        }
    }
}
