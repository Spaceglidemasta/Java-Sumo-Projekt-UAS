package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

import java.util.List;

/**
 * The WorldJunction class is a class that represents sumo junctions and
 * is displayed as a polygon.
 *
 * @author Joel
 */
public class WorldJunction extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color of the junction which is being drawn.
	 * Bound to be UI.roadColor.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color color = UI.roadColor;

	/**
	 * The shape of the junction to draw.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final List<Vector2D> shape;

	//--------------------------------------------------MemberVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public WorldJunction() {
		super();
		shape = null;
		remove();
	}

	/**
	 * The default WorldJunction constructor to spawn a new WorldJunction in a world.
	 *
	 * @param world       The world to which the WorldJunction should be added.
	 * @param canvas      The canvas of the world.
	 * @param displayName The display name which should show up on selection.
	 * @param junctionId  The sumo junction id.
	 * @author Joel
	 */
	public WorldJunction(World world, Canvas canvas, String displayName, String junctionId) {
		super(world, canvas, displayName);

		// set the position of the junction
		setPosition(new Vector2D(SimController.getMainsimcon().getJunctionPos(junctionId)));

		// set the shape of the junction
		shape = getRelativeShape(Meth.convertSumoCoords(SimController.getMainsimcon().getJunctionShape(junctionId)));
	}

	//--------------------------------------------------Constructors--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldJunction in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();

		drawPolygon(shape, color);
	}

	//--------------------------------------------------Methods--------------------------------------------------

}