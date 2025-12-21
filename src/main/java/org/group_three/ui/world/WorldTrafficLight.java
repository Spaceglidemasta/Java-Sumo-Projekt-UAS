package org.group_three.ui.world;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WLink;
import org.group_three.model.WTrafficLight;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.TrafficLightDetailsController;

/**
 * The class to render TrafficLights.
 * Incomplete and only displays stop lines yet.
 *
 * @author Joel
 */
public class WorldTrafficLight extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	public WTrafficLight getwTrafficLight() {
		return wTrafficLight;
	}

	/**
	 * The WTrafficLight object which is grouping the WLink classes.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WTrafficLight wTrafficLight;

	public WLink getwLink() {
		return wLink;
	}

	/**
	 * The WLink object which owns this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WLink wLink;

	/**
	 * The size of the traffic lights stop line.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Vector2D size;
	private TrafficLightDetailsController trafficLightDetailsController;

	//--------------------------------------------------MemberVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public WorldTrafficLight() {
		super();
		this.wTrafficLight = null;
		this.wLink = null;
		this.size = new Vector2D();
		remove();
	}

	/**
	 * The default WorldTrafficLight constructor to spawn a new WorldTrafficLight in a world.
	 *
	 * @param world         The world to which the WorldRoad should be added.
	 * @param canvas        The canvas of the world.
	 * @param displayName   The display name which should show up on selection.
	 * @param wTrafficLight The WTrafficLight reference.
	 * @param wLink         The WLink reference.
	 * @author Joel
	 */
	public WorldTrafficLight(World world, Canvas canvas, String displayName, WTrafficLight wTrafficLight, WLink wLink) {
		super(world, canvas, displayName);
		this.wTrafficLight = wTrafficLight;
		this.wLink = wLink;

		setPosition(wLink.mid);
		setRotation(wLink.angle);

		size = new Vector2D(wLink.getLen(), wLink.getWidth());

		setInteractable(true);
		setSphereCollision(size.y / 2);
		detailClassPath = "/org/group_three/ui/fxml/TrafficLightDetails.fxml";

		/*wTrafficLight.getPhaseIndex();
		wLink.getTLIndex();
		wTrafficLight.getPhaseLen();
		wTrafficLight.setProgram();
		wTrafficLight.pr*/


		Debug.print(wTrafficLight.getProgram().programs.get(wTrafficLight.getProgramID()));

	}

	//--------------------------------------------------Constructors--------------------------------------------------

	/**
	 * The update method which is used to draw the WorldTrafficLight in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();

		Color color = Color.GRAY;
		try {
			color = Meth.SumoClrToClr(wLink.getColor());
		} catch (Exception e) {
			//throw new RuntimeException(e);
			color = Color.GRAY;
		}

		drawRectangle(size.div(2), color);
	}

	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		trafficLightDetailsController = fxmlLoader.getController();
		trafficLightDetailsController.setup(this);
	}

}