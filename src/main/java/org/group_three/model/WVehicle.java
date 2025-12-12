package org.group_three.model;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
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
    private SumoColor color;
    private double speed;

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
    public SumoPosition2D getPos() { return pos;}

    /**
     * @return the Color in RGBA format. All values are public.
     * @author Luca
     * */
    public SumoColor getColor() {return color;}

    /**
     * Sets the Color of the Vehicle.
     * @param clr the SumoColor of the Vehicle
     * @return <code>true</code> if successfull, <code>false</code> if failed
     * */
    public boolean setColor(SumoColor clr) {
        try {
            stc.do_job_set(Vehicle.setColor(vehID, clr));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return speed as double.
     * @author Luca
     * */
    public double getSpeed() {return speed;}


    /**TraaS: "Returns the maximum speed(in m/s) of the named vehicle."
     * @return the MaxSpeed as double, or -1 if failed
     * @author Luca
     * */
    public double getMaxSpeed() {
        try {
            return (double) stc.do_job_get(Vehicle.getMaxSpeed(vehID));
        }
        catch (Exception e){
            e.printStackTrace();
            return -1.0d;
        }
    }

    /**TraaS: "Returns the standard deviation of the estimated maximum speed (see speed factor) divided by this speed."
     * @return the Speed Deviation as double, or -1 if failed
     * @author Luca
     * */
    public double getSpeedDeviation() {
        try {
            return (double) stc.do_job_get(Vehicle.getSpeedDeviation(vehID));
        }
        catch (Exception e){
            e.printStackTrace();
            return -1.0d;
        }
    }

    /**TraaS: "Returns the factor by which the driver multiplies the speed read from street signs to estimate "real" maximum allowed speed."
     * @return the Speed Factor as double, or -1 if failed
     * @author Luca
     * */
    public double getSpeedFactor() {
        try {
            return (double) stc.do_job_get(Vehicle.getSpeedFactor(vehID));
        }
        catch (Exception e){
            e.printStackTrace();
            return -1.0d;
        }
    }


    /**TraaS: "Returns the maximum allowed speed on the current lane regarding speed factor in m/s for this vehicle."
     * @return the Allowed Speed as double, or -1 if failed
     * @author Luca
     * */
    public double getAllowedSpeed() {
        try {
            return (double) stc.do_job_get(Vehicle.getAllowedSpeed(vehID));
        }
        catch (Exception e){
            e.printStackTrace();
            return -1.0d;
        }
    }



    /**TraaS: "Sets the speed (in m/s) of the named vehicle."
     * @param v Geschwindigkeit in m/s
     * @return <code>true</code> if successfull, <code>false</code> if failed
     * @author Luca
     * */
    public boolean setSpeed(double v) {
        try {
            stc.do_job_set(Vehicle.setSpeed(vehID, v));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**TraaS: "Sets the maximum speed (in m/s) of the named vehicle."
     * @param v Geschwindigkeit in m/s
     * @return <code>true</code> if successfull, <code>false</code> if failed
     * @author Luca
     * */
    public boolean setMaxSpeed(double v) {
        try {
            stc.do_job_set(Vehicle.setMaxSpeed(vehID, v));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**TraaS: "Sets the standard deviation of the estimated maximum speed."
     * @param v Geschwindigkeit in m/s
     * @return <code>true</code> if successfull, <code>false</code> if failed
     * @author Luca
     * */
    public boolean setSpeedDeviation(double v) {
        try {
            stc.do_job_set(Vehicle.setSpeedDeviation(vehID, v));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**TraaS: "
     Sets the factor by which the driver multiplies the speed read from street signs to estimate "real" maximum allowed speed."
     * @param v Geschwindigkeit in m/s
     * @return <code>true</code> if successfull, <code>false</code> if failed
     * @author Luca
     * */
    public boolean setSpeedFactor(double v) {
        try {
            stc.do_job_set(Vehicle.setSpeedFactor(vehID, v));
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes the vehicle from the Simulation.
     * @param reason The reason for removing it. idk either
     * @return true if successfull, false if failed
     * @author Luca
     * */
    public boolean remove(byte reason){
        try {
            stc.do_job_set(Vehicle.remove(vehID, reason));
            return true;
        }
        catch (Exception _){
            return false;
        }

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
     * Set Route of the Vehilce via a SumoStringList of edges
     * @param edges edges String list
     * @return true if successfull, false if not
     * @author Luca
     * */
    public boolean setRoute(SumoStringList edges){
        try {
            stc.do_job_set(Vehicle.setRoute(vehID, edges));
        }
        catch (Exception _){
            return false;
        }
        return true;
    }

    /**
     * Set Route of the Vehilce via a Route ID
     * @param RID Route iD
     * @return true if successfull, false if not
     * @author Luca
     * */
    public boolean setRoute(String RID){
        try {
            stc.do_job_set(Vehicle.setRouteID(vehID, RID));
        }
        catch (Exception _){
            return false;
        }
        return true;
    }


    /**
     * Get the RouteID of the Vehicle
     * @return RouteID
     * @author Luca
     * */
    public String getRouteID(){
        try {
            return (String) stc.do_job_get(Vehicle.getRouteID(vehID));
        }
        catch (Exception _){
            return null;
        }
    }

    /**
     * Get the Route Edges of the Vehicle
     * @return RouteID
     * @author Luca
     * */
    public SumoStringList getRouteEdges(){
        try {
            return (SumoStringList) stc.do_job_get(Vehicle.getRoute(vehID));
        }
        catch (Exception _){
            return null;
        }
    }

    /**
     * @return Validity of the Route of the Vehicle as String. Test this.
     * @author Luca
     * */
    public String isRouteValid(){
        try {
            return (String) stc.do_job_get(Vehicle.isRouteValid(vehID));
        }
        catch (Exception _){
            return null;
        }
    }

    /**
     * Move the Vehicle to a new Position.
     * @param LID Lane ID
     * @param pos The position of the Vehicle relative to the lane. I Assume this m from start of the lane.
     * @return <code>true</code> if successfull, <code>false</code> if not
     * @author Luca
     * */
    public boolean moveTo(String LID, double pos){
        try {
            stc.do_job_set(Vehicle.moveTo(vehID,LID, pos));
        }
        catch (Exception _){
            return false;
        }
        return true;
    }


    /**
     * Move the Vehicle to a new Lane
     * @param laneIndex lane index
     * @param duration duration of this proccess
     * @return <code>true</code> if successfull, <code>false</code> if not
     * @author Luca
     * */
    public boolean changeLane(byte laneIndex, double duration){
        try {
            stc.do_job_set(Vehicle.changeLane(vehID, laneIndex, duration));
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
        boolean icolor = true;
        boolean ispeed = true;

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

        //color getter
        try {
            this.color = (SumoColor) stc.do_job_get(Vehicle.getColor(vehID));
        } catch (Exception _) {
            icolor = false;
        }

        //speed getter
        try {
            this.speed = (double) stc.do_job_get(Vehicle.getSpeed(vehID));
        } catch (Exception _) {
            ispeed = false;
        }

        return new WVehicleUpdateObject(ipos,
                                        iangle,
                                        ilane,
                                        icolor,
                                        ispeed
        );
    }

}
