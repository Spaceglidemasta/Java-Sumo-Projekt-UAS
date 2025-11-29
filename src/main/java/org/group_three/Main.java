package org.group_three;

import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.debug.Debug;
import org.group_three.api.SimController;
import org.group_three.model.WVehicle;
import org.group_three.ui.MainApp;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Debug.print("Programm Start");

        SimController sim = new SimController("speedtest.sumocfg");

        //Test Scenario

        Debug.print("Trafic lights: " + sim.jobget(Trafficlight.getIDList()).toString());

        Debug.print("Edges:" + sim.getallEdges());

        Debug.print("Vehicles:" + sim.getallVehicles());

        Debug.print("Vehicle Count: " + sim.jobget(Vehicle.getIDCount()));

        List<String> edgelist = new ArrayList<>();
        edgelist.add("-E2");
        edgelist.add("E1.200");

        SumoStringList edges = new SumoStringList(edgelist);

        sim.jobset(Route.add("r_1", edges));

        Debug.print("Routes:" + sim.getallRoutes());

        WVehicle v = sim.addVehicle("DEFAULT_CONTAINERTYPE", "r_1", 2, 0, 1, (byte)0);

        Debug.print(v.getPos());

        Debug.print("Angle: " + sim.jobget(Vehicle.getAngle(v.getID())).getClass().getName());

        sim.close();

        MainApp aMainGui = new MainApp();
        aMainGui.start(args);

    }
}