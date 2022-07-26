/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herni_plocha;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author tzlat
 */
public class Hrac extends ObecnyObjekt implements IHrac {

    protected String jmeno;
    public Circle zobrazovaciKruh;
    private boolean nabito;
    private double dobaNabijeni;
    private double nabijeni;
    private double rychlostPohybu;
    private boolean rychlaStrelba;
    private boolean rychlyPohyb;
    private boolean stit = true;
    private int skore;
    private double trvaniRychlostPohybu = 10;
    private double trvaniRychlostKulek = 10;

    public Hrac(String jmeno, Circle zobrazovaciKruh, double poziceX, double poziceY, double smer, double dobaNabijeni, double rychlostPohybu) {
        super(poziceX, poziceY, smer);
        this.jmeno = jmeno;
        this.zobrazovaciKruh = zobrazovaciKruh;
        this.dobaNabijeni = dobaNabijeni;
        this.nabijeni = dobaNabijeni;
        this.rychlostPohybu = rychlostPohybu;
    }

    public double getTrvaniRychlostPohybu() {
        return trvaniRychlostPohybu;
    }

    public void setTrvaniRychlostPohybu(double trvaniRychlostPohybu) {
        this.trvaniRychlostPohybu = trvaniRychlostPohybu;
    }

    public double getTrvaniRychlostKulek() {
        return trvaniRychlostKulek;
    }

    public void setTrvaniRychlostKulek(double trvaniRychlostKulek) {
        this.trvaniRychlostKulek = trvaniRychlostKulek;
    }

    public double getNabijeni() {
        return nabijeni;
    }

    public void setNabijeni(double nabijeni) {
        this.nabijeni = nabijeni;
    }

    public int getSkore() {
        return skore;
    }

    public void setSkore(int skore) {
        this.skore = skore;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public Circle getZobrazovaciKruh() {
        return zobrazovaciKruh;
    }

    public void setZobrazovaciKruh(Circle zobrazovaciKruh) {
        this.zobrazovaciKruh = zobrazovaciKruh;
    }

    public double getDobaNabijeni() {
        return dobaNabijeni;
    }

    public void setDobaNabijeni(double dobaNabijeni) {
        this.dobaNabijeni = dobaNabijeni;
    }

    public double getRychlostPohybu() {
        return rychlostPohybu;
    }

    public void setRychlostPohybu(double rychlostPohybu) {
        this.rychlostPohybu = rychlostPohybu;
    }

    public boolean isRychlaStrelba() {
        return rychlaStrelba;
    }

    public void setRychlaStrelba(boolean rychlaStrelba) {
        this.rychlaStrelba = rychlaStrelba;
    }

    public boolean isRychlyPohyb() {
        return rychlyPohyb;
    }

    public void setRychlyPohyb(boolean rychlytPohyb) {
        this.rychlyPohyb = rychlytPohyb;
    }

    public boolean isStit() {
        return stit;
    }

    public void setStit(boolean stit) {
        this.stit = stit;
    }

    @Override
    public void jdiNahoru(double minY, float time) {
        double rychlost = rychlostPohybu;
        if (rychlyPohyb) {
            rychlost *= 2;
        }
        if (poziceY - (dejPolomerKruhu() + rychlost) > minY) {
            poziceY -= rychlost;
            zobrazovaciKruh.setCenterY(poziceY);
        }
    }

    @Override
    public void jdiDolu(double maxY, float time) {
        double rychlost = rychlostPohybu;
        if (rychlyPohyb) {
            rychlost *= 2;
        }
        if (poziceY + (dejPolomerKruhu() + rychlost) < maxY) {
            poziceY += rychlost;
            zobrazovaciKruh.setCenterY(poziceY);
        }
    }

    @Override
    public Projektil vystrel() {
        if (nabito) {
            nabito = false;
            nabijeni = dobaNabijeni;
            return new Projektil(15, new Rectangle(20, 5, zobrazovaciKruh.getFill()), poziceX, poziceY, smer, this);
        } else {
            return null;
        }
    }

    public boolean isNabito() {
        return nabito;
    }

    public void setNabito(boolean nabito) {
        this.nabito = nabito;
    }

    private double dejPolomerKruhu() {
        return zobrazovaciKruh.getRadius();
    }

    @Override
    public void otocSe(double bodX, double bodY) {
        smer = Math.abs(Math.toDegrees(Math.atan2(poziceY - bodY, poziceX - bodX)) - 180.0);
    }

    @Override
    public void update(float time) {
        if (nabijeni > 0) {
            nabito = false;
            nabijeni -= time;
        } else {
            nabito = true;
        }

        if (rychlyPohyb && trvaniRychlostPohybu > 0) {
            trvaniRychlostPohybu -= time;
        } else if (trvaniRychlostPohybu != 10) {
            trvaniRychlostPohybu = 10;
            rychlyPohyb = false;
        }

        if (rychlaStrelba && trvaniRychlostKulek > 0) {
            trvaniRychlostKulek -= time;
        } else if (trvaniRychlostKulek != 10) {
            trvaniRychlostKulek = 10;
            rychlaStrelba = false;
        }

        if (rychlyPohyb) {
            Color barva = new Color(((Color) zobrazovaciKruh.getFill()).getRed(), 1.0, ((Color) zobrazovaciKruh.getFill()).getBlue(), 1.0);
            zobrazovaciKruh.setFill(barva);
        } else {
            Color barva = new Color(((Color) zobrazovaciKruh.getFill()).getRed(), 0, ((Color) zobrazovaciKruh.getFill()).getBlue(), 1.0);
            zobrazovaciKruh.setFill(barva);
        }
        if (rychlaStrelba) {
            Color barva = new Color(1.0, ((Color) zobrazovaciKruh.getFill()).getGreen(), ((Color) zobrazovaciKruh.getFill()).getBlue(), 1.0);
            zobrazovaciKruh.setFill(barva);
        } else {
            Color barva = new Color(0.0, ((Color) zobrazovaciKruh.getFill()).getGreen(), ((Color) zobrazovaciKruh.getFill()).getBlue(), 1.0);
            zobrazovaciKruh.setFill(barva);
        }
        if (stit) {
            Color barva = new Color(((Color) zobrazovaciKruh.getFill()).getRed(), ((Color) zobrazovaciKruh.getFill()).getGreen(), 1.0, 1.0);
            zobrazovaciKruh.setFill(barva);
        } else {
            Color barva = new Color(((Color) zobrazovaciKruh.getFill()).getRed(), ((Color) zobrazovaciKruh.getFill()).getGreen(), 0.0, 1.0);
            zobrazovaciKruh.setFill(barva);
        }
    }
}
