package org.group_three;

import de.tudresden.sumo.cmd.Route;
import org.group_three.debug.Debug;
import org.group_three.api.SimController;
import org.group_three.debug.Console;
import org.group_three.model.WVehicle;
import org.group_three.ui.MainApp;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;



public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        SimController sim = new SimController("net.net.xml", "speedtest.rou.xml");

        Debug.print("Trafic lights: " + sim.job(Trafficlight.getIDList()).toString());

        Debug.print("Routes:" + sim.getallRoutes());

        Debug.print("Edges:" + sim.getallEdges());

        Debug.print("Vehicles:" + sim.getallVehicles());

        Debug.print("Vehicle Count: " + sim.job(Vehicle.getIDCount()));

        //WVehicle v = sim.addVehicle("DEFAULT_CONTAINERTYPE", "!t_8!var#1", 2, 0, 1, (byte)0);

        //Debug.print(v.getPos());

        sim.close();

        MainApp aMainGui = new MainApp();
        aMainGui.start(args);

    }
}