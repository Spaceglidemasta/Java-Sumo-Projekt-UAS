package org.group_three.ui.world;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;

import javafx.scene.image.Image;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.MainWindowSimulationFrameController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class that represents an object in the 2d world subclasses should later be road parts, traffic lights, vehicles,...
 *
 * @author Joel
 */
public abstract class WorldObject {

	// Logger
	private static final Logger log = Logger.getLogger(WorldObject.class.getName());

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The id counter for all world objects.
	 * Keeps track of all instances ever created,
	 * will always be the count of created objects and
	 * not the index of the last created object.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static int idCounter = 0;

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The world of which this object is a part of.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final World world;

	/**
	 * The canvas on which this object should be drawn on.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Canvas renderTarget;

	/**
	 * The Graphics Context of the canvas on which this object should be drawn on.
	 * (is the same across all canvas users)
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final GraphicsContext graphicsContext;


	/**
	 * The DisplayName of this object.
	 * Is not unique.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final String displayName;

	/**
	 * The id of this object.
	 * Is unique.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final String id;


	/**
	 * The absolute position of the object in the world.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D position = new Vector2D();

	/**
	 * The absolute rotation of the object in the world.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private double rotation = 0;


	/**
	 * If the object should be interactable.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private boolean interactable = false;

	/**
	 * The sphere collision radius in meters.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private double sphereCollision = 0;

	/**
	 * The sphere collision color.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color sphereCollisionColor = UI.sphereCollisionColor;

	/**
	 * If box collision should be used.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private boolean useBoxCollision = false;

	/**
	 * The HalfHeight of the box collision.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Vector2D boxCollision = new Vector2D();

	/**
	 * The box collision color.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Color boxCollisionColor = UI.boxCollisionColor;


	/**
	 * The FXML path of the interactable details panel.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private String detailClassPath = "";

	/**
	 * An indicator to check if this world object is currently selected.
	 *
	 * @author Joel
	 */
	@SuppressWarnings({"JavadocDeclaration", "unused"})
	private boolean selected = false;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The empty default constructor.
	 * It's an invalid creation type so its gets removed on creation.
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
	 * The default constructor to create this object.
	 * Requires a world, canvas and displayName reference.
	 *
	 * @author Joel
	 */
	public WorldObject(World world, Canvas canvas, String displayName) {
		this.world = world;
		this.renderTarget = canvas;
		graphicsContext = canvas.getGraphicsContext2D();
		this.displayName = displayName;
		id = createId();

		// add object to world
		getWorld().addWorldObject(this);

		// log WorldObject creation
		log.info("Spawned WorldObject: " + getClass().getSimpleName());
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++GetterSetterMethods++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Sets the details panel FXML which should be loaded on object selection.
	 *
	 * @param detailClassPath The FXML file path (relative path to the fxml folder)
	 * @author Joel
	 */
	public void setDetailClassPath(String detailClassPath) {
		this.detailClassPath = UI.detailClassFolderPath + detailClassPath;
	}

	/**
	 * Gets sphereCollision
	 *
	 * @return radius
	 * @author Joel
	 */
	public double getSphereCollision() {
		return sphereCollision;
	}

	/**
	 * Gets world the object is in
	 *
	 * @return World
	 * @author Joel
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the graphics context for the world the object is in.
	 *
	 * @return Graphics context
	 * @author Joel
	 */
	protected GraphicsContext getGraphicsContext() {
		return graphicsContext;
	}

	/**
	 * Gets the canvas the object should be drawn on.
	 *
	 * @return Canvas
	 * @author Joel
	 */
	public Canvas getRenderTarget() {
		return renderTarget;
	}

	/**
	 * Gets the display name of the object.
	 *
	 * @return String
	 * @author Joel
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets the id of the object.
	 *
	 * @return ClassName_UNIQUENUMBER
	 * @author Joel
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the simple class name.
	 *
	 * @return The class name. For Example "WorldObject"
	 * @author Joel
	 */
	public String getIdName() {
		return getClass().getSimpleName();
	}

	/**
	 * Gets the position of the object in the world.
	 *
	 * @return Vector2D
	 * @author Joel
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Gets the rotation of the object in the world
	 *
	 * @return Double
	 * @author Joel
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Gets if the object is interactable.
	 *
	 * @return Boolean
	 * @author Joel
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean isInteractable() {
		return interactable;
	}


	/**
	 * Sets the sphere collision of the object.
	 *
	 * @param sphereCollision radius
	 * @author Joel
	 */
	public void setSphereCollision(double sphereCollision) {
		this.sphereCollision = sphereCollision;
	}

	/**
	 * sets the position of the object in the world.
	 *
	 * @param position Vector2D
	 * @author Joel
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}

	/**
	 * Set the rotation of the object in the world.
	 *
	 * @param rotation degrees
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

		//Debug.toConsole(rotation);
	}

	/**
	 * Set if the object should use collision and be interactable.
	 *
	 * @param interactable boolean
	 * @author Joel
	 */
	public void setInteractable(boolean interactable) {
		this.interactable = interactable;
	}


	/**
	 * Adder to add a relative position to the object.
	 *
	 * @param position The relative position to add.
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public void addPosition(Vector2D position) {
		Vector2D pos = getPosition();
		pos.x += position.x;
		pos.y *= position.y;
		setPosition(pos);
	}

	/**
	 * Adder to add a rotation to the object.
	 *
	 * @param rotation The rotation to add in degrees.
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public void addRotation(double rotation) {
		setRotation(getRotation() + rotation);
	}


	/**
	 * A method to get the location of where to draw the object on the canvas.
	 *
	 * @return Vector2D location
	 * @author Joel
	 */
	public Vector2D getDrawLocation() {
		return Meth.addRelativeLocation(
				world.getViewerPosition(),
				world.getViewerRotation(),
				getPosition().mul(world.getViewerZoom()
				)
		);
	}

	/**
	 * A method to set the draw transform of the object based on the already used location and rotation.
	 *
	 * @author Joel
	 */
	public void setDrawTransform() {
		graphicsContext.translate(
				getDrawLocation().x + world.getViewerPositionOffset().x,
				getDrawLocation().y + world.getViewerPositionOffset().y
		); // Object Location
		graphicsContext.rotate(
				Meth.addRelativeRotation(world.getViewerRotation(),
						getRotation()
				)
		);
	}

	/**
	 * A method to get the draw center offset for an object. (adjusted for zoom,...)
	 * To draw for example an image perfectly centered.
	 *
	 * @param objectHalfSize The half size of the object.
	 * @return The offset value. (uniform)
	 * @author Joel
	 */
	public double getDrawCenterOffset(double objectHalfSize) {
		return objectHalfSize * world.getViewerZoom() * -1;
	}

	/**
	 * A method to get the draw size on the canvas. (adjusted for zoom,...)
	 *
	 * @param objectHalfSize The half size of the object.
	 * @return The real draw size.
	 * @author Joel
	 */
	public Vector2D getDrawSize(Vector2D objectHalfSize) {
		return new Vector2D(
				getDrawSize(objectHalfSize.x),
				getDrawSize(objectHalfSize.y)
		);
	}

	/**
	 * A method to get the draw size on the canvas. (adjusted for zoom,...)
	 *
	 * @param objectHalfSize The half size of the object.
	 * @return The real draw size. (uniform)
	 * @author Joel
	 */
	public double getDrawSize(double objectHalfSize) {
		return objectHalfSize * 2 * world.getViewerZoom();
	}


	/**
	 * A method to check if box collision should be used.
	 *
	 * @return boolean
	 * @author Joel
	 */
	public boolean useBoxCollision() {
		return useBoxCollision;
	}

	/**
	 * A method to set if box collision should be used.
	 *
	 * @param useBoxCollision boolean
	 * @author Joel
	 */
	public void setUseBoxCollision(boolean useBoxCollision) {
		this.useBoxCollision = useBoxCollision;
	}

	/**
	 * A method to get the box collision size.
	 *
	 * @return Vector2D size of box collision
	 * @author Joel
	 */
	public Vector2D getBoxCollision() {
		return boxCollision;
	}

	/**
	 * A method to set the box collision size.
	 * Also updates the sphere collision
	 *
	 * @param boxCollision Vector2D new size
	 * @author Joel
	 */
	public void setBoxCollision(Vector2D boxCollision) {
		this.boxCollision = boxCollision;

		// update sphere collision size
		setSphereCollision(boxCollision.length());
	}

	//-----------------------------------------------GetterSetterMethods------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++DrawMethods++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to draw a Sphere at the center of the WorldObject.
	 *
	 * @param radius The radius of the sphere.
	 * @param color  The color of the sphere.
	 * @author Joel
	 * @see #drawOval(Vector2D, Color)
	 */
	public void drawSphere(double radius, Color color) {
		graphicsContext.save();
		graphicsContext.setFill(color);
		setDrawTransform();
		graphicsContext.fillOval(
				getDrawCenterOffset(radius),
				getDrawCenterOffset(radius),
				getDrawSize(radius),
				getDrawSize(radius)
		);
		graphicsContext.restore();
	}

	/**
	 * A method to draw an Oval at the center of the WorldObject.
	 *
	 * @param halfSize The half size of the oval (x,y).
	 * @param color    The color of the oval.
	 * @author Joel
	 * @see #drawSphere(double, Color)
	 */
	@SuppressWarnings("unused")
	public void drawOval(Vector2D halfSize, Color color) {
		graphicsContext.save();
		graphicsContext.setFill(color);
		setDrawTransform();
		graphicsContext.fillOval(
				getDrawCenterOffset(halfSize.x),
				getDrawCenterOffset(halfSize.y),
				getDrawSize(halfSize.x),
				getDrawSize(halfSize.y)
		);
		graphicsContext.restore();
	}

	/**
	 * A method to draw a Square at the center of the WorldObject.
	 *
	 * @param halfSize The half size of the square.
	 *                 (The square side length divided by 2)
	 * @param color    The color of the square.
	 * @author Joel
	 * @see #drawRectangle(Vector2D, Color)
	 */
	@SuppressWarnings("unused")
	public void drawSquare(double halfSize, Color color) {
		graphicsContext.save();
		graphicsContext.setFill(color);
		setDrawTransform();
		graphicsContext.fillRect(
				getDrawCenterOffset(halfSize),
				getDrawCenterOffset(halfSize),
				getDrawSize(halfSize),
				getDrawSize(halfSize)
		);
		graphicsContext.restore();
	}

	/**
	 * A method to draw a Rectangle at the center of the WorldObject.
	 *
	 * @param halfSize The half size of the rectangle.
	 * @param color    The color of the rectangle.
	 * @author Joel
	 * @see #drawSquare(double, Color)
	 */
	public void drawRectangle(Vector2D halfSize, Color color) {
		graphicsContext.save();
		graphicsContext.setFill(color);
		setDrawTransform();
		graphicsContext.fillRect(
				getDrawCenterOffset(halfSize.x),
				getDrawCenterOffset(halfSize.y),
				getDrawSize(halfSize.x),
				getDrawSize(halfSize.y)
		);
		graphicsContext.restore();
	}

	/**
	 * A method to draw an Image at the center of the WorldObject.
	 *
	 * @param halfSize The half size of the image.
	 * @param image    The image which should be drawn.
	 * @author Joel
	 */
	public void drawImage(Vector2D halfSize, Image image) {
		graphicsContext.save();
		setDrawTransform();
		graphicsContext.drawImage(
				image,
				getDrawCenterOffset(halfSize.x),
				getDrawCenterOffset(halfSize.y),
				getDrawSize(halfSize.x),
				getDrawSize(halfSize.y)
		);
		graphicsContext.restore();
	}

	/**
	 * A method to draw a Polygon at the center of the WorldObject.
	 *
	 * @param points A list of Vector2D's which contains all the points of the polygon.
	 * @param color  The color of the polygon.
	 * @author Joel
	 */
	public void drawPolygon(List<Vector2D> points, Color color) {
		graphicsContext.save();
		graphicsContext.setFill(color);
		setDrawTransform();

		List<Vector2D> shape = new ArrayList<>();

		for (Vector2D point : points) {
			shape.add(point.mul(getWorld().getViewerZoom()));
		}

		// convert Vector2D list to x and y double arrays
		double[] xPoints = new double[shape.size()];
		double[] yPoints = new double[shape.size()];

		for (int i = 0; i < shape.size(); i++) {
			xPoints[i] = shape.get(i).x;
			yPoints[i] = shape.get(i).y;
		}

		graphicsContext.fillPolygon(xPoints, yPoints, shape.size());
		graphicsContext.restore();
	}

	/**
	 * A method to draw a Line at the center of the WorldObject.
	 *
	 * @param points The points to draw the line from.
	 * @param width  The absolute width of the line.
	 * @param color  The color of the line
	 * @author Joel
	 */
	public void drawLine(List<Vector2D> points, double width, Color color) {
		graphicsContext.save();
		graphicsContext.setStroke(color);
		graphicsContext.setLineWidth(width);
		setDrawTransform();

		List<Vector2D> shape = new ArrayList<>();

		for (Vector2D point : points) {
			shape.add(point.mul(getWorld().getViewerZoom()));
		}

		// convert Vector2D list to x and y double arrays
		double[] xPoints = new double[shape.size()];
		double[] yPoints = new double[shape.size()];

		for (int i = 0; i < shape.size(); i++) {
			xPoints[i] = shape.get(i).x;
			yPoints[i] = shape.get(i).y;
		}

		graphicsContext.strokePolyline(xPoints, yPoints, shape.size());
		graphicsContext.restore();
	}

	/**
	 * A method to draw the collision of the world object.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public void drawCollision() {
		if (!UI.showCollision) return;

		if (!useBoxCollision() || UI.forceShowSphereCollision) {
			// skip if object has no collision enabled or collision size is 0
			if (!isInteractable() || getSphereCollision() <= 0) return;
			drawSphere(sphereCollision, sphereCollisionColor);
		}

		if (useBoxCollision()) {
			// skip if box collision is not enabled or collision component sizes are 0
			if (!useBoxCollision() || getBoxCollision().x <= 0 || getBoxCollision().y <= 0) return;
			drawRectangle(boxCollision, boxCollisionColor);
		}
	}

	//---------------------------------------------------DrawMethods----------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to convert a shape, with absolute world coordinates,
	 * to a shape with to its own base relative coordinates.
	 *
	 * @param absoluteShape The absolute shape positions from SUMO.
	 * @return The relative shape positions.
	 * @author Joel
	 */
	public List<Vector2D> getRelativeShape(List<Vector2D> absoluteShape) {
		List<Vector2D> relativeShape = new ArrayList<>();

		// get the relativ position of the shape point to its base and add it to the return list
		for (Vector2D point : absoluteShape) {
			relativeShape.add(Meth.getRelativeLocation(getPosition(), 0, point));
		}

		return relativeShape;
	}

	/**
	 * A method to create an id for this world object.
	 *
	 * @return The id as id name + total object counter
	 * @author Joel
	 */
	private String createId() {
		return getIdName() + "_" + idCounter++;
	}

	/**
	 * The draw method for this object, abstract and to be implemented in the child classes.
	 *
	 * @author Joel
	 */
	public abstract void update();

	/**
	 * A method to remove this object from the world.
	 *
	 * @author Joel
	 */
	public void remove() {
		// remove object from world
		getWorld().removeWorldObject(this);

		// log WorldObject removal
		log.info("Removed WorldObject: " + getClass().getSimpleName());
	}


	/**
	 * A method to update simulation data.
	 * No use by default, not abstract as its optional
	 *
	 * @author Joel
	 */
	public void updateSim() {
	}

	/**
	 * A method to set up the detail panel.
	 * No use by default, not abstract as its optional
	 *
	 * @param fxmlLoader FXMLLoader for the details panel
	 * @author Joel
	 */
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
	}

	/**
	 * A method to update the details panel.
	 * No use by default, not abstract as its optional
	 *
	 * @author Joel
	 */
	public void updateDetailsPanel() {
	}


	/**
	 * A method that is called when this object is selected.
	 *
	 * @author Joel
	 */
	public void select() {
		selected = true;
		log.info("Selected Object: " + getDisplayName());
		setupDetailsPanel(MainWindowSimulationFrameController.setDetailsPanel(detailClassPath));
	}

	/**
	 * A method that is called when this object is deselected.
	 *
	 * @author Joel
	 */
	public void deselect() {
		selected = false;
		log.info("Deselected Object: " + getDisplayName());
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}