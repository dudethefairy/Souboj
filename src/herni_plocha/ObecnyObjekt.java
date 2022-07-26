/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herni_plocha;

/**
 *
 * @author tzlat
 */
public abstract class ObecnyObjekt {
    protected double poziceX;
    protected double poziceY;
    protected double smer;

    public ObecnyObjekt(double poziceX, double poziceY, double smer) {
        this.poziceX = poziceX;
        this.poziceY = poziceY;
        this.smer = smer;
    }

    public double getPoziceX() {
        return poziceX;
    }

    public void setPoziceX(double poziceX) {
        this.poziceX = poziceX;
    }

    public double getPoziceY() {
        return poziceY;
    }

    public void setPoziceY(double poziceY) {
        this.poziceY = poziceY;
    }
  
    
    public double getSmer() {
        return smer;
    }

    public void setSmer(double smer) {
        this.smer = smer;
    }
    
}
