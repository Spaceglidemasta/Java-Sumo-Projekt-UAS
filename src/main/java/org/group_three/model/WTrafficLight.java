package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoTLSProgram;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;
import org.group_three.utils.Sumo2DLine;

public class WTrafficLight {


    private final String trafficLightID;
    private final SimController simcon;


    private Sumo2DLine pos;
    private int phaseState;
    private String phaseName;
    private double phaseDuration;
    private String programID;
    private String state;



    public WTrafficLight(String id, SimController simcon){
        this.trafficLightID = id;
        this.simcon = simcon;
    }


    public String getID() {return trafficLightID;}

    public SimController getSumoCon() {return simcon;}

    public double getPhaseState() {return phaseState;}

    public Sumo2DLine getPos() {return pos;}

    public String getPhaseName() {return phaseName;}

    public double getPhaseDuration() {return phaseDuration;}

    public String getProgramID() {return programID;}

    public String getState() {return state;}


    public boolean setCompleteRYGDefinition(String trafficLightID, SumoTLSProgram prgm){
        try{
            simcon.jobset(Trafficlight.setCompleteRedYellowGreenDefinition(trafficLightID,prgm));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setPhase(String trafficLightID, int index){
        try{
            simcon.jobset(Trafficlight.setPhase(trafficLightID,index));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean steRYGState (String trafficLightID, String state){
        try{
            simcon.jobset(Trafficlight.setRedYellowGreenState(trafficLightID,state));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean setProgram (String trafficLightID, String prgmID){
        try{
            simcon.jobset(Trafficlight.setProgram(trafficLightID,prgmID));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void update() throws Exception{

        this.pos = (Sumo2DLine) simcon.getStopLineVector(trafficLightID);

        this.phaseState = (int) simcon.jobget(Trafficlight.getPhase(trafficLightID));

        this.phaseName = (String) simcon.jobget(Trafficlight.getPhaseName(trafficLightID));

        this.phaseDuration = (double) simcon.jobget(Trafficlight.getPhaseDuration(trafficLightID));

        this.programID = (String) simcon.jobget(Trafficlight.getProgram(trafficLightID));

        this.state = (String) simcon.jobget(Trafficlight.getRedYellowGreenState(trafficLightID));

    }

}
