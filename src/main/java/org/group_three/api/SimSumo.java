package org.group_three.api;

// external libaries
import org.eclipse.sumo.libsumo.Simulation;
import org.eclipse.sumo.libsumo.StringVector;
import org.group_three.debug.Debug;

// packages

public class SimSumo {

    public SimSumo(){

        Debug.print("SimSumo invoked");

        Simulation.preloadLibraries();
        Simulation.start(new StringVector(new String[] {"sumo", "-n", "src/main/resources/net.net.xml",
                "-r", "src/main/resources/net.rou.xml"
        }));
    }
}
