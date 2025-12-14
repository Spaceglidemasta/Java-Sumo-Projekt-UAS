package org.group_three.api;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.*;
import de.tudresden.sumo.util.SumoCommand;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.model.WVehicle;
import org.group_three.utils.Formatting;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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
    private static SimController mainsimcon = null;


// ******************************************************
//                      SIMULATION
// ******************************************************

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

            // THIS SINGULAR TIMESTEP IS NECESSARY TO -LOAD ALL VEHICLES-; DO NOT REMOVE
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
            if(stc != null && !stc.isClosed()) stc.close();
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
        Debug.print("Compiled Execution detected");
        return jarDir.getParentFile().getParentFile().getParentFile();

    }

    /**
     * @return steptime as int, or <code>-1</code> if failed
     * @author Luca
     * */
    public int getTime(){
        try {
            return (int) stc.do_job_get(Simulation.getTime());

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Returns the global / static _mainsim.
     * @author Luca
     * */
    @MayReturnNull
    public static SimController getMainsimcon() {

        if(mainsimcon == null || mainsimcon.getStc().isClosed()) return null;

        return mainsimcon;

    }

    /**
     * Sets this Simulation as the new, global, main simulation. <br>
     * Beware that this overwrites the old one
     * @author Luca
     * */
    public void setMainstc(boolean close_old){

        if(mainsimcon != null && !mainsimcon.getStc().isClosed() && close_old){
            mainsimcon.close();
        }

        mainsimcon = this;

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
    @MayReturnNull
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
    @MayReturnNull
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
    @MayReturnNull
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
    @MayReturnNull
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
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    @MayReturnNull
    public SumoStringList getPolygonIDList() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (SumoStringList) stc.do_job_get(Polygon.getIDList());

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // ******************************************************
    // **                   Edges                          **
    // ******************************************************






    /**
     * Function to return all Lane IDs
     * @return IDs of all Lanes in a network
     * @author Leon
     */
    @MayReturnNull
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
     * @author Leon, Luca
     */
    @MayReturnNull
    public LinkedList<SumoPosition2D> getLaneShape(String laneID) {

        try {
            return ((SumoGeometry) stc.do_job_get(Lane.getShape(laneID))).coords;

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
    @MayReturnNull
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
    // **                 Vehicle  & Route                 **
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
     * @return the instantiated WVehicle
     * @author Luca
     * */
    @MayReturnNull
    public WVehicle addVehicle(String typeID, String routeID, int depart, double pos, double speed, int lane){

        try {
            String newVIDstr = Formatting.uniquegen("wveh_", "");

            stc.do_job_set(Vehicle.add(newVIDstr, typeID, routeID, depart, pos, speed, (byte)lane));
            return new WVehicle(newVIDstr, stc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Adds a Route to the Simulation with a given SumoStringList of edges
     * @param edges The edges of the Route
     * @return The created Route ID, or null if failed.
     * @author Luca
     * */
    @MayReturnNull
    public String addRoute(SumoStringList edges){
        try {
            String RID = Formatting.uniquegen("r_", "");

            stc.do_job_set(Route.add(RID, edges));

            return RID;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all the Edges inside a Route
     * @param RID RouteID
     * @return The edges as a SumoStringList, or null if failed
     * @author Luca
     * */
    @MayReturnNull
    public SumoStringList getRouteEdges(String RID){
        try {
            return (SumoStringList) stc.do_job_get(Route.getEdges(RID));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    @MayReturnNull
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
    @MayReturnNull
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
    @MayReturnNull
    public SumoLinkList getControlledLinks(String linkID) {
        try {
            return (SumoLinkList) stc.do_job_get(Trafficlight.getControlledLinks(linkID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**This function supposedly returns it in meters but since one meter
     * equals one coordinate unit, they can just be used as the width in coordinates)
     * @param laneID ID of the chosen lane with format:("E1_0")
     * @return Returns the width of a chosen lane.
     * @author Leon
     * */
    @MayReturnNull
    public Double getLaneWidth(String laneID) {
        try {
            return (Double) stc.do_job_get(Lane.getWidth(laneID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * @author Luca
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
    @MayReturnNull
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
    @MayReturnNull
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
    @MayReturnNull
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
     * Gets you the coordinates of the corner points of the junction.
     * @return coords, or <code>null</code> if failed.
     * @author Leon
     * */
    @MayReturnNull
    public LinkedList<SumoPosition2D> getJunctionShape(String laneID) {
        try {
            return ((SumoGeometry) stc.do_job_get(Junction.getShape(laneID))).coords;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @return the type of the poly, or null if failed
     * @author Luca
     * */
    @MayReturnNull
    public SumoGeometry getPolyType(String pid){
        try {
            return (SumoGeometry) stc.do_job_get(Polygon.getShape(pid));
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
    @MayReturnNull
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
