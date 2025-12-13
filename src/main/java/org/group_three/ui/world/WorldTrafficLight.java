package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import org.group_three.model.WTrafficLight;

/**
 * The class to render TrafficLights.
 *
 * @author Joel
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
     * The update method which is used to draw the WorldTrafficLight in the world.
     *
     * @author Joel
     */
    @Override
    public void update() {
        super.update();

    }
}
