package org.group_three.model;

import de.tudresden.sumo.cmd.Vehicle;
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

    public String getPos(){
        try {
            return (String) _sumcon.do_job_get(Vehicle.getPosition(vehID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
