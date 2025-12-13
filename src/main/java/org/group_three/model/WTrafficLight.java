package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoLink;
import de.tudresden.sumo.objects.SumoLinkList;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;
import org.group_three.ui.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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

    public List<WTrafficLight> getAllWTLs() {
        return allWTLs;
    }

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


    /**
     * Returns the TL Phase as an integer from <code>[0:2]</code>
     * @return phase or <code>-1</code> if failed
     * @author Luca
     * */
    public int getPhase() {
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
    public String getProgramID() {
        try {
            return (String) stc.do_job_get(Trafficlight.getProgram(TLID));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

                Vector2D mid = last.add(stlast).div(2);

                double width = msc.getLaneWidth(LID);
                double len = mid.length();
                double angle = last.getDirectionAngle(stlast);

                WLink wlink = new WLink(mid, width, len, angle, LID);

                wtl.addWlink(wlink);

                //wtl.printLinks();

            }

            allWTLs.add(wtl);

        }

        return allWTLs;
    }


}
