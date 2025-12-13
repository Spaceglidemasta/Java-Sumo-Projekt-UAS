package org.group_three.debug;


import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import org.group_three.api.SimController;

/**
 * Tester Class so we don't have the Main function cluttered full of
 * Random test methods. There is no other use for this.
 * @author Luca
 * */
public class StaticTester {

    public StaticTester() {
        SimController sim = new SimController("uascity/osm.sumocfg");

        //Test Scenario

        Debug.print("Trafic lights: " + sim.jobget(Trafficlight.getIDList()).toString());

        Debug.print("Edges:" + sim.getEdgeIDList());

        Debug.print("Vehicles:" + sim.getVehicleIDList());

        Debug.print("Vehicle Count: " + sim.jobget(Vehicle.getIDCount()));

        /*
        * List<String> edgelist = new ArrayList<>();
        edgelist.add("-E2");
        edgelist.add("E1.200");

        SumoStringList edges = new SumoStringList(edgelist);

        sim.jobset(Route.add("r_1", edges));

        Debug.print("Routes:" + sim.getRouteIDList());

        WVehicle v = sim.addVehicle("DEFAULT_CONTAINERTYPE", "r_1", 2, 0, 1, (byte)0);

        Debug.print(v.getPos());
        *
        *
        * */


        




        Debug.print("Junction Positions");
        for(String junc : sim.getJunctionIDList()){
            Debug.print(junc + ": " + sim.getJunctionPos(junc));
        }

        Debug.print("TL phases");
        for(String tl : sim.getTLIDList()){
            Debug.print(tl + ": " + sim.getTLPhase(tl));
        }

        Debug.print("Vehicle Positions");
        for(String veh : sim.getVehicleIDList()){;
            Debug.print(veh + ": " + sim.jobget(Vehicle.getPosition(veh)));
        }

        sim.close();
    }


}
