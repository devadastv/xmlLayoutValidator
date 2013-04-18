/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author devadas
 */
public class ScreenMapper extends Container
{
    ArrayList gadgetMap;
    Font HELP_ITEM_FONT = new Font("Helvetica LT Bold", Font.PLAIN, 24);

    public void displayScreen(Object obj)
    {
        System.out.println("setting mapper obj");

        this.gadgetMap = (ArrayList) obj;
        // Sort gadgets based on its image dimension to prevent overlapping
        Collections.sort(gadgetMap);
        System.out.println("Painting gadgetMap = " + gadgetMap);
    }

    @Override
    public void paint(Graphics g)
    {
        System.out.println("MAPPER PAINT");
        super.paint(g);
        ArrayList listOfStringLocations = new ArrayList();
        Iterator iterator = gadgetMap.iterator();
        while (iterator.hasNext())
        {
            GadgetDisplayElement gadgetElement = (GadgetDisplayElement) iterator.next();
            Color gadgetColor = GadgetConfig.getGadgetColor(gadgetElement.getGadgetType());
            g.setColor(gadgetColor);
            if (gadgetElement.getGadgetType() == GadgetConfig.RENDER_ITEM_TEXT_OR_IMAGE)
            {
                g.fillRect(gadgetElement.getBounds().x, gadgetElement.getBounds().y,
                    gadgetElement.getBounds().width, gadgetElement.getBounds().height);
            } else
            {
                g.drawRect(gadgetElement.getBounds().x, gadgetElement.getBounds().y,
                        gadgetElement.getBounds().width, gadgetElement.getBounds().height);
            }
            g.setColor(gadgetColor.brighter().brighter().brighter().brighter());
            Image image = gadgetElement.getBackgroundImage();
            if (null != image)
            {
                g.drawImage(image, gadgetElement.getBounds().x, gadgetElement.getBounds().y,
                        gadgetElement.getBounds().width, gadgetElement.getBounds().height, this);
            }

            // Logic to prevent the gadget name to display at different co-ordinates even when
            // the gadget co-ordinates are the same
            int stringXPos = gadgetElement.getBounds().x + 20;
            int stringYPos = gadgetElement.getBounds().y;
            int index = 1;
            Point stringLocation;
            while (true)
            {
                stringLocation = new Point(stringXPos, stringYPos + (index * 20))
                {
                    /**
                     * Make sure strings are not drawn together
                     */
                    public boolean equals(Object obj)
                    {
                        if (obj instanceof Point)
                        {
                            Point pointToCompare = (Point) obj;
                            int verticalDifference = this.y - pointToCompare.y;
                            int horizontalDifference = this.x - pointToCompare.x;
                            if ((verticalDifference < 19 && verticalDifference > -19) && (horizontalDifference < 20 && horizontalDifference > -20))
                            {
                                return true;
                            } else
                            {
                                return false;
                            }
                        }
                        return false;
                    }
                };
                if (!listOfStringLocations.contains(stringLocation))
                {
                    listOfStringLocations.add(stringLocation);
                    break;
                }
                index++;
            }
            if (gadgetElement.getGadgetType() == GadgetConfig.HELP_ITEM)
            {
                Font defaultFont = g.getFont();
                g.setFont(HELP_ITEM_FONT);
                g.drawString(gadgetElement.getDisplayText(), gadgetElement.getBounds().x, gadgetElement.getBounds().y);
                g.setFont(defaultFont);
            } else
            {
                g.drawString(gadgetElement.getGadgetName(), stringLocation.x, stringLocation.y);
            }

        }
    }
}
