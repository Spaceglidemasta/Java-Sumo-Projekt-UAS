package org.group_three.api;

public class SimController {

    private SimSumo _simsumo;
    private SimTraci _simtraci;

    public SimController(){
        this._simsumo = new SimSumo();
        this._simtraci = new SimTraci();
    }

}
