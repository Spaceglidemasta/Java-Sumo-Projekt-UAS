package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group_three.constants.UI;
import org.group_three.ui.Vector2D;


public class VehicleFilterController {

	@FXML private TextField speedMin;

	@FXML private TextField speedMax;


	@FXML
	private void initialize() {
		speedMin.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_VehicleSpeed = new Vector2D(Math.abs(Double.parseDouble(newText)), UI.viewFilter_VehicleSpeed.y);
				speedMin.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				speedMin.textProperty().set(oldText);
			}
		});

		speedMax.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_VehicleSpeed = new Vector2D(UI.viewFilter_VehicleSpeed.x, Math.abs(Double.parseDouble(newText)));
				speedMax.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				speedMax.textProperty().set(oldText);
			}
		});
	}
}
