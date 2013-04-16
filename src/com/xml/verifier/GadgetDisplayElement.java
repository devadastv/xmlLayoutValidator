/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author devadas
 */
public class GadgetDisplayElement {
    private String gadgetName;
    private Rectangle bounds;
    private short gadgetType;
    private String backgroundImagePath;

    public GadgetDisplayElement(String gadgetName, Rectangle bounds, short gadgetType, String backgroundImagePath) {
        this.gadgetName = gadgetName;
        this.bounds = bounds;
        this.gadgetType = gadgetType;
        this.backgroundImagePath = backgroundImagePath;
    }

    @Override
    public String toString() {
        return "[gadgetName = " + gadgetName + " bounds = " + bounds + "]";
    }

    
    /**
     * @return the gadgetName
     */
    public String getGadgetName() {
        return gadgetName;
    }

    /**
     * @return the bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * @return the gadgetType
     */
    public short getGadgetType() {
        return gadgetType;
    }

    /**
     * @return the backgroundImage
     */
    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }
    
}
