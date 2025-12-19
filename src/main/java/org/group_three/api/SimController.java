package org.group_three.api;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.objects.*;
import de.tudresden.sumo.util.SumoCommand;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.model.WEdge;
import org.group_three.model.WPolygon;
import org.group_three.model.WTrafficLight;
import org.group_three.model.WVehicle;
import org.group_three.utils.Formatting;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

/**
 * <h1>SimController</h1>
 * Class used to Connect & Control to SUMO via the TraaS API <br>
 * The constructor does everything that is connection-based for you,
 * you only need to simcon.close() the simulation after you are done. <br><br>
 * This class is also host to the all[...]s Lists / Hashmaps, which you
 * want to look into if you want to understand the inner workings of the
 * wrapper classes for the Objects contained by the simulation.
 * @see org.group_three.model
 * @author Luca
 */
public class SimController {

    private static final Logger log =
            Logger.getLogger(SimController.class.getName());

	//The connection to the Sumo simulation. Invoked in the constructor and destroyed with .close()
    private SumoTraciConnection stc; //
    private static final String networkfname = "net.net.xml";
    private static final String routefname = "net.rou.xml";

    // Easy mode
    private static SimController mainsimcon = null;

    // WObjects Collectors
    private List<WTrafficLight> allWTLs = new ArrayList<>();
    private List<WPolygon> allPolys = new ArrayList<>();
    private HashMap<String, WEdge> allroads = new HashMap<>();

// ******************************************************
//                      SIMULATION
// ******************************************************

    public SimController(){
        this(networkfname, routefname);
    }

    public SimController(String cfg) {
        log.info("SimController invoked");

        try {

            // get folder location of Project
            File jarDir = getProjectLocation();

            // opens said folder
            File resourcesDir = new File(jarDir, "SumoConfig");


            // Try SUMO_HOME/bin/sumo.exe, otherwise use resourcesDir/sumo.exe
            String sumoHome = System.getenv("SUMO_HOME");

            // possible location of sumo.exe in %SUMO_HOME%/bin
            File sumoExe = decideSumo(sumoHome, resourcesDir);

            log.info("sumo.exe found: " + sumoExe.getAbsolutePath());

            // opens the sumocfg file inside the resources Folder
            File sumocfg = new File(resourcesDir, cfg);
            if (!sumocfg.exists()) {
                throw new FileNotFoundException(cfg + " not found");
            }
            else {
                log.info("sumocfg file found at " + sumocfg.getAbsolutePath());
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
            log.severe("Error in SimController init: " + Arrays.toString(e.getStackTrace()));
        }

        log.info("SimController startup was successfull");
        Debug.toConsole("SimController startup was successfull");
    }

    /**
     * decides where the sumo.exe is.
     * @param sumoHome The location of env %SUMO_HOME%
     * @param resourcesDir The location of SumoConfig
     * @return Which one was decided upon. sumoHome most of the time
     * @author Luca
     * */
    private static File decideSumo(String sumoHome, File resourcesDir) throws FileNotFoundException {
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
            throw new FileNotFoundException("sumo.exe not found");
        }
        return sumoExe;
    }

