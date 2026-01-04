package org.group_three.model;
import de.tudresden.sumo.cmd.Edge;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.api.SimController;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.logging.Logger;

/**
 * <h1>Sumo Road</h1>
 * <s>Simple</s> class for storing a "road"<br>
 * => A connection between 2 junctions
 * @author Luca
 * */
public class WEdge extends WObject{

    private static final Logger log =
            Logger.getLogger(WEdge.class.getName());

    private final String from;
    private final String to;
    private List<String> laneIDs;
    private final String name;

    private List<Integer> vehDensityPerStep = new ArrayList<>();

    public WEdge(SimController sumcon, String f, String t, String id, String name){
        super(sumcon, id);
        from = f;
        to = t;
        laneIDs = new ArrayList<>();
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getName() { return name; }

    public List<Integer> getVehDensityPerStep() { return vehDensityPerStep; }

    public long getVehDensitySum(){
        return (long) Meth.sumOfList(vehDensityPerStep);
    }

    /**
     * Adds the number of vehicles that were present on this edge within the last step.
     * <p>Used for statistics.</p>
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @author Luca
     * */
    public boolean addVehDensityCount(){
        try {
            vehDensityPerStep.add((int) simcon.jobget(Edge.getLastStepVehicleNumber(id)));
            return true;
        } catch (Exception e){
            log.warning("Getting last step's vehicle number on Edge " + id + " failed.");
            return false;
        }
    }

    /**
     * Calculates the mean of relative load / occupancy ratio of this vehicle via
     * a counter which indicates how many vehicles were present each second, and a
     * counter which counted all vehicles which were active each second.
     * @return mean of relative load / occupancy ratio
     * @author Luca
     * @see WEdge#getVehDensityPerStep()
     * @see org.group_three.service.StatCollector#()
     * */
    public double getOccupancyRatio(){
        return (double) getVehDensitySum() / simcon.getStatcol().getVehicleMaxDenseValue();
    }

    /**
     * Calculates the Length via <code>from</code> and <code>to</code>.
     * @return length as double
     * @author Luca
     * */
    public double getLength(){

        Vector2D f = new Vector2D(simcon.getJunctionPos(from));
        Vector2D t = new Vector2D(simcon.getJunctionPos(to));

        return t.sub(f).length();
    }

    /**
     * Adds a Lane to the laneIDlist
     * @param LID Lane id
     * @return true if successful, false if the array is null
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

    public List<WEdge> getAllroads() {
        return new ArrayList<>(simcon.getAllroads().values());
    }

    /**
     * Prints the content of this class
     * @author Luca
     * */
    public void print(){
        System.out.println(name + ": " + getId());
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
    public void printAll(){
        for(WEdge sr : getAllroads()){
            sr.print();
        }
    }

    /**
     * <h2>loadRoads</h2>
     * loads all Edges in a given network file into a WEdge. <br>
     * This has the contents "from", "to" and "id". This gets stored in
     * the static variable "allroads" (List< SumoRoads >). You may retrieve this
     * via .getAllroads() or .getRoad(EID).
     * @param network The network file. If we are working with a config file, you need to parse it
     *                into utils.PathUtils.getNetfromSConfig(sumocfg) first.
     * @return True of successful, false if not
     * @author Luca
     * */
    public static boolean loadRoads(SimController simcon, File network){
        try {

            //Debug.print("location: " + network.toString());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc;

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

                if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element edgeElement = (Element) edgeNode;

                    String id   = edgeElement.getAttribute("id");
                    String _from = edgeElement.getAttribute("from");
                    String _to   = edgeElement.getAttribute("to");
                    String _name = edgeElement.getAttribute("name");

                    if (_from.isEmpty() || _to.isEmpty()) {
                        continue;
                    }

                    WEdge sr = new WEdge(simcon, _from, _to, id, _name);

                    NodeList lanelist = edgeElement.getElementsByTagName("lane");

                    for(int j = 0; j < lanelist.getLength(); j++){
                        Node lanenode = lanelist.item(j);

                        if(lanenode.getNodeType() == Node.ELEMENT_NODE){

                            Element laneelement = (Element) lanenode;

                            String _laneid = laneelement.getAttribute("id");

                            sr.addLane(_laneid);

                        }
                    }

                    simcon.addToAllroads(id, sr);
                }
            }

        } catch (Exception e) {
            log.severe("Loading all WEdges failed: " + Arrays.toString(e.getStackTrace()));
            return false;
        }

        log.fine("Loading all WEdges successful");
        return true;
    }

    /**
     * Gives you the corresponding WEdge to a given EdgeID.
     * @param EID the EdgeID as String
     * @return the Road as WEdge or null if none is found.
     * @author Luca
     * */
    public  WEdge getRoad(String EID){
        return simcon.getAllroads().get(EID);
    }


    /** Static method to calculate the length of a route.
     * @param edges A list containing all WEdges of the Route.
     * @return the sum of all lengths from each edge
     * @author Luca
     * */
    public static double  getRouteLength(List<WEdge> edges){

        return edges.stream().mapToDouble(
                WEdge::getLength
        ).sum();
    }

    /** Static method to calculate the length of a route.
     * @param edges A SumoStringList containing all EdgeIDs
     * @return the sum of all lengths from each edge
     * @author Luca
     * */
    public static double  getRouteLength(SumoStringList edges){

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            log.warning("SimController is null.");
            return -1.0d;
        }

        HashMap<String, WEdge> edgehash = simcon.getAllroads();

        double out = 0.0d;

        for(String EID : edges){
            WEdge wedge = edgehash.get(EID);

            if(wedge == null) continue;

            out += wedge.getLength();

        }

        return out;
    }




}
