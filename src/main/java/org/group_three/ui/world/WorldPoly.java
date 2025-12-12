package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.WPolygon;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * The WorldPoly class is a class to render polygons in the world.
 * Main use is to render sumo polys such as buildings and more,
 * which are not needed for the actual simulation itself.
 *
 * @author Joel
 */
public class WorldPoly extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color of the poly which is being drawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color color;

	/**
	 * The shape of the poly to draw.
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
	public WorldPoly() {
		super();
		this.color = null;
		this.shape = new ArrayList<>();
		remove();
	}

	/**
	 * The default WorldPoly constructor to spawn a new WorldPoly in a world.
	 *
	 * @param world  The world to which the WorldPoly should be added.
	 * @param canvas The canvas of the world.
	 * @author Joel
	 */
	public WorldPoly(World world, Canvas canvas, WPolygon poly) {
		// call super and set the display name to the poly type
		super(world, canvas, poly.getType());

		// convert the sumo color value from the poly to a javafx color value and set it
		this.color = Meth.SumoClrToClr(poly.getColor());

		// set the shape of the poly, by first converting it to a relative shape
		this.shape = getRelativeShape(Meth.convertSumoCoords(poly.getShape().coords));
	}

	//--------------------------------------------------Constructors--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldPoly in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();

		if (UI.showPolys) drawPolygon(shape, color);
	}

	//--------------------------------------------------Methods--------------------------------------------------

}