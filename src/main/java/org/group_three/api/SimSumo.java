package org.group_three.api;

// external libaries
import org.eclipse.sumo.libsumo.Simulation;
import org.eclipse.sumo.libsumo.StringVector;
import org.group_three.debug.Debug;

// packages

public class SimSumo {

    private final double steptime = 0.5;


    public SimSumo(){

        Debug.print("SimSumo invoked");

        Simulation.preloadLibraries();
        Simulation.start(new StringVector(new String[] {"sumo", "-n", "src/main/resources/net.net.xml",
                "-r", "src/main/resources/net.rou.xml"
        }));

    }

    public void step(){
        Simulation.step(steptime);
    }

}
