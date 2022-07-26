/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herni_plocha;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import souboj.FXMLDocumentController;
import souboj.Souboj;

/**
 *
 * @author tzlat
 */
public class HerniPlocha {

    private TypHry typHry;
    private boolean konecHry = false;
    private boolean vyhra = false;
    public Pane plocha;
    public Label hrac1Skore;
    public Label hrac2Skore;
    public Label cas;
    public Hrac hrac1;
    public Hrac hrac2;
    public double prefWidth;
    public double prefHeight;
    private GameLoopTimer timer;
    public LinkedList<Projektil> kulky;
    public LinkedList<Vylepseni> vylepseni;
    private boolean nepritelPohybNahoru = true;
    private double intervalNepritelPohyb = 0.2 + Math.random() * 2;
    private double intervalVylepseniObjeveni = 10 + Math.random() * 10;
    private long zacatekHry = System.currentTimeMillis();
    FXMLDocumentController controler;
    MediaPlayer playerZap;
    MediaPlayer playerPowerUp;

    public double getIntervalVylepseniObjeveni() {
        return intervalVylepseniObjeveni;
    }

    public void setIntervalVylepseniObjeveni(double intervalVylepseniObjeveni) {
        this.intervalVylepseniObjeveni = intervalVylepseniObjeveni;
    }

    public HerniPlocha(Pane plocha, Hrac hrac1, Hrac hrac2, double width, double height, Label hrac1Skore, Label hrac2Skore, Label cas, FXMLDocumentController controler) {
        this.controler = controler;
        playerZap = new MediaPlayer(controler.getSoundZap());
        playerPowerUp = new MediaPlayer(controler.getSoundPowerUp());
        kulky = new LinkedList<>();
        vylepseni = new LinkedList<>();
        this.plocha = plocha;
        this.hrac1 = hrac1;
        this.hrac2 = hrac2;
        this.cas = cas;
        this.hrac1Skore = hrac1Skore;
        this.hrac2Skore = hrac2Skore;
        this.prefWidth = width;
        this.prefHeight = height;
        timer = new GameLoopTimer() {
            @Override
            public void tick(float secondsSinceLastFrame) {
                if (intervalNepritelPohyb > 0) {
                    intervalNepritelPohyb -= secondsSinceLastFrame;
                } else {
                    nepritelPohybNahoru = !nepritelPohybNahoru;
                    intervalNepritelPohyb = 0.2 + Math.random() * 2;
                }

                updateLabels();
                hlidejKonec(secondsSinceLastFrame);
                updateHraci(secondsSinceLastFrame);
                nahodnyPohybSoupere(secondsSinceLastFrame);
                pohybKlavesy(secondsSinceLastFrame);
                updateObjeveniVylepseni(secondsSinceLastFrame);
                LinkedList<Projektil> prochazeni = new LinkedList<>();
                prochazeni.addAll(kulky);
                for (Projektil kulka : prochazeni) {
                    kulka.Let(plocha, HerniPlocha.this, secondsSinceLastFrame);
                }
            }
        };
        if (Math.random() < 0.5) {
            nepritelPohybNahoru = false;
        }
    }

    public GameLoopTimer getTimer() {
        return timer;
    }

    public void setTimer(GameLoopTimer timer) {
        this.timer = timer;
    }

    public boolean isVyhra() {
        return vyhra;
    }

    public void setVyhra(boolean vyhra) {
        this.vyhra = vyhra;
    }

    private void updateHraci(float time) {
        hrac1.update(time);
        hrac2.update(time);
    }

