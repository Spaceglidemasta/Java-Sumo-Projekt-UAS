package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.SimControlController;

import java.util.*;

/**
 * a class that represents the 2d world
 *
 * @author Joel
 */
public class World {
	/**
	 * The offset in pixel from the top left corner of the simulation view towards the center of the simulation view.
	 * The x and y components will always be positive.
	 *
	 * @author Joel
	 */
	private Vector2D viewerPositionOffset = new Vector2D();

	/**
	 * The position of the world viewer.
	 * Can also be described as the world offset towards the middle of the screen.
	 *
	 * @author Joel
	 */
	private Vector2D viewerPosition = new Vector2D(0, 0);

	/**
	 * The rotation of the world viewer. (0 to <360 in degrees)
	 *
	 * @author Joel
	 */
	private double viewerRotation = 0;

	/**
	 * The world viewers zoom.
	 * Will always be positive.
	 *
	 * @author Joel
	 */
	private double viewerZoom = 1;

	/**
	 * The zoom limit of the world viewers zoom.
	 * To limit how far the world view can be zoomed in and out.
	 *
	 * @author Joel
	 */
	private Vector2D viewerZoomLimit = new Vector2D(0.1, 10);

	/**
	 * The world size itself.
	 * Will be calculated based on the loaded simulation.
	 * Always positive.
	 *
	 * @author Joel
	 */
	private Vector2D worldSize = new Vector2D(512, 256);

	/**
	 * The list of WorldObjects in the world.
	 * Is used to decide what to render in the world for example.
	 *
	 * @author Joel
	 */
	private List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	/**
	 * The worlds base color.
	 * Visualizes the world bounds.
	 *
	 * @author Joel
	 */
	private Color worldColor = Color.GREY;

	/**
	 * The background color of the world view.
	 * Visualizes out of bounds.
	 *
	 * @author Joel
	 */
	private Color backgroundColor = Color.BLACK;


	/**
	 * @author Joel
	 */
	public GraphicsContext graphicsContext;

	/**
	 * @author Joel
	 */
	public Canvas worldStaticRenderTarget;


