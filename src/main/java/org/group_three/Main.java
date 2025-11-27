package org.group_three;



//external libraries
import de.tudresden.sumo.cmd.Trafficlight;
import org.group_three.api.SimController;
import org.group_three.debug.Console;
import org.group_three.ui.MainApp;


//packages
import org.group_three.debug.Debug;

public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        SimController sim = new SimController();

        Debug.print(sim.job(Trafficlight.getIDList()).toString());

        sim.close();

        MainApp aMainGui = new MainApp();
        aMainGui.start(args);

    }
}