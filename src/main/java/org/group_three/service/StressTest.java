package org.group_three.service;

import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.api.SimController;
import org.group_three.constants.Sumo;
import org.group_three.debug.Debug;
import org.group_three.model.WEdge;
import org.group_three.model.WVehicle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**<h1>StressTest</h1>
 * Class for stress testing the simulation
 *
 * @author Leon
 * */
public class StressTest {

    /**
     * The Strength of the stress test is determined by this number.
     * It is calculated by taking the total length of the network,
     * and subtracting the chosen strength number for each car created.
     * If the number is high, fewer cars are created, if the number is low,
     * more cars are created.
     * <br>
     * After some testing we found that 100 works well and the simulation tends not to crash.
     *  */
    private int strength = 100;
    /**
     * The routs are randomly generated, and so the creation of a rout can fail, which results in
     * no car being created. This variable determines how many attempts to create a valid rout are made.
     * If you choose to increase the number, you might encounter some lag or even crashes.
     *  */
    private int routTries = 2;
    /**
     * Determines the speed with which the car is spawned.
     *  */
    private int startSpeed = 5;

    /**
     * Allows you to modify the color that the stress test cars are spawned with.
     *  */
    private SumoColor color = new SumoColor(255, 255, 255, 255);

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setRoutTries(int routTries) {
        this.routTries = routTries;
    }

    public void setStartSpeed(int startSpeed) {
        this.startSpeed = startSpeed;
    }

    public void setColor(SumoColor color) {
        this.color = color;
    }

    public int getRoutTries() {
        return routTries;
    }

    public int getStartSpeed() {
        return startSpeed;
    }

    public int getStrength() {
        return strength;
    }

    public SumoColor getColor() {
        return color;
    }

    /**
     * Stress testing function that spawns the chosen amount of cars based on the strength and
     * other variables defined above.
     *
     * @author Leon
     * */
    public void Test(){
        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

        List<WEdge> roads = simcon.getAllroads().values().stream().toList();

        for (int i = (int) simcon.getNetworkLength(); i >= strength-1; i = i-strength){
            SumoStringList strings = new SumoStringList();

            String routeId = null;

            for(int j = 0; j < routTries; j++) {
                int randomStart = ThreadLocalRandom.current().nextInt(roads.size());
                strings.add(roads.get(randomStart).getId());

                int randomEnd = ThreadLocalRandom.current().nextInt(roads.size());
                strings.add(roads.get(randomEnd).getId());

                routeId = SimController.getMainsimcon().addRoute(strings);

                if(routeId != null){
                    break;
                }
            }
            if (routeId != null) {
                WVehicle wVehicle = SimController.getMainsimcon().addVehicle(
                        Sumo.DEFAULT_VEHICLE,
                        routeId,
                        0,
                        0,
                        startSpeed
                        ,0
                );
                if (wVehicle != null) wVehicle.setColor(color);
            }
        }
    }
}
