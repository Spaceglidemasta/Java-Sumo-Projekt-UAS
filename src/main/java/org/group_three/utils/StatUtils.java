package org.group_three.utils;


import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;

import java.util.List;

/**
 * Utility Class for Statistics.
 * @author Luca
 * */
public final class StatUtils {


    /**
     * Compares the values of the given SumoColors,
     * including the opacity. Commutative.
     * @param first first color
     * @param sec second color
     * @return If both SumoColors are of equal values.
     * @author Luca
     * */
    public static boolean equalSColor(SumoColor first, SumoColor sec){
        return (
                   first.r == sec.r
                && first.b == sec.b
                && first.g == sec.g
                && first.a == sec.a
                );
    }


    /**
     * Exports the state of the Main simulation
     * <p> WARNING Creates Files </p>
     * @param filename The name of the file to be created
     * @return true of successful, false if not
     * @author Luca
     * */
    @Deprecated
    public static boolean exportState(String filename) {

        return SimController.getMainsimcon().jobset(Simulation.saveState("output/" + filename));

    }

    /**
     * Exports the state of the given simulation
     * <p> WARNING Creates Files </p>
     * @param filename The name of the file to be created
     * @param stc the SumoTraciConnection of the Simulation
     * @return true of successful, false if not
     * @author Luca
     * */
    @Deprecated
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
     * <p> WARNING Creates Files </p>
     * @param filename The name of the file to be created
     * @param simcon the SimController of the Simulation
     * @return true of successful, false if not
     * @author Luca
     * */
    @Deprecated
    public static boolean exportState(String filename, SimController simcon) {

        return  simcon.jobset(Simulation.saveState("output/" + filename));

    }




}
