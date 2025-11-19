package org.group_three.api;

import it.polito.appeal.traci.SumoTraciConnection;
import org.group_three.debug.Debug;


public class SimController {

    private SimSumo _simsumo;
    private SimTraci _simtraci;

    public SimController(){
        Debug.print("SimController invoked");

        try {
            SumoTraciConnection sumcon = new SumoTraciConnection(
                    "src/main/resources/sumo.exe",
                    "src/main/resources/net.net.xml",
                    "src/main/resources/net.rou.xml");

            sumcon.runServer(8813);

            for (int i = 0; i < 5; i++) {
                sumcon.do_timestep();
            }

            sumcon.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Debug.print("SimController finished");

    }





}
