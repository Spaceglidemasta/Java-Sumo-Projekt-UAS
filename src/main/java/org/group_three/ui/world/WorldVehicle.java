package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.ui.ColoredIconManager;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

public class WorldVehicle extends WorldObject {
	public static ColoredIconManager iconManager = new ColoredIconManager(UI.carIcon);

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldVehicle() {
		super();
		remove();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldVehicle(World world, Canvas canvas, String displayName) {
		super(world, canvas, displayName);
		setInteractable(true);
	}


	public Color getImageTint() {
		return imageTint;
	}

	public void setImageTint(Color imageTint) {
		this.imageTint = imageTint;
	}

	private Color imageTint = new Color(1, 1, 1, 1);
	private double worldSize = 1; // in meters


	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		drawCollision();
		Image visualImage = iconManager.getIcon(getImageTint());
		Vector2D rect = new Vector2D(visualImage.getWidth(), visualImage.getHeight());
		rect = rect.div(10);
		getGraphicsContext().save();
		//graphicsContext.setFill(Color.BLUE);
		Vector2D drawLoc = Meth.addRelativeLocation(getWorld().getViewerPosition(), getWorld().getViewerRotation(), getPosition().mul(getWorld().getViewerZoom()));

		getGraphicsContext().translate(drawLoc.x + getWorld().getViewerPositionOffset().x, drawLoc.y + getWorld().getViewerPositionOffset().y); // Object Location
		getGraphicsContext().rotate(Meth.addRelativeRotation(getWorld().getViewerRotation(), getRotation()));
		getGraphicsContext().drawImage(visualImage, (rect.x/2) * getWorld().getViewerZoom() * -1, (rect.y/2) * getWorld().getViewerZoom() * -1, rect.x * getWorld().getViewerZoom(), rect.y * getWorld().getViewerZoom());
		//graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		getGraphicsContext().restore();
	}
}
