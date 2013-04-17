/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author devadas
 */
public class LayoutDisplay extends Frame {

    ScreenMapper mapper;
    public LayoutDisplay() {
        
        this.setBounds(0, 0, 1320, 730);
        this.setBackground(Color.white);
    }
    
    public void initGUI()
    {
        Container container = new Container()
        {

            @Override
            public void paint(Graphics g) {
                
                g.setColor(Color.lightGray);
                g.fillRect(0, 0, 1280, 720);
                super.paint(g);
            }
            
        };
        container.setBounds(this.getBounds());
        mapper = new ScreenMapper();
        mapper.setBounds(this.getBounds());
        container.add(mapper);
        mapper.setVisible(true);
        container.setVisible(true);
        this.add(container);
        this.setVisible(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        });
    }
    
    public void displayScreen(Object obj)
    {
        mapper.displayScreen(obj);
        this.repaint();
    }
}
