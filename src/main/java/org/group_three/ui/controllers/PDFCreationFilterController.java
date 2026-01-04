package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.group_three.api.SimController;
import org.group_three.constants.enums.stats.EdgeSortOption;
import org.group_three.constants.enums.style.CSSStyle;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;

public class PDFCreationFilterController {

	public Stage stage;

	@FXML
	private TextField length;

	@FXML
	private ColorPicker color;

	@FXML
	private CheckBox avgSpeed;

	@FXML
	private ComboBox edgeSort;

	@FXML
	private void initialize() {
		edgeSort.getItems().addAll(
				EdgeSortOption.usage,
				EdgeSortOption.length,
				EdgeSortOption.name);
		edgeSort.getSelectionModel().selectFirst();
	}

	@FXML
	private void onSave() {
		SimController simcon = SimController.getMainsimcon();

		if(simcon == null) {
			stage.close();
			return;
		}

		int lengthValue = Integer.parseInt("0" + length.getText());
		//if (lengthValue == 0) lengthValue = 1;

        if(color.getOpacity() == 0) Debug.print("Transparent");

		simcon.queueryStats(

                "Output_Collection",

				"VehicleData",
				avgSpeed.isSelected(),
				(color.getValue() != Color.TRANSPARENT) ? Meth.ClrToSumoClr(color.getValue()) : null,

				"EdgeData",
				(EdgeSortOption) edgeSort.getSelectionModel().getSelectedItem(),
				lengthValue,

                CSSStyle.DEFAULT

		);

		simcon.exportStatsToPDF();
		stage.close();
	}
}