    public SimController(String net, String rou){
        log.info("SimController invoked");

        //try & catch to catch exceptions
        try {
            // get folder location of Project
            File jarDir = getProjectLocation();

            // opens said folder
            File resourcesDir = new File(jarDir, "SumoConfig");


            // Try SUMO_HOME/bin/sumo.exe, otherwise use resourcesDir/sumo.exe
            String sumoHome = System.getenv("SUMO_HOME");

            // possible location of sumo.exe in %SUMO_HOME%/bin
            //A ? B : C <=> if A then B else C
            File sumoExe = decideSumo(sumoHome, resourcesDir);

            log.info("sumo.exe found: " + sumoExe.getAbsolutePath());

            // opens the network file inside the resources Folder
            File networknet = new File(resourcesDir, net);

            if (!networknet.exists()) {
                throw new Exception(net + " not found");
            }
            else {
                log.info("network file found at: " + networknet.getAbsolutePath());
            }
            // opens the route file inside the resources Folder
            File routenet = new File(resourcesDir, rou);

            if (!routenet.exists()) {
                throw new Exception(rou + " not found");
            }
            else {
                log.info("route file found at: " + routenet.getAbsolutePath());
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
            log.severe("Error in SimController init: " + Arrays.toString(e.getStackTrace()));
        }

        log.info("SimController startup was successfull");
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
            log.severe("Step execution failed: " + Arrays.toString(e.getStackTrace()));
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
                log.severe("CRITICAL ERROR: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("execution failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("closing failed: " + Arrays.toString(ise.getStackTrace()));
        }
    }

    /**Saves the State as a file. The format and output depends on the extension.
     * <p> WARNING Creates Files </p>
     * @param filetype the extension. Include the dot: saveState(".xml")
     *                 ".csv" and ".xml" are the only ones we found to make sense.
     * @return The filename, or null if without success.
     * @author Luca
     * */
    @MayReturnNull
    public String saveState(String filetype){

        if(!Files.exists(Path.of("output"))){
            log.warning("There was not \"output\" directory found. Are you running from a .jar? Make one.");
            log.warning("saveState aborted");
            return null;
        }

        try {
            String filename = Formatting.uniquegen("savedState_", filetype);
            stc.do_job_set(Simulation.saveState("output/" + filename));
            log.info("State successfully saved to output/" + filename);
            return filename;
        }
        catch (Exception e){
            log.warning("There was an error saving the State: " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    /**
     * @author Luca
     * @return the location(FILE) the Project is located in, or null if failed.
     * Knows if the Program is compiled & executed or a jar file
    **/
    @MayReturnNull
    public static File getProjectLocation() {

        //the location of this class in the URL format
        URL mainURL = SimController.class.getProtectionDomain().getCodeSource().getLocation();
        URI mainURI;

        //"cast" to URI format
        try {
            mainURI = mainURL.toURI();
        } catch (Exception e) {
            log.severe("Converting URL to URI failed; \"" + mainURL.toString() + "\" contains URI invalid characters.");
            return null;
        }
        //checks if the last "cast" was successfull.


        //transforms the URI to a FILE
        File jarDir = new File(mainURI);

        //if compiled to a Jar
        if(jarDir.isFile()){
            log.info("Jar Execution detected");
            return jarDir.getParentFile();
        }

        //if compiled normally
        log.info("Compiled Execution detected");
        return jarDir.getParentFile().getParentFile().getParentFile();

    }

    /**
     * @return step-time as int, or <code>-1</code> if failed
     * @author Luca
     * */
    public int getTime(){
        try {
			double time = (double) stc.do_job_get(Simulation.getTime());

            return Math.toIntExact(Math.round(time));

        } catch (Exception e) {
            log.severe("getting Time failed: " + Arrays.toString(e.getStackTrace()));
            return -1;
        }
    }

    /**
     * Returns the global / static mainsimcon.
     * @author Luca
     * */
    @MayReturnNull
    public static SimController getMainsimcon() {

        if(mainsimcon == null || mainsimcon.getStc().isClosed()) {

            log.warning("Mainsimcon is closed or null.");

            return null;
        }

        return mainsimcon;

    }

    /**
     * Sets this Simulation as the new, global, main simulation. <br>
     * Beware that this overwrites the old one
     * @author Luca
     * */
    public void setAsMainsimcon(boolean close_old){

        if(mainsimcon != null && !mainsimcon.getStc().isClosed() && close_old){
            mainsimcon.close();
        }

        mainsimcon = this;

        log.info("Main SUMO Simulation was overwritten.");
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }


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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }


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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }


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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }


    }


