package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.*;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * Traffic Light Wrapper Class
 *
 * @author Luca, Leon
 * */
public class WTrafficLight {


    private final String TLID;
    private final SumoTraciConnection stc;
    private List<WLink> allWlinks;
    private static List<WTrafficLight> allWTLs;



    public WTrafficLight(String id) {
        this.TLID = id;
        this.stc = SimController.getMainsimcon().getStc();
        allWlinks = new ArrayList<>();
    }


    public String getID() {
        return TLID;
    }

    public SumoTraciConnection getStc() {
        return stc;
    }


    public void printLinks(){

        SumoLinkList out = new SumoLinkList();
        try {
            out = (SumoLinkList) stc.do_job_get(Trafficlight.getControlledLinks(TLID));

        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(TLID + ":");
        for(Object k : out){
            System.out.println("    " + TLID +  ": " + k.toString());
        }


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
        try {
            stc.do_job_set(Trafficlight.setPhase(TLID, index));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**This does not give you the color of the TL as an index!!!
     * @return the index of the current phase[0:] Or <code>-1</code> if failed
     * @author Luca
     * */
    public int getPhaseIndex() {
        try {
            return (int) stc.do_job_get(Trafficlight.getPhase(TLID));
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Sets the phase length of the TL
     * @param t duration in (probably) s
     * @return true if success, false if not
     * @author Luca
     * */
    public boolean setPhaseLen(double t){
        try {
            stc.do_job_set(Trafficlight.setPhaseDuration(TLID, t));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @return phase len as double, or -1.0d if failed
     * @author Luca
     * */
    public double getPhaseLen(){
        try {
            return (double) stc.do_job_get(Trafficlight.getPhaseDuration(TLID));

        } catch (Exception e) {
            e.printStackTrace();
            return -1.0d;
        }
    }

    /**
     * Sets the program of the TL via the program ID
     * @param PID Program ID
     * @return true if success, false if not
     * @author Luca
     * */
    public boolean setProgram(String PID) {
        try {
            stc.do_job_set(Trafficlight.setProgram(TLID, PID));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get program ID from the TLID
     * @return PID / null
     * @author Luca
     * */
    @MayReturnNull
    public String getProgramID() {
        try {
            return (String) stc.do_job_get(Trafficlight.getProgram(TLID));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /** This only retrieves the states. You probably want to use loadLinkedStateColors()
     * Get program ID from the TLID
     * @return The states of all Linked Wlinks.
     * @author Luca
     * */
    @MayReturnNull
    public SumoStringList getLinkStates() {
        try {
            return (SumoStringList) stc.do_job_get(Trafficlight.getRedYellowGreenState(TLID));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the Colors for all Links of this TL via the simulation. <br>
     * Loaded Colors can then be retrieved via wLink.getColor()
     * @return The states of all Linked WLinks.
     * @author Luca
     * */
    @MayReturnNull
    public SumoStringList loadLinkedStateColors() {
        SumoStringList lstates = this.getLinkStates();

        if(lstates == null) return null;

        int i = 0;

        for(String state : lstates){

            SumoColor sc = new SumoColor();

            if(Objects.equals(state, "r") || Objects.equals(state, "R"))        sc = new SumoColor(255,0,0,255);
            else if(Objects.equals(state, "g") || Objects.equals(state, "G"))   sc = new SumoColor(0,255,0,255);
            else if(Objects.equals(state, "y") || Objects.equals(state, "Y"))   sc = new SumoColor(0,255,255,255);

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
    public static List<WTrafficLight> loadAll() {

        allWTLs = new ArrayList<>();

        SimController msc = SimController.getMainsimcon();

        SumoStringList allTLIDs = msc.getTrafficLightsIDList();

        for(String TLID : allTLIDs){

            WTrafficLight wtl = new WTrafficLight(TLID);

            List<String> allLIDs = msc.getControlledLanes(TLID);

            for(String LID : allLIDs){

                LinkedList<SumoPosition2D> shape = msc.getLaneShape(LID);

                Vector2D last = new Vector2D(shape.getLast());
                Vector2D stlast = new Vector2D(shape.get(shape.size() - 2));

	            double angle = last.getDirectionAngle(stlast);

                double width = msc.getLaneWidth(LID);
                double len = 0.5;

                WLink wlink = new WLink(last, width, len, angle, LID);

                wtl.addWlink(wlink);

                //wtl.printLinks();

            }

            allWTLs.add(wtl);

        }

        return allWTLs;
    }


}
