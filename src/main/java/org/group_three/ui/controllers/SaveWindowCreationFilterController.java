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

/**
 * The controller for the pdf/csv stat data export.
 *
 * @author Joel
 */
public class SaveWindowCreationFilterController {

	/**
	 * The stage which this FXML/Controller is part of.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	public Stage stage;

	/**
	 * The street length filter.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField length;

	/**
	 * The vehicle color filter.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ColorPicker color;

	/**
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private CheckBox avgSpeed;

	/**
	 * The ComboBox for the edge sort.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ComboBox<EdgeSortOption> edgeSort;

	/**
	 * The ComboBox for the pdf's CSS style.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ComboBox<CSSStyle> style;

	/**
	 * The initialize method of this controller.
	 * Initializes the ComboBoxes.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {
		// Ini edge sort combo box
		edgeSort.getItems().addAll(EdgeSortOption.values());
		edgeSort.getSelectionModel().select(EdgeSortOption.usage);

		// Ini pdf style combo box
		style.getItems().addAll(CSSStyle.values());
		style.getSelectionModel().select(CSSStyle.DEFAULT);
	}

	public void disableStyle() {
		style.setDisable(true);
	}

	/**
	 * A method that gets called when the save button is pressed.
	 * Grabs the data based on the given parameters from the FXML and exports it.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSave() {
		// get sim controller and validate
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) {
			// close window
			stage.close();
			return;
		}

		// collect stat data
		simcon.queueryStats(

				// data name
				DefaultStasticValues.STATCOLLECTION_NAME,

				// veh data name
				DefaultStasticValues.VEHSTAT_NAME,
				// sort by avg vehicle speed bool
				avgSpeed.isSelected(),
				// filter for specific vehicle color, if opacity 0 no filter
				(color.getValue().getOpacity() != 0) ? Meth.ClrToSumoClr(color.getValue()) : null,

				// edge data name
				DefaultStasticValues.EDGESTAT_NAME,
				// sort data by street name, usage or street length
				edgeSort.getValue(),
				// filter streets below this length, "0" added in front so integer parsing still works if no input is present
				Integer.parseInt("0" + length.getText()),

				// pdf style
				style.getValue()

		);

		// export stat data
		if (stage.getTitle().contains(Documents.CSV_EXTENSION)) {
			// export as csv
			simcon.exportStatsAsZippedCSVs();
		} else {
			// export as pdf
			simcon.exportStatsToPDF();
		}

		// close window
		stage.close();
	}
}