package org.group_three.utils;

import de.tudresden.sumo.objects.SumoPosition2D;
/**
 *
 * @author Leon
 */
public class TLStopLine {
    public String laneID;
    public SumoPosition2D point1;
    public SumoPosition2D point2;

    public TLStopLine(String laneID, SumoPosition2D point1, SumoPosition2D point2) {
        this.laneID = laneID;
        this.point1 = point1;
        this.point2 = point2;
    }

    public TLStopLine(SumoPosition2D point1, SumoPosition2D point2) {
        this.point1 = point1;
        this.point2 = point2;
        this.laneID = "null";
    }
}
