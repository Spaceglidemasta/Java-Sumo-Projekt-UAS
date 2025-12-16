package org.group_three.model;

import de.tudresden.sumo.objects.SumoColor;
import org.group_three.ui.Vector2D;

/**
 * Wrapper class for individual links of Traffic lights <br>
 * Does not extend to WObject, because it's technically just an
 * Information holder, not a real wrapper for an Object in the
 * SUMO-Simulation.
 * @author Luca
 */
public class WLink {

    public final int TLIndex;
    public Vector2D mid;
    public double width;
    public double len;
    public double angle;
    public SumoColor color;

    public WLink(int TLIndex, Vector2D mid, double width, double len, double angle, String laneID) {
        this.TLIndex = TLIndex;
        this.mid = mid;
        this.width = width;
        this.len = len;
        this.angle = angle;
        this.laneID = laneID;
    }

    public int getTLIndex() {return TLIndex;}

    public String laneID;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLen() {
        return len;
    }

    public void setLen(double len) {
        this.len = len;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public SumoColor getColor() {
        return color;
    }

    public void setColor(SumoColor color) {
        this.color = color;
    }

}
