package org.group_three.ui.world;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;

import javafx.scene.image.Image;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

/**
 * a class that represents an object in the 2d world subclasses should later be road parts, traffic lights, vehicles,...
 * will be divided into static and dynamic for rendering efficiency
 *
 * @author Joel
 */
public class WorldObject {
	/**
	 * @author Joel
	 */
	private final World world;
	/**
	 * @author Joel
	 */
	private final Canvas renderTarget;
	/**
	 * is the same across all canvas users
	 *
	 * @author Joel
	 */
	private final GraphicsContext graphicsContext;

	/**
	 * not unique
	 *
	 * @author Joel
	 */
	private final String displayName;
	/**
	 * unique
	 *
	 * @author Joel
	 */
	private final String id;
	/**
	 * keeps track of all instances ever created, will always be the count of created objects and not the index of the last created object
	 *
	 * @author Joel
	 */
	private static int idCounter = 0;

	/**
	 * @author Joel
	 */
	private Vector2D position = new Vector2D();
	/**
	 * @author Joel
	 */
	private double rotation = 0;

	/**
	 * @author Joel
	 */
	private boolean interactable = false;
	/**
	 * Radius in meters?
	 *
	 * @author Joel
	 */
	private double sphereCollision = 32;
	/**
	 * @author Joel
	 */
	private final Color sphereCollisionColor = UI.sphereCollisionColor;
	/**
	 * @author Joel
	 */
	private boolean useBoxCollision = false;
	/**
	 * HalfHeight
	 *
	 * @author Joel
	 */
	private Vector2D boxCollision = new Vector2D();
	/**
	 * @author Joel
	 */
	private final Color boxCollisionColor = UI.boxCollisionColor;


	// ????
	public String detailClassPath = "";


	//++++++++++++++++++++++++++++++++++++++++++++++++++Constructor++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldObject() {
		world = null;
		renderTarget = null;
		graphicsContext = null;
		displayName = "None";
		id = "Invalid";
		remove();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldObject(World world, Canvas canvas, String displayName) {
		this.world = world;
		this.renderTarget = canvas;
		graphicsContext = canvas.getGraphicsContext2D();
		this.displayName = displayName;
		id = createId();
		Debug.print(getId());
		getWorld().addWorldObject(this);
	}

	//--------------------------------------------------Constructor--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Comment
	 *
	 * @return
	 * @author Joel
	 */
	public double getSphereCollision() {
		return sphereCollision;
	}

	/**
	 * Comment
	 *
	 * @return
	 * @author Joel
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Comment
	 *
	 * @return
	 * @author Joel
	 */
	protected GraphicsContext getGraphicsContext() {
		return graphicsContext;
	}

	/**
	 * Comment
	 *
	 * @return
	 * @author Joel
	 */
	public Canvas getRenderTarget() {
		return renderTarget;
	}

	/**
	 * @return
	 * @author Joel
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return
	 * @author Joel
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 * @author Joel
	 */
	public String getIdName() {
		//return "WorldObject";
		return getClass().getSimpleName();
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Comment
	 *
	 * @return Return-Comment
	 * @author Joel
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @return
	 * @author Joel
	 */
	public boolean isInteractable() {
		return interactable;
	}

	//--------------------------------------------------GetterMethods--------------------------------------------------

	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Comment
	 *
	 * @param position Param-Comment
	 * @author Joel
	 */
	public void setPosition(Vector2D position) {
		/*double worldSizeX = world.getWorldSize().x - sphereCollision / 2;
		if (position.x < worldSizeX) {
			position.x = worldSizeX;
		} else if (position.x > worldSizeX) {
			position.x = worldSizeX;
		} else {

		}

		double worldSizeY = world.getWorldSize().y - sphereCollision / 2;
		if (position.y < worldSizeY) {
			position.y = worldSizeY;
		} else if (position.y > worldSizeY) {
			position.y = worldSizeY;
		}*/

		this.position = position;
		//this.position.y *= -1;
	}

	/**
	 * Comment
	 *
	 * @param rotation Param-Comment
	 * @author Joel
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;

		// Clamp rotation from 0 to 359.99...
		while (this.rotation < 0) {
			this.rotation += 360;
		}
		while (this.rotation >= 360) {
			this.rotation -= 360;
		}

		Debug.toConsole(rotation);
	}

	/**
	 * @param interactable
	 * @author Joel
	 */
	public void setInteractable(boolean interactable) {
		this.interactable = interactable;
	}

	//--------------------------------------------------SetterMethods--------------------------------------------------

	//++++++++++++++++++++++++++++++++++++++++++++++++++AdderMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Comment
	 *
	 * @param position Param-Comment
	 * @author Joel
	 */
	public void addPosition(Vector2D position) {
		Vector2D pos = getPosition();
		pos.x += position.x;
		pos.y *= position.y;
		setPosition(pos);
	}

	/**
	 * Comment
	 *
	 * @param rotation Param-Comment
	 * @author Joel
	 */
	public void addRotation(double rotation) {
		setRotation(getRotation() + rotation);
	}

	//--------------------------------------------------AdderMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++#####++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * @return
	 * @author Joel
	 */
	private String createId() {
		return getIdName() + "_" + idCounter++;
	}

	/**
	 * @author Joel
	 */
	public void remove() {
		getWorld().removeWorldObject(this);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public void update() {
		drawCollision();
	}

	/**
	 * @author Joel
	 */
	public void drawCollision() {
		graphicsContext.save();
		graphicsContext.setFill(sphereCollisionColor);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillOval((sphereCollision) * world.getViewerZoom() * -1, (sphereCollision) * world.getViewerZoom() * -1, sphereCollision * 2 * world.getViewerZoom(), sphereCollision * 2 * world.getViewerZoom());
		graphicsContext.restore();


		if (!useBoxCollision()) return;
		graphicsContext.save();
		graphicsContext.setFill(boxCollisionColor);
		//Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillRect((boxCollision.x) * world.getViewerZoom() * -1, (boxCollision.y) * world.getViewerZoom() * -1, boxCollision.x * 2 * world.getViewerZoom(), boxCollision.y * 2 * world.getViewerZoom());
		graphicsContext.restore();


	}
	/*
	public void drawCollision() {
		graphicsContext.save();
		graphicsContext.setFill(Color.RED);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillOval ((sphereCollision/2) * world.getViewerZoom() * -1, (sphereCollision/2) * world.getViewerZoom() * -1, sphereCollision * world.getViewerZoom(), sphereCollision * world.getViewerZoom());
		graphicsContext.restore();
	}*/

	//--------------------------------------------------#####--------------------------------------------------


	public void updateSim() {
	}

	public boolean useBoxCollision() {
		return useBoxCollision;
	}

	public void setUseBoxCollision(boolean useBoxCollision) {
		this.useBoxCollision = useBoxCollision;
	}

	public Vector2D getBoxCollision() {
		return boxCollision;
	}

	public void setBoxCollision(Vector2D boxCollision) {
		this.boxCollision = boxCollision;
	}

	public Color getBoxCollisionColor() {
		return boxCollisionColor;
	}

	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
	}

	public void updateDetailsPanel() {
	}
}
