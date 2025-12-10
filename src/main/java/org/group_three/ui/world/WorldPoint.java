package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

/**
 * The WorldPoint class is a simple class, which extends the WorldObject class.
 * It's only purpose is to easily be able to draw a dot in the simulation view.
 * Good for debugging, coordination visualization.
 *
 * @author Joel
 */
public class WorldPoint extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color of the point which is being drawn.
	 * @author Joel
	 */
	private Color color;

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
		remove();
	}

	/**
	 * The default WorldPoint constructor to spawn a new WorldPoint in a world.
	 *
	 * @param world       The world to which the WorldPoint should be added.
	 * @param canvas      The canvas of the world.
	 * @param displayName The display name which should show up on selection.
	 * @author Joel
	 */
	public WorldPoint(World world, Canvas canvas, String displayName, Color color) {
		super(world, canvas, displayName);
		this.color = color;
	}

	//--------------------------------------------------Constructors--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The getter methode for the color of the WorldPoint.
	 * @return The current color of the WorldPoint.
	 */
	public Color getColor() {
		return color;
	}

	//--------------------------------------------------GetterMethods--------------------------------------------------

	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The setter method for the color of the WorldPoint.
	 * @param color The new color of the WorldPoint.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	//--------------------------------------------------SetterMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldPoint in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		//super.update();
		Vector2D rect = new Vector2D(16, 16);
		getGraphicsContext().save();
		getGraphicsContext().setFill(color);
		Vector2D drawLoc = Meth.addRelativeLocation(getWorld().getViewerPosition(), getWorld().getViewerRotation(), getPosition().mul(getWorld().getViewerZoom()));

		getGraphicsContext().translate(drawLoc.x + getWorld().getViewerPositionOffset().x, drawLoc.y + getWorld().getViewerPositionOffset().y); // Object Location
		getGraphicsContext().rotate(Meth.addRelativeRotation(getWorld().getViewerRotation(), getRotation()));
		getGraphicsContext().fillOval((rect.x / 2) * getWorld().getViewerZoom() * -1, (rect.y / 2) * getWorld().getViewerZoom() * -1, rect.x * getWorld().getViewerZoom(), rect.y * getWorld().getViewerZoom());
		getGraphicsContext().restore();
	}

	//--------------------------------------------------Methods--------------------------------------------------

}