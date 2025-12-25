package org.group_three.service.records;

import de.tudresden.sumo.objects.SumoColor;
import org.group_three.api.SimController;
import org.group_three.model.WVehicle;

import java.util.ArrayList;
import java.util.List;

/**<h2>VehicleRec</h2>
 * Record for holding Vehicle Data. Includes a collect method which is used
 * to collect necessary data.
 * @see WVehicle
 * @author Luca
 * */
public record VehicleRec(String vehID, double avgspeed, SumoColor color) {

    public static List<VehicleRec> collect(SimController simcon){

        List<VehicleRec> data = new ArrayList<>();

        for(WVehicle wveh : simcon.getAllVehicles().values()){

            VehicleRec vrec = new VehicleRec(
                    wveh.getID(),
                    wveh.getAvgSpeed(),
                    wveh.getColor()
            );

            data.add(vrec);

        }

        return data;
    }

}
