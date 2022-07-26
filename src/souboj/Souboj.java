/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souboj;

import herni_plocha.HerniPlocha;
import herni_plocha.Hrac;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author tzlat
 */
public class Souboj extends Application {

    public static Stage primaryStage;
    public static Set<String> currentlyActiveKeys;
    public final static double prefWidth = 1024.0;
    public final static double prefHeight = 768.0;
    public static double mouseX = 0;
    public static double mouseY = 0;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        currentlyActiveKeys = new HashSet<>();
        scene.setOnKeyPressed(event -> {
            String codeString = event.getCode().toString();
            if (!currentlyActiveKeys.contains(codeString)) {
                currentlyActiveKeys.add(codeString);
            }
        });
        scene.setOnKeyReleased(event -> {
            String codeString = event.getCode().toString();
            if (currentlyActiveKeys.contains(codeString)) {
                currentlyActiveKeys.remove(codeString);
            }
        });
        stage.setTitle("Souboj");
        stage.setMaxWidth(prefWidth);
        stage.setMaxHeight(prefHeight);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setAlwaysOnTop(false);
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
