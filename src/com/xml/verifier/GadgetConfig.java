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
public class GadgetConfig
{
    public static final short GADGET_NOT_SUPPORTED = -1;
    public static final short VERTICAL_LIST = 0;
    public static final short HORIZONTAL_LIST = 1;
    public static final short GRID = 2;
    public static final short FOCUS_BOX = 3;
    public static final short VERTICAL_BOX = 4;
    public static final short SELECTION_OVAL = 5;
    public static final short ACTION_HELP = 6;
    public static final short VEIL_RECTANGLE = 7;
    public static final short TEXT_BOX = 8;
    public static final short ARROWS = 9;
    public static final short IMAGE_BOX = 10;
    public static final short HELP_ITEM = 11;

    public static short getGadgetTypeIfSupported(String gadgetName)
    {
        if (gadgetName.equals("verticalList"))
        {
            return VERTICAL_LIST;
        }
        if (gadgetName.equals("horizontalList"))
        {
            return HORIZONTAL_LIST;
        }
        if (gadgetName.equals("grid"))
        {
            return GRID;
        }
        if (gadgetName.equals("focusBox"))
        {
            return FOCUS_BOX;
        }
        if (gadgetName.equals("verticalBox"))
        {
            return VERTICAL_BOX;
        }
        if (gadgetName.equals("selectionOval"))
        {
            return SELECTION_OVAL;
        }
        if (gadgetName.equals("actionHelp"))
        {
            return ACTION_HELP;
        }
        if (gadgetName.equals("veilRectangle"))
        {
            return VEIL_RECTANGLE;
        }
        if (gadgetName.equals("textBox"))
        {
            return TEXT_BOX;
        }
        if (gadgetName.equals("arrows"))
        {
            return ARROWS;
        }
        if (gadgetName.equals("imageBox"))
        {
            return IMAGE_BOX;
        }
        if (gadgetName.equals("helpItem"))
        {
            return HELP_ITEM;
        }
        return GADGET_NOT_SUPPORTED;
    }

    public static Color getGadgetColor(short gadgetType)
    {
        switch (gadgetType)
        {
            case VERTICAL_LIST:
                return Color.red;
            case HORIZONTAL_LIST:
                return Color.green;
            case FOCUS_BOX:
                return Color.green;
            case GRID:
                return Color.cyan;
            case VERTICAL_BOX:
                return Color.blue;
            case SELECTION_OVAL:
                return Color.magenta;
            case ACTION_HELP:
                return Color.white;
            case VEIL_RECTANGLE:
                return Color.PINK;
            case TEXT_BOX:
                return Color.darkGray;
            case ARROWS:
                return Color.RED;
            case IMAGE_BOX:
                return Color.green;
            case HELP_ITEM:
                return Color.cyan;
            default:
                return Color.gray;
        }
    }
}
