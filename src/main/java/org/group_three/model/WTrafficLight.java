package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.*;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.Vector2D;

import java.util.*;


/**
 * Traffic Light Wrapper Class
 *
 * @author Luca, Leon
 * */
public class WTrafficLight extends WObject {

    private List<WLink> allWlinks;
    private static List<WTrafficLight> allWTLs;

    public WTrafficLight(SimController simcon, String id) {
        super(simcon, id);
        allWlinks = new ArrayList<>();
    }


    public String getID() {
        return id;
    }

    /**
     * Adds a WLink to the local instance allWlinks list
     * @param wlink the WLink to be added
     * @author Luca
     * */
    public void addWlink(WLink wlink) {
        allWlinks.add(wlink);
    }

    public static List<WTrafficLight> getAllWTLs() {
        return allWTLs;
    }

    public List<WLink> getAllWlinks() {return allWlinks;}

    /**
     * Sets the Phase
     * @param index Phase index from [0:2]
     * @return true if success, false if not
     * @author Leon
     * */
    public boolean setPhase(int index) {
        return simcon.jobset(Trafficlight.setPhase(id, index));
    }


    /**This does not give you the color of the TL as an index!!!
     * @return the index of the current phase[0:]
     * @author Luca
     * */
    public int getPhaseIndex() {

         return (int) simcon.jobget(Trafficlight.getPhase(id));
    }

    /**
     * Sets the phase length of the TL
     * @param t duration in (probably) s
     * @return true if success, false if not
     * @author Luca
     * */
    public boolean setPhaseLen(double t){

        return simcon.jobset(Trafficlight.setPhaseDuration(id, t));

    }


    /**
     * @return phase len as double, or -1.0d if failed
     * @author Luca
     * */
    public double getPhaseLen(){

        return (double) simcon.jobget(Trafficlight.getPhaseDuration(id));

    }

    /**
     * Sets the program of the TL via the program ID
     * @param PID Program ID
     * @return true if success, false if not
     * @author Luca
     * */
    public boolean setProgram(String PID) {

        return simcon.jobset(Trafficlight.setProgram(id, PID));

    }

    /**
     * Get program ID from the id
     * @return PID / null
     * @author Luca
     * */
    @MayReturnNull
    public String getProgramID() {
        return (String) simcon.jobget(Trafficlight.getProgram(id));
    }


    /** This only retrieves the states. You probably want to use loadLinkedStateColors()
     * Get program ID from the id
     * @return The states of all Linked Wlinks or null.
     * @author Luca
     * */
    @MayReturnNull
    public String getLinkStates() {
        return (String) simcon.jobget(Trafficlight.getRedYellowGreenState(id));
    }

    /**
     * Loads the Colors for all Links of this TL via the simulation. <br>
     * Loaded Colors can then be retrieved via wLink.getColor()
     * @return The states of all Linked WLinks.
     * @author Luca
     * */
    @MayReturnNull
    public List<String> loadLinkedStateColors() {
        List<String> lstates = Arrays.asList(this.getLinkStates().split(""));
        int i = 0;

        for(String state : lstates){

            SumoColor sc = new SumoColor();

            if( Objects.equals(state, "R"))        sc = new SumoColor(255,0,0,255);
            else if(Objects.equals(state, "r"))    sc = new SumoColor(255,0,0,100);

            else if(Objects.equals(state, "G"))   sc = new SumoColor(0,255,0,255);
            else if(Objects.equals(state, "g"))   sc = new SumoColor(0,255,0,100);


            else if(Objects.equals(state, "y") || Objects.equals(state, "Y"))   sc = new SumoColor(255,255,0,255);

            allWlinks.get(i).setColor(sc);

            i++;
        }

        return lstates;

    }



    /**<h2>loadAll</h2>
     * Loads all Traffic Lights and controlled Lanes into the Static variable allWTLs
     * @return Said List
     * @author Luca
     * */
    public static List<WTrafficLight> loadAll(SimController simcon) {

        allWTLs = new ArrayList<>();

        SumoStringList allTLIDs = simcon.getTrafficLightsIDList();

        for(String TLID : allTLIDs){

            WTrafficLight wtl = new WTrafficLight(simcon, TLID);

            List<String> allLIDs = simcon.getControlledLanes(TLID);

            int TLIndex = 0;
            for(String LID : allLIDs){

                LinkedList<SumoPosition2D> shape = simcon.getLaneShape(LID);

                Vector2D last = new Vector2D(shape.getLast());
                Vector2D stlast = new Vector2D(shape.get(shape.size() - 2));

	            double angle = last.getDirectionAngle(stlast);

                double width = simcon.getLaneWidth(LID);
                double len = 0.5;

                WLink wlink = new WLink(TLIndex, last, width, len, angle, LID);

                wtl.addWlink(wlink);

                //wtl.printLinks();

                TLIndex++;

            }

            allWTLs.add(wtl);

        }

        return allWTLs;
    }


}
