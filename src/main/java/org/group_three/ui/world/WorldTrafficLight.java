package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;
import org.group_three.model.WLink;
import org.group_three.model.WTrafficLight;
import org.group_three.ui.Vector2D;

/**
 * The class to render TrafficLights.
 *
 * @author Joel
 */
public class WorldTrafficLight extends WorldObject {
//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++


    //--------------------------------------------------MemberVariables--------------------------------------------------

    private WTrafficLight wtl;
	private WLink wL;
	private Vector2D size = new Vector2D();

    //++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++


    @SuppressWarnings("unused")
    public WorldTrafficLight() {
        super();
        remove();
    }


	/**
	 * @param world A
	 * @param canvas A
	 * @param displayName A
	 * @param wTrafficLight A
	 * @param wLink A
	 * @author Joel
	 */
    public WorldTrafficLight(World world, Canvas canvas, String displayName, WTrafficLight wTrafficLight, WLink wLink) {
        super(world, canvas, displayName);
        this.wtl = wTrafficLight;
		this.wL = wLink;

		setPosition(wL.mid);
		setRotation(wL.angle);
		size = new Vector2D(wL.getLen(), wL.getWidth());
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

		drawSphere(2,Color.RED);
		drawRectangle(size.div(2), Color.ALICEBLUE);
    }
}
