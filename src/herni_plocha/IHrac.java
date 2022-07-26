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
public interface IHrac {

    public void update(float time);

    public Projektil vystrel();

    public void jdiNahoru(double minY, float time);

    public void jdiDolu(double maxY, float time);

    public void otocSe(double bodX, double bodY);
}
