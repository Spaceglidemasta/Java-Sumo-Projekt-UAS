package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.model.WEdge;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.*;

import java.util.logging.Logger;

/**
 * The controller for the VehicleDetails.
 *
 * @author Joel
 */
public class DetailsPanel_Vehicle_Controller {

	// Logger
	private static final Logger log = Logger.getLogger(DetailsPanel_Vehicle_Controller.class.getName());

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The world object id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField id;

	/**
	 * The world objects display name.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField displayName;

	/**
	 * The sumo vehicle id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField sumoId;


	/**
	 * The vehicle speed to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField speed;

	/**
	 * The vehicle speed to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField speedFactor;

	/**
	 * The vehicle color to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ColorPicker color;

	/**
	 * The world vehicle that is owning this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private WorldVehicle worldVehicle;

	/**
	 * The button to enable route selection.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private Button selectRouteButton;

	/**
	 * The reference of the WorldRoute when this object is selected.
	 *
	 * @author Joel
	 */
	private WorldRoute worldRoute;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The initialize method of the vehicle details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {

		// validate speed factor input to be a number/double and update speed factor on sumo vehicle
		speedFactor.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				worldVehicle.getwVehicle().setSpeedFactor(Math.abs(Double.parseDouble(newText)));
				speedFactor.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				speedFactor.textProperty().set(oldText);
				log.warning("SpeedFactor: InvalidInput");
			}
		});

		// validate color input and update vehicle color on assignment
		color.valueProperty().addListener(
				(_, _, newColor) -> {
					try {
						worldVehicle.setColor(newColor);
						worldVehicle.update();
					} catch (Exception e) {
						log.warning("SpeedFactor: InvalidInput");
					}
				});
	}

	/**
	 * The setup method for this class to fill it with data.
	 *
	 * @param worldVehicle The world vehicle which this controller is from.
	 * @author Joel
	 */
	public void setup(WorldVehicle worldVehicle) {
		this.worldVehicle = worldVehicle;

		id.setText(worldVehicle.getId());
		displayName.setText(worldVehicle.getDisplayName());
		sumoId.setText(worldVehicle.getwVehicle().getID());
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		speedFactor.setText(String.valueOf(worldVehicle.getwVehicle().getSpeedFactor()));
		color.setValue(worldVehicle.getColor());
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Called when the route selection button of the ui is pressed and
	 * request a route selection from SimView2D.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRouteSelectPressed() {
		// request route selection
		SimView2D.setRouteSelection(worldVehicle);
	}

	/**
	 * A method to set a new route for the vehicle.
	 * Also creates a new WorldRoute to display the new route.
	 *
	 * @param worldObject The new WorldRoad to drive to.
	 * @author Joel
	 */
	public void routeSelected(WorldObject worldObject) {
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// skip if object is not a road
		if (worldObject.getClass() != WorldRoad.class) return;

		// remove old route if present
		if (worldRoute != null) {
			worldRoute.remove();
			worldRoute = null;
		}


		// skip if wedge reference is null
		WEdge wEdge = ((WorldRoad) worldObject).getwEdge();
		if (wEdge == null) return;

		// create new route
		SumoStringList route = new SumoStringList();
		route.add(worldVehicle.getwVehicle().getRouteEdges().get(worldVehicle.getwVehicle().getRouteIndex()));
		route.add(wEdge.getId());


		// validate route, if not valid skip
		if (simcon.addRoute(route) == null) return;


		// set new vehicle route
		worldVehicle.getwVehicle().changeRoute(route);

		// spawn new world vehicle route object
		worldRoute = new WorldRoute(
				worldVehicle.getWorld(),
				worldVehicle.getWorld().getRenderTarget(),
				"",
				worldVehicle.getwVehicle().getRouteEdges()
		);
	}

	/**
	 * A method to create and assign a new WorldRoute object.
	 *
	 * @author Joel
	 */
	public void createWorldVehicleRoute() {
		worldRoute = new WorldRoute(
				worldVehicle.getWorld(),
				worldVehicle.getWorld().getRenderTarget(),
				"",
				worldVehicle.getwVehicle().getRouteEdges());
	}

	/**
	 * A method remove the assigned WorldRoute object.
	 *
	 * @author Joel
	 */
	public void removeWorldVehicleRoute() {
		worldRoute.remove();
	}

	/**
	 * The update method for this class to update its data.
	 *
	 * @author Joel
	 */
	public void update() {
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		speedFactor.setText(String.valueOf(worldVehicle.getwVehicle().getSpeedFactor()));
		color.setValue(worldVehicle.getColor());
	}

	/**
	 * A method to visually kill the details tab. (greyed out and locked controls)
	 * Should be called when the WorldObject class gets removed.
	 *
	 * @author Joel
	 */
	public void kill() {
		// all buttons
		id.setDisable(true);
		displayName.setDisable(true);
		sumoId.setDisable(true);
		speed.setDisable(true);
		color.setDisable(true);
		selectRouteButton.setDisable(true);
		speedFactor.setDisable(true);

		// remove world route
		removeWorldVehicleRoute();
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}