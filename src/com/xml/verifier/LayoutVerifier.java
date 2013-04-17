/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xml.verifier;

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author devadas
 */
public class LayoutVerifier
{
    public static final String OUTPUT_XML_DIR = "D:\\dev\\dstvo\\tools\\Tools-ng-encapsulation-dstvo\\";
    public static final String STATIC_BACKGROUND_IMAGE = null;
    HashMap screenNameToGadgetMap;
    private int indexForGadget;
    private Rectangle actionHelpBounds;

    public static void main(String[] a)
    {
        new LayoutVerifier().doAction();
    }

    private void doAction()
    {
        System.out.println("\n\n PLEASE WAIT WHILE THE XML FILES ARE BEING PARSED...");
        File[] xmlFilesToProcess = scanFilesInFolder();
        parseAllScreensAndPopulateMap(xmlFilesToProcess);
        printListOfScreensForUserInput();
        processUserInput();
    }

    public LayoutVerifier()
    {
        screenNameToGadgetMap = new HashMap();
    }

    private File[] scanFilesInFolder()
    {
        File flattenedDir = new File(OUTPUT_XML_DIR);
        File[] listOfFlattenedFiles = flattenedDir.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File file, String string)
            {
                if ((string.endsWith("xml")) && ((string.startsWith("output"))))
                {
                    return true;
                }
                return false;
            }
        });
        return listOfFlattenedFiles;
    }

    private void parseAllScreensAndPopulateMap(File[] xmlFilesToProcess)
    {
        for (int i = 0; i < xmlFilesToProcess.length; i++)
        {
            File file = xmlFilesToProcess[i];
            DocumentBuilder dBuilder;
            try
            {
                dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                if (isScreenFile(doc))
                {
                    ArrayList gadgetPropertyList = new ArrayList();
                    indexForGadget = 0;
                    verifyLayout(doc.getDocumentElement(), gadgetPropertyList);
                    addStaticBackground(gadgetPropertyList);
                }
            } catch (Exception ex)
            {
            }
        }
    }

    private void printListOfScreensForUserInput()
    {
        ArrayList screenList = new ArrayList(screenNameToGadgetMap.keySet());
        Collections.sort(screenList);
        Iterator iterator = screenList.iterator();
        int index = 0;
        while (iterator.hasNext())
        {
            String screenNameInMap = (String) iterator.next();
            System.out.println("" + ++index + ". " + screenNameInMap);
        }
        System.out.println("Any other number : EXIT");
        System.out.println("Please select the screen to analyze: ");
    }

    private void processUserInput()
    {
        ArrayList screenList = new ArrayList(screenNameToGadgetMap.keySet());
        Collections.sort(screenList);
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int screenIndex = Integer.parseInt(s);
        if (screenIndex > 0 && screenIndex <= screenList.size())
        {
            System.out.println("screen selected is : " + screenList.get(screenIndex - 1));
        } else
        {
            System.out.println("EXITING...");
            System.exit(0);
        }
        ArrayList gadgetMap = (ArrayList) screenNameToGadgetMap.get(screenList.get(screenIndex - 1));
        LayoutDisplay display = new LayoutDisplay();
        display.initGUI();
        display.displayScreen(gadgetMap);
    }

    private void addStaticBackground(ArrayList gadgetPropertyList)
    {
        if (null != STATIC_BACKGROUND_IMAGE)
        {
            gadgetPropertyList.add(new GadgetDisplayElement("STATIC BACKGROUND CONFIGURED IN CODE", new Rectangle(0, 0, 1280, 720),
                    GadgetConfig.VERTICAL_BOX, getFormattedPath(STATIC_BACKGROUND_IMAGE), null));
        }
    }

    public boolean isScreenFile(Document doc)
    {
        NodeList node = doc.getElementsByTagName("*");
        if (node.getLength() > 1 && node.item(1).getNodeName().equals("screen"))
        {
            return true;
        }
        return false;
    }

    public void verifyLayout(Node node, ArrayList gadgetPropertyList)
    {
        String screenName = null;
        if (node != null)
        {
            if (node.getChildNodes().getLength() > 0)
            {
                for (int i = 0; i < node.getChildNodes().getLength(); i++)
                {
                    String nodeName = node.getChildNodes().item(i).getNodeName();
                    short gadgetType = GadgetConfig.getGadgetTypeIfSupported(nodeName);
                    if (gadgetType != GadgetConfig.GADGET_NOT_SUPPORTED)
                    {
                        addToLayout(node.getChildNodes().item(i), gadgetPropertyList, gadgetType);
                    }
                    if (nodeName.equals("screen"))
                    {
                        screenName = node.getChildNodes().item(i).getAttributes().getNamedItem("name").getNodeValue();
                    }
                    verifyLayout(node.getChildNodes().item(i), gadgetPropertyList);
                }
            }
        }

        if (null != screenName && !screenNameToGadgetMap.containsKey(screenName) && gadgetPropertyList.size() > 0)
        {
            screenNameToGadgetMap.put(screenName, gadgetPropertyList);
        }
    }

    private void addToLayout(Node gadgetNode, ArrayList gadgetPropertyList, short gadgetType)
    {
        int x = 0, y = 0, width = 0, height = 0;
        String formattedImagePath = null, displayText = null;
        String gadgetName = null;
        if (null != gadgetNode.getAttributes().getNamedItem("name"))
        {
            gadgetName = gadgetNode.getAttributes().getNamedItem("name").getNodeValue();
        } else
        {
            gadgetName = gadgetNode.getNodeName() + indexForGadget++; // Takes default node name if name is not configured
        }
        if (gadgetNode.getChildNodes().getLength() > 0)
        {
            for (int i = 0; i < gadgetNode.getChildNodes().getLength(); i++)
            {
                String nodeName = gadgetNode.getChildNodes().item(i).getNodeName();
                if (nodeName.equals("x"))
                {
                    x = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("y"))
                {
                    y = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("width"))
                {
                    width = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("height"))
                {
                    height = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent());
                }
                if (nodeName.equals("image"))
                {
                    String fullPath = gadgetNode.getChildNodes().item(i).getTextContent();
                    formattedImagePath = getFormattedPath(fullPath);
//                    System.out.println("fullPath = " + fullPath + " formattedImagePath = " + formattedImagePath);
                }
                if (nodeName.equals("relativeX"))
                {
                    x = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent()) + actionHelpBounds.x;
                }
                if (nodeName.equals("relativeY"))
                {
                    y = Integer.parseInt(gadgetNode.getChildNodes().item(i).getTextContent()) + actionHelpBounds.y;
                }
                if (nodeName.equals("text"))
                {
                    displayText = gadgetNode.getChildNodes().item(i).getTextContent();
                }
            }
            if (gadgetType == GadgetConfig.IMAGE_BOX)
            {
                formattedImagePath = getImageForImageBox(gadgetNode);
            }
            Rectangle bounds = new Rectangle(x, y, width, height);
            if (gadgetType == GadgetConfig.ACTION_HELP)
            {
                actionHelpBounds = bounds;
            }

            gadgetPropertyList.add(new GadgetDisplayElement(gadgetName, bounds, gadgetType, formattedImagePath, displayText));
        }
    }

    /**
     * Returns the path along with its parent directory
     * @param fullPath - a path in format "Folder1/Folder2/<Any level of folders>/xyz/abc/Image.png
     * @return a path of format "abc/Image.png" corresponding to the parameter
     */
    private String getFormattedPath(String fullPath)
    {
        String formattedImagePath = null;
        if (null != fullPath && fullPath.trim().length() != 0 && fullPath.indexOf("/") != -1)
        {
            formattedImagePath = fullPath.substring(fullPath.lastIndexOf("/", fullPath.lastIndexOf("/") - 1));
            // Special case for News. Folder name in xml is News24 and in PC is News.
            if (formattedImagePath.startsWith("/News24"))
            {
                formattedImagePath = formattedImagePath.replaceFirst("News24", "News");
            }
        }
        return formattedImagePath;
    }

    private String getImageForImageBox(Node imageBoxNode)
    {
        String imagePath = null;
        // && (null == imagePath)
        for (int i = 0; i < imageBoxNode.getChildNodes().getLength(); i++)
        {
            // Get data node
            if (imageBoxNode.getChildNodes().item(i).getNodeName().equals("data"))
            {
                Node dataNode = imageBoxNode.getChildNodes().item(i);
                // iterate thru data children
                for (int j = 0; j < dataNode.getChildNodes().getLength(); j++)
                {
                    // Get conditional data node
                    if (dataNode.getChildNodes().item(j).getNodeName().equals("conditionalData"))
                    {
                        Node conditionalDataNode = dataNode.getChildNodes().item(j);
                        // Iterate thru conditional data children (context and group items)
                        for (int k = 0; k < conditionalDataNode.getChildNodes().getLength(); k++)
                        {
                            // Get GroupItem child
                            if (conditionalDataNode.getChildNodes().item(k).getNodeName().equals("groupItem"))
                            {
                                // Get GroupItem image path attribute
                                Node groupItemNode = conditionalDataNode.getChildNodes().item(k);
                                imagePath = groupItemNode.getAttributes().getNamedItem("imagePath").getNodeValue();
                            }
                        }
                    }
                }
            }
        }
        return getFormattedPath(imagePath);
    }
}
