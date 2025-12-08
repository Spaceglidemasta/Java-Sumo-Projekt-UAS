package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.debug.Debug;
import org.group_three.ui.world.WorldVehicle;

import java.io.IOException;

public class VehicleDetailsController {
	@FXML
	private TextField id;
	@FXML
	private TextField displayName;
	@FXML
	private TextField sumoId;
	@FXML
	private TextField speed;
	@FXML
	private ColorPicker color;

	private WorldVehicle worldVehicle;

	@FXML
	private void initialize() throws IOException {
		Debug.print("VehicleDetails Controller loaded.");
	}

	public void setup(WorldVehicle worldVehicle) {
		this.worldVehicle = worldVehicle;

		id.setText(worldVehicle.getId());
		displayName.setText(worldVehicle.getDisplayName());
		sumoId.setText(worldVehicle.getwVehicle().getID());
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		color.setValue(worldVehicle.getColor());
	}

	public void update() {
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		color.setValue(worldVehicle.getColor());
	}

	public void kill() {
		// disable everything
	}
}
