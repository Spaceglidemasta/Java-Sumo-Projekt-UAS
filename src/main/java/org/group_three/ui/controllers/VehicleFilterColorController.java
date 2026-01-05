package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.group_three.debug.Debug;

import java.util.Objects;

public class VehicleFilterColorController {
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private AnchorPane root;

	@FXML
	private void onRemoveButtonClicked() {
		vehicleFilterController.updatedColorPickerList(colorPicker, true);
		((VBox) root.getParent()).getChildren().remove(root);
	}

	private VehicleFilterController vehicleFilterController;

	public void setup(VehicleFilterController vehicleFilterController) {
		this.vehicleFilterController = vehicleFilterController;
		vehicleFilterController.updatedColorPickerList(colorPicker, false);
	}

	@FXML
	private void initialize() {
	}
}
