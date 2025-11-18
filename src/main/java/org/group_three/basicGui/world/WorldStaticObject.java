package org.group_three.basicGui.world;

import org.group_three.basicGui.Vector2D;

import javafx.scene.image.Image;

public class WorldStaticObject {
	private Vector2D position = new Vector2D();
	private double rotation = 0;
	private double sphereCollision = 0;
	private Vector2D boxCollision = new Vector2D();
	private boolean useBoxCollision = false;
	private Image visualImage;
	private boolean interactable = false;

	WorldStaticObject() {}

	WorldStaticObject(double sphereCollision) {
		this.sphereCollision = sphereCollision;
	}



	//public boolean shouldDraw() {
	//	return true;
	//}
}
