package org.group_three.api;

import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;


public class SimController {

    private SimSumo _simsumo;
    private SimTraci _simtraci;

    public SimController(){
        Debug.print("SimController invoked");

        SumoTraciConnection sumcon = new SumoTraciConnection(   "src/main/resources/sumo.exe",
                                                                "src/main/resources/net.net.xml",
                                                                "src/main/resources/net.rou.xml");

        Debug.print("SimController finished");

    }





}
