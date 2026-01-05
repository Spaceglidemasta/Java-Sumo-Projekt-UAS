package org.group_three.constants;

import de.tudresden.sumo.objects.SumoColor;

public final class DefaultStasticValues {
    public final static String STATCOLLECTION_NAME = "Output_Collection";
    public final static String VEHSTAT_NAME = "VehicleData";
    public final static String EDGESTAT_NAME = "EdgeData";


    public final static String ATT_VEH_ID_NAME = "Vehicle ID";
    public final static String ATT_AVG_SPEED_NAME = "Average Speed";
    public final static String ATT_COLOR_NAME = "Color";
    ///  Red
    public final static SumoColor VEH_FILTER_COLOR = new SumoColor(255,0,0,255);

    public final static String ATT_EDGE_NAME_NAME = "Name";
    public final static String ATT_OCRATIO_NAME = "Occupancy Ratio";
    public final static String ATT_LENGTH_NAME = "Length";
}
