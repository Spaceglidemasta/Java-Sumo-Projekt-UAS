package org.group_three.utils;


import de.tudresden.sumo.objects.SumoPosition2D;

public class Sumo2DVector {
    public SumoPosition2D start;
    public SumoPosition2D end;

    public Sumo2DVector(SumoPosition2D start, SumoPosition2D end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "(" + start + " -> " + end + ")";
    }
}
