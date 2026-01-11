package org.group_three.ui.world;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.model.WEdge;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.DetailsPanelRoadController;

/**
 * The class to represent roads in the world.
 *
 * @author Joel
 */
public class WorldRoad extends WorldObject {

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color of the road.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color color;

	/**
	 * The wrapper class of the edge.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WEdge wEdge;

	/**
	 * The size of the road.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D size;

	/**
	 * The sumo id of the road.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private String sumoId = "";

	/**
	 * The details panel controller for this object.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private DetailsPanelRoadController detailsPanelRoadController;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	public WorldRoad() {
		super();
		this.color = null;
		this.wEdge = null;
		remove();
	}

	/**
	 * The default WorldRoad constructor to spawn a new WorldRoad in a world.
	 *
	 * @param world       The world to which the WorldRoad should be added.
	 * @param canvas      The canvas of the world.
	 * @param displayName The display name which should show up on selection.
	 * @author Joel
	 */
	public WorldRoad(World world, Canvas canvas, String displayName, Color color, Vector2D start, Vector2D end, double width, String id, WEdge wEdge) {
		super(world, canvas, displayName);
		this.color = color;
		this.sumoId = id;
		this.wEdge = wEdge;

		setDetailClassPath("DetailsPanelRoad.fxml");

		// calculate and set center position of the road
		Vector2D a = Meth.getRelativeLocation(start, 0, end);
		setPosition(
				start.add(a.div(2))
		);

		// set road rotation based on start to end angle
		setRotation(start.getDirectionAngle(end));

		//noinspection SuspiciousNameCombination
		size = new Vector2D(a.length() / 2, width);

		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size);
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++GetterSetterMethods++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets the wedge object from this class.
	 *
	 * @return The wEdge object.
	 * @author Joel
	 */
	public WEdge getwEdge() {
		return wEdge;
	}

	/**
	 * Gets the sumo id from this object.
	 *
	 * @return The sumo id.
	 * @author Joel
	 */
	public String getSumoId() {
		return sumoId;
	}

	/**
	 * Gets the details panel controller form this object.
	 *
	 * @return The controller object.
	 * @author Joel
	 */
	public DetailsPanelRoadController getDetailsPanelRoadController() {
		return detailsPanelRoadController;
	}

	//-----------------------------------------------GetterSetterMethods------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to set up the details panel when the object is selected.
	 *
	 * @param fxmlLoader The FXML loader of the loaded details panel.
	 * @author Joel
	 */
	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		detailsPanelRoadController = fxmlLoader.getController();
		detailsPanelRoadController.setup(this);
	}

	/**
	 * A method that is being called when this object gets deselected.
	 *
	 * @author Joel
	 */
	@Override
	public void deselect() {
		// run deselect on the details panel controller
		if (detailsPanelRoadController != null) {
			detailsPanelRoadController.deselect();
		}
	}

	/**
	 * The update method which is used to draw the WorldRoad in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		// draw the road as a rectangle. use the color WHITE for the road if high contrast is enabled
		drawRectangle(size, UI.highContrast ? Color.WHITE : color);
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}