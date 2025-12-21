package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.WorldRoute;
import org.group_three.ui.world.WorldTrafficLight;
import org.group_three.ui.world.WorldVehicle;

/**
 * The controller for the VehicleDetails.
 *
 * @author Joel
 */
public class TrafficLightDetailsController {

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

	@FXML
	private TextField remainingRedTime;
	@FXML
	private TextField remainingYellowTime;
	@FXML
	private TextField remainingGreenTime;
	@FXML
	private TextField redTime;
	@FXML
	private TextField yellowTime;
	@FXML
	private TextField greenTime;
	@FXML
	private CheckBox override;


	private WorldTrafficLight worldTrafficLight;

	public void setup(WorldTrafficLight worldTrafficLight) {
		this.worldTrafficLight = worldTrafficLight;

		id.setText(this.worldTrafficLight.getId());
		displayName.setText(this.worldTrafficLight.getDisplayName());
		sumoId.setText("StopLineIndex: " + this.worldTrafficLight.getwLink().getTLIndex());
		override.setSelected(this.worldTrafficLight.getwTrafficLight().isCustomPhasesActive());

		override.selectedProperty().addListener((_, _, newValue) -> {
			this.worldTrafficLight.getwTrafficLight().setCustomPhasesActive(newValue);
		});



	}


	/**
	 * The initialize method of the vehicle details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {


	}


	/**
	 * The event to toggle view locking of the vehicle.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRemaningRedTimeClicked() {
		Debug.print("onRemaningRedTimeClicked");
	}

	/**
	 * The event to toggle view locking of the vehicle.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRemaningYellowTimeClicked() {
		Debug.print("onRemaningYellowTimeClicked");
	}

	/**
	 * The event to toggle view locking of the vehicle.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRemaningGreenTimeClicked() {
		Debug.print("onRemaningGreenTimeClicked");
	}




}
