package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.group_three.api.SimController;
import org.group_three.constants.DefaultStasticValues;
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

        //add "0" at the beginning, so that no input resolves to 0
		int lengthValue = Integer.parseInt("0" + length.getText());


		simcon.queueryStats(

                DefaultStasticValues.STATCOLLECTION_NAME,

				DefaultStasticValues.VEHSTAT_NAME,
				avgSpeed.isSelected(),
				(color.getValue().getOpacity() != 0) ? Meth.ClrToSumoClr(color.getValue()) : null,

				DefaultStasticValues.EDGESTAT_NAME,
				(EdgeSortOption) edgeSort.getSelectionModel().getSelectedItem(),
				lengthValue,

                style.getValue()

		);

		simcon.exportStatsToPDF();
		stage.close();
	}
}
