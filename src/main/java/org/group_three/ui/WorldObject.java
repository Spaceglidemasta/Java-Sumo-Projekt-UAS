package org.group_three.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;

import javafx.scene.image.Image;

/**
 * a class that represents an object in the 2d world subclasses should later be road parts, traffic lights, vehicles,...
 * will be divided into static and dynamic for rendering efficiency
 *
 * @author Joel
 */
public class WorldObject {
	public Vector2D position = new Vector2D();
	public double rotation = 0;
	public double sphereCollision = 32; // Radius
	public Color sphereCollisionColor = new Color(1,0,0,0.1);
	public Vector2D boxCollision = new Vector2D();
	public boolean useBoxCollision = false;
	public Image visualImage;
	public Color imageTint = new Color(1, 1, 1, 1);
	public boolean interactable = false;
	public World world;
	public GraphicsContext graphicsContext;
	public Canvas renderTarget;
	public String displayName = "";
	public String id = "";
	public double worldSize = 1; // in meters

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldObject() {
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param sphereCollision
	 * Param-Comment
	 */
	public WorldObject(double sphereCollision) {
		this.sphereCollision = sphereCollision;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param position
	 * Param-Comment
	 */
	public void setPosition(Vector2D position) {
		/*double worldSizeX = world.getWorldSize().x - sphereCollision / 2;
		if (position.x < worldSizeX) {
			position.x = worldSizeX;
		} else if (position.x > worldSizeX) {
			position.x = worldSizeX;
		} else {

		}

		double worldSizeY = world.getWorldSize().y - sphereCollision / 2;
		if (position.y < worldSizeY) {
			position.y = worldSizeY;
		} else if (position.y > worldSizeY) {
			position.y = worldSizeY;
		}*/

		this.position = position;
		//this.position.y *= -1;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param position
	 * Param-Comment
	 */
	public void addPosition(Vector2D position) {
		Vector2D pos = getPosition();
		pos.x += position.x;
		pos.y *= position.y;
		setPosition(pos);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param rotation
	 * Param-Comment
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;

		// Clamp rotation from 0 to 359.99...
		while (this.rotation < 0) {
			this.rotation += 360;
		}
		while (this.rotation >= 360) {
			this.rotation -= 360;
		}

		Debug.toConsole(rotation);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param rotation
	 * Param-Comment
	 */
	public void addRotation(double rotation) {
		setRotation(getRotation() + rotation);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public void update() {
		drawCollision();
		Vector2D rect = new Vector2D(visualImage.getWidth(), visualImage.getHeight());
		rect = rect.div(10);
		graphicsContext.save();
		//graphicsContext.setFill(Color.BLUE);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.drawImage(addImageTint(visualImage), (rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		//graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		graphicsContext.restore();
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("Updated WorldObject");
	}

	public void drawCollision() {
		graphicsContext.save();
		graphicsContext.setFill(sphereCollisionColor);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillOval ((sphereCollision) * world.getViewerZoom() * -1, (sphereCollision) * world.getViewerZoom() * -1, sphereCollision*2 * world.getViewerZoom(), sphereCollision*2 * world.getViewerZoom());
		graphicsContext.restore();
	}
	/*
	public void drawCollision() {
		graphicsContext.save();
		graphicsContext.setFill(Color.RED);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillOval ((sphereCollision/2) * world.getViewerZoom() * -1, (sphereCollision/2) * world.getViewerZoom() * -1, sphereCollision * world.getViewerZoom(), sphereCollision * world.getViewerZoom());
		graphicsContext.restore();
	}*/

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
