package org.group_three.api;

import org.group_three.debug.Debug;

public class SimController {

    private SimSumo _simsumo;
    private SimTraci _simtraci;

    public SimController(){
        Debug.print("SimController invoked");

        this._simsumo = new SimSumo();
        this._simtraci = new SimTraci();
    }

}
