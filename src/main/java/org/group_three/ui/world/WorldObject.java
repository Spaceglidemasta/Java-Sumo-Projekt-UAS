package org.group_three.ui.world;

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
	private final World world;
	private final Canvas renderTarget;
	private final GraphicsContext graphicsContext; // is the same across all canvas users

	private final String displayName; // not unique
	private final String id; // unique
	private static int idCounter = 0; // keeps track of all instances ever created, will always be the count of created objects and not the index of the last created object

	private Vector2D position = new Vector2D();
	private double rotation = 0;

	private boolean interactable = false;
	private double sphereCollision = 32; // Radius in meters?
	private Color sphereCollisionColor = UI.sphereCollisionColor;
	private Vector2D boxCollision = new Vector2D();
	private boolean useBoxCollision = false;

	private Image visualImage;

	private double worldSize = 1; // in meters


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
		id = createId();
		Debug.print(getId());
		getWorld().addWorldObject(this);
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



	//++++++++++++++++++++++++++++++++++++++++++++++++++Getter++++++++++++++++++++++++++++++++++++++++++++++++++

	public double getSphereCollision() {
		return sphereCollision;
	}

	public Image getVisualImage() {
		return visualImage;
	}

	public World getWorld() {
		return world;
	}

	protected GraphicsContext getGraphicsContext() {
		return graphicsContext;
	}

	public Canvas getRenderTarget() {
		return renderTarget;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getId() {
		return id;
	}

	public String getIdName() {
		//return "WorldObject";
		return getClass().getSimpleName();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public double getRotation() {
		return rotation;
	}

	public boolean isInteractable() {
		return interactable;
	}

	//--------------------------------------------------Getter--------------------------------------------------

	//++++++++++++++++++++++++++++++++++++++++++++++++++Setter++++++++++++++++++++++++++++++++++++++++++++++++++

	public void setVisualImage(Image visualImage) {
		this.visualImage = visualImage;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param position
	 * Param-Comment
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
	 * @author Joel
	 *
	 * @param rotation
	 * Param-Comment
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

	public void setInteractable(boolean interactable) {
		this.interactable = interactable;
	}

	//--------------------------------------------------Setter--------------------------------------------------

	//++++++++++++++++++++++++++++++++++++++++++++++++++Adder++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param position
	 * Param-Comment
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
	 * @author Joel
	 *
	 * @param rotation
	 * Param-Comment
	 */
	public void addRotation(double rotation) {
		setRotation(getRotation() + rotation);
	}

	//--------------------------------------------------Adder--------------------------------------------------



	//++++++++++++++++++++++++++++++++++++++++++++++++++#####++++++++++++++++++++++++++++++++++++++++++++++++++



	public void remove() {
		getWorld().removeWorldObject(this);
	}

	private String createId() {
		return getIdName() + "_" + idCounter++;
	}







	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public void update() {
		drawCollision();
		/*Vector2D rect = new Vector2D(visualImage.getWidth(), visualImage.getHeight());
		rect = rect.div(10);
		graphicsContext.save();
		//graphicsContext.setFill(Color.BLUE);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.drawImage(addImageTint(visualImage), (rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		//graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		graphicsContext.restore();*/
	}

	public void drawCollision() {
		graphicsContext.save();
		graphicsContext.setFill(sphereCollisionColor);
		Vector2D drawLoc = Meth.addRelativeLocation(world.getViewerPosition(), world.getViewerRotation(), getPosition().mul(world.getViewerZoom()));

		graphicsContext.translate(drawLoc.x + world.getViewerPositionOffset().x, drawLoc.y + world.getViewerPositionOffset().y); // Object Location
		graphicsContext.rotate(Meth.addRelativeRotation(world.getViewerRotation(), getRotation()));
		graphicsContext.fillOval ((sphereCollision) * world.getViewerZoom() * -1, (sphereCollision) * world.getViewerZoom() * -1, sphereCollision*2 * world.getViewerZoom(), sphereCollision*2 * world.getViewerZoom());
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
}