	private Vector2D worldOffset = new Vector2D();

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public World() {
		SimControlController.setPlay(false);
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public double getViewerRotation() {
		return viewerRotation;
	}

	/**
	 * Comment
	 *
	 * @param rotation Param-Comment
	 * @author Joel
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

		if (Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(rotation);

		requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @param rotation Param-Comment
	 * @author Joel
	 */
	public void addViewerRotation(double rotation) {
		setViewerRotation(getViewerRotation() + rotation);
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public double getViewerZoom() {
		return viewerZoom;
	}

	/**
	 * Comment
	 *
	 * @param zoom Param-Comment
	 * @author Joel
	 */
	public void setViewerZoom(double zoom) {
		if (zoom < viewerZoomLimit.x) {
			zoom = viewerZoomLimit.x;
		} else if (zoom > viewerZoomLimit.y) {
			zoom = viewerZoomLimit.y;
		} else {
			viewerZoom = zoom;
		}

		if (Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(zoom);

		requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @param zoom Param-Comment
	 * @author Joel
	 */
	public void addViewerZoom(double zoom) {
		setViewerZoom(getViewerZoom() + zoom);
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public Vector2D getViewerPositionOffset() {
		return viewerPositionOffset;
	}

	/**
	 * Comment
	 *
	 * @param positionOffset Param-Comment
	 * @author Joel
	 */
	public void setViewerPositionOffset(Vector2D positionOffset) {
		viewerPositionOffset = positionOffset;

		requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public Vector2D getViewerPosition() {
		return viewerPosition;
	}

	/**
	 * Comment
	 *
	 * @param position Param-Comment
	 * @author Joel
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
	 * @param position Param-Comment
	 * @author Joel
	 */
	public void addViewerPosition(Vector2D position) {
		Vector2D pos = getViewerPosition();
		pos.x += position.x;
		pos.y += position.y;
		setViewerPosition(pos);

		if (Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(getViewerPosition().x + " " + getViewerPosition().y);
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public Vector2D getWorldSize() {
		return worldSize;
	}

	public void setWorldSize(Vector2D worldSize) {
		this.worldSize = worldSize;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 * @see #update()
	 */
	public void requestUpdate() {
		update();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 * @see #requestUpdate()
	 */
	private void update() {
		graphicsContext.save();
		graphicsContext.setFill(backgroundColor);
		graphicsContext.fillRect(0, 0, worldStaticRenderTarget.getWidth(), worldStaticRenderTarget.getHeight());
		graphicsContext.restore();


		//graphicsContext.save();
		//graphicsContext.setFill(worldColor);
		//graphicsContext.translate(getViewerPosition().x + getViewerPositionOffset().x + getWorldOffset().x * getViewerZoom(), getViewerPosition().y + getViewerPositionOffset().y + getWorldOffset().y * getViewerZoom()); // Object Location
		//graphicsContext.rotate(getViewerRotation());
		//graphicsContext.fillRect((getWorldSize().x / 2) * getViewerZoom() * -1, (getWorldSize().y / 2) * getViewerZoom() * -1, getWorldSize().x * getViewerZoom(), getWorldSize().y * getViewerZoom());
		//graphicsContext.restore();

		for (WorldObject object : worldObjects) {
			object.update();
		}
	}

	public Vector2D getWorldOffset() {
		return worldOffset;
	}

	public void setWorldOffset(Vector2D worldOffset) {
		this.worldOffset = worldOffset;
	}

	public List<WorldObject> getWorldObjects() {
		return worldObjects;
	}

	public void setWorldObjects(List<WorldObject> worldObjects) {
		this.worldObjects = worldObjects;
	}

	/**
	 * Comment
	 *
	 * @param object Param-Comment
	 * @author Joel
	 */
	public void addWorldObject(WorldObject object) {
		worldObjects.add(object);
	}


	/**
	 * Comment
	 *
	 * @param object
	 * @author Joel
	 */
	public void removeWorldObject(WorldObject object) {
		worldObjects.remove(object);
	}

	/**
	 * Is missing a render check to only test objects that are currently rendered on the canvas. aka not outside of the frame
	 *
	 * @param worldPosition
	 * @return
	 * @author Joel
	 */
	public WorldObject interact(Vector2D worldPosition) {
		if (worldObjects.isEmpty()) return null;

		List<Double> distances = new ArrayList<>() {
		};
		List<WorldObject> interactableObjects = new ArrayList<>() {
		};
		List<WorldObject> boxCollisionHits = new ArrayList<>() {
		};

		for (WorldObject worldObject : worldObjects) {
			if (!worldObject.isInteractable()) continue;

			double distanceToObject = worldObject.getPosition().sub(worldPosition).length();

			if (distanceToObject <= worldObject.getSphereCollision()) {
				if (worldObject.useBoxCollision()) {
					Vector2D relativeHitPosition = Meth.getRelativeLocation(worldObject.getPosition(), worldObject.getRotation(), worldPosition);
					Vector2D relativeHalfHeightHit = relativeHitPosition.abs();
					Debug.print(relativeHalfHeightHit);

					// add only to box collision hit list if hit is inside of collision
					if (worldObject.getBoxCollision().x >= relativeHalfHeightHit.x &&
							worldObject.getBoxCollision().y >= relativeHalfHeightHit.y
					) {
						boxCollisionHits.add(worldObject);
						//Debug.print("BoxCollision");
					}

				} else {
					distances.add(distanceToObject);
					interactableObjects.add(worldObject);

					//Debug.print("SphereCollision");
				}
			}

			//Debug.print(distanceToObject);
			//Debug.print(boxCollisionHits.size());
		}

		if (!boxCollisionHits.isEmpty()) return boxCollisionHits.getFirst();

		if (interactableObjects.isEmpty() || distances.isEmpty()) return null;

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


	/**
	 * Is missing a render check to only test objects that are currently rendered on the canvas. aka not outside of the frame
	 *
	 * @param worldPosition
	 * @return
	 * @author Joel
	 */
		/*
	public WorldObject interact(Vector2D worldPosition) {
		if (worldObjects.isEmpty()) return null;

		List<Double> distances = new ArrayList<>() {
		};
		List<WorldObject> interactableObjects = new ArrayList<>() {
		};

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
	 */
}