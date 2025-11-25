package org.group_three.ui.idkyet;

// The Vector2D class contains two double values x and y.
// Can be used to represent 2D world coordinates for example.
public class Vector2D {
	public double x = 0;
	public double y = 0;

	public Vector2D() {
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Vector2D + Vector2D
	public Vector2D add(Vector2D v) {
		return new Vector2D(x + v.x, y + v.y);
	}

	// Vector2D - Vector2D
	public Vector2D sub(Vector2D v) {
		return new Vector2D(x - v.x, y - v.y);
	}

	// Vector2D * Vector2D
	public Vector2D mul(double v) {
		return new Vector2D(x * v, y * v);
	}

	// Vector2D / Vector2D
	public Vector2D div(double v) {
		return new Vector2D(x / v, y / v);
	}

	// Convert Vector2D to string
	@Override
	public String toString() {
		return "Vector2D{" + "x=" + x + ", y=" + y + "}";
	}

	// Round the Vector2D's x and y value to 9 decimal places.
	public Vector2D round() {
		double x = Math.round(this.x * 1_000_000_000d) / 1_000_000_000d;
		double y = Math.round(this.y * 1_000_000_000d) / 1_000_000_000d;

		return new Vector2D(x, y);
	}

	// Returns the logical rotation in degrees of a two-dimensional vector.
	// Up is 0°, Right is 90°, Down is 180° and Left is 270°
	public double getRotation() {
		double rotation = Math.toDegrees(Math.atan2(-y, x)) + 90;

		if (rotation < 0) {
			rotation += 360;
		}

		return rotation;
	}

	// A function to return the Vector2D with a flipped y (-y) axis.
	// This is used to account for the y-axis of JavaFX which is -y by default.
	// (Moving downwards results in a positive y value in JavaFX, instead of a negative one.)
	public Vector2D flipY() {
		return new Vector2D(x, -y);
	}
}
