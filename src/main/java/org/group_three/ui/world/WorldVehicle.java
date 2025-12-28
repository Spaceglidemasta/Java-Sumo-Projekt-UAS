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
import org.group_three.ui.controllers.VehicleDetailsController;

import static org.group_three.ui.Meth.ClrToSumoClr;
import static org.group_three.ui.Meth.SumoClrToClr;

/**
 * THe vehicle to be rendered on the canvas.
 *
 * @author Joel
 */
public class WorldVehicle extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The scale of the vehicle.
	 *
	 * @author Luca, Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static double scale_size = 1;

	/**
	 * The scaled size of the vehicle
	 *
	 * @author Luca, Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static final Vector2D size = new Vector2D(5 * scale_size, 2.5 * scale_size);


	/**
	 * The icon manager of this class.
	 * Manages icons and makes sure to not create the same icon twice.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	public static final ColoredIconManager iconManager = new ColoredIconManager(UI.carIcon);

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
	public VehicleDetailsController vehicleDetailsController;

	/**
	 * The wVehicle object for this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WVehicle wVehicle;

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

		detailClassPath = "/org/group_three/ui/fxml/VehicleDetails.fxml";
		setInteractable(true);
		setUseBoxCollision(true);
		setBoxCollision(size.div(2));
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

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

	//--------------------------------------------------GetterMethods---------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Sets the color via wVehicle
	 *
	 * @param color in Color (JavaFX)
	 * @author Luca
	 */
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
	public boolean setColor(SumoColor color) {
		if (wVehicle.setColor(color)) {
			this.color = SumoClrToClr(color);
			return true;
		} else {
			return false;
		}
	}

	//--------------------------------------------------SetterMethods---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * An update method which should not exist I think?
	 * Needs to be moved over to other methods and be removed.
	 *
	 * @author Joel
	 */
	@Override
	public void updateSim() {
		wVehicle.update();
		setRotation(360 - wVehicle.getAngle() + 90);
		setPosition(Meth.addRelativeLocation(new Vector2D(wVehicle.getPos()), getRotation(), new Vector2D(-size.x/2, 0)));
		setColor(wVehicle.getColor());

		updateDetailsPanel();
	}

	/**
	 * The method to update the render of the vehicle.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();
		Image visualImage = iconManager.getIcon(getColor());



		//wVehicle.boogieWonderland();

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
		vehicleDetailsController = fxmlLoader.getController();
		vehicleDetailsController.setup(this);
	}

	/**
	 * A method to update the details panel when the object is selected.
	 *
	 * @author Joel
	 */
	@Override
	public void updateDetailsPanel() {
		if (vehicleDetailsController == null) {
			//Debug.print("vehicleDetailsController is invalid.");
			return;
		}

		vehicleDetailsController.update();
	}

	/**
	 * A method to remove the vehicle from the world.
	 *
	 * @author Joel
	 */
	@Override
	public void remove() {
		super.remove();
		if (vehicleDetailsController != null) vehicleDetailsController.kill();
	}

	@Override
	public void select() {
		super.select();
		vehicleDetailsController.createWorlVehicleRoute();
	}

	@Override
	public void deselect() {
		super.deselect();
		vehicleDetailsController.removeWorlVehicleRoute();
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}