package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.*;
import org.group_three.api.SimController;
import org.group_three.constants.enums.TLColor;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.Vector2D;

import java.util.*;
import java.util.logging.Logger;


/**
 * <h1>WTrafficLight</h1>
 * Traffic Light Wrapper Class.
 * @see org.group_three.model.WLink
 * @see org.group_three.model.WPhase
 * @author Luca
 * */
public class WTrafficLight extends WObject {

    static final Logger log = Logger.getLogger(WTrafficLight.class.getName());

    //unfinal this when adding add-link functionality
    private final List<WLink> allWlinks;
    private boolean customPhasesActive = false;
    private List<WPhase> customProgram = new ArrayList<>();

    public WTrafficLight(SimController simcon, String id) {
        super(simcon, id);
        allWlinks = new ArrayList<>();
    }

    public boolean isCustomPhasesActive() {
        return customPhasesActive;
    }

    public void setCustomPhasesActive(boolean customPhasesActive) {
        this.customPhasesActive = customPhasesActive;
    }

    public List<WPhase> getCustomProgram() {
        return customProgram;
    }

    public void setCustomProgram(List<WPhase> customProgram) {
        this.customProgram = customProgram;
    }

    /**
     * Adds a Phase to the Custom List of Phases / Custom Program
     * @param phase The WPhase to be added
     * @author Luca
     * */
    public void addCustomPhase(WPhase phase){
        this.customProgram.add(phase);
    }

    /**
     * Adds a WLink to the local instance allWlinks list
     * @param wlink the WLink to be added
     * @author Luca
     * */
    public void addWlink(WLink wlink) {
        allWlinks.add(wlink);
    }

    public HashMap<String, WTrafficLight> getAllWTLs() {
        return simcon.getAllWTLs();
    }

    public List<WLink> getAllWlinks() {return allWlinks;}

    /**
     * A PHASE IS A PHASE OF A WHOLE PROGRAM, NOT THE COLOR OF ONE TL-Link
     * @param index Phase index of the Phase inside the Program
     * @return true if success, false if not
     * @author Leon
     * */
    public boolean setPhase(int index) {
        return simcon.jobset(Trafficlight.setPhase(id, index));
    }


    /**This does not give you the color of the TL as an index, but the index of the phase in the program.
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
    public boolean setProgramID(String PID) {

        return simcon.jobset(Trafficlight.setProgram(id, PID));
    }

    public boolean setProgram(SumoTLSProgram tls) {
        return simcon.jobset(Trafficlight.setCompleteRedYellowGreenDefinition(id, tls));
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

    /**
     * Get program of the TL. <br>
     * @return The SumoTLSController of the Program
     * @author Luca
     * */
    @MayReturnNull
    public SumoTLSController getProgram() {
        return (SumoTLSController) simcon.jobget(Trafficlight.getCompleteRedYellowGreenDefinition(id));
    }


    /**This only retrieves the states. You probably want to use loadLinkedStateColors()
     * Get program ID from the id
     * @return The states of all Linked WLinks or null.
     * @author Luca
     * */
    @MayReturnNull
    public String getLinkStates() {
        return (String) simcon.jobget(Trafficlight.getRedYellowGreenState(id));
    }

    /**
     * Sets the Phase Index [0:2] of the TL. <br>
     * @param TLID Traffic Light ID
     * @param iPhase Phase index as int, starting at 0 (?)
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @author Luca
     * */
    public boolean setTLPhase(String TLID, int iPhase){

        log.fine("TrafficLight " + TLID + ": Phase changed to index " + iPhase);
        return simcon.jobset(Trafficlight.setPhase(TLID, iPhase));
    }

    /**
     * Returns the Traffic Light phase as Index [0:2] <br>
     * @param TLID ID of the Traffic Light
     * @return Phase of the TL, <code>null</code> if getter failed.
     * @author Luca
     */
    public int getTLPhase(String TLID) {

        return (int) simcon.jobget(Trafficlight.getPhase(TLID));



    }

    /**
     * Returns the Traffic Light phase duration <br>
     * @param TLID ID of the Traffic Light
     * @return Duration of the Phase, <code>-1</code> if getter failed.
     * @author Leon
     */
    public String getRYGState(String TLID) {

        return (String) simcon.jobget(Trafficlight.getRedYellowGreenState(TLID));

    }

    /**
     * Returns the Traffic Light phase duration <br>
     * @param TLID ID of the Traffic Light
     * @return Duration of the Phase, <code>-1</code> if getter failed.
     * @author Leon
     */
    public boolean setRYGState(String TLID, String state) {

         return simcon.jobset(Trafficlight.setRedYellowGreenState(TLID, state));

    }



    /**
     * Sets the Length of the current Phase of the given TL.
     * @param TLID Traffic Light ID
     * @param dur new Duration in seconds(?)( <- This not my questionmark, the TraaS doc also has a "?")
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @author Luca
     * */
    public boolean setTLPhaseLen(String TLID, double dur){

        return simcon.jobset(Trafficlight.setPhaseDuration(TLID, dur));

    }

    /**
     * Returns the requested Parameter of a Trafficlight
     * @param TLID TrafficLight ID
     * @param param The requested parameter
     * @return <code>null</code> if failed, else the requested parameter
     * @author Luca
     * */
    @MayReturnNull
    public String getTLParam(String TLID, String param){

        return  (String) simcon.jobget(Trafficlight.getParameter(TLID,param));

    }


    /**
     * Sets the given Parameter of a Trafficlight
     * @param TLID TrafficLight ID
     * @param param the parameter which is supposed to change
     * @param value the value that is to be inserted in the given parameter
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @author Luca
     * */
    public boolean setTLParam(String TLID, String param, String value){

        return simcon.jobset(Trafficlight.setParameter(TLID, param, value));

    }


    /**<h2>setCustomProgramLinkColor</h2>
     * Sets the Color of a desired link inside a phase inside the customProgram.
     * @param program_index the index of the phase inside customProgram
     * @param link_index the index of the link inside the phase
     * @param color the desired TLColor
     * @return <code>true</code> if success, <code>false</code> if not.
     * @author Luca
     * */
    public boolean setCustomProgramLinkColor(int program_index, int link_index, TLColor color) {

        if(program_index >= customProgram.size()) {
            log.warning("Given program_index index out of bounds: " + program_index + ". Max Index: " + (customProgram.size() - 1));
            return false;
        }

        WPhase wphase = customProgram.get(program_index);

        if(link_index >= wphase.program.size()) {
            log.warning("Given link_index index out of bounds: " + link_index + ". Max Index: " + (wphase.program.size() - 1));
            return false;
        }

        Character col = null;

        if (color == TLColor.RED) col = 'R';
        if (color == TLColor.YELLOW) col = 'Y';
        if (color == TLColor.GREEN) col = 'G';

        if(col == null){
            log.severe("Given TL Color is really weird, color not changed.");
            return false;
        }

        wphase.program.set(link_index, col);

        return true;
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
     * @param simcon the SimController this is to be invoked upon
     * @return Said List
     * @author Luca
     * */
    public static HashMap<String, WTrafficLight> loadAll(SimController simcon) {

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

            simcon.addToAllWTLs(TLID, wtl);

        }

        log.fine("Loading all TrafficLights was successful.");

        return simcon.getAllWTLs();
    }



}
