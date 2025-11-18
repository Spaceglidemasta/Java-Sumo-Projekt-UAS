package org.group_three.basicGui.world;

import org.group_three.basicGui.Vector2D;

public class WorldStaticObject {
	private double circleCollision = 0;
	private Vector2D boxCollision = new Vector2D();
	private boolean useBoxCollision = false;

	WorldStaticObject() {}

	WorldStaticObject(double circleCollision) {
		this.circleCollision = circleCollision;
	}



	public boolean shouldDraw() {
		return true;
	}
}
