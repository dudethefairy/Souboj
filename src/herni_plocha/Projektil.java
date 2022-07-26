/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herni_plocha;

import java.util.LinkedList;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * @author tzlat
 */
public class Projektil extends ObecnyObjekt {

    private double rychlost;
    public Rectangle zobrazovaciObdelnik;
    private Hrac vlastnik;

    public Projektil(double rychlost, Rectangle zobrazovaciObdelnik, double poziceX, double poziceY, double smer, Hrac vlastnik) {
        super(poziceX, poziceY, smer);
        if (vlastnik.isRychlaStrelba()) {
            this.rychlost = rychlost * 2;
        } else {
            this.rychlost = rychlost;
        }
        this.zobrazovaciObdelnik = zobrazovaciObdelnik;
        Point2D posun = urciPosunPodUhlem(vlastnik.zobrazovaciKruh.getRadius(), smer);
        poziceX = posun.getX();
        poziceY = posun.getY();
        this.zobrazovaciObdelnik.setX(poziceX + zobrazovaciObdelnik.getWidth() / 2);
        this.zobrazovaciObdelnik.setY(poziceY + zobrazovaciObdelnik.getHeight() / 2);
        this.vlastnik = vlastnik;
        Rotate rotace = new Rotate(180 - smer, poziceX + zobrazovaciObdelnik.getWidth() / 2, poziceY + zobrazovaciObdelnik.getHeight() / 2);
        this.zobrazovaciObdelnik.getTransforms().addAll(rotace);
    }

    public void Let(Pane panel, HerniPlocha plocha, float time) {
        Rotate rotace = new Rotate(180 - smer, poziceX + zobrazovaciObdelnik.getWidth() / 2, poziceY + zobrazovaciObdelnik.getHeight() / 2);
        zobrazovaciObdelnik.getTransforms().clear();
        zobrazovaciObdelnik.getTransforms().addAll(rotace);
        Point2D posun = urciPosunPodUhlem(rychlost, smer);
        poziceX += posun.getX();
        poziceY += posun.getY();
        zobrazovaciObdelnik.setX(poziceX);
        zobrazovaciObdelnik.setY(poziceY);
        LinkedList<Vylepseni> vylepseni = new LinkedList<Vylepseni>();
        vylepseni.addAll(plocha.vylepseni);
        for (Vylepseni vyl : vylepseni) {
            if (narazilaKulkaNaKruh(zobrazovaciObdelnik, vyl.zobrazovaciKruh)) {
                plocha.playerZap.play();
                panel.getChildren().remove(vyl.zobrazovaciKruh);
                plocha.vylepseni.remove(vyl);
                switch (vyl.druh) {
                    case RychlostKulek:
                        vlastnik.setRychlaStrelba(true);
                        vlastnik.setTrvaniRychlostKulek(10);
                        break;
                    case RychlostPohybu:
                        vlastnik.setRychlyPohyb(true);
                        vlastnik.setTrvaniRychlostPohybu(10);
                        break;
                    case Stit:
                        vlastnik.setStit(true);
                        break;
                }
                plocha.kulky.remove(this);
                panel.getChildren().remove(zobrazovaciObdelnik);
            }
        }

        if (vlastnik == plocha.hrac1) {

            if (narazilaKulkaNaKruh(zobrazovaciObdelnik, plocha.hrac2.zobrazovaciKruh)) {
                plocha.kulky.remove(this);
                panel.getChildren().remove(zobrazovaciObdelnik);
                if (plocha.hrac2.isStit()) {
                    plocha.hrac2.setStit(false);
                } else {
                    plocha.hrac1.setSkore(plocha.hrac1.getSkore() + 1);
                }
            }
        }
        if (vlastnik == plocha.hrac2) {

            if (narazilaKulkaNaKruh(zobrazovaciObdelnik, plocha.hrac1.zobrazovaciKruh)) {
                plocha.kulky.remove(this);
                panel.getChildren().remove(zobrazovaciObdelnik);

                if (plocha.hrac1.isStit()) {
                    plocha.hrac1.setStit(false);
                } else {
                    plocha.hrac2.setSkore(plocha.hrac2.getSkore() + 1);
                }
            }
        }
        if (poziceX > plocha.prefWidth || poziceX < 0 || poziceY > plocha.prefHeight || poziceY < 0) {
            plocha.kulky.remove(this);
            panel.getChildren().remove(zobrazovaciObdelnik);
        }
    }

    public Point2D urciPosunPodUhlem(double vzdalenost, double uhel) {
        double uhelUpraveny = uhel;
        int posun = 0;
        while (uhelUpraveny >= 90) {
            uhelUpraveny -= 90;
            posun++;
        }
        double posunY = Math.abs(Math.sin(Math.toRadians(uhelUpraveny)) * vzdalenost);
        double posunX = Math.sqrt(Math.abs(vzdalenost * vzdalenost) - Math.abs(posunY * posunY));
        if (posun % 2 != 0) {
            double pom = posunY;
            posunY = posunX;
            posunX = pom;
        }
//        if (smer > 270 || smer < 90) {
//            posunX = posunX;
//        }
        if (smer > 90 && smer < 270) {
            posunX = -posunX;
        }
//        if (smer > 180 && smer != 360) {
//            poziceY += posunY;
//        }
        if (smer < 180 && smer != 0) {
            posunY = -posunY;
        }
        return new Point2D(posunX, posunY);
    }

    public double getRychlost() {
        return rychlost;
    }

    public void setRychlost(double rychlost) {
        this.rychlost = rychlost;
    }

    public Rectangle getZobrazovaciObdelnik() {
        return zobrazovaciObdelnik;
    }

    public void setZobrazovaciObdelnik(Rectangle zobrazovaciObdelnik) {
        this.zobrazovaciObdelnik = zobrazovaciObdelnik;
    }

    private boolean narazilaKulkaNaKruh(Rectangle kulka, Circle kruh) {
        double x1x3 = kulka.getX();
        double y1y2 = kulka.getY();
        double x2x4 = kulka.getX() + kulka.getWidth();
        double y3y4 = kulka.getY() + kulka.getHeight();
        if (jeBodVKruhu(x1x3, y1y2, kruh.getCenterX(), kruh.getCenterY(), kruh.getRadius())
                || jeBodVKruhu(x2x4, y1y2, kruh.getCenterX(), kruh.getCenterY(), kruh.getRadius())
                || jeBodVKruhu(x1x3, y3y4, kruh.getCenterX(), kruh.getCenterY(), kruh.getRadius())
                || jeBodVKruhu(x2x4, y3y4, kruh.getCenterX(), kruh.getCenterY(), kruh.getRadius())) {
            return true;
        }
        return false;
    }

    private boolean jeBodVKruhu(double bodX, double bodY, double kruhX, double kruhY, double kruhR) {
        if (Math.sqrt(Math.pow(bodX - kruhX, 2) + Math.pow(bodY - kruhY, 2)) <= kruhR) {
            return true;
        }
        return false;
    }
}
