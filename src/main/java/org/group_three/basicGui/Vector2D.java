package org.group_three.basicGui;

public class Vector2D {
	public double x = 0;
	public double y = 0;

	public Vector2D() {
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D add(Vector2D v) {
		return new Vector2D(x + v.x, y + v.y);
	}

	public Vector2D sub(Vector2D v) {
		return new Vector2D(x - v.x, y - v.y);
	}

	public Vector2D mul(double v) {
		return new Vector2D(x * v, y * v);
	}
}
