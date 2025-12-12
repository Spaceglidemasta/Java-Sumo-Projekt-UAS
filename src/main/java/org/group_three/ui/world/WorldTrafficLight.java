package org.group_three.ui.world;

import de.tudresden.sumo.objects.SumoPosition2D;
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

import java.util.LinkedList;
import java.util.List;

/**
 * @author Leon
 */
public class WorldTrafficLight extends WorldObject {
//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++


    //--------------------------------------------------MemberVariables--------------------------------------------------
    public Vector2D rechteck = new Vector2D(5,10);
    private WTrafficLight wtl;
    private Vector2D position;
    private double rotation;
    private Vector2D size;
    //++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++


    @SuppressWarnings("unused")
    public WorldTrafficLight() {
        super();
        remove();
    }


    public WorldTrafficLight(World world, Canvas canvas, String displayName, WTrafficLight trafficLight) {
        super(world, canvas, displayName);
        this.wtl = trafficLight;
        initGeometry(wtl.getID());
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

    private void initGeometry(String tlId) {
        // last point = position
        List<SumoPosition2D> lastPoints = wtl.getStopLinePoint(tlId);
        List<SumoPosition2D> secondPoints = wtl.getSecondToLast(tlId);

        if (lastPoints != null && !lastPoints.isEmpty() &&
                secondPoints != null && !secondPoints.isEmpty()) {

            SumoPosition2D last = lastPoints.get(0);
            SumoPosition2D second = secondPoints.get(0);

            Vector2D pLast = Meth.convertSumoCoords(new LinkedList<>(List.of(last))).get(0);
            Vector2D pSecond = Meth.convertSumoCoords(new LinkedList<>(List.of(second))).get(0);

            this.position = pLast;
            setPosition(position);
            // rotation from vector
            Vector2D dir = pLast.sub(pSecond);
            this.rotation = Math.toDegrees(Math.atan2(dir.y, dir.x));
            setRotation(rotation);
            // size from lane width
            //double laneWidth = SimController.getMainsimcon().getLaneWidth();
            //this.size = new Vector2D(laneWidth / 2.0, 3);
        }
    }

    @Override
    public void update() {
        super.update();
            drawRectangle(rechteck, Color.AQUA);
    }
}
