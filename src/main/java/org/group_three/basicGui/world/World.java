package org.group_three.basicGui.world;

import org.group_three.basicGui.Vector2D;
import org.group_three.debug.Debug;

public class World {
	private Vector2D viewerPositionOffset = new Vector2D();
	private Vector2D viewerPosition = new Vector2D();
	private double viewerRotation = 0;
	private double viewerZoom = 1;
	private Vector2D viewerZoomLimit = new Vector2D(0.1, 2);
	private Vector2D worldSize = new Vector2D(1000, 500);
	private WorldObject[] worldObjects;

	World() {
	}

	public double getViewerRotation() {
		return viewerRotation;
	}

	public void setViewerRotation(double rotation) {
		viewerRotation = rotation;

		// Clamp rotation from 0 to 359.99...
		while (viewerRotation < 0) {
			viewerRotation += 360;
		}
		while (viewerRotation >= 360) {
			viewerRotation -= 360;
		}

		Debug.print(rotation);
	}

	public void addViewerRotation(double rotation) {
		setViewerRotation(getViewerRotation() + rotation);
	}

	public double getViewerZoom() {
		return viewerZoom;
	}

	public void setViewerZoom(double zoom) {
		if (zoom < viewerZoomLimit.x) {
			zoom = viewerZoomLimit.x;
		} else if (zoom > viewerZoomLimit.y) {
			zoom = viewerZoomLimit.y;
		}

		Debug.print(zoom);
	}

	public void addViewerZoom(double zoom) {
		setViewerZoom(getViewerZoom() + zoom);
	}

	public Vector2D getViewerPositionOffset() {
		return viewerPositionOffset;
	}

	public void setViewerPostionOffset(Vector2D positionOffset) {
		viewerPositionOffset = positionOffset;
	}

	public Vector2D getViewerPosition() {
		return viewerPosition;
	}

	public void setViewerPostion(Vector2D position) {
		if (position.x < worldSize.x) {
			position.x = worldSize.x;
		} else if (position.x > worldSize.x) {
			position.x = worldSize.x;
		}

		if (position.y < worldSize.y) {
			position.y = worldSize.y;
		} else if (position.y > worldSize.y) {
			position.y = worldSize.y;
		}
	}

	public void addViewerPosition(Vector2D position) {
		Vector2D pos = getViewerPosition();
		pos.x += position.x;
		pos.y *= position.y;
		setViewerPostion(pos);
	}

	public Vector2D getWorldSize() {
		return worldSize;
	}
}
