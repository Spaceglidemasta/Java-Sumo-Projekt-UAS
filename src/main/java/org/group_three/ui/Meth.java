package org.group_three.ui;

import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Meth, the custom Math class
 *
 * @author Joel
 */
public class Meth {

	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Empty private constructor, to avoid construction,
	 * as the Meth class is a static class.
	 *
	 * @author Joel
	 */
	private Meth() {
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Just uses linear interpolation to lerp between two vectors.
	 *
	 * @param v0   Start value.
	 * @param v1   End value.
	 * @param lerp Lerp progress.
	 * @return The lerped Vector2D.
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public static Vector2D lerp(Vector2D v0, Vector2D v1, double lerp) {
		return v0.add(v0.sub(v1).mul(lerp));
	}

	/**
	 * Just uses linear interpolation to lerp between two doubles.
	 *
	 * @param d0   Start value.
	 * @param d1   End value.
	 * @param lerp Lerp progress.
	 * @return The lerped double.
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
	@SuppressWarnings("unused")
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


	/**
	 * Converts SumoColor to JavaFX's Color
	 *
	 * @param sumoColor SumoColor to be converted. (RGBA)
	 * @return Color, also in RGBA but normalized.
	 * @author Luca
	 *
	 */
	public static Color SumoClrToClr(SumoColor sumoColor) {
		return new Color(
				// &0xFF doesn't change numbers, but returns a signed byte:
				// (signed) 1111 1111 (=-1) => (signed) 0 1111 1111(=255)
				// then, JavaFX's Color needs values in [0:1] => / 255.0d
				(sumoColor.r & 0xFF) / 255.0d,
				(sumoColor.g & 0xFF) / 255.0d,
				(sumoColor.b & 0xFF) / 255.0d,
				(sumoColor.a & 0xFF) / 255.0d
		);
	}

	/**
	 * @param clr
	 * @return
	 * @author Luca
	 */
	public static SumoColor ClrToSumoClr(Color clr) {
		return new SumoColor(
				(int) (clr.getRed() * 255),
				(int) (clr.getGreen() * 255),
				(int) (clr.getBlue() * 255),
				(int) (clr.getOpacity() * 255)
		);
	}


	/**
	 * A method to convert Sumo Coordinates to a list of Vector2D's.
	 * Can be used to convert a SumoGeometry for example.
	 *
	 * @param sumoPosition2DS The input coordinate list.
	 * @return A list of Vector2D points.
	 * @author Joel
	 */
	public static List<Vector2D> convertSumoCoords(LinkedList<SumoPosition2D> sumoPosition2DS) {
		List<Vector2D> vector2DS = new ArrayList<>();

		for (SumoPosition2D sumoPosition2D : sumoPosition2DS) {
			vector2DS.add(new Vector2D(sumoPosition2D));
		}

		return vector2DS;
	}

	//---------------------------------------------------ClassMethods---------------------------------------------------

}