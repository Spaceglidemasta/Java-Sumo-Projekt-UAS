package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;
import org.group_three.ui.Vector2D;

import java.util.*;

/**
 * a class that represents the 2d world
 *
 * @author Joel
 */
public class World {
	private Vector2D viewerPositionOffset = new Vector2D();
	private Vector2D viewerPosition = new Vector2D(0,0);
	private double viewerRotation = 0;
	private double viewerZoom = 1;
	private Vector2D viewerZoomLimit = new Vector2D(0.1, 2);
	private Vector2D worldSize = new Vector2D(512, 256);
	private List<WorldObject> worldObjects = new ArrayList<WorldObject>();
	private Color worldColor = Color.GREY;
	private Color backgroundColor = Color.BLACK;
	public GraphicsContext graphicsContext;
	public Canvas worldStaticRenderTarget;

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public World() {
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public double getViewerRotation() {
		return viewerRotation;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param rotation
	 * Param-Comment
	 */
	public void setViewerRotation(double rotation) {
		// Clamp rotation from 0 to 359.99...
		while (rotation < 0) {
			rotation += 360;
		}
		while (rotation >= 360) {
			rotation -= 360;
		}

		viewerRotation = rotation;

        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(rotation);

		requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param rotation
	 * Param-Comment
	 */
	public void addViewerRotation(double rotation) {
		setViewerRotation(getViewerRotation() + rotation);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public double getViewerZoom() {
		return viewerZoom;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param zoom
	 * Param-Comment
	 */
	public void setViewerZoom(double zoom) {
		if (zoom < viewerZoomLimit.x) {
			zoom = viewerZoomLimit.x;
		} else if (zoom > viewerZoomLimit.y) {
			zoom = viewerZoomLimit.y;
		} else {
			viewerZoom = zoom;
		}

        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(zoom);

		requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param zoom
	 * Param-Comment
	 */
	public void addViewerZoom(double zoom) {
		setViewerZoom(getViewerZoom() + zoom);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public Vector2D getViewerPositionOffset() {
		return viewerPositionOffset;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param positionOffset
	 * Param-Comment
	 */
	public void setViewerPositionOffset(Vector2D positionOffset) {
		viewerPositionOffset = positionOffset;

		requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public Vector2D getViewerPosition() {
		return viewerPosition;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param position
	 * Param-Comment
	 */
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

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param position
	 * Param-Comment
	 */
	public void addViewerPosition(Vector2D position) {
		Vector2D pos = getViewerPosition();
		pos.x += position.x;
		pos.y += position.y;
		setViewerPosition(pos);

        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(getViewerPosition().x + " " + getViewerPosition().y);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public Vector2D getWorldSize() {
		return worldSize;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @see #update()
	 */
	public void requestUpdate() {
		update();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @see #requestUpdate()
	 */
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

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param object
	 * Param-Comment
	 */
	public void addWorldObject(WorldObject object) {
		worldObjects.add(object);
	}


	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param object
	 */
	public void removeWorldObject(WorldObject object) {
		worldObjects.remove(object);
	}

	/**
	 * Is missing a render check to only test objects that are currently rendered on the canvas. aka not outside of the frame
	 * @author Joel
	 * @param worldPosition
	 * @return
	 */
	public WorldObject interact(Vector2D worldPosition) {
		if (worldObjects.isEmpty()) return null;

		List<Double> distances = new ArrayList<>() {};
		List<WorldObject> interactableObjects = new ArrayList<>() {};

		for (WorldObject worldObject : worldObjects) {
			if (!worldObject.isInteractable()) continue;

			double distanceToObject = worldObject.getPosition().sub(worldPosition).length();

			if (distanceToObject <= worldObject.getSphereCollision()) {
				distances.add(distanceToObject);
				interactableObjects.add(worldObject);
			}

			Debug.print(distanceToObject);
		}

		if (interactableObjects.isEmpty()) return null;
		if (distances.isEmpty()) return null;

		double shortestDistance = distances.getFirst();
		int shortestDistanceIndex = 0;
		int index = 0;

		for (double distance : distances) {
			if (distance < shortestDistance) {
				shortestDistance = distance;
				shortestDistanceIndex = index;
			}
			index++;
		}

		return interactableObjects.get(shortestDistanceIndex);
	}
}