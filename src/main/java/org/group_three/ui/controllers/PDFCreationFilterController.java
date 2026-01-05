package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.group_three.api.SimController;
import org.group_three.constants.DefaultStasticValues;
import org.group_three.constants.Documents;
import org.group_three.constants.enums.stats.EdgeSortOption;
import org.group_three.constants.enums.style.CSSStyle;
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

                DefaultStasticValues.STATCOLLECTION_NAME,

				DefaultStasticValues.VEHSTAT_NAME,
				avgSpeed.isSelected(),
				(color.getValue().getOpacity() != 0) ? Meth.ClrToSumoClr(color.getValue()) : null,

                DefaultStasticValues.EDGESTAT_NAME,
				edgeSort.getValue(),
				Integer.parseInt("0" + length.getText()),

                style.getValue()

		);

		if (stage.getTitle().contains(Documents.CSV_EXTENSION)) {
			simcon.exportStatsAsZippedCSVs();
		} else {
			simcon.exportStatsToPDF();
		}

		stage.close();
	}
}
