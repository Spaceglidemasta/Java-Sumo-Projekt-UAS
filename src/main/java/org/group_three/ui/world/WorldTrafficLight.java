package org.group_three.ui.world;

import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.WTrafficLight;
import org.group_three.model.WVehicle;
import org.group_three.ui.ColoredIconManager;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.utils.Sumo2DLine;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Leon
 */
public class WorldTrafficLight extends WorldObject {
//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++


    //--------------------------------------------------MemberVariables--------------------------------------------------

    private WTrafficLight wtl;

    //++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++


    @SuppressWarnings("unused")
    public WorldTrafficLight() {
        super();
        remove();
    }


    public WorldTrafficLight(World world, Canvas canvas, String displayName, WTrafficLight trafficLight, int index) {
        super(world, canvas, displayName);
        this.wtl = trafficLight;

    }
    //--------------------------------------------------Constructors--------------------------------------------------


    //++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods++++++++++++++++++++++++++++++++++++++++++++++++++



    //--------------------------------------------------GetterMethods--------------------------------------------------

    //++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods++++++++++++++++++++++++++++++++++++++++++++++++++




    //--------------------------------------------------SetterMethods--------------------------------------------------


    //++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * The update method which is used to draw the WorldPoint in the world.
     *
     * @author Joel
     */
    @Override
    public void update() {
        super.update();

    }
}
