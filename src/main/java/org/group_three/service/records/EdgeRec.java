package org.group_three.service.records;

import org.group_three.api.SimController;
import org.group_three.model.WEdge;

import java.util.ArrayList;
import java.util.List;

/**<h2>EdgeRec</h2>
 * Record for holding Edge Data. Includes a collect method which is used
 * to collect necessary data.
 * @see WEdge
 * @author Luca
 * */
public record EdgeRec(String name, double usage, double length)  {

    public static List<EdgeRec> collect(SimController simcon){

        List<EdgeRec> data = new ArrayList<>();

        for(WEdge wEdge : simcon.getAllroads().values()){

            EdgeRec vrec = new EdgeRec(
                    wEdge.getName(),
                    wEdge.getOccupancyRatio(),
                    wEdge.getLength()
            );

            data.add(vrec);

        }

        return data;
    }


}
