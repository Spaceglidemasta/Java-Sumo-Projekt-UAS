package org.group_three.utils;


import de.tudresden.sumo.objects.SumoPosition2D;

public class Sumo2DVector {
    public SumoPosition2D start;
    public SumoPosition2D end;

    public Sumo2DVector(double x, double y) {
        this.start = new SumoPosition2D(x, y);
        this.end = new SumoPosition2D(x, y);
    }

}
