package org.group_three.debug;


import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import org.group_three.api.SimController;
import org.group_three.debug.annotations.StaticClass;
import org.group_three.model.WPolygon;
import org.group_three.service.Statistic;
import org.group_three.service.Table;

import java.util.function.Function;
import java.util.logging.Level;

/**
 * Tester Class so we don't have the Main function cluttered full of
 * Random test methods. There is no other use for this.
 * @author Luca
 * */
@StaticClass
public abstract class StaticTester {

    @Deprecated
    private StaticTester() {
        SimController sim = new SimController("uascity/osm.sumocfg");

        //Test Scenario

        Debug.log("Trafic lights: " + sim.jobget(Trafficlight.getIDList()).toString(), Level.FINE);

        Debug.log("Edges:" + sim.getEdgeIDList(), Level.FINE);

        Debug.log("Vehicles:" + sim.getVehicleIDList(), Level.FINE);

        Debug.log("Vehicle Count: " + sim.jobget(Vehicle.getIDCount()), Level.FINE);

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







        Debug.log("Junction Positions", Level.FINE);
        for(String junc : sim.getJunctionIDList()){
            Debug.log(junc + ": " + sim.getJunctionPos(junc), Level.FINE);
        }

        Debug.log("TL phases", Level.FINE);
        for(String tl : sim.getTrafficLightsIDList()){
            Debug.log(tl + ": " + sim.getTLPhase(tl), Level.FINE);
        }

        Debug.log("Vehicle Positions", Level.FINE);
        for(String veh : sim.getVehicleIDList()){;
            Debug.log(veh + ": " + sim.jobget(Vehicle.getPosition(veh)), Level.FINE);
        }

        sim.close();
    }


    public static void TableToCSVExample(SimController simcon){
        Table polyTable = new Table("UID", "Type", "color");

        for(WPolygon poly : simcon.getAllPolys()){
            try {
                polyTable.add(
                        poly.getId(),
                        poly.getType(),
                        poly.getColor()
                );
            } catch ( Exception e){
                e.printStackTrace();
                return;
            }

        }

        polyTable.outAsCSV();
    }

    public static void StatisticGraphExample() {
        SimController sc = SimController.getMainsimcon();

        Statistic<Double> stat = new Statistic<Double>("Average Speed per second","time", "average speed");

        for(int i = 0; i < 30; i++){
            sc.step();
            try {
                stat.add(
                        (double) sc.getTime(),
                        sc.getAverageVehSpeed()
                );
            } catch ( Exception e){
                e.printStackTrace();
                return;
            }

        }


        Function<Object, Object> fun  = stat.getGraphOf("time", "average speed");

        System.out.println(stat.getName());
        for(double i = 0; i < 30; i++){
            System.out.println(i + ": " + fun.apply(i));
        }

    }


}
