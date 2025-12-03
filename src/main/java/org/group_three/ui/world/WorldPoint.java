package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

/**
 * @author Joel
 */
public class WorldPoint extends WorldObject {

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldPoint() {
		super();
		remove();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldPoint(World world, Canvas canvas, String displayName) {
		super(world, canvas, displayName);
	}


	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		drawCollision();
		Vector2D rect = new Vector2D(32, 32);
		getGraphicsContext().save();
		getGraphicsContext().setFill(Color.RED);
		Vector2D drawLoc = Meth.addRelativeLocation(getWorld().getViewerPosition(), getWorld().getViewerRotation(), getPosition().mul(getWorld().getViewerZoom()));

		getGraphicsContext().translate(drawLoc.x + getWorld().getViewerPositionOffset().x, drawLoc.y + getWorld().getViewerPositionOffset().y); // Object Location
		getGraphicsContext().rotate(Meth.addRelativeRotation(getWorld().getViewerRotation(), getRotation()));
		getGraphicsContext().fillOval((rect.x / 2) * getWorld().getViewerZoom() * -1, (rect.y / 2) * getWorld().getViewerZoom() * -1, rect.x * getWorld().getViewerZoom(), rect.y * getWorld().getViewerZoom());
		getGraphicsContext().restore();
	}
}
