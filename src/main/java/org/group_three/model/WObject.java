package org.group_three.model;

import org.group_three.api.SimController;

/**
 * <h1>WObject</h1>
 * Parent Class for most Object-Wrapper-Classes.
 * @see WEdge
 * @see WTrafficLight
 * @see WPolygon
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
