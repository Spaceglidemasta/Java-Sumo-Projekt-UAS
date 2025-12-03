package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

public class WorldVehicle extends WorldObject {
	//public static ColoredIconManager iconManager = createCIM();

	/*private static ColoredIconManager createCIM() {
		return (iconManager == null) ? new ColoredIconManager(UI.carIcon) : null;
	}*/

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldVehicle() {
		super();
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

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		drawCollision();
		Vector2D rect = new Vector2D(getVisualImage().getWidth(), getVisualImage().getHeight());
		rect = rect.div(10);
		getGraphicsContext().save();
		//graphicsContext.setFill(Color.BLUE);
		Vector2D drawLoc = Meth.addRelativeLocation(getWorld().getViewerPosition(), getWorld().getViewerRotation(), getPosition().mul(getWorld().getViewerZoom()));

		getGraphicsContext().translate(drawLoc.x + getWorld().getViewerPositionOffset().x, drawLoc.y + getWorld().getViewerPositionOffset().y); // Object Location
		getGraphicsContext().rotate(Meth.addRelativeRotation(getWorld().getViewerRotation(), getRotation()));
		getGraphicsContext().drawImage(addImageTint(getVisualImage()), (rect.x/2) * getWorld().getViewerZoom() * -1, (rect.y/2) * getWorld().getViewerZoom() * -1, rect.x * getWorld().getViewerZoom(), rect.y * getWorld().getViewerZoom());
		//graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		getGraphicsContext().restore();
	}

	private Image addImageTint(Image input) {
		//GPT
		int w = (int) input.getWidth();
		int h = (int) input.getHeight();

		WritableImage tinted = new WritableImage(w, h);
		PixelReader reader = input.getPixelReader();
		PixelWriter writer = tinted.getPixelWriter();

		Color tintColor = imageTint;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Color c = reader.getColor(x, y);

				if (c.getOpacity() > 0) {
					Color newColor = new Color(
							tintColor.getRed(),
							tintColor.getGreen(),
							tintColor.getBlue(),
							c.getOpacity()
					);
					writer.setColor(x, y, newColor);
				} else {
					writer.setColor(x, y, c);
				}
			}
		}

		return  tinted;

	}
}
