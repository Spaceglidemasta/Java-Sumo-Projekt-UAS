package org.group_three.ui;

import de.tudresden.sumo.objects.SumoColor;
import javafx.scene.paint.Color;

/**
 * Meth, the custom Math class
 *
 * @author Joel
 */
public class Meth {
	/**
	 * Just uses linear interpolation to lerp between two vectors.
	 *
	 * @param v0   Param-Comment
	 * @param v1   Param-Comment
	 * @param lerp Param-Comment
	 * @return Return-Comment
	 * @author Joel
	 */
	public static Vector2D lerp(Vector2D v0, Vector2D v1, double lerp) {
		return v0.add(v0.sub(v1).mul(lerp));
	}

	/**
	 * Just uses linear interpolation to lerp between two doubles.
	 *
	 * @param d0   Param-Comment
	 * @param d1   Param-Comment
	 * @param lerp Param-Comment
	 * @return Return-Comment
	 * @author Joel
	 */
	public static double lerp(double d0, double d1, double lerp) {
		return d0 + (d1 - d0) * lerp;
	}

	/**
	 * A method to get the relative location(vector) of two vectors in world (non-relative) space.
	 *
	 * @param location         Param-Comment
	 * @param rotation         Param-Comment
	 * @param relativeLocation Param-Comment
	 * @return Return-Comment
	 * @author Joel
	 * @see #addRelativeLocation(Vector2D location, double rotation, Vector2D relativeLocation)
	 */
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

	/**
	 * A method to add a relative location to a world space vector.
	 *
	 * @param location         Param-Comment
	 * @param rotation         Param-Comment
	 * @param relativeLocation Param-Comment
	 * @return Return-Comment
	 * @author Joel
	 * @see #getRelativeLocation(Vector2D location, double rotation, Vector2D relativeLocation)
	 */
	public static Vector2D addRelativeLocation(Vector2D location, double rotation, Vector2D relativeLocation) {
		double r = Math.toRadians(rotation); // Math.cos and Math.sin work with radians, but input rotation is in degrees
		double cosr = Math.cos(r);
		double sinr = Math.sin(r);
		double x = location.x + relativeLocation.x * cosr - relativeLocation.y * sinr; // Meth
		double y = location.y + relativeLocation.x * sinr + relativeLocation.y * cosr; // Meth

		return new Vector2D(x, y).round();
	}

	/**
	 * basically just does double - double right now, might change
	 *
	 * @param rotation         Param-Comment
	 * @param relativeRotation Param-Comment
	 * @return Return-Comment
	 * @author Joel
	 * @see #addRelativeRotation(double rotation, double relativeRotation)
	 */
	public static double getRelativeRotation(double rotation, double relativeRotation) {
		return rotation - relativeRotation;
	}

	/**
	 * basically just does double + double right now, might change
	 *
	 * @param rotation         Param-Comment
	 * @param relativeRotation Param-Comment
	 * @return Return-Comment
	 * @author Joel
	 * @see #getRelativeRotation(double rotation, double relativeRotation)
	 */
	public static double addRelativeRotation(double rotation, double relativeRotation) {
		return rotation + relativeRotation;
	}

	public static Color convertSumoColor(SumoColor sumoColor) {
		return new Color(
				byteToDouble(sumoColor.r),
				byteToDouble(sumoColor.g),
				byteToDouble(sumoColor.b),
				byteToDouble(sumoColor.a)
		);
	}

	public static double byteToDouble(byte input) {
		// byte to double conversion
		double value = (double) input / 255;

		// double clamp to 0 - 1
		if (value < 0) value = 0;
		else if (value > 1) value = 1;

		return value;
	}

	public static SumoColor convertColorToSumoColor(Color color) {
		return new SumoColor(
				doubleToByte(color.getRed()),
				doubleToByte(color.getGreen()),
				doubleToByte(color.getBlue()),
				doubleToByte(color.getOpacity())
		);
	}

	public static byte doubleToByte(double input) {
		//
		double value = input;

		// double clamp to 0 - 1
		if (value < 0) value = 0;
		else if (value > 1) value = 1;

		return (byte) Math.round(value * 255);
	}
}
