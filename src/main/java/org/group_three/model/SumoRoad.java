package org.group_three.model;
import javafx.util.Pair;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

/**
 * Simple class for storing a "road"<br>
 * => A connection between 2 junctions
 * @author Luca
 * */
public class SumoRoad {
    private String from;
    private String to;
    private String edgeID;
    private static List<SumoRoad> allroads;

    private SumoRoad() {};

    public SumoRoad(String f, String t, String id){
        from = f;
        to = t;
        edgeID = id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public static boolean loadRoads(File network){
        try {

            File sl = SimController.getSumoLoc();

            Debug.print("location: " + network.toString());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(network);

            doc.getDocumentElement().normalize();

            NodeList edgeList = doc.getElementsByTagName("edge");

            for (int i = 0; i < edgeList.getLength(); i++) {
                Node edgeNode = edgeList.item(i);

                if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element edgeElement = (Element) edgeNode;

                    String id   = edgeElement.getAttribute("id");
                    String _from = edgeElement.getAttribute("from");
                    String _to   = edgeElement.getAttribute("to");

                    if (!_from.isEmpty() && !_to.isEmpty()) {
                        allroads.add(new SumoRoad(_from, _to, id));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
