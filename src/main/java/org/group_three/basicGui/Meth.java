package org.group_three.basicGui;

// Meth, the custom Math class
public class Meth {
	public static Vector2D lerp(Vector2D v0, Vector2D v1, double lerp) {
		return v0.add(v0.sub(v1).mul(lerp));
	}

	public static Vector2D getRelativeLocation(Vector2D location, double rotation, Vector2D relativeLocation) {
		double r = Math.toRadians(rotation); // Math.cos and Math.sin work with radians, but input rotation is in degrees
		double cosr = Math.cos(r);
		double sinr = Math.sin(r);

		double dx = relativeLocation.x - location.x;
		double dy = relativeLocation.y - location.y;

		double x = dx * cosr + dy * sinr;
		double y = -dx * sinr + dy * cosr;

		return new Vector2D(x, y).round(); // return of relative location
	}

	public static Vector2D addRelativeLocation(Vector2D location, double rotation, Vector2D relativeLocation) {
		double r = Math.toRadians(rotation); // Math.cos and Math.sin work with radians, but input rotation is in degrees
		double cosr = Math.cos(r);
		double sinr = Math.sin(r);
		double x = location.x + relativeLocation.x * cosr - relativeLocation.y * sinr; // Meth
		double y = location.y + relativeLocation.x * sinr + relativeLocation.y * cosr; // Meth

		return new Vector2D(x, y).round();
	}

	public static double getRelativeRotation(double rotation, double relativeRotation) {
		return 0;
	}

	public static double addRelativeRotation(double rotation, double relativeRotation) {
		return 0;
	}

}
