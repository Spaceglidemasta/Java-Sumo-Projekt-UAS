package org.group_three.model;

import de.tudresden.sumo.objects.SumoColor;
import org.group_three.ui.Vector2D;

/**
 *
 * @author Leon
 */
public class WLink {


    public Vector2D mid;
    public double width;
    public double len;
    public double angle;
    public SumoColor color;

    public WLink(Vector2D mid, double width, double len, double angle, String laneID) {
        this.mid = mid;
        this.width = width;
        this.len = len;
        this.angle = angle;
        this.laneID = laneID;
    }

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
