package org.group_three.model;

/**
 * <h1>WVehicleUpdateObject </h1>
 * An Update Class for the WVehicle Class. <br>
 * Its attributes are public & final, so you can determine
 * if an update was successfull via <br>
 * <code> if(wvuo.pos == false) {Debug.print("Position update failed!");} </code> <br>
 * or just check all attributes with <code>wvuo.successfull()</code>.
 * @author Luca
 * */
class WVehicleUpdateObject {
    public final boolean pos;
    public final boolean angle;

    public WVehicleUpdateObject(boolean pos, boolean angle){
        this.pos = pos;
        this.angle = angle;
    }

    public boolean successfull() {

        if(!pos) return false;
        if(!angle) return false;

        return true;
    }
}