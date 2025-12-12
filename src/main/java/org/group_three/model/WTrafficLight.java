package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
import de.tudresden.sumo.objects.SumoTLSProgram;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.utils.TLStopLine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WTrafficLight {


    private final String trafficLightID;
    private final SimController simcon;


    private List<SumoPosition2D>  pos;
    private int phaseState;
    private String phaseName;
    private double phaseDuration;
    private String programID;
    private String state;


    public WTrafficLight(String id) {
        this.trafficLightID = id;
        this.simcon = SimController.getMainsimcon();
    }


    public String getID() {
        return trafficLightID;
    }

    public SimController getSumoCon() {
        return simcon;
    }

    public double getPhaseState() {
        return phaseState;
    }

    public List<SumoPosition2D> getPos() {
        return pos;
    }


    public String getPhaseName() {
        return phaseName;
    }

    public double getPhaseDuration() {
        return phaseDuration;
    }

    public String getProgramID() {
        return programID;
    }

    public String getState() {
        return state;
    }


    public boolean setCompleteRYGDefinition(String trafficLightID, SumoTLSProgram prgm) {
        try {
            simcon.jobset(Trafficlight.setCompleteRedYellowGreenDefinition(trafficLightID, prgm));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setPhase(String trafficLightID, int index) {
        try {
            simcon.jobset(Trafficlight.setPhase(trafficLightID, index));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean steRYGState(String trafficLightID, String state) {
        try {
            simcon.jobset(Trafficlight.setRedYellowGreenState(trafficLightID, state));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setProgram(String trafficLightID, String prgmID) {
        try {
            simcon.jobset(Trafficlight.setProgram(trafficLightID, prgmID));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void update() throws Exception {

        this.pos = getLastStopLinePoints(trafficLightID);

        this.phaseState = (int) simcon.jobget(Trafficlight.getPhase(trafficLightID));

        this.phaseName = (String) simcon.jobget(Trafficlight.getPhaseName(trafficLightID));

        this.phaseDuration = (double) simcon.jobget(Trafficlight.getPhaseDuration(trafficLightID));

        this.programID = (String) simcon.jobget(Trafficlight.getProgram(trafficLightID));

        this.state = (String) simcon.jobget(Trafficlight.getRedYellowGreenState(trafficLightID));

    }


    public List<SumoPosition2D> getLastStopLinePoints(String TLID) {
        List<SumoPosition2D> lastPoints = new ArrayList<>();
        SumoStringList linkedLanes = simcon.getControlledLanes(TLID);

        for (String laneID : linkedLanes) {
            LinkedList<SumoPosition2D> edgeShape = simcon.getLaneShape(laneID);

            lastPoints.add(edgeShape.getLast());
        }
        return lastPoints;
    }



    public List<TLStopLine> getStopLineData(String TLID) {
        List<TLStopLine> laneDataList = new ArrayList<>();
        SumoStringList linkedLanes = simcon.getControlledLanes(TLID);
        for (String laneID : linkedLanes) {
            LinkedList<SumoPosition2D> edgeShape = simcon.getLaneShape(laneID);

            TLStopLine data = new TLStopLine(laneID, edgeShape.get(edgeShape.size() - 2), edgeShape.getLast());
            laneDataList.add(data);
        }

        return laneDataList;
    }

}
