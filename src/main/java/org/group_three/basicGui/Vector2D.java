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

	public Vector2D div(double v) {
		return new Vector2D(x / v, y / v);
	}

	@Override
	public String toString() {
		return "Vector2D{" + "x=" + x + ", y=" + y + "}";
	}

	public Vector2D round() {
		double x = Math.round(this.x * 1_000_000_000d) / 1_000_000_000d;
		double y = Math.round(this.y * 1_000_000_000d) / 1_000_000_000d;

		return new Vector2D(x, y);
	}

	public double getRotation() {
		double rotation = Math.toDegrees(Math.atan2(-y, x)) + 90;

		if (rotation < 0) {
			rotation += 360;
		}

		return rotation;
	}

	public Vector2D flipY() {
		return new Vector2D(x, -y);
	}
}
