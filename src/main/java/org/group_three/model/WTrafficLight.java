package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
import de.tudresden.sumo.objects.SumoTLSProgram;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.utils.LaneStopLineData;
import org.group_three.utils.Sumo2DLine;

import java.util.ArrayList;
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

    public List<SumoPosition2D>  getPos() {
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

        this.pos = getStopLinePoint(trafficLightID);

        this.phaseState = (int) simcon.jobget(Trafficlight.getPhase(trafficLightID));

        this.phaseName = (String) simcon.jobget(Trafficlight.getPhaseName(trafficLightID));

        this.phaseDuration = (double) simcon.jobget(Trafficlight.getPhaseDuration(trafficLightID));

        this.programID = (String) simcon.jobget(Trafficlight.getProgram(trafficLightID));

        this.state = (String) simcon.jobget(Trafficlight.getRedYellowGreenState(trafficLightID));

    }

    public List<SumoPosition2D> getStopLinePoint(String TLID) {
        List<SumoPosition2D> laneStopLines = new ArrayList<>();
        SumoStringList linkedLanes = simcon.getControlledLanes(TLID);

        for (String laneID : linkedLanes) {
            String edgeShape = simcon.getLaneShape(laneID).toString();
            String[] coordinates = edgeShape.split(" ");

//            String secondToLastCoordinate = coordinates[coordinates.length - 2];
            String lastCoordinate = coordinates[coordinates.length - 1];
//            String[] secondToLastCoord = secondToLastCoordinate.split(",");
            String[] lastCoord = lastCoordinate.split(",");

//            double x1 = Double.parseDouble(secondToLastCoord[0]);
//            double y1 = Double.parseDouble(secondToLastCoord[1]);
            double x = Double.parseDouble(lastCoord[0].replaceAll("[^0-9.-]", ""));
            double y = Double.parseDouble(lastCoord[1].replaceAll("[^0-9.-]", ""));

            //SumoPosition2D point2 = new SumoPosition2D(x2, y2);

            SumoPosition2D point1 = new SumoPosition2D(x, y);
            laneStopLines.add(point1);
            Debug.print("Lane " + laneID + " last point: " + x + "," + y);
        }
        return laneStopLines;
    }
}