    private void hlidejKonec(float time) {
        if (typHry == TypHry.NaBody) {
            if (hrac1.getSkore() >= 25) {
                konecHry = true;
                vyhra = true;
                timer.stop();
            }
            if (hrac2.getSkore() >= 25) {
                konecHry = true;
                vyhra = false;
                timer.stop();
            }
        } else {
            long casZbyva = 120 - (System.currentTimeMillis() - zacatekHry) / 1000;
            if (casZbyva <= 0) {
                konecHry = true;
                if (hrac1.getSkore() > hrac2.getSkore()) {
                    vyhra = true;
                    timer.stop();
                } else {
                    vyhra = false;
                    timer.stop();
                }
            }
        }
    }

    private void updateObjeveniVylepseni(float time) {
        if (intervalVylepseniObjeveni > 0) {
            intervalVylepseniObjeveni -= time;
        } else {
            double poziceX = Math.random() * prefWidth;
            double poziceY = Math.random() * prefHeight;
            double radius = prefWidth / 60;
            DruhVylepseni druh = dejNahodne();

            Vylepseni vylepseni = new Vylepseni(new Circle(poziceX, poziceY, radius, dejBarvuVylepseni(druh)), druh, poziceX, poziceY, 0);
            plocha.getChildren().add(vylepseni.zobrazovaciKruh);
            this.vylepseni.add(vylepseni);
            intervalVylepseniObjeveni = 5 + Math.random() * 10;
        }

    }

    public DruhVylepseni dejNahodne() {
        int druh = 1 + (int) (Math.random() * 3);
        switch (druh) {
            case 1:
                return DruhVylepseni.RychlostKulek;
            case 2:
                return DruhVylepseni.RychlostPohybu;
            default:
                return DruhVylepseni.Stit;
        }
    }

    public Color dejBarvuVylepseni(DruhVylepseni druh) {
        switch (druh) {
            case RychlostKulek:
                return Color.RED;
            case RychlostPohybu:
                return Color.GREEN;
            case Stit:
                return Color.BLUE;
        }
        return null;
    }

    public void hracStrili(Hrac hrac) {
        Projektil kulka = hrac.vystrel();
        if (kulka != null) {
            playerZap.play();
            kulky.add(kulka);
            plocha.getChildren().add(kulka.getZobrazovaciObdelnik());
        }
    }

    public void updateLabels() {
        hrac1Skore.setText(String.valueOf(hrac1.getSkore()));
        hrac2Skore.setText(String.valueOf(hrac2.getSkore()));
        long casZbyva = 120 - (System.currentTimeMillis() - zacatekHry) / 1000;
        cas.setText(String.valueOf(casZbyva));
    }

    public void nahodnyPohybSoupere(float secondsSinceLastFrame) {
        if (vylepseni.isEmpty()) {
            hrac2.otocSe(hrac1.poziceX, hrac1.poziceY);
        } else {
            hrac2.otocSe(vylepseni.getFirst().poziceX, vylepseni.getFirst().poziceY);
        }

        hracStrili(hrac2);

        if (nepritelPohybNahoru) {
            hrac2.jdiNahoru(0, secondsSinceLastFrame);
        } else {
            hrac2.jdiDolu(prefHeight, secondsSinceLastFrame);
        }
    }

    public void pohybKlavesy(float secondsSinceLastFrame) {
        hrac1.otocSe(Souboj.mouseX, Souboj.mouseY);

        if (Souboj.currentlyActiveKeys.contains("UP")) {
            hrac1.jdiNahoru(0, secondsSinceLastFrame);
        }

        if (Souboj.currentlyActiveKeys.contains("DOWN")) {
            hrac1.jdiDolu(prefHeight, secondsSinceLastFrame);
        }
    }

    public TypHry getTypHry() {
        return typHry;
    }

    public void setTypHry(TypHry typHry) {
        this.typHry = typHry;
    }

    public boolean isKonecHry() {
        return konecHry;
    }

    public void setKonecHry(boolean konecHry) {
        this.konecHry = konecHry;
    }

    public long getZacatekHry() {
        return zacatekHry;
    }

    public void setZacatekHry(long zacatekHry) {
        this.zacatekHry = zacatekHry;
    }

}
