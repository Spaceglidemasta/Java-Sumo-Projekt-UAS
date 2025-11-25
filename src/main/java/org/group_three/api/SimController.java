package org.group_three.api;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import it.polito.appeal.traci.SumoTraciConnection;
import javafx.beans.binding.ObjectExpression;
import org.group_three.debug.Debug;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SimController {
    //The connection to the Sumo simulation. Invoked in the constructor and destroyed with .close()
    private SumoTraciConnection _sumcon;
    private static final String networkfname = "net.net.xml";
    private static final String routefname = "net.rou.xml";

    public SimController(){
        Debug.print("SimController invoked");

        //try & catch to catch exceptions
        try {
            // get folder location of Project
            File jarDir = getSumoLoc();

            // opens said folder
            File resourcesDir = new File(jarDir, "resources");


            // Try SUMO_HOME/bin/sumo.exe, otherwise use resourcesDir/sumo.exe
            String sumoHome = System.getenv("SUMO_HOME");

            // possible location of sumo.exe in %SUMO_HOME%/bin
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

    //closes the SumoTraciConnection
    public void close(){

        try {
            // this can actually throw an ISE even tho it's not indicated.
            _sumcon.close();
        }
        catch (IllegalStateException ise){
            ise.printStackTrace();
        }


    }


    public List<Objects> getTrafficLights(){
        try {
            return (List<Objects>) _sumcon.do_job_get(Trafficlight.getIDList());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Objects>();
    }

    public void addVehicle(Vehicle v){

    }



    /*
        returns the location(FILE) the Project is located in.
        Knows if the Program is compiled & executed or a jar file
    */
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
}
