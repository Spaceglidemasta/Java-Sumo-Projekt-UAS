package org.group_three;



//external libraries
import org.eclipse.sumo.libsumo.*;
import org.group_three.api.SimController;
import org.group_three.basicGui.MainGui;

//packages
import org.group_three.api.SimSumo;
import org.group_three.debug.Debug;


public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        Simulation.preloadLibraries();
        Simulation.start(new StringVector(new String[] {"sumo", "-n", "src/main/resources/net.net.xml",
                "-r", "src/main/resources/net.rou.xml"
        }));

        for (int i = 0; i < 5; i++) {
            Simulation.step();
        }

        Simulation.close();


        MainGui aMainGui = new MainGui();
        aMainGui.start(args);



    }
}