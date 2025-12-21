package org.group_three.model;

/**
 * <h1>WVehicleUpdateObject </h1>
 * An Update Class for the WVehicle Class. <br>
 * Its attributes are public & final, so you can determine
 * if an update was successful via <br>
 * <code> if(wvuo.pos == false) {Debug.print("Position update failed!");} </code> <br>
 * or just check all attributes with <code>wvuo.successful()</code>.
 * @author Luca
 * */
class WVehicleUpdateObject {
    public final boolean pos;
    public final boolean angle;
    public final boolean lane;
    public final boolean color;
    public final boolean speed;

    public WVehicleUpdateObject(boolean pos,
                                boolean angle,
                                boolean lane,
                                boolean color,
                                boolean speed){
        this.pos = pos;
        this.angle = angle;
        this.lane = lane;
        this.color = color;
        this.speed = speed;
    }

    public boolean successful() {

        if(!pos) return false;
        if(!angle) return false;
        if(!lane) return false;
        if(!color) return false;
        if(!speed) return false;

        return true;
    }
}