/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 *
 * @author devadas
 */
public class GadgetDisplayElement implements Comparable
{
    private String gadgetName;
    private Rectangle bounds;
    private short gadgetType;
    private String backgroundImagePath;
    private Image backgroundImage;

    public GadgetDisplayElement(String gadgetName, Rectangle bounds, short gadgetType, String backgroundImagePath)
    {
        this.gadgetName = gadgetName;
        this.bounds = bounds;
        this.gadgetType = gadgetType;
        this.backgroundImagePath = backgroundImagePath;
    }

    @Override
    public String toString()
    {
        return "[gadgetName = " + gadgetName + " bounds = " + bounds + " backgroundImagePath = " + backgroundImagePath + "]";
    }

    /**
     * @return the gadgetName
     */
    public String getGadgetName()
    {
        return gadgetName;
    }

    /**
     * @return the bounds
     */
    public Rectangle getBounds()
    {
        return bounds;
    }

    /**
     * @return the gadgetType
     */
    public short getGadgetType()
    {
        return gadgetType;
    }

    /**
     * @return the backgroundImage
     */
    public String getBackgroundImagePath()
    {
        return backgroundImagePath;
    }

    public Image getBackgroundImage()
    {
        if (null != backgroundImagePath)
        {
            backgroundImage = loadImage(backgroundImagePath);
        }
        return backgroundImage;
    }

    private static Image loadImage(String fileName)
    {
        fileName = "/home/devadas/dev/dstvo/Resources/Images/News/" + fileName;
        Image image = Toolkit.getDefaultToolkit().createImage(fileName);
        MediaTracker tracker = new MediaTracker(new Container());
        tracker.addImage(image, 0);
        try
        {
            tracker.waitForAll();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        return image;
    }

    @Override
    public int compareTo(Object obj)
    {
        if (obj instanceof GadgetDisplayElement)
        {
            GadgetDisplayElement objectToCompare = (GadgetDisplayElement) obj;
            Image thisBgImage = getBackgroundImage();
            Image othersBgImage = objectToCompare.getBackgroundImage();
            if (null != thisBgImage)
            {
                if (null != othersBgImage)
                {
                    int thisImageArea = thisBgImage.getHeight(null) * thisBgImage.getWidth(null);
                    int othersImageArea = othersBgImage.getHeight(null) * othersBgImage.getWidth(null);
                    if (thisImageArea == othersImageArea)
                    {
                        return 0;
                    }
                    else if (thisImageArea > othersImageArea)
                    {
                        return -1; 
                    }
                    else 
                    {
                        return 1;
                    }
                }
                else 
                {
                    return -1;
                }
            }
            else 
            {
                if (null != othersBgImage)
                {
                    return 1;
                }
                else 
                {
                    return 0;
                }
            }
        }
        else
        {
            return 1;
        }
    }
}
