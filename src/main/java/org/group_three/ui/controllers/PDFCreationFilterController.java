package org.group_three.ui.controllers;

import de.tudresden.sumo.cmd.Edge;
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

import javax.swing.text.html.CSS;

public class PDFCreationFilterController {

	public Stage stage;

	@FXML
	private TextField length;

	@FXML
	private ColorPicker color;

	@FXML
	private CheckBox avgSpeed;

	@FXML
	private ComboBox<EdgeSortOption> edgeSort;

	@FXML
	private ComboBox<CSSStyle> style;

	@FXML
	private void initialize() {
		edgeSort.getItems().addAll(EdgeSortOption.values());
		edgeSort.getSelectionModel().select(EdgeSortOption.usage);

		style.getItems().addAll(CSSStyle.values());
		style.getSelectionModel().select(CSSStyle.DEFAULT);
	}

	@FXML
	private void onSave() {
		SimController simcon = SimController.getMainsimcon();

		if(simcon == null) {
			stage.close();
			return;
		}

		simcon.queueryStats(

                "Output_Collection",

				"VehicleData",
				avgSpeed.isSelected(),
				(color.getValue().getOpacity() != 0) ? Meth.ClrToSumoClr(color.getValue()) : null,

				"EdgeData",
				edgeSort.getSelectionModel().getSelectedItem(),
				Integer.parseInt("0" + length.getText()),

                style.getSelectionModel().getSelectedItem()

		);

		if (stage.getTitle().contains(".csv")) {
			simcon.exportStatsAsZippedCSVs();
		} else {
			simcon.exportStatsToPDF();
		}

		stage.close();
	}
}
