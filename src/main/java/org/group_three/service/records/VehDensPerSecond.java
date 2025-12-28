package org.group_three.service.records;

import org.group_three.api.SimController;
import org.group_three.constants.enums.AttributeStyle;
import org.group_three.debug.annotations.PrintStyle;
import org.group_three.model.WEdge;

import java.util.ArrayList;
import java.util.List;

/**<h2>VehDensPerSecond</h2>
 * Record for holding the Vehicle Density of each road per second.
 * @see WEdge
 * @author Luca
 * */
public record VehDensPerSecond(@PrintStyle(AttributeStyle.COLUMN) int ... vehicles_on_edges) {

    public static List<VehDensPerSecond> collect(SimController simcon){

        List<VehDensPerSecond> data = new ArrayList<>();


        for(int step = 0; step < simcon.getTime() - 1; step++){

            List<Integer> vehDenseThisStep = new ArrayList<>();

            for(WEdge wEdge : simcon.getAllroads().values()){

                vehDenseThisStep.add(wEdge.getVehDensityPerStep().get(step));

            }

            VehDensPerSecond densrec = new VehDensPerSecond(
                    vehDenseThisStep.stream().mapToInt(
                            i->i
                    ).toArray());

            data.add(densrec);
        }

        return data;
    }


}
