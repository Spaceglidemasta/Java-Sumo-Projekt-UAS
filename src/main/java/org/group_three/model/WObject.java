package org.group_three.model;

import org.group_three.api.SimController;

/**
 * Parent Class for all Wrapper Classes except WLink & WVehicle.
 * @author Luca
 * */
public abstract class WObject {

    protected String id;
    protected final SimController simcon;


    public WObject(SimController simcon, String id){
        this.id = id;
        this.simcon = simcon;
    }

    public SimController getSimcon() {
        return simcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
