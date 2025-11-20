package org.group_three.api;

import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;
import java.io.File;
import java.net.URI;
import java.net.URL;


public class SimController {


    public SimController(){
        Debug.print("SimController invoked");

        try {
            File jarDir = getSumoLoc();

            File resourcesDir = new File(jarDir, "resources");

            File sumoexe = new File(resourcesDir, "sumo.exe");
            Debug.print(sumoexe.getAbsolutePath());
            if (!sumoexe.exists()) {
                throw new Exception("sumo.exe not found");
            }
            else {
                Debug.print("sumo.exe found");
            }

            File networknet = new File(resourcesDir, "net.net.xml");
            Debug.print(networknet.getAbsolutePath());
            if (!networknet.exists()) {
                throw new Exception("net.net.xml not found");
            }
            else {
                Debug.print("network file found");
            }

            File routenet = new File(resourcesDir, "net.rou.xml");
            Debug.print(routenet.getAbsolutePath());
            if (!routenet.exists()) {
                throw new Exception("net.rou.xml not found");
            }
            else {
                Debug.print("route file found");
            }

            SumoTraciConnection sumcon = new SumoTraciConnection(
                    sumoexe.getAbsolutePath(),
                    networknet.getAbsolutePath(),
                    routenet.getAbsolutePath()
            );

            sumcon.printSumoOutput(true);
            sumcon.printSumoError(true);
            sumcon.runServer(8813);

            Debug.print("timesteps:");
            for (int i = 0; i < 5; i++) {
                sumcon.do_timestep();
            }

            sumcon.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.print("SimController finished");

    }

    private static File getSumoLoc() {
        URL mainURL = SimController.class.getProtectionDomain().getCodeSource().getLocation();
        URI mainURI = null;

        try {
            mainURI = mainURL.toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert mainURI != null;



        File jarDir = new File(mainURI);

        if(jarDir.isFile()){
            Debug.print("Jar Execution detected");
            return jarDir.getParentFile();
        }

        Debug.print("Normal Execution detected");
        return jarDir.getParentFile().getParentFile().getParentFile();



    }
}
