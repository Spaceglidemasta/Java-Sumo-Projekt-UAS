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
import java.util.ArrayList;
import java.util.HashMap;
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
    private static HashMap<String, SumoRoad> allroads;

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

    public static List<SumoRoad> getAllroads() {
        return new ArrayList<>(allroads.values());
    }

    /**
     * Prints the content of this class
     * @author Luca
     * */
    public void print(){
        System.out.println("RoadID: " + edgeID);
        System.out.println("    from: " + from);
        System.out.println("    to: " + to);
    }

    /**
     * Prints all the static allroads array
     * @author Luca
     * */
    public static void printAll(){
        for(SumoRoad sr : getAllroads()){
            sr.print();
        }
    }

    /**
     * loads all Edges in a given network file into a SumoRoad. <br>
     * This has the contents "from", "to" and "edgeID". This gets stored in
     * the static variable "allroads" (List< SumoRoads >). You may retrieve this
     * via .getAllroads() or .getRoad(EID).
     * @param network The network file. If we are working with a config file, you need to parse it
     *                into utils.PathUtils.getNetfromSConfig(sumocfg) first.
     * @return True of successfull, false if not
     * @author Luca
     * */
    public static boolean loadRoads(File network){
        try {

            allroads = new HashMap<>();

            File sl = SimController.getSumoLoc();

            //Debug.print("location: " + network.toString());

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
                        allroads.put(id, new SumoRoad(_from, _to, id));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Gives you the corresponding SumoRoad to a given EdgeID.
     * @param EID the EdgeID as String
     * @return the Road as SumoRoad or null if none is found.
     * @author Luca
     * */
    public static SumoRoad getRoad(String EID){
        return allroads.get(EID);
    }


}
