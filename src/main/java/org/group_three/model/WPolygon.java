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
public class WPolygon {

    private String PoID;
    private SimController simcon;
    private SumoColor color;
    private SumoGeometry shape;
    private String type;
    private static List<WPolygon> allPolys;

    public WPolygon(String id, SimController sc, SumoColor clr, SumoGeometry sh, String ty){
        PoID = id;
        simcon = sc;
        color = clr;
        shape = sh;
        type = ty;
    }

    public String getPoID() {return PoID;}

    public static List<WPolygon> getAllPolys() {return allPolys;}

    /**
     * Loads all Polygons from the Main Simulation into allPolys
     * @return true of successfull, false if not.
     * @author Luca
     * */
    public static boolean loadAllPolys(){

        SimController sc = SimController.getMainsimcon();

        allPolys = new ArrayList<>();

        SumoStringList polys = sc.getPolygonIDList();

        if(polys == null) return false;

        for(String pid : polys){

            SumoColor clr = (SumoColor) sc.jobget(Polygon.getColor(pid));
            SumoGeometry sh = (SumoGeometry) sc.jobget(Polygon.getShape(pid));
            String ty = (String) sc.jobget(Polygon.getType(pid));

            WPolygon poly = new WPolygon(pid, sc, clr, sh, ty);
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
        return simcon.jobget(Polygon.getParameter(PoID, param));
    }


    public void print() {
        System.out.println(PoID);
    }

    public static void printAll(){

        for(WPolygon wpoly : allPolys){
            wpoly.print();
        }

    }


}
