package org.group_three.ui.idkyet.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.group_three.ui.idkyet.Vector2D;
import org.group_three.debug.Debug;

import java.util.*;

public class World {
	private Vector2D viewerPositionOffset = new Vector2D();
	private Vector2D viewerPosition = new Vector2D();
	private double viewerRotation = 0;
	private double viewerZoom = 1;
	private Vector2D viewerZoomLimit = new Vector2D(0.1, 2);
	private Vector2D worldSize = new Vector2D(512, 256);
	private List<WorldObject> worldObjects = new ArrayList<WorldObject>();
	private Color worldColor = Color.GREY;
	private Color backgroundColor = Color.BLACK;
	public GraphicsContext graphicsContext;
	public Canvas worldStaticRenderTarget;

	public World() {
	}

	public double getViewerRotation() {
		return viewerRotation;
	}

	public void setViewerRotation(double rotation) {
		// Clamp rotation from 0 to 359.99...
		while (rotation < 0) {
			rotation += 360;
		}
		while (rotation >= 360) {
			rotation -= 360;
		}

		viewerRotation = rotation;

		Debug.print(rotation);

		requestUpdate();
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
		} else {
			viewerZoom = zoom;
		}

		Debug.print(zoom);

		requestUpdate();
	}

	public void addViewerZoom(double zoom) {
		setViewerZoom(getViewerZoom() + zoom);
	}

	public Vector2D getViewerPositionOffset() {
		return viewerPositionOffset;
	}

	public void setViewerPositionOffset(Vector2D positionOffset) {
		viewerPositionOffset = positionOffset;

		requestUpdate();
	}

	public Vector2D getViewerPosition() {
		return viewerPosition;
	}

	public void setViewerPosition(Vector2D position) {
		/*if (position.x < -(worldSize.x/2)) {
			position.x = -(worldSize.x/2);
		} else if (position.x > (worldSize.x/2)) {
			position.x = (worldSize.x/2);
		} else {
			viewerPosition.x = position.x;
		}

		if (position.y < -(worldSize.y/2)) {
			position.y = -(worldSize.y/2);
		} else if (position.y > (worldSize.y/2)) {
			position.y = (worldSize.y/2);
		} else {
			viewerPosition.y = position.y;
		}*/

		viewerPosition = position;

		requestUpdate();
	} // world bounds limit doesn't account for rotation yet, well or scale, disabled for now

	public void addViewerPosition(Vector2D position) {
		Vector2D pos = getViewerPosition();
		pos.x += position.x;
		pos.y += position.y;
		setViewerPosition(pos);

		Debug.print(getViewerPosition().x + " " + getViewerPosition().y);
	}

	public Vector2D getWorldSize() {
		return worldSize;
	}

	private void requestUpdate() {
		update();
	}

	private void update() {
		graphicsContext.save();
		graphicsContext.setFill(backgroundColor);
		graphicsContext.fillRect(0, 0, worldStaticRenderTarget.getWidth(), worldStaticRenderTarget.getHeight());
		graphicsContext.restore();


		graphicsContext.save();
		graphicsContext.setFill(worldColor);
		graphicsContext.translate(getViewerPosition().x + getViewerPositionOffset().x, getViewerPosition().y + getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(getViewerRotation());
		graphicsContext.fillRect((getWorldSize().x/2) * getViewerZoom() * -1, (getWorldSize().y/2) * getViewerZoom() * -1, getWorldSize().x * getViewerZoom(), getWorldSize().y * getViewerZoom());
		graphicsContext.restore();

		for (WorldObject object : worldObjects) {
			object.update();
		}
	}

	public void addWorldObject(WorldObject object) {
		worldObjects.add(object);
	}
}
