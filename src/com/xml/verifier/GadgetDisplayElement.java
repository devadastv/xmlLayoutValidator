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
    private String displayText;

    public GadgetDisplayElement(String gadgetName, Rectangle bounds, short gadgetType, String backgroundImagePath, String displayText)
    {
        this.gadgetName = gadgetName;
        this.bounds = bounds;
        this.gadgetType = gadgetType;
        this.backgroundImagePath = backgroundImagePath;
        this.displayText = displayText;
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

    public Image getBackgroundImage()
    {
        if (null != backgroundImagePath)
        {
            backgroundImage = loadImage(backgroundImagePath);
        }
        return backgroundImage;
    }

    /**
     * @return the displayText
     */
    public String getDisplayText()
    {
        return displayText;
    }

    private static Image loadImage(String fileName)
    {
        fileName = LayoutVerifier.OUTPUT_XML_DIR + "/../../Resources/Images/" + fileName;
        Image image = Toolkit.getDefaultToolkit().createImage(fileName);
        MediaTracker tracker = new MediaTracker(new Container());
        tracker.addImage(image, 0);
        try
        {
            tracker.waitForAll();
        } catch (InterruptedException ex)
        {
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
                    } else if (thisImageArea > othersImageArea)
                    {
                        return -1;
                    } else
                    {
                        return 1;
                    }
                } else
                {
                    return -1;
                }
            } else
            {
                if (null != othersBgImage)
                {
                    return 1;
                } else
                {
                    return 0;
                }
            }
        } else
        {
            return 1;
        }
    }
}
