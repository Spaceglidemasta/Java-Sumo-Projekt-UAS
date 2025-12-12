package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * The WorldPoint class is a simple class, which extends the WorldObject class.
 * It's only purpose is to easily be able to draw a dot in the simulation view.
 * Good for debugging, coordination visualization.
 *
 * @author Joel
 */
@SuppressWarnings("unused")
public class WorldPoint extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color of the point which is being drawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color color;

	/**
	 * The radius of the WorldPoint.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final double radius;

	//--------------------------------------------------MemberVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public WorldPoint() {
		super();
		this.color = null;
		this.radius = 0;
		remove();
	}

	/**
	 * The default WorldPoint constructor to spawn a new WorldPoint in a world.
	 *
	 * @param world  The world to which the WorldPoint should be added.
	 * @param canvas The canvas of the world.
	 * @author Joel
	 */
	public WorldPoint(World world, Canvas canvas, Color color, double radius) {
		super(world, canvas, "WorldPoint");
		this.color = color;
		this.radius = radius;
	}

	//--------------------------------------------------Constructors--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldPoint in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();
		drawSphere(radius, color);
	}

	//--------------------------------------------------Methods--------------------------------------------------

}