/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
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

    public static final String BASE_DIR = "/home/devadas/dev/dstvo/tools/Tools-ng-encapsulation-dstvo";
    HashMap screenNameToGadgetMap;
    private int indexForGadget;

    public static void main(String[] a) {
        new LayoutVerifier().scanFilesInFolder();
    }

    public LayoutVerifier() {
        screenNameToGadgetMap = new HashMap();
    }

    private void scanFilesInFolder() {
        File flattenedDir = new File(BASE_DIR);
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
                    ArrayList gadgetNameToBoundsMap = new ArrayList();
                    indexForGadget = 0;
                    verifyLayout(doc.getDocumentElement(), gadgetNameToBoundsMap);
                }

            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        }

        
        
        // OPTIONS - after reading
        
        ArrayList screenList = new ArrayList(screenNameToGadgetMap.keySet());
//        System.out.println("screenList size = " + screenList.size() + " screenNameToGadgetMap size = " + screenNameToGadgetMap.size());
        Iterator iterator = screenList.iterator();
        int index = 0;
        while (iterator.hasNext())
        {
            String screenNameInMap = (String) iterator.next();
            System.out.println("" + ++index + ". " + screenNameInMap);
        }
        System.out.println("Any other number : EXIT");
        System.out.println("Please select the screen to check: ");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int screenIndex = Integer.parseInt(s);
        if (screenIndex > 0 && screenIndex <=  screenList.size())
        {
            System.out.println("screen selected is : " + screenList.get(screenIndex - 1));
        } else
        {
            System.out.println("EXITING...");
            System.exit(0);
        }
        
//        s = in.nextLine();
         
        ArrayList gadgetMap = (ArrayList) screenNameToGadgetMap.get(screenList.get(screenIndex - 1));
        System.out.println("gadgetMap = " + gadgetMap);
        
//        Arrays.s
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

    public void verifyLayout(Node node, ArrayList gadgetNameToBoundsMap) {
        
        String screenName = null;
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
        if (null != screenName && !screenNameToGadgetMap.containsKey(screenName) && gadgetNameToBoundsMap.size() > 0)
        {
            System.out.println("");
            screenNameToGadgetMap.put(screenName, gadgetNameToBoundsMap);
        }
        
    }

    private void addToLayout(Node gadgetNode, ArrayList gadgetNameToBoundsMap, short gadgetType) {
        int x = 0, y = 0, width = 0, height = 0;
        String formattedImagePath = null;
        String gadgetName = null;
        if (null != gadgetNode.getAttributes().getNamedItem("name"))
        {
            gadgetName = gadgetNode.getAttributes().getNamedItem("name").getNodeValue();
        }
        else
        {
            gadgetName = gadgetNode.getNodeName() + indexForGadget++; // Takes default node name if name is not configured
        }
        
//        System.out.println(" Gadget name is " + gadgetName);

        if (gadgetNode.getChildNodes().getLength() > 0) {
            for (int i = 0; i < gadgetNode.getChildNodes().getLength(); i++) {
                String nodeName = gadgetNode.getChildNodes().item(i).getNodeName();
                if (nodeName.equals("x")) {
                    x = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("y")) {
                    y = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("width")) {
                    width = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("height")) {
                    height = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("image")) {
                    String fullPath = gadgetNode.getChildNodes().item(i).getTextContent();
                    System.out.println("fullPath = " + fullPath + " fullPath.lastIndexOf(/) = " + fullPath.lastIndexOf("/"));
                    System.out.println("Modified :" + fullPath.substring(fullPath.lastIndexOf("/", fullPath.lastIndexOf("/") - 1)));
                    if (null != fullPath && fullPath.trim().length() != 0 && fullPath.indexOf("/") != -1)
                    {
                        formattedImagePath = fullPath.substring(fullPath.lastIndexOf("/", fullPath.lastIndexOf("/") - 1));
                        if (formattedImagePath.startsWith("/News24"))
                        {
                            formattedImagePath = formattedImagePath.replaceFirst("News24", "News");
                        }
                    }
                    
                    System.out.println("fullPath = " + fullPath + " formattedImagePath = " + formattedImagePath);
                }
            }
        }
        Rectangle bounds = new Rectangle(x, y, width, height);
        gadgetNameToBoundsMap.add(new GadgetDisplayElement(gadgetName, bounds, gadgetType, formattedImagePath));
    }
}