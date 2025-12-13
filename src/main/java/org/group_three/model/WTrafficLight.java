package org.group_three.model;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
import de.tudresden.sumo.objects.SumoTLSProgram;
import it.polito.appeal.traci.SumoTraciConnection;
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
    private final SumoTraciConnection stc;


    private List<SumoPosition2D>  pos;
    private String programID;



    public WTrafficLight(String id) {
        this.trafficLightID = id;
        this.stc = SimController.getMainsimcon().getStc();
    }


    public String getID() {
        return trafficLightID;
    }

    public SumoTraciConnection getStc() {
        return stc;
    }

    public List<SumoPosition2D> getPos() {
        return pos;
    }


    public String getProgramID() {
        return programID;
    }




    public boolean setPhase(String trafficLightID, int index) {
        try {
            stc.do_job_set(Trafficlight.setPhase(trafficLightID, index));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean setProgram(String trafficLightID, String prgmID) {
        try {
            stc.do_job_set(Trafficlight.setProgram(trafficLightID, prgmID));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
