/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Color;

/**
 *
 * @author devadas
 */
public class GadgetConfig {

    public static final short GADGET_NOT_SUPPORTED = -1;
    public static final short VERTICAL_LIST = 0;
    public static final short HORIZONTAL_LIST = 1;
    public static final short GRID = 2;
    public static final short FOCUS_BOX = 3;
    public static final short VERTICAL_BOX = 4;
    public static final short SELECTION_OVAL = 5;
    public static final short ACTION_HELP = 6;
    public static final short VEIL_RECTANGLE = 7;
    
    

    public static short getGadgetType(String gadgetName) {
        if (gadgetName.equals("verticalList")) {
            return VERTICAL_LIST;
        }
        if (gadgetName.equals("focusBox")) {
            return HORIZONTAL_LIST;
        }
        if (gadgetName.equals("grid")) {
            return GRID;
        }
        if (gadgetName.equals("focusBox")) {
            return FOCUS_BOX;
        }
        if (gadgetName.equals("verticalBox")) {
            return VERTICAL_BOX;
        }
        if (gadgetName.equals("selectionOval")) {
            return SELECTION_OVAL;
        }
        if (gadgetName.equals("actionHelp")) {
            return ACTION_HELP;
        }
        if (gadgetName.equals("veilRectangle")) {
            return VEIL_RECTANGLE;
        }
        return GADGET_NOT_SUPPORTED;
    }

    public static Color getGadgetColor(short gadgetType) {
        switch (gadgetType) {
            case VERTICAL_LIST:
                return Color.red;
            case HORIZONTAL_LIST:
                return Color.green;
            case FOCUS_BOX:
                return Color.cyan;
            case GRID:
                return Color.yellow;
            case VERTICAL_BOX:
                return Color.blue;
            case SELECTION_OVAL:
                return Color.magenta;
            case ACTION_HELP:
                return Color.ORANGE;
            case VEIL_RECTANGLE:
                return Color.PINK;
            default:
                return Color.gray;
        }
    }
}
