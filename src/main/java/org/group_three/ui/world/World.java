package org.group_three.ui.world;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.CanvasController;
import org.group_three.ui.controllers.SimControlController;

import java.util.*;

/**
 * A class that represents the 2d world.
 *
 * @author Joel
 */
public class World {

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The offset in pixel from the top left corner of the simulation view towards the center of the simulation view.
	 * The x and y components will always be positive.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D viewerPositionOffset = new Vector2D();

	/**
	 * The position of the world viewer.
	 * Can also be described as the world offset towards the middle of the screen.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D viewerPosition = new Vector2D(0, 0);

	/**
	 * The rotation of the world viewer. (0 to <360 in degrees)
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private double viewerRotation = 0;

	/**
	 * The world viewers zoom.
	 * Will always be positive.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private double viewerZoom = 1;

	/**
	 * The zoom limit of the world viewers zoom.
	 * To limit how far the world view can be zoomed in and out.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Vector2D viewerZoomLimit = UI.zoomLimit;

	/**
	 * The world size itself.
	 * Will be calculated based on the loaded simulation.
	 * Always positive.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D worldSize = new Vector2D(512, 256);

	/**
	 * The world offset to display non-zero center worlds properly.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D worldOffset = new Vector2D();

	/**
	 * The list of WorldObjects in the world.
	 * Is used to decide what to render in the world for example.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	/**
	 * The worlds base color.
	 * Visualizes the world bounds.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color worldColor = UI.worldColor;

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * The background color of the world view.
	 * Visualizes out of bounds.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Color backgroundColor = UI.worldColor;


	/**
	 * The graphics context of the canvas which is being used to drawn on it.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	public GraphicsContext graphicsContext;

	/**
	 * The canvas to draw on.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	public Canvas worldStaticRenderTarget;

	//-------------------------------------------------MemberVariables--------------------------------------------------

	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default constructor for thw World class.
	 * Disables play on the simulation controls, so it doesn't autoplay the simulation on world swap.
	 *
	 * @author Joel
	 */
	public World() {
		SimControlController.setPlay(false);
		CanvasController.rotationIndicatorStatic.setRotate(0);

		timeline = new Timeline(new KeyFrame(Duration.seconds(1/UI.maxSimulationViewFps), e -> updateTick()) );
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets the world viewers current rotation.
	 *
	 * @return The rotation in degrees. (0 to <360)
	 * @author Joel
	 */
	public double getViewerRotation() {
		return viewerRotation;
	}

	/**
	 * Gets the world viewer's current zoom.
	 *
	 * @return The zoom value as a double.
	 * @author Joel
	 */
	public double getViewerZoom() {
		return viewerZoom;
	}

	/**
	 * Gets the current world viewer's position offset.
	 * Aka the offset from the top left corner of the canvas towards the center of it.
	 *
	 * @return The offset as a Vector2D.
	 * @author Joel
	 */
	public Vector2D getViewerPositionOffset() {
		return viewerPositionOffset;
	}

	/**
	 * Gets the world viewer's position.
	 *
	 * @return The position as a Vector2D.
	 * @author Joel
	 */
	public Vector2D getViewerPosition() {
		return viewerPosition;
	}

	/**
	 * Gets the world (bound) size.
	 *
	 * @return The world size as a half Vector2D.
	 * @author Joel
	 */
	public Vector2D getWorldSize() {
		return worldSize;
	}

	/**
	 * Gets the world offset, to properly display non-zero center worlds.
	 *
	 * @return The world's bounds center offset.
	 * @author Joel
	 */
	public Vector2D getWorldOffset() {
		return worldOffset;
	}

	/**
	 * Gets the world object list.
	 *
	 * @return The world object.
	 * @author Joel
	 */
	public List<WorldObject> getWorldObjects() {
		return worldObjects;
	}

	//--------------------------------------------------GetterMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Sets the world viewers current rotation.
	 *
	 * @param rotation The rotation in degrees. (0 to <360)
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
		CanvasController.rotationIndicatorStatic.setRotate(360-rotation);

		requestUpdate();
	}

	/**
	 * Sets the world viewer's zoom.
	 *
	 * @param zoom The zoom value as a double.
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

		requestUpdate();
	}

	/**
	 * Sets the world viewer's offset.
	 *
	 * @param positionOffset The offset as a Vector2D.
	 * @author Joel
	 */
	public void setViewerPositionOffset(Vector2D positionOffset) {
		viewerPositionOffset = positionOffset;

		requestUpdate();
	}

