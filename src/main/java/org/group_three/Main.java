package org.group_three;

import org.eclipse.sumo.libsumo.*;
import org.group_three.basicGui.MainGui;



public class Main {
    public static void main(String[] args) {
        Simulation.preloadLibraries();
        Simulation.start(new StringVector(new String[] {"sumo", "-n", "src/main/resources/net.net.xml",
                                                                "-r", "src/main/resources/net.rou.xml"
                                                        }));

        System.out.println("Program start HERE");

        MainGui aMainGui = new MainGui();
        aMainGui.start(args);
    }
}