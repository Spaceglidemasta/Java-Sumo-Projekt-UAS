package org.group_three.api;

import org.eclipse.sumo.libsumo.Simulation;
import org.eclipse.sumo.libsumo.StringVector;

public class SimSumo {

    public SimSumo(){

        System.out.println("api.SimSumo: Simstart");

        Simulation.preloadLibraries();
        Simulation.start(new StringVector(new String[] {"sumo", "-n", "src/main/resources/net.net.xml",
                "-r", "src/main/resources/net.rou.xml"
        }));
    }
}
