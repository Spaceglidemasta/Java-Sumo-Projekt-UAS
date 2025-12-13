package org.group_three.ui.world;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.model.WEdge;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.RoadDetailsController;

public class WorldRoad extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	Color color;
	public WEdge wEdge;
	Vector2D from;
	Vector2D to;
	Vector2D size;
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
	public WorldRoad(World world, Canvas canvas, String displayName, Color color, WEdge wEdge) {
		super(world, canvas, displayName);
		this.color = color;
		this.wEdge = wEdge;

		detailClassPath = "/org/group_three/ui/fxml/RoadDetails.fxml";

		from = new Vector2D(SimController.getMainsimcon().getJunctionPos(wEdge.getFrom()));
		to = new Vector2D(SimController.getMainsimcon().getJunctionPos(wEdge.getTo()));

		Vector2D a = Meth.getRelativeLocation(from, 0, to);
		setPosition(
				from.add(a.div(2))
		);
		setRotation(from.getDirectionAngle(to));

		size = new Vector2D(a.length()/2, 0);

		for (String laneId : wEdge.getLaneIDs()) {
			size.y += SimController.getMainsimcon().getLaneWidth(laneId);
		}

		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size.add(new Vector2D(5,5)));
	}

	/**
	 * The default WorldRoad constructor to spawn a new WorldRoad in a world.
	 *
	 * @param world       The world to which the WorldRoad should be added.
	 * @param canvas      The canvas of the world.
	 * @param displayName The display name which should show up on selection.
	 * @author Joel
	 */
	public WorldRoad(World world, Canvas canvas, String displayName, Color color, Vector2D start, Vector2D end, double width) {
		super(world, canvas, displayName);
		this.color = color;
		this.id = id;

		detailClassPath = "/org/group_three/ui/fxml/RoadDetails.fxml";

		from = start;
		to = end;

		Vector2D a = Meth.getRelativeLocation(from, 0, to);
		setPosition(
				from.add(a.div(2))
		);
		setRotation(from.getDirectionAngle(to));

		size = new Vector2D(a.length()/2, width);

		//for (String laneId : wEdge.getLaneIDs()) {
		//	size.y += SimController.getMainsimcon().getLaneWidth(laneId);
		//}

		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size);
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------GetterMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------SetterMethods---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	RoadDetailsController roadDetailsController;

	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		roadDetailsController = fxmlLoader.getController();
		roadDetailsController.setup(this);
	}

	/**
	 * The update method which is used to draw the WorldPoint in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();

		drawRectangle(size, color);
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}