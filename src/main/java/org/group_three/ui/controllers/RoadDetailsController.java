package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.debug.Debug;
import org.group_three.ui.world.WorldVehicle;

import java.io.IOException;

public class RoadDetailsController {
	@FXML
	private TextField id;
	@FXML
	private TextField displayName;
	@FXML
	private TextField sumoId;

	@FXML
	private void initialize() throws IOException {
		Debug.print("VehicleDetails Controller loaded.");
	}
}
