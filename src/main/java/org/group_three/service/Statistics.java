package org.group_three.service;


import de.tudresden.sumo.cmd.Simulation;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;

import java.util.List;

/**
 * Class for collecting statistics.
 * Can be instantiated to collect stats of one Simulation,
 * but also has plenty of Static functions.
 * @author Luca
 * */
public class Statistics {

    private SimController simcon;
    private List<String> content;


    /**
     * Exports the state of the Main simulation
     * @param filename The name of the file to be created
     * @return true of successfull, false if not
     * @author Luca
     * */
    public static boolean exportState(String filename) {

        return SimController.getMainsimcon().jobset(Simulation.saveState("output/" + filename));

    }

    /**
     * Exports the state of the given simulation
     * @param filename The name of the file to be created
     * @param stc the SumoTraciConnection of the Simulation
     * @return true of successfull, false if not
     * @author Luca
     * */
    public static boolean exportState(String filename, SumoTraciConnection stc) {
        try {
            stc.do_job_set(Simulation.saveState("output/" + filename));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Exports the state of the given simulation
     * @param filename The name of the file to be created
     * @param simcon the SimController of the Simulation
     * @return true of successfull, false if not
     * @author Luca
     * */
    public static boolean exportState(String filename, SimController simcon) {

        return  simcon.jobset(Simulation.saveState("output/" + filename));

    }




}