	/**
	 * Sets the world viewer's position.
	 * <br> world bounds limit doesn't account for rotation yet, well or scale, disabled for now
	 *
	 * @param position The position as a Vector2D.
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
	}

	/**
	 * Sets the world (bound) size.
	 *
	 * @param worldSize The new half world size.
	 * @author Joel
	 */
	public void setWorldSize(Vector2D worldSize) {
		this.worldSize = worldSize;
	}

	/**
	 * Sets the world offset, to properly display non-zero center worlds.
	 *
	 * @param worldOffset The world offset as a Vector2D.
	 * @author Joel
	 */
	public void setWorldOffset(Vector2D worldOffset) {
		this.worldOffset = worldOffset;
	}

	/**
	 * Sets the world object list.
	 *
	 * @param worldObjects The new world object list.
	 * @author Joel
	 */
	public void setWorldObjects(List<WorldObject> worldObjects) {
		this.worldObjects = worldObjects;
	}

	//--------------------------------------------------SetterMethods---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++AdderMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Add a rotation value to the world viewer's rotation.
	 *
	 * @param rotation The rotation in degrees.
	 * @author Joel
	 */
	public void addViewerRotation(double rotation) {
		setViewerRotation(getViewerRotation() + rotation);
	}

	/**
	 * Adds a zoom value to the current world viewer's zoom.
	 *
	 * @param zoom The zoom amount to increase as a double.
	 * @author Joel
	 */
	public void addViewerZoom(double zoom) {
		setViewerZoom(getViewerZoom() + zoom);
	}

	/**
	 * Adds a world viewer's position.
	 *
	 * @param position The offset position as a Vector2D.
	 * @author Joel
	 */
	public void addViewerPosition(Vector2D position) {
		Vector2D pos = getViewerPosition();
		pos.x += position.x;
		pos.y += position.y;
		setViewerPosition(pos);
	}

	/**
	 * Add's a world object to the world object list.
	 *
	 * @param object The world object to add.
	 * @author Joel
	 */
	public void addWorldObject(WorldObject object) {
		worldObjects.add(object);
	}

	//---------------------------------------------------AdderMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++RemoverMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Removes a world object to the world object list.
	 *
	 * @param object The world object to remove.
	 * @author Joel
	 */
	public void removeWorldObject(WorldObject object) {
		worldObjects.remove(object);
	}

	//--------------------------------------------------RemoverMethods--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to request render updates.
	 * Not really implemented yet and just forwards the request directly to render it.
	 *
	 * @author Joel
	 * @see #update()
	 */
	public void requestUpdate() {
		requestedUpdate = true;
	}



	private boolean requestedUpdate = false;
	public final Timeline timeline;

	private void updateTick() {
		// skip if no update is requested
		if (!requestedUpdate) return;

		// update canvas
		update();

		// set requestUpdate to false after updating
		requestedUpdate = false;
	}


	/**
	 * A method to render the world bounds and background.
	 *
	 * @author Joel
	 * @see #requestUpdate()
	 */
	private void update() {
		graphicsContext.save();
		graphicsContext.setFill(UI.highContrast ? Color.BLACK : backgroundColor);
		graphicsContext.fillRect(0, 0, worldStaticRenderTarget.getWidth(), worldStaticRenderTarget.getHeight());
		graphicsContext.restore();

		for (WorldObject object : worldObjects) {
			object.update();
		}
	}

	/**
	 * A method to interact/select a world object when clicking into the simulation view.
	 * Is missing a render check to only test objects that are currently rendered on the canvas. aka not outside the frame
	 *
	 * @param worldPosition The interacted world position.
	 * @return The hit world object.
	 * @author Joel
	 */
    @MayReturnNull
	public WorldObject interact(Vector2D worldPosition) {
		if (worldObjects.isEmpty()) return null;

		List<Double> distances = new ArrayList<>();
		List<WorldObject> interactableObjects = new ArrayList<>();
		List<WorldObject> boxCollisionHits = new ArrayList<>();

		// reversed WorldObject list so you always select newer objects over older, so you can select cars over roads etc
		for (WorldObject worldObject : worldObjects.reversed()) {
			if (!worldObject.isInteractable()) continue;

			double distanceToObject = worldObject.getPosition().sub(worldPosition).length();

			if (distanceToObject <= worldObject.getSphereCollision()) {
				if (worldObject.useBoxCollision()) {
					Vector2D relativeHitPosition = Meth.getRelativeLocation(worldObject.getPosition(), worldObject.getRotation(), worldPosition);
					Vector2D relativeHalfHeightHit = relativeHitPosition.abs();
					//Debug.print(relativeHalfHeightHit);

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

	//-----------------------------------------------------Methods------------------------------------------------------

}