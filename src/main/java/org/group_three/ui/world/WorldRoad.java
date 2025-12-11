package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.model.SumoRoad;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

public class WorldRoad extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	Color color;
	SumoRoad sumoRoad;
	Vector2D from;
	Vector2D to;
	Vector2D size;

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
	public WorldRoad(World world, Canvas canvas, String displayName, Color color) {
		super(world, canvas, displayName);
		this.color = color;
	}

	/**
	 * The default WorldRoad constructor to spawn a new WorldRoad in a world.
	 *
	 * @param world       The world to which the WorldRoad should be added.
	 * @param canvas      The canvas of the world.
	 * @param displayName The display name which should show up on selection.
	 * @author Joel
	 */
	public WorldRoad(World world, Canvas canvas, String displayName, Color color, SumoRoad sumoRoad) {
		super(world, canvas, displayName);
		this.color = color;
		this.sumoRoad = sumoRoad;

		detailClassPath = "/org/group_three/ui/fxml/RoadDetails.fxml";

		from = new Vector2D(SimController.getMainsimcon().getJunctionPos(sumoRoad.getFrom()));
		to = new Vector2D(SimController.getMainsimcon().getJunctionPos(sumoRoad.getTo()));

		Vector2D a = Meth.getRelativeLocation(from, 0, to);
		setPosition(
				from.add(a.div(2))
		);
		setRotation(from.getDirectionAngle(to));
		size = new Vector2D(a.length()/2, 3);

		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size.add(new Vector2D(5,5)));
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++GetterClassMethods++++++++++++++++++++++++++++++++++++++++++++++++

	//------------------------------------------------GetterClassMethods------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------GetterMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++SetterClassMethods++++++++++++++++++++++++++++++++++++++++++++++++

	//------------------------------------------------SetterClassMethods------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------SetterMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++AdderClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++

	//------------------------------------------------AdderClassMethods-------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++AdderMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	//---------------------------------------------------AdderMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++RemoverMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	//--------------------------------------------------RemoverMethods--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	//---------------------------------------------------ClassMethods---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

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