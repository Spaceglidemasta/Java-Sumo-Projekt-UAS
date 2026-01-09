package org.group_three.ui.world;

import de.tudresden.sumo.objects.SumoColor;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.model.WVehicle;
import org.group_three.ui.ColoredIconManager;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.DetailsPanel_Vehicle_Controller;

import java.util.logging.Logger;

import static org.group_three.ui.Meth.ClrToSumoClr;
import static org.group_three.ui.Meth.SumoClrToClr;

/**
 * The vehicle object to be rendered on the canvas.
 *
 * @author Joel
 */
public class WorldVehicle extends WorldObject {

	// Logger
	private static final Logger log = Logger.getLogger(WorldVehicle.class.getName());

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The scale of the vehicle.
	 *
	 * @author Luca, Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static double scale = UI.vehicleScale;

	/**
	 * The scaled size of the vehicle
	 *
	 * @author Luca, Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static Vector2D size = new Vector2D(5 * scale, 2.5 * scale);

	/**
	 * The icon manager of this class.
	 * Manages icons and makes sure to not create the same icon twice.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static final ColoredIconManager iconManager = new ColoredIconManager(UI.carIcon);

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color of the vehicle.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Color color = UI.defaultVehicleColor;

	/**
	 * The details controller when the object got selected.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private DetailsPanel_Vehicle_Controller detailsPanelVehicleController;

	/**
	 * The wVehicle object for this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WVehicle wVehicle;

	/**
	 * If the vehicle is in boogie (disco) mode.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private boolean boogieMode = false;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	public WorldVehicle() {
		super();
		this.wVehicle = null;
		remove();
	}

	/**
	 * The default WorldVehicle constructor to spawn a new WorldVehicle in the world.
	 *
	 * @param world       The world to which this object should be added.
	 * @param canvas      The canvas on which this object should be drawn on.
	 * @param displayName The display name for this object.
	 * @param wVehicle    The wVehicle object that owns this class.
	 * @author Joel
	 */
	public WorldVehicle(World world, Canvas canvas, String displayName, WVehicle wVehicle) {
		super(world, canvas, displayName);

		this.wVehicle = wVehicle;
		updateSim();

		setDetailClassPath("DetailsPanel_Vehicle.fxml");
		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size.div(2));
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++GetterSetterClassMethods+++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The getter for the vehicle scale.
	 *
	 * @return The (global) vehicle scale.
	 * @author Joel
	 */
	public static double getScale() {
		return scale;
	}

	/**
	 * The setter for the vehicle scale.
	 * Adjust related values too.
	 *
	 * @param scale
	 * @author Joel
	 */
	public static void setScale(double scale) {
		WorldVehicle.scale = scale;
		WorldVehicle.size = new Vector2D(5 * scale, 2.5 * scale);
		log.info("VehicleScale changed to: " + scale);
	}

	//---------------------------------------------GetterSetterClassMethods---------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++GetterSetterMethods++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets the vVehicle object of this class.
	 *
	 * @return The wVehicle object for this class.
	 * @author Joel
	 */
	public WVehicle getwVehicle() {
		return wVehicle;
	}

	/**
	 * Gets the current color of the vehicle.
	 *
	 * @return JavaFX color.
	 * @author Joel
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color via wVehicle
	 *
	 * @param color in Color (JavaFX)
	 * @author Luca
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean setColor(Color color) {

		if (wVehicle.setColor(ClrToSumoClr(color))) {
			this.color = color;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the color via wVehicle
	 *
	 * @param color in SumoColor
	 * @author Luca
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean setColor(SumoColor color) {
		if (wVehicle.setColor(color)) {
			this.color = SumoClrToClr(color);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The getter for the details panel controlle rof this object.
	 *
	 * @return The detail panel controller for this object.
	 * @author Joel
	 */
	public DetailsPanel_Vehicle_Controller getDetailsPanelVehicleController() {
		return detailsPanelVehicleController;
	}

	//-----------------------------------------------GetterSetterMethods------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to update this object with new sumo simulation data.
	 *
	 * @author Joel
	 */
	@Override
	public void updateSim() {
		wVehicle.update();
		setRotation(360 - wVehicle.getAngle() + 90);
		setPosition(Meth.addRelativeLocation(new Vector2D(wVehicle.getPos()), getRotation(), new Vector2D(-size.x / 2, 0)));
		setColor(wVehicle.getColor());

		updateDetailsPanel();
	}

	/**
	 * A method to check if the vehicle currently should be displayed or if its filtered out.
	 *
	 * @return If the vehicle should be displayed.
	 * @author Joel
	 */
	private boolean filterCheck() {
		// speed filter check
		double speed = getwVehicle().getSpeed();
		if (speed < UI.viewFilter_VehicleSpeed.x) return false;
		if (UI.viewFilter_VehicleSpeed.y != -1 && speed > UI.viewFilter_VehicleSpeed.y) return false;

		// color filter check
		if (!UI.viewFilter_VehicleColor.isEmpty() && !UI.viewFilter_VehicleColor.contains(getColor())) return false;

		// pos filter check
		if ((UI.viewFilter_PositionRadius != 0) && (UI.viewFilter_Position.getDistance(getPosition()) > UI.viewFilter_PositionRadius))
			return false;

		// no filter
		return true;
	}

	/**
	 * The method to update the render of the vehicle.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		// get colored icon from icon manager
		Image visualImage = iconManager.getIcon(getColor());

		// scale confirmation / force update
		if (UI.vehicleScale != getScale()) setScale(UI.vehicleScale);

		// skip if vehicle is filtered out
		if (!filterCheck()) return;

		// check if boogie mode should be enabled
		if (boogieMode) {
			wVehicle.boogieWonderland();
		} else {
			if (getColor().getOpacity() != 0 && getColor().getOpacity() != 1) {
				boogieMode = true;
				log.info("WorldVehicle: BoogieMode Enabled.");
			}
		}

		drawImage(size.div(2), visualImage);
	}

	/**
	 * A method to set up the details panel when the object is selected.
	 *
	 * @param fxmlLoader The FXML loader of the loaded details panel.
	 * @author Joel
	 */
	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		detailsPanelVehicleController = fxmlLoader.getController();
		detailsPanelVehicleController.setup(this);
	}

	/**
	 * A method to update the details panel when the object is selected.
	 *
	 * @author Joel
	 */
	@Override
	public void updateDetailsPanel() {
		if (detailsPanelVehicleController == null) return;

		detailsPanelVehicleController.update();
	}

	/**
	 * A method to remove the vehicle from the world.
	 *
	 * @author Joel
	 */
	@Override
	public void remove() {
		super.remove();

		// destroy details panel on removal
		if (detailsPanelVehicleController != null) detailsPanelVehicleController.kill();
	}

	/**
	 * A method which is called on object selection.
	 * Adds a world route for the route of this vehicle.
	 *
	 * @author Joel
	 */
	@Override
	public void select() {
		super.select();

		// create/show world vehicle route on selection
		detailsPanelVehicleController.createWorlVehicleRoute();
	}

	/**
	 * A method which is called on object deselection.
	 * Removes the related world route if present.
	 *
	 * @author Joel
	 */
	@Override
	public void deselect() {
		super.deselect();

		// remove/hide world vehicle route on selection
		detailsPanelVehicleController.removeWorlVehicleRoute();
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}