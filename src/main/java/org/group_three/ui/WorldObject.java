package org.group_three.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;

import javafx.scene.image.Image;

// a class that represents an object in the 2d world subclasses should later be road parts, traffic lights, vehicles,...
// will be divided into static and dynamic for rendering efficiency
public class WorldObject {
	public Vector2D position = new Vector2D();
	public double rotation = 0;
	public double sphereCollision = 0;
	public Vector2D boxCollision = new Vector2D();
	public boolean useBoxCollision = false;
	public Image visualImage;
	public boolean interactable = false;
	public World world;
	public GraphicsContext graphicsContext;
	public Canvas renderTarget;

	public WorldObject() {
	}

	public WorldObject(double sphereCollision) {
		this.sphereCollision = sphereCollision;
	}

	public Vector2D getPosition() {
		return position;
	}

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

	public void addPosition(Vector2D position) {
		Vector2D pos = getPosition();
		pos.x += position.x;
		pos.y *= position.y;
		setPosition(pos);
	}

	public double getRotation() {
		return rotation;
	}

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

	public void addRotation(double rotation) {
		setRotation(getRotation() + rotation);
	}

	public void update() {
		Vector2D rect = new Vector2D(64,32);
		graphicsContext.save();
		graphicsContext.setFill(Color.BLUE);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		graphicsContext.restore();
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("Updated WorldObject");
	}
}
