/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xml.verifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.List;

/**
 *
 * @author user
 */
public class ListView extends Component {
    List listOfItems;
    public void setList(List list)
    {
        System.out.println("Setting renderers list...");
        this.listOfItems = list;
    }

    @Override
    public void paint(Graphics g)
    {
        System.out.println("Inside list paint... listOfItems = " + listOfItems);
        g.setColor(Color.white);
        if (null != listOfItems)
        {
            for (int i = 0; i < listOfItems.size(); i++)
            {
                String entry = (String) listOfItems.get(i);
                String modifiedText = ("" + (i + 1) + ". " + entry);
                System.out.println("i = " + i + " getX() = " + getX()+ " getY() = " + getY() + " (20 * i) = " + (i * 20)) ;
                System.out.println("modifiedText = " + modifiedText);
                g.drawString(modifiedText, getX(), (i * 20));
            }
        }
    }
}