    // ******************************************************
    // **                   Edges & Lanes                  **
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

    }

    // ******************************************************
    // **                 Vehicle  & Route                 **
    // ******************************************************

    /**
     * <h2>addVehicle</h2>
     * Basically the normal Vehicle.add(), but it handles the VehicleID setting logic for you.
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }



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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }


    /**
     * @return Average vehicle-speed from all loaded Vehicles, or -1.0d if failed.
     * @author Luca
     * */
    public double getAverageVehSpeed(){

        SumoStringList allVIDs = getVehicleIDList();

        if(allVIDs == null) {
            log.severe("getAverageVehSpeed failed. VehicleIDList is null.");
            return -1.0d;
        }

        double speedcount = 0;
        double vehcount = 0;


        for(String VID : allVIDs){
            speedcount += (double) jobget(Vehicle.getSpeed(VID));
            vehcount++;
        }

        return speedcount / vehcount;
    }

    // ******************************************************
    // **               Traffic Lights                     **
    // ******************************************************

    //Old Text of mine when I thought we don't need a TL Wrapper-Class:
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

    }

    /**This function supposedly returns it in meters but since one meter
     * equals one coordinate unit, they can just be used as the width in coordinates
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

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
            log.fine("TrafficLight " + TLID + ": Phase changed to index " + iPhase);
            stc.do_job_set(Trafficlight.setPhase(TLID, iPhase));
            return true;
        }
        catch (Exception e){
            log.severe("setter failed: " + Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    /**
     * Returns the Traffic Light phase as Index [0:2] <br>
     * @param TLID ID of the Traffic Light
     * @return Phase of the TL, <code>-1</code> if getter failed.
     * @author Luca
     */
    public int getTLPhase(String TLID) {
        try {
            return (int) stc.do_job_get(Trafficlight.getPhase(TLID));
        }
        catch (Exception e){
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return -1;
        }
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
            return true;
        } catch (Exception e) {
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return false;
        }


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

        try {
            return  (String) stc.do_job_get(Trafficlight.getParameter(TLID,param));
        }
        catch (Exception e){
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

    }


    /**
     * Sets the given Parameter of a Trafficlight
     * @param TLID TrafficLight ID
     * @param param the parameter which is supposed to change
     * @param value the value that is to be inserted in the given parameter
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @author Luca
     * */
    public boolean setTLParam(String TLID, String param, String value){
        try {
            stc.do_job_set(Trafficlight.setParameter(TLID, param, value));
            return true;
        }
        catch (Exception e){
            log.severe("setter failed: " + Arrays.toString(e.getStackTrace()));
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
    public SumoStringList getJunctionIDList() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (SumoStringList) stc.do_job_get(Junction.getIDList());
        }
        catch (Exception e) {
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }

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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
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
            log.severe("getter failed: " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }




    // ******************************************************
    // **          General Getters and Setters             **
    // ******************************************************



    public List<WPolygon> getAllPolys() {
        return allPolys;
    }

    public HashMap<String, WEdge> getAllroads() {
        return allroads;
    }

    public List<WTrafficLight> getAllWTLs() {
            return allWTLs;
    }

    /**
     * @return the Road / WEdge of allRoads via the given Edge ID
     * @param EID  EdgeID
     * @author Luca
     * */
    @MayReturnNull
    public WEdge getRoad(String EID) {
        return getAllroads().get(EID);
    }

    public void setAllWTLs(List<WTrafficLight> allWTLs) {
        this.allWTLs = allWTLs;
    }

    public void setAllPolys(List<WPolygon> allPolys) {
        this.allPolys = allPolys;
    }

    public void setAllroads(HashMap<String, WEdge> allroads) {
        this.allroads = allroads;
    }


    /**
     * Adds a WTrafficLight to the WTL Collection of this SimController.
     * @param wtl The WTrafficLight to be added
     * @return the new size of the collector List
     * @author Luca
     * */
    public int addToAllWTLs(WTrafficLight wtl){
        allWTLs.add(wtl);
        return allWTLs.size();
    }

    /**
     * Adds a WPolygon to the WPoly Collection of this SimController.
     * @param poly The WPolygon to be added
     * @return the new size of the collector List
     * @author Luca
     * */
    public int addToAllPolys(WPolygon poly){
        allPolys.add(poly);
        return allPolys.size();
    }

    /**
     * Adds a WEdge to the WEdge Collection of this SimController.
     * @param id The id of the Edge. Used for hashing
     * @param edge The WEdge to be added.
     * @return the new size of the collector List
     * @author Luca
     * */
    public int addToAllroads(String id, WEdge edge){
        allroads.put(id, edge);
        return allroads.size();
    }



    /**
     * Getter of the Simulation
     * If you want to print the returned object, .to_String() is probably the best option.
     * @param scmd The SumoCommand to be executed upon the Simulation.
     * @example
     * <code>
     * simcon.jobget(Vehicle.getIDList())
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
            log.severe("GET JOB FAILED: " + scmd.toString() + "\n"
            + Arrays.toString(e.getStackTrace()));
            return null;

        }
    }

    /**
     * Setter of the Simulation
     * @param scmd The SumoCommand to be executed upon the Simulation.
     * @return <code>true</code> if successfull, <code>false</code> if not.
     * @example
     * <code>
     * simcon.jobset(Vehicle.add(vehID, typeID, routeID, depart, speed, lane))
     * </code>
     * */
    public boolean jobset(SumoCommand scmd){
        try {
            stc.do_job_set(scmd);
            return true;
        }
        catch (Exception e){
            log.severe("SET JOB FAILED: " + scmd.toString() + "\n"
                    + Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
}