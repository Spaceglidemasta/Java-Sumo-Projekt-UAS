package org.group_three.api;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.*;
import de.tudresden.sumo.subscription.SubscribtionVariable;
import de.tudresden.sumo.util.SumoCommand;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;
import org.group_three.model.WVehicle;
import org.group_three.utils.Sumo2DVector;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>SimController</h1>
 * Class used to Connect & Control to SUMO via the TraaS API <br>
 * The constructor does everything for you, you only need
 * to SimController.close() the Simulation after you are done.
 * @author Luca
 */
public class SimController {

	//The connection to the Sumo simulation. Invoked in the constructor and destroyed with .close()
    private SumoTraciConnection stc; //
    private static final String networkfname = "net.net.xml";
    private static final String routefname = "net.rou.xml";

    // Easy mode
    private static SimController mainstc = null;


/// ******************************************************
/// **                   SIMULATION                     **
/// ******************************************************

    public SimController(){
        this(networkfname, routefname);
    }

    public SimController(String cfg) {
        Debug.print("SimController invoked");

        try {

            // get folder location of Project
            File jarDir = getSumoLoc();

            // opens said folder
            File resourcesDir = new File(jarDir, "SumoConfig");


            // Try SUMO_HOME/bin/sumo.exe, otherwise use resourcesDir/sumo.exe
            String sumoHome = System.getenv("SUMO_HOME");

            // possible location of sumo.exe in %SUMO_HOME%/bin
            File sumoExe = decideSumo(sumoHome, resourcesDir);

            Debug.print("sumo.exe found: " + sumoExe.getAbsolutePath());

            // opens the sumocfg file inside the resources Folder
            File sumocfg = new File(resourcesDir, cfg);
            Debug.print(sumocfg.getAbsolutePath());
            if (!sumocfg.exists()) {
                throw new Exception(cfg + " not found");
            }
            else {
                Debug.print("sumocfg file found");
            }

            // establishes the connection to sumo with the route and network via TraaS
            stc = new SumoTraciConnection(
                    sumoExe.getAbsolutePath(),
                    sumocfg.getAbsolutePath()
            );

            // options that set sumo to print outputs & errors.
            stc.printSumoOutput(true);
            stc.printSumoError(true);
            stc.runServer(8813);

            // THIS SINGULAR TIMESTEP IS NECESSARY TO LOAD ALL VEHICLES; DO NOT REMOVE
            stc.do_timestep();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.print("SimController startup was successfull");
        Debug.toConsole("SimController startup was successfull");
    }

    /**
     * decides where the sumo.exe is.
     * @param sumoHome The location of env %SUMO_HOME%
     * @param resourcesDir The location of SumoConfig
     * @return Which one was decided upon. sumoHome most of the time
     * @author Luca
     * */
    private static File decideSumo(String sumoHome, File resourcesDir) throws Exception {
        //A ? B : C <=> if A then B else C
        File sumoExeHome = (sumoHome != null)
                ? new File(sumoHome + "/bin/sumo.exe")
                : null;

        // possible location of sumo.exe in resources
        File sumoExeResources = new File(resourcesDir, "sumo.exe");

        // Decide final path
        File sumoExe = (sumoExeHome != null && sumoExeHome.exists())
                ? sumoExeHome
                : sumoExeResources;

        if (!sumoExe.exists()) {
            throw new Exception("sumo.exe not found");
        }
        return sumoExe;
    }

    public SimController(String net, String rou){
        Debug.print("SimController invoked");

        //try & catch to catch exceptions
        try {
            // get folder location of Project
            File jarDir = getSumoLoc();

            // opens said folder
            File resourcesDir = new File(jarDir, "SumoConfig");


            // Try SUMO_HOME/bin/sumo.exe, otherwise use resourcesDir/sumo.exe
            String sumoHome = System.getenv("SUMO_HOME");

            // possible location of sumo.exe in %SUMO_HOME%/bin
            //A ? B : C <=> if A then B else C
            File sumoExe = decideSumo(sumoHome, resourcesDir);

            Debug.print("sumo.exe found: " + sumoExe.getAbsolutePath());

            // opens the network file inside the resources Folder
            File networknet = new File(resourcesDir, net);
            Debug.print(networknet.getAbsolutePath());
            if (!networknet.exists()) {
                throw new Exception(net + " not found");
            }
            else {
                Debug.print("network file found");
            }
            // opens the route file inside the resources Folder
            File routenet = new File(resourcesDir, rou);
            Debug.print(routenet.getAbsolutePath());
            if (!routenet.exists()) {
                throw new Exception(rou + " not found");
            }
            else {
                Debug.print("route file found");
            }

            // establishes the connection to sumo with the route and network via TraaS
            stc = new SumoTraciConnection(
                    sumoExe.getAbsolutePath(),
                    networknet.getAbsolutePath(),
                    routenet.getAbsolutePath()
            );

            // options that set sumo to print outputs & errors.
            stc.printSumoOutput(true);
            stc.printSumoError(true);
            stc.runServer(8813);

            // THIS SINGULAR TIMESTEP IS NECESSARY TO LOAD ALL VEHICLES; DO NOT REMOVE
            stc.do_timestep();


        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.print("SimController startup was successfull");
        Debug.toConsole("SimController startup was successfull");

    }

    /**
     * Does 1 step in the simulation.
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @author Luca
     * */
    public boolean step(){

        try {
            stc.do_timestep();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Does n steps in the simulation.
     * @param n The number of steps to be done
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @author Luca
     * */
    public boolean step(int n){

        for(int i = 0; i < n; i++){
            try {
                stc.do_timestep();
            } catch (Exception e) {
                Debug.print("CRITICAL ERROR:");
                e.printStackTrace();
                return false;
            }

        }

        return true;
    }


    /**
     * Sets the Time of the Simulation to (time), needs to be in the Future.
     * @param time The time in the future to travel to.
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @author Luca
     * */
    public boolean timetravel_to(double time){

        try {
            stc.do_timestep(time);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



    /**
     * closes the SumoTraciConnection
     * @author Luca
     * */
    public void close(){

        try {
            // this can actually throw an ISE even tho it's not indicated.
            stc.close();
        }
        catch (IllegalStateException ise){
            ise.printStackTrace();
        }


    }

    /**
     * @author Luca
     * @return the location(FILE) the Project is located in.
     * Knows if the Program is compiled & executed or a jar file
    **/
    public static File getSumoLoc() {

        //the location of this class in the URL format
        URL mainURL = SimController.class.getProtectionDomain().getCodeSource().getLocation();
        URI mainURI = null;

        //"cast" to URI format
        try {
            mainURI = mainURL.toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //checks if the last "cast" was successfull.
        assert mainURI != null;

        //transforms the URI to a FILE
        File jarDir = new File(mainURI);

        //if compiled to a Jar
        if(jarDir.isFile()){
            Debug.print("Jar Execution detected");
            return jarDir.getParentFile();
        }

        //if compiled normally
        Debug.print("Normal Execution detected");
        return jarDir.getParentFile().getParentFile().getParentFile();

    }

    /**
     * Returns the global / static _mainsim.
     * @author Luca
     * */
    public static SimController getMainstc() {return mainstc;}

    /**
     * Sets this Simulation as the new, global, main simulation. <br>
     * Beware that this overwrites the old one
     * @author Luca
     * */
    public void setMainstc(boolean close_old){

        if(mainstc != null && close_old){
            mainstc.close();
        }

        mainstc = this;

        Debug.print("Main SUMO Simulation was overwritten.");
    }

    // ******************************************************
    // **                   Getters                        **
    // ******************************************************

    /**
     * Getter for the SumoTraciConnection
     * @author Luca
     * */
    public SumoTraciConnection getStc() {
        return stc;
    }


    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    public SumoStringList getTrafficLightsIDList(){
        try {
            return (SumoStringList) stc.do_job_get(Trafficlight.getIDList());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //Funny no template Language incoming

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    public SumoStringList getVehicleIDList() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (SumoStringList) stc.do_job_get(Vehicle.getIDList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    public SumoStringList getRouteIDList() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (SumoStringList) stc.do_job_get(Route.getIDList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    public SumoStringList getEdgeIDList() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (SumoStringList) stc.do_job_get(Edge.getIDList());

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     * Function to return all Lane IDs
     * @return IDs of all Lanes in a network
     * @author Leon
     */
    public SumoStringList getLaneIDList() {
        try {
            return (SumoStringList) stc.do_job_get(Lane.getIDList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //



    /**
     * Function to return coordinates from the start of the lane to the end, including
     * the small bevels that make the road slightly curve
     * @param laneID The ID of the lane
     * @return coordinates from start to end
     * @author Leon
     */
    public String getLaneEdgeParam(String laneID) {

        try {
            SumoGeometry shape = (SumoGeometry) stc.do_job_get(Lane.getShape(laneID));
            if (shape != null){
                return shape.toString();
            }
            else {
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @param edgeID The String of the EdgeID you want the number of lanes from.
     * @author Luca
     * */
    public SumoStringList getLaneNumOfEdge(String edgeID) {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (SumoStringList) stc.do_job_get(Edge.getLaneNumber(edgeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ******************************************************
    // **                   Vehicle                        **
    // ******************************************************

    /**
     * Basicly the normal Vehicle.add(), but it handles the VehicleID setting logic for you.
     *
     * @param typeID The type of the Vehicle; Use constants.Vehicle for this.
     * @param routeID The ID of the route where the Vehicle is supposed to land
     * @param depart departing delay in ms
     * @param pos Position in m from the start of the Route
     * @param speed Speed? Not clear from declaration
     * @param lane departing lane. Use .getLaneNum(edgeID) for lane number.
     * @author Luca
     * */
    public WVehicle addVehicle(String typeID, String routeID, int depart, double pos, double speed, int lane){

        try {
            int newVID = (int) stc.do_job_get(Vehicle.getIDCount()) - 1;
            do {
                newVID = newVID + 1;
            }
            while(getVehicleIDList().contains("wveh_" + newVID));

            String newVIDstr = "wveh_" + newVID;

            stc.do_job_set(Vehicle.add(newVIDstr, typeID, routeID, depart, pos, speed, (byte)lane));
            return new WVehicle(newVIDstr, stc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    // ******************************************************
    // **               Traffic Lights                     **
    // ******************************************************
    /*"Why is this not its own Wrapper-Class like WVehicle?"
     * There simply aren't enough functions & attributes to a TL
     * that we need, that it's worth to build its own class.
     * It would also be too much of a hassle to work with an own
     * Wrapper-class, that needs to be connected to the Simulation
     * via an Attribute.
     *
     * Guide on how to use this:
     * TRAFFIC LIGHTS DO NOT HAVE POSITIONS!
     * You get their location via their Junctions (.getTLJunctions(TLID))
     * Logically there are no real Trafficlight-Poles inside the simulation,
     * there are only TL Logics Applied to each Junction, which is why they are
     * not bound to a 2D position.
     * Any Parameters that are not Listed in these methods can be get and set
     * via .getTLParam() and .setTLParam()
     * */

    /**
     * @return All junctionID's connected to the TL.
     * @author Luca
     * */
    public List<String> getTLJunctions(String TLID){
        try {
            return (SumoStringList) stc.do_job_get(Trafficlight.getControlledJunctions(TLID));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return All LaneID's controlled by the TL.
     * @author Leon
     * */
    public SumoStringList getControlledLanes(String TLID) {
        try {
            return (SumoStringList) stc.do_job_get(Trafficlight.getControlledLanes(TLID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return All Link's controlled by the TL.
     * (Weird format "E1_0#:clusterJ4_J5_0_0#E0.16_0")
     * @author Leon
     * */
    public SumoLinkList getControlledLinks(String linkID) {
        try {
            return (SumoLinkList) stc.do_job_get(Trafficlight.getControlledLinks(linkID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**@param laneID ID of the chosen lane with format:("E1_0")
     * @return Returns the width of a chosen lane.
     * (It supposedly prints it in meters but since one meter
     * equals one coordinate unit, they can just be used
     * as the width in coords)
     * @author Leon
     * */
    public Double getLaneWidth(String laneID) {
        try {
            return (Double) stc.do_job_get(Lane.getWidth(laneID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**This function can return <code>null</code> <br>
     * This function is used in tandem with the getStopLineCoords function.
     * This function passes the second to last and the last coordinate
     * of all Lanes that are controlled by the chosen TL and then returns
     * the stop line coordinates.
     * @param TLID ID of the chosen TL with format:("clusterJ4_J5")
     * @return returns the data as Sumo2DVector
     * @author Leon
     * */
    public List<Sumo2DVector> getStopLineVector(String TLID){
        List<SumoPosition2D> stopLinePositions = new ArrayList<>();
        try {
            SumoStringList linkedLanes = getControlledLanes(TLID);

            for(String laneID: linkedLanes){

                String edgeShape = getLaneEdgeParam(laneID);
                String[] coordinates = edgeShape.split(" ");

                String secondToLastCoordinate = coordinates[coordinates.length - 2];
                String lastCoordinate = coordinates[coordinates.length - 1];

                String[] secondToLastCoord = secondToLastCoordinate.split(",");
                String[] lastCoord = lastCoordinate.split(",");

                double x1 = Double.parseDouble(secondToLastCoord[0]);
                double y1 = Double.parseDouble(secondToLastCoord[1]);
                double x2 = Double.parseDouble(lastCoord[0]);
                double y2 = Double.parseDouble(lastCoord[1]);

                stopLinePositions.add(new SumoPosition2D(x1, y1));
                stopLinePositions.add(new SumoPosition2D(x2, y2));


            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return stopLineCalculation(stopLinePositions);
    }

    /**This is a function that calculates the two positions of the Stop line
     * (Start and end) using the two positions from the previous function.
     * It first calculates the direction vector and makes it perpendicular to the
     * direction that the lane is.
     * It uses the last position of the lane as a middle and calculates two points, 1.6
     * units in each direction, since the standard width is 3.2.
     * @param stopLinePositions Takes in the List that was made with getStopLinePos()
     * @return returns the data as Sumo2DVector.
     * @author Leon
     * */
    public List<Sumo2DVector> stopLineCalculation(List<SumoPosition2D> stopLinePositions) {
        List<Sumo2DVector> perpendicularPoints = new ArrayList<>();
        final double OFFSET = 1.6;  // Half the lane

        // Iterate (second-to-last, last) for each lane
        for (int i = 0; i < stopLinePositions.size(); i += 2) {
            if (i + 1 >= stopLinePositions.size()) break; // safety check

            SumoPosition2D point1 = stopLinePositions.get(i);
            SumoPosition2D point2 = stopLinePositions.get(i + 1);

            // Direction vector of the street
            double dx = point2.x - point1.x;
            double dy = point2.y - point1.y;

            // Normalize
            double magnitude = Math.sqrt(dx * dx + dy * dy);
            if (magnitude == 0) continue; // avoid division by zero

            // Perpendicular vector
            double perpX = dy / magnitude;
            double perpY = -dx / magnitude;

            // Scale by offset
            perpX *= OFFSET;
            perpY *= OFFSET;

            // Use the LAST coordinate (point2) as center
            double xLeft = point2.x - perpX;
            double yLeft = point2.y - perpY;
            double xRight = point2.x + perpX;
            double yRight = point2.y + perpY;

            //Add both points
            SumoPosition2D leftPoint = new SumoPosition2D(xLeft, yLeft);
            SumoPosition2D rightPoint = new SumoPosition2D(xRight, yRight);

            // Create a line segment from left to right
            Sumo2DVector stopLine = new Sumo2DVector(leftPoint, rightPoint);
            perpendicularPoints.add(stopLine);

            //Debug.print("Stop line: " + stopLine);

        }

        return perpendicularPoints;
    }

    /**
     * Sets the Phase Index [0:2] of the TL. <br>
     * @param TLID Traffic Light ID
     * @param iPhase Phase index as int, starting at 0 (?)
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @author Luca
     * */
    public boolean setTLPhase(String TLID, int iPhase){

        try {
            Debug.toConsole("TrafficLight " + TLID + ": Phase changed to index " + iPhase);
            stc.do_job_set(Trafficlight.setPhase(TLID, iPhase));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns the Traffic Light phase as Index [0:2] <br>
     * @param TLID ID of the Traffic Light
     * @return Phase of the TL, <code>-1</code> if getter failed.
     * @author Luca
     */
    public int getTLPhase(String TLID) {
        int iphase;
        try {
            iphase = (int) stc.do_job_get(Trafficlight.getPhase(TLID));
        }
        catch (Exception e){
            e.printStackTrace();
            iphase = -1;
        }
        return iphase;
    }

    /**
     * Sets the Length of the current Phase of the given TL.
     * @param TLID Traffic Light ID
     * @param dur new Duration in seconds(?)( <- This not my questionmark, the TraaS doc also has a "?")
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * */
    public boolean setTLPhaseLen(String TLID, double dur){

        try {
            stc.do_job_set(Trafficlight.setPhaseDuration(TLID, dur));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Returns the requested Parameter of a Trafficlight
     * @param TLID TrafficLight ID
     * @param param The requested parameter
     * @return <code>null</code> if failed, else the requested parameter
     * @author Luca
     * */
    public String getTLParam(String TLID, String param){
        String retparam;
        try {
            retparam = (String) stc.do_job_get(Trafficlight.getParameter(TLID,param));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return retparam;
    }


    /**
     * Sets the given Parameter of a Trafficlight
     * @param TLID TrafficLight ID
     * @param param the parameter which is supposed to change
     * @param value the value that is to be inserted in the given parameter
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * */
    public boolean setTLParam(String TLID, String param, String value){
        try {
            stc.do_job_set(Trafficlight.setParameter(TLID, param, value));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    // ******************************************************
    // **                      Junctions                   **
    // ******************************************************

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    public List<String> getJunctionIDList() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (List<String>) stc.do_job_get(Junction.getIDList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Gets you the SumoPosition2D of the Junction. Attributes are public.<br>
     * @example <code>
     * double x = j.getPos().x <br>
     * double y = j.getPos().y = y
     * </code>
     * @return SumoPosition2D, or <code>null</code> if failed.
     * @author Luca
     * */
    public SumoPosition2D getJunctionPos(String juncID){

        try {
            return (SumoPosition2D) stc.do_job_get(Junction.getPosition(juncID));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets you the String of the corner points (of the polygon) of the junction.<br>
     * @return String, or <code>null</code> if failed.
     * @author Leon
     * */
    public String getJunctionShape(String laneID) {

        try {
            SumoGeometry shape = (SumoGeometry) stc.do_job_get(Junction.getShape(laneID));
            if (shape != null){
                return shape.toString();
            }
            else {
                return  null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    // ******************************************************
    // **          General Getters and Setters             **
    // ******************************************************


    /**
     * Getter of the Simulation
     * If you want to print the returned object, .to_String() is probably the best option.
     * @param scmd The SumoCommand to be executed upon the Simulation.
     * @example
     * <code>
     * sumcon.jobget(Vehicle.getIDList())
     * </code>
     * @return "Object" of the .do_job_get() method. Yes, you need to unsafe cast this.
     * <code>null</code> if failed.
     * */
    public Object jobget(SumoCommand scmd){
        try {
            return stc.do_job_get(scmd);
        }
        catch (Exception e){
            Debug.print("GET JOB FAILED: " + scmd.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Setter of the Simulation
     * @param scmd The SumoCommand to be executed upon the Simulation.
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @example
     * <code>
     * sumcon.jobset(Vehicle.add(vehID, typeID, routeID, depart, speed, lane))
     * </code>
     * */
    public boolean jobset(SumoCommand scmd){
        try {
            stc.do_job_set(scmd);
        }
        catch (Exception e){
            Debug.print("DO JOB FAILED: " + scmd.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
