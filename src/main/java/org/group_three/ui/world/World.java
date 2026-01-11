package org.group_three.ui.world;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import org.group_three.constants.UI;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.SimulationViewController;
import org.group_three.ui.controllers.MainWindowSimulationControlsController;
import org.group_three.ui.controllers.StatisticsAnalyticsController;

import java.util.*;
import java.util.logging.Logger;

/**
 * A class that represents the 2d world.
 *
 * @author Joel
 */
public class World {

	// Logger
	private static final Logger log = Logger.getLogger(World.class.getName());

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
	private final List<WorldObject> worldObjects = new ArrayList<>();


	/**
	 * The graphics context of the canvas which is being used to draw.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final GraphicsContext graphicsContext;

	/**
	 * The canvas to draw on.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Canvas renderTarget;

	/**
	 * A boolean if the world should update or not.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private boolean requestedUpdate = false;

	/**
	 * The update timer that triggers the update tick.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Timeline updateTimer;

	//-------------------------------------------------MemberVariables--------------------------------------------------

	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default constructor for thw World class.
	 * Disables play on the simulation controls, so it doesn't autoplay the simulation on world swap.
	 *
	 * @author Joel
	 */
	public World(Canvas renderTarget) {
		this.renderTarget = renderTarget;
		graphicsContext = renderTarget.getGraphicsContext2D();

		MainWindowSimulationControlsController.setPlay(false);
		SimulationViewController.rotationIndicatorStatic.setRotate(0);
		StatisticsAnalyticsController.clear();

		updateTimer = new Timeline(new KeyFrame(Duration.seconds(1 / UI.maxSimulationViewFps), _ -> updateTick()));
		updateTimer.setCycleCount(Timeline.INDEFINITE);
		updateTimer.play();

		log.info("Created new World.");
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++GetterSetterMethods++++++++++++++++++++++++++++++++++++++++++++++++

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
	@SuppressWarnings("unused")
	public Vector2D getWorldSize() {
		return worldSize;
	}

	/**
	 * Gets the world offset, to properly display non-zero center worlds.
	 *
	 * @return The world's bounds center offset.
	 * @author Joel
	 */
	@SuppressWarnings("unused")
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

	/**
	 * A method to get the worlds render target. (canvas)
	 *
	 * @return A canvas
	 * @author Joel
	 */
	public Canvas getRenderTarget() {
		return renderTarget;
	}


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

		// update the rotation indicator
		SimulationViewController.rotationIndicatorStatic.setRotate(360 - rotation);

		requestUpdate();
	}

	/**
	 * Sets the world viewer's zoom.
	 *
	 * @param zoom The zoom value as a double.
	 * @author Joel
	 */
	public void setViewerZoom(double zoom) {
		// it somehow works if though logically I would think it wouldn't
		// will stay like this as its working and trying to clean it up broke functionality for some reason
		if (zoom < viewerZoomLimit.x) {
			//noinspection UnusedAssignment
			zoom = viewerZoomLimit.x;
		} else if (zoom > viewerZoomLimit.y) {
			//noinspection UnusedAssignment
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
	 *
	 * @param position The position as a Vector2D.
	 * @author Joel
	 */
	public void setViewerPosition(Vector2D position) {
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
		setViewerPosition(getViewerPosition().add(position));
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


	/**
	 * Removes a world object to the world object list.
	 *
	 * @param object The world object to remove.
	 * @author Joel
	 */
	public void removeWorldObject(WorldObject object) {
		worldObjects.remove(object);
	}

	/**
	 * A method to get the update timer.
	 * @return Timeline
	 * @author Joel
	 */
	public Timeline getUpdateTimer() {
		return updateTimer;
	}

	//-----------------------------------------------GetterSetterMethods------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to request render updates.
	 *
	 * @author Joel
	 * @see #update()
	 */
	public void requestUpdate() {
		requestedUpdate = true;
	}

	/**
	 * A tick method to check and update the world.
	 * runs CONSTANTLY with a minimum delay of 1 second divided by UI.maxSimulationViewFps
	 *
	 * @author Joel
	 */
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
		// save graphics context
		graphicsContext.save();

		// use different background color in high contrast mode
		graphicsContext.setFill(UI.highContrast ? UI.worldHighContrastColor : UI.worldColor);

		// draw rect over the whole canvas, to clear every pixel
		graphicsContext.fillRect(0, 0, renderTarget.getWidth(), renderTarget.getHeight());

		// restore graphics context
		graphicsContext.restore();

		// update all world objects in the world
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
		// skip if there are no world objects in the world
		if (worldObjects.isEmpty()) return null;

		// check for collisions
		// --->

		List<Double> distances = new ArrayList<>();
		List<WorldObject> interactableObjects = new ArrayList<>();
		List<WorldObject> boxCollisionHits = new ArrayList<>();

		// reversed WorldObject list so you always select newer objects over older, so you can select cars over roads etc.
		// loop through every world object
		for (WorldObject worldObject : worldObjects.reversed()) {
			// skip world object check if not interactable
			if (!worldObject.isInteractable()) continue;

			// get distance between world object and interact position
			double distanceToObject = worldObject.getPosition().sub(worldPosition).length();

			// if interact position is inside the sphere collision (checked if distance less equal sphere collision radius)
			if (distanceToObject <= worldObject.getSphereCollision()) {

				// check if box collision is enabled
				if (worldObject.useBoxCollision()) { // if true -> check for box collision
					// BoxCollision
					// get absolute relative hit position
					Vector2D relativeHitPosition = Meth.getRelativeLocation(worldObject.getPosition(), worldObject.getRotation(), worldPosition);
					Vector2D relativeHalfHeightHit = relativeHitPosition.abs();

					// add only to box collision hit list if hit is inside of collision
					if (worldObject.getBoxCollision().x >= relativeHalfHeightHit.x &&
							worldObject.getBoxCollision().y >= relativeHalfHeightHit.y
					) {
						boxCollisionHits.add(worldObject);
					}

				} else { // if false -> add sphere collision
					// SphereCollision
					distances.add(distanceToObject);
					interactableObjects.add(worldObject);
				}
			}
		}
		// <---

		// if there is a box collision hit return the first object
		if (!boxCollisionHits.isEmpty()) return boxCollisionHits.getFirst();

		// if there are no hits return null
		if (interactableObjects.isEmpty() || distances.isEmpty()) return null;

		// if there are hits at this point in the code they will be sphere collisions
		// find nearest sphere collision in hit list and return
		double shortestDistance = distances.getFirst();
		int shortestDistanceIndex = 0;
		int index = 0;

		// loop through every sphere collision hit and adjust the shortest distance and the indexes
		for (double distance : distances) {
			if (distance < shortestDistance) {
				shortestDistance = distance;
				shortestDistanceIndex = index;
			}
			index++;
		}

		// return nearest sphere collision hit result to the interact position
		return interactableObjects.get(shortestDistanceIndex);
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}