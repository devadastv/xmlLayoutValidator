/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author devadas
 */
public class LayoutVerifier {

    HashMap screenNameToGadgetMap;
    private int indexForGadget;

    public static void main(String[] a) {
        new LayoutVerifier().scanFilesInFolder();
    }

    public LayoutVerifier() {
        screenNameToGadgetMap = new HashMap();
    }

    private void scanFilesInFolder() {
        File flattenedDir = new File("<Encapsulation_dir_name_here>");
        File[] listOfFlattenedFiles = flattenedDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File file, String string) {
                if ((string.endsWith("xml")) && ((string.startsWith("output")))) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < listOfFlattenedFiles.length; i++) {
            File file = listOfFlattenedFiles[i];
            System.out.println("Filelist = " + file);

            DocumentBuilder dBuilder;
            try {
                dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                if (verifyScreenLayout(doc.getElementsByTagName("*"))) {
                    HashMap gadgetNameToBoundsMap = new HashMap();
                    indexForGadget = 0;
                    verifyLayout(doc.getDocumentElement(), gadgetNameToBoundsMap);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        
        
        // OPTIONS - after reading
        
        Iterator iterator = screenNameToGadgetMap.keySet().iterator();
        while (iterator.hasNext()) {
            String screenNameInMap = (String) iterator.next();
//                    System.out.println("FROM MAP name = " + screenNameInMap);
        }

        HashMap gadgetMap = (HashMap) screenNameToGadgetMap.get("<SAMPLE_XML_NAME_HERE>");
        System.out.println("gadgetMap = " + gadgetMap);
//        iterator = gadgetMap.values().iterator();
//        while (iterator.hasNext()) {
//            Rectangle gadgetBounds = (Rectangle) iterator.next();
//            System.out.println("Bounds = " + gadgetBounds);
//        }
        LayoutDisplay display = new LayoutDisplay();
        display.initGUI();
        display.displayScreen(gadgetMap);

    }

    public boolean verifyScreenLayout(NodeList node) {
        if (node.getLength() > 1 && node.item(1).getNodeName().equals("screen")) {
            System.out.println("Screen name is : " + node.item(1).getAttributes().getNamedItem("name").getNodeValue());
            return true;
        }
        return false;
    }

    public void verifyLayout(Node node, HashMap gadgetNameToBoundsMap) {
        
        String screenName = "";
        if (node != null) {
            if (node.getChildNodes().getLength() > 0) {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    String nodeName = node.getChildNodes().item(i).getNodeName();
//                    System.out.println("[DTV] nodeName = " + nodeName);
//                    if ((nodeName.equals("grid")) || (nodeName.equals("verticalList"))
//                            || (nodeName.equals("focusBox")) || (nodeName.equals("verticalBox"))) {
//                        addToLayout(node.getChildNodes().item(i), gadgetNameToBoundsMap);
//                    }
                    short gadgetType = GadgetConfig.getGadgetType(nodeName);
                    if (gadgetType != GadgetConfig.GADGET_NOT_SUPPORTED)
                    {
                        addToLayout(node.getChildNodes().item(i), gadgetNameToBoundsMap, gadgetType);
                    }
                    if (nodeName.equals("screen")) {
                        screenName = node.getChildNodes().item(i).getAttributes().getNamedItem("name").getNodeValue();
                        System.out.println("screenName = " + screenName);
                    }
                    verifyLayout(node.getChildNodes().item(i), gadgetNameToBoundsMap);
                }
            }
        }
        if (!screenNameToGadgetMap.containsKey(screenName))
        {
            screenNameToGadgetMap.put(screenName, gadgetNameToBoundsMap);
        }
        
    }

    private void addToLayout(Node gadgetNode, HashMap gadgetNameToBoundsMap, short gadgetType) {
        int x = 0, y = 0, width = 0, height = 0;
        String gadgetName = null;
        if (null != gadgetNode.getAttributes().getNamedItem("name"))
        {
            gadgetName = gadgetNode.getAttributes().getNamedItem("name").getNodeValue();
        }
        else
        {
            gadgetName = gadgetNode.getNodeName() + indexForGadget++; // Takes default node name if name is not configured
        }
        
        System.out.println(" Gadget name is " + gadgetName);

        if (gadgetNode.getChildNodes().getLength() > 0) {
            for (int i = 0; i < gadgetNode.getChildNodes().getLength(); i++) {
                String nodeName = gadgetNode.getChildNodes().item(i).getNodeName();
//                System.out.println(">>>>>> [DTV] gadget child nodeName = " + nodeName);
                // Do the mapping node tree here
                if (nodeName.equals("x")) {
                    x = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
//                    System.out.println("X value is " + x);
                }
                if (nodeName.equals("y")) {
                    y = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
//                    System.out.println("Y value is " + y);
                }
                if (nodeName.equals("width")) {
                    width = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
//                    System.out.println("width value is " + width);
                }
                if (nodeName.equals("height")) {
                    height = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
//                    System.out.println("height value is " + height);
                }
            }
        }
        Rectangle bounds = new Rectangle(x, y, width, height);
        System.out.println("gadgetName = " + gadgetName + " is having bounds = " + bounds);
        
        gadgetNameToBoundsMap.put(gadgetName, new GadgetDisplayElement(gadgetName, bounds, gadgetType, null));
    }
}