package org.group_three.api;

import de.tudresden.sumo.cmd.*;
import de.tudresden.sumo.util.SumoCommand;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;
import org.group_three.model.WVehicle;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class used to Connect & Control to SUMO via the TraaS API <br>
 * The constructor does everything for you, you only need
 * to SimController.close() the Simulation after you are done.
 * @author Luca
 */
public class SimController {
    //The connection to the Sumo simulation. Invoked in the constructor and destroyed with .close()
    private SumoTraciConnection _sumcon;
    private static final String networkfname = "net.net.xml";
    private static final String routefname = "net.rou.xml";


    public SimController(){
        this(networkfname, routefname);
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

            Debug.print("sumo.exe found: " + sumoExe.getAbsolutePath());

            // opens the network file inside the resources Folder
            File networknet = new File(resourcesDir, networkfname);
            Debug.print(networknet.getAbsolutePath());
            if (!networknet.exists()) {
                throw new Exception("net.net.xml not found");
            }
            else {
                Debug.print("network file found");
            }
            // opens the route file inside the resources Folder
            File routenet = new File(resourcesDir, routefname);
            Debug.print(routenet.getAbsolutePath());
            if (!routenet.exists()) {
                throw new Exception("net.rou.xml not found");
            }
            else {
                Debug.print("route file found");
            }

            // establishes the connection to sumo with the route and network via TraaS
            _sumcon = new SumoTraciConnection(
                    sumoExe.getAbsolutePath(),
                    networknet.getAbsolutePath(),
                    routenet.getAbsolutePath()
            );

            // options that set sumo to print outputs & errors.
            _sumcon.printSumoOutput(true);
            _sumcon.printSumoError(true);
            _sumcon.runServer(8813);

            // does 5 steps in the Simulation
            Debug.print("timesteps:");
            for (int i = 0; i < 5; i++) {
                _sumcon.do_timestep();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.print("SimController startup was successfull");
        Debug.toConsole("SimController startup was successfull");

    }

    /**
     * closes the SumoTraciConnection
     * @author Luca
     * */
    public void close(){

        try {
            // this can actually throw an ISE even tho it's not indicated.
            _sumcon.close();
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
    private static File getSumoLoc() {

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

    @Deprecated
    public List<Objects> getTrafficLights(){
        try {
            return (List<Objects>) _sumcon.do_job_get(Trafficlight.getIDList());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Objects>();
    }

    //Funny no template Language incoming

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @author Luca
     * */
    public List<String> getallVehicles() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (List<String>) _sumcon.do_job_get(Vehicle.getIDList());
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
    public List<String> getallRoutes() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (List<String>) _sumcon.do_job_get(Route.getIDList());
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
    public List<String> getallEdges() {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (List<String>) _sumcon.do_job_get(Edge.getIDList());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Beware that this uses unchecked casting and CAN return null.
     * @param edgeID The String of the EdgeID you want the number of lanes from.
     * @author Luca
     * */
    public List<String> getLaneNum(String edgeID) {

        try {
            // This so badly done by the TU Dresden that I don't have another choice but to unchecked cast this
            return (List<String>) _sumcon.do_job_get(Edge.getLaneNumber(edgeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Basicly the normal Vehicle.add(), but it handles the VehicleID setting logic for you.
     *
     * @param typeID The type of the Vehicle; Use constants.Vehicle for this.
     * @param routeID The ID of the route where the Vehicle is supposed to land
     * @param depart (0 indexing) The lane it's supposed to depart in. Use SimController.job(Edge.getIDCount()) for max lane num.
     * @param pos Position in m from the start of the Route
     * @param speed Speed? Not clear from declaration
     * @param lane 🌭
     * @author Luca
     * */
    public WVehicle addVehicle(String typeID, String routeID, int depart, double pos, double speed, byte lane){;

        try {
            int newVID = (int) _sumcon.do_job_get(Vehicle.getIDCount()) - 1;
            do {
                newVID = newVID + 1;
            }
            while(getallVehicles().contains("t_" + newVID));

            String newVIDstr = "t_" + newVID;

            _sumcon.do_job_set(Vehicle.add(newVIDstr, typeID, routeID, depart, pos, speed, lane));
            return new WVehicle(newVIDstr, _sumcon);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Getter of the Simulation
     * If you want to print the returned object, .to_String() is probably the best option.
     * @param scmd The SumoCommand to be executed upon the Simulation.
     * @example
     * <code>
     * sumcon.jobget(Vehicle.getIDList())
     * </code>
     * @return 🎲
     * */
    public Object jobget(SumoCommand scmd){
        try {
            return _sumcon.do_job_get(scmd);
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
     * @example
     * <code>
     * sumcon.jobset(Vehicle.add(vehID, typeID, routeID, depart, speed, lane)
     * </code>
     * */
    public void jobset(SumoCommand scmd){
        try {
            _sumcon.do_job_set(scmd);
        }
        catch (Exception e){
            Debug.print("DO JOB FAILED: " + scmd.toString());
            e.printStackTrace();
        }

    }

}
