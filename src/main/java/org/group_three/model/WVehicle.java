package org.group_three.model;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * A Wrapper Class for Vehicle which uses only the VehicleID to get and set values.
 * @author Luca
 * */
public class WVehicle {

    private final String vehID;
    private final SumoTraciConnection _sumcon;

    /**
     * Only constructor for this Class, invokes a new Vehicle with just the VehicleID
     *
     * @param id VehicleID
     * @author Luca
     *
     */
    public WVehicle(String id, SumoTraciConnection sc){
        this.vehID = id;
        this._sumcon = sc;
    }

    /**
     * Gets you the SumoPosition2D of the Vehicle. Attributes are public.<br>
     * @example <code>
     * double x = v.getPos().x <br>
     * double y = v.getPos().y = y
     * </code>
     * @return SumoPosition2D
     * @author Luca
     * */
    public SumoPosition2D getPos(){
        try {
            return (SumoPosition2D) _sumcon.do_job_get(Vehicle.getPosition(vehID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
