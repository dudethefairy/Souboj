/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herni_plocha;

import javafx.scene.shape.Circle;

/**
 *
 * @author tzlat
 */
public class Vylepseni extends ObecnyObjekt {

    public Circle zobrazovaciKruh;
    public DruhVylepseni druh;

    public Vylepseni(Circle zobrazovaciKruh, DruhVylepseni druh, double poziceX, double poziceY, double smer) {
        super(poziceX, poziceY, smer);
        this.zobrazovaciKruh = zobrazovaciKruh;
        this.druh = druh;
    }

}
