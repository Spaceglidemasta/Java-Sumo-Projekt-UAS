package org.group_three.basicGui.world;

import org.group_three.basicGui.Vector2D;
import org.group_three.debug.Debug;

import javafx.scene.image.Image;

public class WorldObject {
	private Vector2D position = new Vector2D();
	private double rotation = 0;
	private double sphereCollision = 0;
	private Vector2D boxCollision = new Vector2D();
	private boolean useBoxCollision = false;
	private Image visualImage;
	private boolean interactable = false;
	private World world;

	WorldObject() {
	}

	WorldObject(double sphereCollision) {
		this.sphereCollision = sphereCollision;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPostion(Vector2D position) {
		double worldSizeX = world.getWorldSize().x - sphereCollision / 2;
		if (position.x < worldSizeX) {
			position.x = worldSizeX;
		} else if (position.x > worldSizeX) {
			position.x = worldSizeX;
		}

		double worldSizeY = world.getWorldSize().y - sphereCollision / 2;
		if (position.y < worldSizeY) {
			position.y = worldSizeY;
		} else if (position.y > worldSizeY) {
			position.y = worldSizeY;
		}
	}

	public void addPosition(Vector2D position) {
		Vector2D pos = getPosition();
		pos.x += position.x;
		pos.y *= position.y;
		setPostion(pos);
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

		Debug.print(rotation);
	}

	public void addRotation(double rotation) {
		setRotation(getRotation() + rotation);
	}
}
