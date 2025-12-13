package org.group_three.model;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * <h1>Sumo Road</h1>
 * <s>Simple</s> class for storing a "road"<br>
 * => A connection between 2 junctions
 * @author Luca
 * */
public class WEdge {
    private String from;
    private String to;
    private String edgeID;
    private String name;
    private List<String> laneIDs;
    private static HashMap<String, WEdge> allroads;

    private WEdge() {};

    public WEdge(String f, String t, String id, String n){
        from = f;
        to = t;
        edgeID = id;
        name = n;
        laneIDs = new ArrayList<>();
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

    public String getName() {
        return name;
    }

    /**
     * Adds a Lane to the laneIDlist
     * @param LID Lane id
     * @return true if successfull, false if the array is null
     * @author Luca
     * */
    public boolean addLane(String LID) {
        if(laneIDs != null) {
            laneIDs.add(LID);
            return true;
        }
        else return false;
    }

    /**
     * @return the laneIDs list
     * @author Luca
     * */
    public List<String> getLaneIDs() {return laneIDs; }

    public static List<WEdge> getAllroads() {
        return new ArrayList<>(allroads.values());
    }

    /**
     * Prints the content of this class
     * @author Luca
     * */
    public void print(){
        System.out.println(name + ": " + edgeID);
        System.out.println("    from: " + from);
        System.out.println("    to: " + to);
        System.out.println("    Lanes:");
        for(String laneid : laneIDs){
            System.out.println("        laneid: " + laneid);
        }

    }

    /**
     * Prints all the static allroads array
     * @author Luca
     * */
    public static void printAll(){
        Debug.print("All Roads:");
        for(WEdge sr : getAllroads()){
            sr.print();
        }
    }

    /**
     * <h2>loadRoads</h2>
     * loads all Edges in a given network file into a WEdge. <br>
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

            //Debug.print("location: " + network.toString());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            //tests if the .net.xml file is zipped into .gz
            if (network.getName().endsWith(".gz")) {
                try (InputStream fis = new FileInputStream(network);
                     InputStream gis = new GZIPInputStream(fis)) {

                    doc = dBuilder.parse(gis);
                }
            } else {
                doc = dBuilder.parse(network);
            }

            doc.getDocumentElement().normalize();

            NodeList edgeList = doc.getElementsByTagName("edge");

            for (int i = 0; i < edgeList.getLength(); i++) {
                Node edgeNode = edgeList.item(i);

                //if the node is an element
                if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element edgeElement = (Element) edgeNode;

                    String id   = edgeElement.getAttribute("id");
                    String _from = edgeElement.getAttribute("from");
                    String _to   = edgeElement.getAttribute("to");
                    String _name = edgeElement.getAttribute("name");

                    if (_from.isEmpty() || _to.isEmpty()) {
                        continue;
                    }

                    WEdge sr = new WEdge(_from, _to, id, _name);

                    NodeList lanelist = edgeElement.getElementsByTagName("lane");

                    for(int j = 0; j < lanelist.getLength(); j++){
                        Node lanenode = lanelist.item(j);

                        if(lanenode.getNodeType() == Node.ELEMENT_NODE){

                            Element laneelement = (Element) lanenode;

                            String _laneid = laneelement.getAttribute("id");

                            sr.addLane(_laneid);

                        }
                    }

                    allroads.put(id, sr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Gives you the corresponding WEdge to a given EdgeID.
     * @param EID the EdgeID as String
     * @return the Road as WEdge or null if none is found.
     * @author Luca
     * */
    public static WEdge getRoad(String EID){
        return allroads.get(EID);
    }


}
