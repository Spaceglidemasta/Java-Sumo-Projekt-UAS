package org.group_three.basicGui;

public class Meth {
	public static Vector2D lerp(Vector2D v0, Vector2D v1, double lerp) {
		return v0.add(v0.sub(v1).mul(lerp));
	}
}
