package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.group_three.constants.UI;
import org.group_three.ui.SimView2D;
import org.group_three.ui.Vector2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VehicleFilterController {

	@FXML private TextField speedMin;

	@FXML private TextField speedMax;

	@FXML private TextField radius;

	@FXML private VBox colorFilterList;


	@FXML
	private void initialize() {
		speedMin.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_VehicleSpeed = new Vector2D(Math.abs(Double.parseDouble(newText)), UI.viewFilter_VehicleSpeed.y);
				speedMin.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
				SimView2D.getWorld().requestUpdate();
			} catch (Exception e) {
				speedMin.textProperty().set(oldText);
			}
		});

		speedMax.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_VehicleSpeed = new Vector2D(UI.viewFilter_VehicleSpeed.x, Math.abs(Double.parseDouble(newText)));
				speedMax.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
				SimView2D.getWorld().requestUpdate();
			} catch (Exception e) {
				speedMax.textProperty().set(oldText);
			}
		});

		radius.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_PositionRadius = Math.abs(Double.parseDouble(newText));
				radius.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
				SimView2D.getWorld().requestUpdate();
			} catch (Exception e) {
				radius.textProperty().set(oldText);
			}
		});
	}

	@FXML
	private void onColorAddButtonClicked() {
		FXMLLoader loader = new FXMLLoader(
				SimView2D.class.getResource("/org/group_three/ui/fxml/VehicleFilter_ColorListEntry.fxml")
		);

		Node detailsNode;
		try {
			detailsNode = loader.load();
			((VehicleFilterColorController) loader.getController()).setup(this);

			colorFilterList.getChildren().add(colorFilterList.getChildren().size()-1, detailsNode);

		} catch (IOException e) {
			//e.printStackTrace();
			//return null;
		}
	}

	@FXML
	private void onSelectPos() {
		SimView2D.setRequestPosition(this);
	}

	public void receivePosition(Vector2D pos) {
		UI.viewFilter_Position = pos;
		SimView2D.getWorld().requestUpdate();
	}

	private final List<ColorPicker> colorPickers = new ArrayList<>();

	public void updatedColorPickerList(ColorPicker picker, boolean remove) {
		if (remove) {
			colorPickers.remove(picker);
			picker.valueProperty().removeListener((_) -> onColorPickerValueChanged());
		} else {
			colorPickers.add(picker);
			picker.valueProperty().addListener((_) -> onColorPickerValueChanged());
		}
		onColorPickerValueChanged();
	}

	private void onColorPickerValueChanged() {
		UI.viewFilter_VehicleColor.clear();
		for (ColorPicker picker : colorPickers) {
			UI.viewFilter_VehicleColor.add(picker.getValue());
		}

		SimView2D.getWorld().requestUpdate();
	}
}
