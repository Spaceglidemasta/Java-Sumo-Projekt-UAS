package org.group_three.ui.idkyet;

/**
 * The Vector2D class contains two double components: x and y.
 * Can be used to represent 2D world coordinates for example.
 *
 * @author Joel
 *
 * @see #Vector2D()
 * @see #Vector2D(double x, double y)
 */
public class Vector2D {
	public double x = 0;
	public double y = 0;

	/**
	 * The constructor to initialize the Vector2D with its x- and y-components being 0.
	 *
	 * @author Joel
	 *
	 * @see Vector2D
	 * @see #Vector2D(double x, double y)
	 */
	public Vector2D() {
	}

	/**
	 * The constructor to initialize the Vector2D with its x- and y-components.
	 *
	 * @author Joel
	 *
	 * @param x
	 * The x-component of the Vector2D.
	 *
	 * @param y
	 * The y-component of the Vector2D.
	 *
	 * @see Vector2D
	 * @see #Vector2D()
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * A method to mathematically add another Vector2D to the Vector2D.
	 * (Adds the components together.)
	 *
	 * @author Joel
	 *
	 * @param v
	 * The other Vector2D, which should be added.
	 *
	 * @return
	 * The result of the addition of both Vector2Ds.
	 *
	 * @see #sub(Vector2D v)
	 * @see #mul(double v)
	 * @see #div(double v)
	 */
	public Vector2D add(Vector2D v) {
		return new Vector2D(x + v.x, y + v.y);
	}

	/**
	 * A method to mathematically subtract another Vector2D from the Vector2D.
	 * (Subtracts the components.)
	 *
	 * @author Joel
	 *
	 * @param v
	 * The other Vector2D, which should be subtracted.
	 *
	 * @return
	 * The result of the subtraction of both Vector2Ds.
	 *
	 * @see #add(Vector2D v)
	 * @see #mul(double v)
	 * @see #div(double v)
	 */
	public Vector2D sub(Vector2D v) {
		return new Vector2D(x - v.x, y - v.y);
	}

	/**
	 * A method to mathematically multiply the Vector2D with a double.
	 * (Multiplies the components.)
	 *
	 * @author Joel
	 *
	 * @param v
	 * The double which the Vector2D should be multiplied with.
	 *
	 * @return
	 * The result of the multiplication.
	 *
	 * @see #sub(Vector2D v)
	 * @see #add(Vector2D v)
	 * @see #div(double v)
	 */
	public Vector2D mul(double v) {
		return new Vector2D(x * v, y * v);
	}

	/**
	 * A method to mathematically divide the Vector2D with a double.
	 * (Divides the components.)
	 *
	 * @author Joel
	 *
	 * @param v
	 * The double which the Vector2D should be divided with.
	 *
	 * @return
	 * The result of the division.
	 *
	 * @see #sub(Vector2D v)
	 * @see #add(Vector2D v)
	 * @see #mul(double v)
	 */
	public Vector2D div(double v) {
		return new Vector2D(x / v, y / v);
	}

	/**
	 * Overrides the toString() method to return the Vector2D properly as a string.
	 *
	 * @author Joel
	 *
	 * @return
	 * The Vector2D as a string.
	 */
	@Override
	public String toString() {
		return "Vector2D{x=" + x + ", y=" + y + "}";
	}

	/**
	 * A method to round the x- and y-components of the Vector2D to 9 decimal places.
	 * Should be used to correct the component values after mathematical operations.
	 * (because of the imprecise nature of doubles)
	 *
	 * @author Joel
	 *
	 * @return
	 * The Vector2D with its components rounded to 9 decimal places.
	 */
	public Vector2D round() {
		double x = Math.round(this.x * 1_000_000_000d) / 1_000_000_000d;
		double y = Math.round(this.y * 1_000_000_000d) / 1_000_000_000d;

		return new Vector2D(x, y);
	}

	/**
	 * A method to get the logical rotation of the Vector2D in degrees.
	 *
	 * @author Joel
	 *
	 * @return
	 * The rotation in degrees.
	 * Up is 0°, Right is 90°, Down is 180° and Left is 270°
	 */
	public double getRotation() {
		double rotation = Math.toDegrees(Math.atan2(-y, x)) + 90;

		if (rotation < 0) {
			rotation += 360;
		}

		return rotation;
	}

	/**
	 * A method to negate the y-component of a Vector2D.
	 * This is used to account for the y-axis of JavaFX which is -y by default.
	 * (Moving downwards results in a positive y value in JavaFX, instead of a negative one.)
	 *
	 * @author Joel
	 *
	 * @return
	 * The Vector2D with it's y-component negated.
	 *
	 * @see #negate()
	 */
	public Vector2D flipY() {
		return new Vector2D(x, -y);
	}

	/**
	 * A method to negate a Vector2D's x- and y-components.
	 *
	 * @author Joel
	 *
	 * @return
	 * The negated Vector2D.
	 *
	 * @see #flipY()
	 */
	public Vector2D negate() {
		return  new Vector2D(-x, -y);
	}

	/**
	 * A method to get the length of a Vector2D.
	 * √(x²+y²)
	 *
	 * @author Joel
	 *
	 * @return
	 * The vectors length as a double,
	 */
	public double length() {
		return Math.sqrt(x*x + y*y);
	}

	/**
	 * A method to rotate the Vector2D.
	 * The pivot point is 0|0.
	 *
	 * @author Joel
	 *
	 * @param degrees
	 * The rotation value in degrees, to rotate the Vector2D with.
	 *
	 * @return
	 * The rotated Vector2D object.
	 *
	 * @see #rotate(double degrees, Vector2D pivot)
	 */
	public Vector2D rotate(double degrees) {
		return this.rotate(degrees, new Vector2D());
	}

	/**
	 * A method to rotate the Vector2D around a given pivot point.
	 *
	 * @author Joel
	 *
	 * @param degrees
	 * The rotation value in degrees, to rotate the Vector2D with.
	 *
	 * @param pivot
	 * The pivot point to rotate the Vector2D around.
	 *
	 * @return
	 * The rotated Vector2D object.
	 *
	 * @see #rotate(double degrees)
	 */
	public Vector2D rotate(double degrees, Vector2D pivot) {
		return Meth.addRelativeLocation(pivot, degrees, Meth.getRelativeLocation(pivot, 0, this));
	}
}
