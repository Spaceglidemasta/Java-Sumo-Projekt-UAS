package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.ui.SimView2D;
import org.group_three.ui.Vector2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VehicleFilterController {

	@FXML private TextField speedMin;

	@FXML private TextField speedMax;

	@FXML private VBox colorFilterList;


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

	@FXML
	private void onColorAddButtonClicked() {
		FXMLLoader loader = new FXMLLoader(
				SimView2D.class.getResource("/org/group_three/ui/fxml/VehicleFilter_ColorListEntry.fxml")
		);

		Node detailsNode;
		try {
			detailsNode = loader.load();
			((VehicleFilterColorController) loader.getController()).setup(this);

			// set constraints
			//AnchorPane.setLeftAnchor(detailsNode, 0.0);
			//AnchorPane.setRightAnchor(detailsNode, 0.0);
			//AnchorPane.setTopAnchor(detailsNode, 0.0);
			//AnchorPane.setBottomAnchor(detailsNode, 0.0);

			// remove old details panel data and add new panel (from selected param)
			//colorFilterList.getChildren().clear();
			colorFilterList.getChildren().add(colorFilterList.getChildren().size()-1, detailsNode);

		} catch (IOException e) {
			//e.printStackTrace();
			//return null;
		}
	}

	//public List<Color> colorList = new ArrayList<>();
}
