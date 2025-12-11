package org.group_three.utils;


import de.tudresden.sumo.objects.SumoPosition2D;

/**
 * Called a vector but its just 2 points. <br>
 * used for straight Lines
 * @author Luca
 * */
public class Sumo2DLine {
    public SumoPosition2D start;
    public SumoPosition2D end;

    public Sumo2DLine(SumoPosition2D start, SumoPosition2D end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "(" + start + " -> " + end + ")";
    }
}
