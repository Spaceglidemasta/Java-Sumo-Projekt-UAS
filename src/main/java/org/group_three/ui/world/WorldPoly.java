package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
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

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default color of the poly which is being drawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color defaultColor;

	/**
	 * The high contrast color of the poly which is being drawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color highContrastColor;

	/**
	 * The shape of the poly to draw.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final List<Vector2D> shape;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	public WorldPoly() {
		super();
		this.defaultColor = null;
		this.highContrastColor = null;
		this.shape = new ArrayList<>();
		remove();
	}

	/**
	 * The default WorldPoly constructor to spawn a new WorldPoly in a world.
	 *
	 * @param world  The world to which the WorldPoly should be added.
	 * @param canvas The canvas of the world.
	 * @param poly   The WPolygon wrapper object.
	 * @author Joel
	 */
	public WorldPoly(World world, Canvas canvas, WPolygon poly) {
		// call super and set the display name to the poly type
		super(world, canvas, poly.getType());

		// convert the sumo color value from the poly to a javafx color value and set it
		this.defaultColor = Meth.SumoClrToClr(poly.getColor());

		// convert the sumo color value from the poly to a javafx color value, adjust it for high contrast and set it
		Color c = Meth.SumoClrToClr(poly.getColor()).grayscale();
		this.highContrastColor = new Color(
				c.getRed() * 0.1,
				c.getGreen() * 0.1,
				c.getBlue() * 0.1,
				c.getOpacity()
		);


		// get poly shape points
		List<Vector2D> shapePoints = Meth.convertSumoCoords(poly.getShape().coords);

		// add all shape points together and then divide by shape count to get middle point
		Vector2D polyCenterPosition = new Vector2D();
		for (Vector2D vector2D : shapePoints) {
			polyCenterPosition = polyCenterPosition.add(vector2D);
		}
		polyCenterPosition = polyCenterPosition.div(shapePoints.size());

		// set WorldPoly position to center of poly
		setPosition(polyCenterPosition);

		// set the shape of the poly, by first converting it to a relative shape
		this.shape = getRelativeShape(Meth.convertSumoCoords(poly.getShape().coords));
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldPoly in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		// skip if polys are disabled
		if (!UI.showPolys) return;

		// draw the poly in grayscale with a 10% brightness adjustment if height contrast mode is active
		drawPolygon(shape, UI.highContrast ? highContrastColor : defaultColor);
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}