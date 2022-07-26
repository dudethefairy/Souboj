/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package souboj;

import herni_plocha.HerniPlocha;
import herni_plocha.Hrac;
import herni_plocha.Projektil;
import herni_plocha.TypHry;
import herni_plocha.Vylepseni;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author tzlat
 */
public class FXMLDocumentController implements Initializable {
    
    public HerniPlocha plocha;
    private Hrac hrac1;
    private Hrac hrac2;
    private Circle kruh1;
    private Circle kruh2;
    @FXML
    public Pane paneHraciPlocha;
    public Label skoreHrac1;
    public Label skoreHrac2;
    public Label cas;
    public ChangeListener listener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            startHry();
        }
    };
    public ChangeListener listenerKonec = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            restartHry();
        }
        
    };
    double prefWidth = Souboj.prefWidth;
    double prefHeight = Souboj.prefHeight - 38;
    double startXHrac1 = prefWidth / 20.0;
    double startYHrac1 = prefHeight / 2.0;
    double startXHrac2 = prefWidth - prefWidth / 20.0;
    double startYHrac2 = prefHeight / 2.0;
    double radiusHracu = prefWidth / 40.0;
    String musicFile = "zvuky//music.mp3";
    String soundFileZap = "zvuky//zap.mp3";
    String soundFilePowerUp = "zvuky//powerup.mp3";
    
    Media music = new Media(new File(musicFile).toURI().toString());
    Media soundZap = new Media(new File(musicFile).toURI().toString());
    Media soundPowerUp = new Media(new File(musicFile).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(music);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mediaPlayer.setOnEndOfMedia(
                new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.setVolume(0.5);
        skoreHrac1 = new Label("0");
        skoreHrac1.setLayoutX(prefWidth / 2 - prefHeight / 5);
        skoreHrac1.setLayoutY(prefHeight / 50);
        skoreHrac1.fontProperty().setValue(new Font(20));
        skoreHrac1.textProperty().addListener(listenerKonec);
        skoreHrac2 = new Label("0");
        skoreHrac2.setLayoutX(prefWidth / 2 + prefHeight / 5);
        skoreHrac2.setLayoutY(prefHeight / 50);
        skoreHrac2.fontProperty().setValue(new Font(20));
        skoreHrac2.textProperty().addListener(listenerKonec);
        cas = new Label("120");
        cas.setLayoutX(prefWidth / 2);
        cas.setLayoutY(prefHeight / 50);
        cas.fontProperty().setValue(new Font(20));
        cas.textProperty().addListener(listenerKonec);
        hrac1 = new Hrac("Pepa", kruh1 = new Circle(startXHrac1, startYHrac1, radiusHracu, Color.BLACK), startXHrac1, startYHrac1, 0, 0.4, 5);
        hrac2 = new Hrac("Jarda", kruh2 = new Circle(startXHrac2, startYHrac2, radiusHracu, Color.BLACK), startXHrac2, startYHrac2, 180, 0.4, 5);
        plocha = new HerniPlocha(paneHraciPlocha, hrac1, hrac2, prefWidth, prefHeight, skoreHrac1, skoreHrac2, cas, this);
        kruh1 = hrac1.getZobrazovaciKruh();
        kruh2 = hrac2.getZobrazovaciKruh();
        paneHraciPlocha.getChildren().add(kruh1);
        paneHraciPlocha.getChildren().add(kruh2);
        paneHraciPlocha.getChildren().add(skoreHrac1);
        paneHraciPlocha.getChildren().add(skoreHrac2);
        paneHraciPlocha.getChildren().add(cas);
        paneHraciPlocha.widthProperty().addListener(listener);
    }
    
    @FXML
    private void mouseClickedPane(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            plocha.hracStrili(hrac1);
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            if (jeBodVKruhu(event.getX(), event.getY(), plocha.hrac1.zobrazovaciKruh.getCenterX(), plocha.hrac1.zobrazovaciKruh.getCenterY(), plocha.hrac1.zobrazovaciKruh.getRadius())) {
                
            }
        }
    }
    
    public void startHry() {
        Souboj.currentlyActiveKeys.clear();
        ButtonType naCas = new ButtonType("Na čas", ButtonBar.ButtonData.OK_DONE);
        ButtonType naBody = new ButtonType("Na body", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(naCas);
        alert.getButtonTypes().add(naBody);
        alert.setHeaderText("");
        alert.setTitle("Zvolte Typ Hry");
        alert.setContentText("Typ Hry:\nNa čas: 120s\nNa body: 25 bodů\n\nManuál:\nŠipky nahoru a dolů - pohyb\nLevé tlačítko myši - střelba\nČervená - rychlost kulek (10s)\nZelená - rychlost pohybu (10s)\nModrá - štít (1 zásah)");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.orElse(naBody) == naCas) {
            plocha.setTypHry(TypHry.NaCas);
            plocha.setZacatekHry(System.currentTimeMillis());
            cas.setVisible(true);
        } else {
            plocha.setTypHry(TypHry.NaBody);
            cas.setVisible(false);
        }
        if (!mediaPlayer.statusProperty().get().equals(Status.PLAYING)) {
            mediaPlayer.play();
        }
        paneHraciPlocha.widthProperty().removeListener(listener);
        plocha.getTimer().start();
    }
    
    public void restartHry() {
        if (!plocha.getTimer().isActive() && plocha.isKonecHry()) {
            Souboj.currentlyActiveKeys.clear();
            ButtonType znovu = new ButtonType("Ano", ButtonBar.ButtonData.OK_DONE);
            ButtonType konec = new ButtonType("Konec", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(znovu);
            alert.getButtonTypes().add(konec);
            alert.setHeaderText("");
            if (plocha.isVyhra()) {
                alert.setTitle("Výhra!");
                alert.setContentText("Vyhráli jste!!\nChcete hrát ještě?");
            } else {
                alert.setTitle("Prohra!");
                alert.setContentText("Prohráli jste :(\nChcete hrát ještě?");
            }
            
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.orElse(konec) == znovu) {
                plocha.setKonecHry(false);
                plocha.setZacatekHry(System.currentTimeMillis());
                plocha.hrac1.setSkore(0);
                plocha.hrac2.setSkore(0);
                plocha.setVyhra(false);
                plocha.hrac2.setStit(true);
                plocha.hrac1.setStit(true);
                plocha.hrac2.setStit(true);
                plocha.hrac1.setRychlyPohyb(false);
                plocha.hrac2.setRychlyPohyb(false);
                plocha.hrac1.setRychlaStrelba(false);
                plocha.hrac2.setRychlaStrelba(false);
                plocha.hrac1.setPoziceY(startYHrac1);
                plocha.hrac2.setPoziceY(startYHrac2);
                plocha.hrac1.zobrazovaciKruh.setCenterY(startYHrac1);
                plocha.hrac2.zobrazovaciKruh.setCenterY(startYHrac2);
                plocha.setIntervalVylepseniObjeveni(10 + Math.random() * 10);
                for (Projektil kulka : plocha.kulky) {
                    paneHraciPlocha.getChildren().remove(kulka.zobrazovaciObdelnik);
                }
                for (Vylepseni vylepseni : plocha.vylepseni) {
                    paneHraciPlocha.getChildren().remove(vylepseni.zobrazovaciKruh);
                }
                plocha.kulky.clear();
                plocha.vylepseni.clear();
                startHry();
            } else {
                System.exit(0);
            }
        }
    }
    
    @FXML
    private void mouseMovePane(MouseEvent event) {
        Souboj.mouseX = event.getX();
        Souboj.mouseY = event.getY();
        restartHry();
    }
    
    private boolean jeBodVKruhu(double bodX, double bodY, double kruhX, double kruhY, double kruhR) {
        if (Math.sqrt(Math.pow(bodX - kruhX, 2) + Math.pow(bodY - kruhY, 2)) <= kruhR) {
            return true;
        }
        return false;
    }
    
    public Media getSoundZap() {
        return soundZap;
    }
    
    public Media getSoundPowerUp() {
        return soundPowerUp;
    }
    
}
