package org.group_three.model;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.api.SimController;

/**
 * <h1>WVehicle</h1>
 * A Wrapper Class for Vehicle which uses only the VehicleID to get and set values.
 * @author Luca
 * */
public class WVehicle {

    private final String vehID;
    private final SumoTraciConnection stc;


    //Attributes-Attributes
    private SumoPosition2D pos;
    private double angle;
    private int lane;

    /// Dont use this! >:(
    private WVehicle(){
        //Redundant bs to quiet intelliJ
        this.vehID = null;
        this.stc = null;
    }


    /**
     * Only constructor for this Class, invokes a new Vehicle with just the VehicleID
     *
     * @param id VehicleID
     * @author Luca
     *
     */
    public WVehicle(String id, SumoTraciConnection stc){
        this.vehID = id;
        this.stc = stc;
    }


    /**
     * Only constructor for this Class, invokes a new Vehicle with just the VehicleID
     *
     * @param id VehicleID
     * @author Luca
     *
     */
    public WVehicle(String id, SimController sumcon){
        this.vehID = id;
        this.stc = sumcon.getStc();
    }


    /**
     * Returns the Vehicle ID
     * @author Luca
     * */
    public String getID() {return vehID;}

    /**
     * Returns the Sumo Connection
     * @author Luca
     * */
    public SumoTraciConnection getSumoCon() {return stc;}

    /**
     * Returns the Angle
     * @author Luca
     * */
    public double getAngle() {return angle;}

    /**
     * Returns the index of the Lane. <br>
     * This is NOT the Lane ID. If you want the LaneID, try: <br>
     * <code>simcon.jobget(Vehicle.getLaneID(VehID))</code>
     * @author Luca
     * */
    public int getLane() {return lane;}

    /**
     * Gets you the SumoPosition2D of the Vehicle. Attributes are public.<br>
     * @example <code>
     * double x = v.getPos().x <br>
     * double y = v.getPos().y = y
     * </code>
     * @return SumoPosition2D
     * @author Luca
     * */
    public SumoPosition2D getPos(){ return pos;}


    /**
     * Removes the vehicle from the Simulation.
     * @param reason The reason for removing it. idk either
     * @return true if successfull, false if failed
     * @author Luca
     * */
    public boolean remove(byte reason){
        try {
            stc.do_job_set(Vehicle.remove(vehID, reason));
        }
        catch (Exception _){
            return false;
        }
        return true;
    }

    /**
     * Removes the vehicle from the Simulation.
     * @return true if successfull, false if failed
     * @author Luca
     * */
    public boolean remove(){
        try {
            stc.do_job_set(Vehicle.remove(vehID, (byte)0));
        }
        catch (Exception _){
            return false;
        }
        return true;
    }



    /**
     * Updates the attributes of the Vehicle via the Simulation. <br>
     * This should be done after every Simulation step.
     * @return VehicleUpdateObject - An object with public attributes indicating
     * if the getters were successfull.
     * @author Luca
     * */
    public WVehicleUpdateObject update() {

        boolean ipos = true;
        boolean iangle = true;
        boolean ilane = true;

        //position getter
        try {
            this.pos = (SumoPosition2D) stc.do_job_get(Vehicle.getPosition(vehID));
        }
        catch (Exception _) {
            ipos = false;
        }

        //angle getter
        try {
            this.angle = (double) stc.do_job_get(Vehicle.getAngle(vehID));
        }
        catch (Exception _) {
            iangle = false;
        }

        //lane getter
        try {
            this.lane = (int) stc.do_job_get(Vehicle.getLaneIndex(vehID));
        }
        catch (Exception _) {
            ilane = false;
        }

        return new WVehicleUpdateObject(ipos, iangle, ilane);
    }

}
