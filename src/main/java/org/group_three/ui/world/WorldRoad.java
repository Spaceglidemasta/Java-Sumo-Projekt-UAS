package org.group_three.ui.world;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.model.WEdge;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.RoadDetailsController;

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
	private Color color;

	/**
	 * The wrapper class of the edge.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WEdge wEdge;

	/**
	 * The start point of the road.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D from;

	/**
	 * The end point of the road.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D to;

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
	public String id = "";

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("unused")
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
		this.id = id;
		this.wEdge = wEdge;

		detailClassPath = "/org/group_three/ui/fxml/RoadDetails.fxml";

		from = start;
		to = end;

		Vector2D a = Meth.getRelativeLocation(from, 0, to);
		setPosition(
				from.add(a.div(2))
		);
		setRotation(from.getDirectionAngle(to));

		//noinspection SuspiciousNameCombination
		size = new Vector2D(a.length() / 2, width);


		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size);
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public RoadDetailsController roadDetailsController;

	/**
	 * A method to set up the details panel when the object is selected.
	 *
	 * @param fxmlLoader The FXML loader of the loaded details panel.
	 * @author Joel
	 */
	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		roadDetailsController = fxmlLoader.getController();
		roadDetailsController.setup(this);
	}

	@Override
	public void deselect() {
		if (roadDetailsController != null) {
			roadDetailsController.deselect();
		}
	}

	/**
	 * The update method which is used to draw the WorldRoad in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		drawRectangle(size, UI.highContrast ? Color.WHITE : color);
	}

	/**
	 * Gets the wedge object from this class.
	 *
	 * @return The wEdge object.
	 * @author Joel
	 */
	public WEdge getwEdge() {
		return wEdge;
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}