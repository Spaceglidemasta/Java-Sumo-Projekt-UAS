package org.group_three.model;


import de.tudresden.sumo.cmd.Polygon;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoGeometry;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.api.SimController;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Polygon Wrapper Class</h1>
 *
 * */
public class WPolygon extends WObject {

    private SumoColor color;
    private SumoGeometry shape;
    private String type;
    private static List<WPolygon> allPolys;

    public WPolygon(SimController sc,String id, SumoColor clr, SumoGeometry sh, String ty){
        super(sc, id);

        color = clr;
        shape = sh;
        type = ty;
    }

    public String getId() {return id;}

    public static List<WPolygon> getAllPolys() {return allPolys;}

    /**
     * Loads all Polygons from the Main Simulation into allPolys
     * @return true of successfull, false if not.
     * @author Luca
     * */
    public static boolean loadAllPolys(SimController simcon){

        allPolys = new ArrayList<>();

        SumoStringList polys = simcon.getPolygonIDList();

        if(polys == null) return false;

        for(String pid : polys){

            SumoColor clr = (SumoColor) simcon.jobget(Polygon.getColor(pid));
            SumoGeometry sh = (SumoGeometry) simcon.jobget(Polygon.getShape(pid));
            String ty = (String) simcon.jobget(Polygon.getType(pid));

            WPolygon poly = new WPolygon(simcon, pid,  clr, sh, ty);
            allPolys.add(poly);
        }

        return true;
    }

    /**
     * Returns the Color of the Polygon in the Simulation
     * @return <code>SumoColor</code> of the Polygon
     * @author Luca
     * */
    public SumoColor getColor(){
        return color;
    }

    /**
     * Returns the Shape of the Polygon in the Simulation
     * @return <code>SumoGeometry</code> of the Polygon
     * @author Luca
     * */
    public SumoGeometry getShape(){
        return shape;
    }

    /**
     * Returns the Type of the Polygon in the Simulation
     * @return <code>Type</code>(String) of the Polygon
     * @author Luca
     * */
    public String getType(){
        return type;
    }


    /**
     * Returns the given Parameter of the Polygon in the Simulation<br>
     * Requires Casting.
     * @param param The parameter to be queried
     * @return <code>Parameter</code> of the Polygon
     * @author Luca
     * */
    public Object getParameter(String param){
        return simcon.jobget(Polygon.getParameter(id, param));
    }


    public void print() {
        System.out.println(id);
    }

    public static void printAll(){

        for(WPolygon wpoly : allPolys){
            wpoly.print();
        }

    }


}
