package org.group_three.debug;


import com.sun.jdi.InvalidTypeException;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.api.SimController;
import org.group_three.debug.annotations.StaticClass;
import org.group_three.debug.exceptions.InvalidArgumentCount;
import org.group_three.model.WPolygon;
import org.group_three.model.WVehicle;
import org.group_three.service.Statistic;
import org.group_three.service.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Tester Class so we don't have the Main function cluttered full of
 * Random test methods. There is no other use for this.
 * @author Luca
 * */
@StaticClass
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
        for(String tl : sim.getTrafficLightsIDList()){
            Debug.print(tl + ": " + sim.getTLPhase(tl));
        }

        Debug.print("Vehicle Positions");
        for(String veh : sim.getVehicleIDList()){;
            Debug.print(veh + ": " + sim.jobget(Vehicle.getPosition(veh)));
        }

        sim.close();
    }


    public static void TableToCSVExample(){
        Table polyTable = new Table("UID", "Type", "color");

        for(WPolygon poly : WPolygon.getAllPolys()){
            try {
                polyTable.add(
                        poly.getPoID(),
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

        Table speedtable = new Table("time", "average speed");

        for(int i = 0; i < 30; i++){
            sc.step();
            try {
                speedtable.add(
                        sc.getTime(),
                        sc.getAverageVehSpeed()
                );
            } catch ( Exception e){
                e.printStackTrace();
                return;
            }

        }

        Statistic stat = new Statistic("Average speed per time", speedtable);

        Function<Object, Object> fun  = stat.getGraphOf("time", "average speed");

        for(int i = 0; i < 30; i++){
            System.out.println(i + ": " + fun.apply(i));
        }


    }


}
