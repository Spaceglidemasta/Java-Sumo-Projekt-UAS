package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Filter_VehicleColorElement_Controller {
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private AnchorPane root;

	@FXML
	private void onRemoveButtonClicked() {
		filterVehicleController.updatedColorPickerList(colorPicker, true);
		((VBox) root.getParent()).getChildren().remove(root);
	}

	private FilterVehicleController filterVehicleController;

	public void setup(FilterVehicleController filterVehicleController) {
		this.filterVehicleController = filterVehicleController;
		filterVehicleController.updatedColorPickerList(colorPicker, false);
	}

	@FXML
	private void initialize() {
	}
}
