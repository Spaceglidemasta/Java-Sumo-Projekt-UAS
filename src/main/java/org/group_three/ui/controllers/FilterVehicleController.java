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
import java.util.logging.Logger;


/**
 * The filter controller for vehicles.
 *
 * @author Joel
 */
public class FilterVehicleController {

	// Logger
	private static final Logger log = Logger.getLogger(FilterVehicleController.class.getName());

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The minimum speed value to display vehicles
	 *
	 * @author Joel
	 */
	@FXML
	private TextField speedMin;

	/**
	 * The maximum speed value to display vehicles
	 *
	 * @author Joel
	 */
	@FXML
	private TextField speedMax;

	/**
	 * The radius around which vehicles should be displayed.
	 * 0 for it to be disabled.
	 *
	 * @author Joel
	 */
	@FXML
	private TextField radius;

	/**
	 * The whitelist of colors to display (all if empty)
	 *
	 * @author Joel
	 */
	@FXML
	private VBox colorFilterList;

	/**
	 * A list of color pickers from the color filter sub list.
	 *
	 * @author Joel
	 */
	private final List<ColorPicker> colorPickers = new ArrayList<>();

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default initialize method, that binds to text field changes to validate them and use the given data
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {

		// validate speed min input to be a number/double
		speedMin.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_VehicleSpeed = new Vector2D(Math.abs(Double.parseDouble(newText)), UI.viewFilter_VehicleSpeed.y);
				speedMin.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
				SimView2D.getWorld().requestUpdate();
			} catch (Exception e) {
				speedMin.textProperty().set(oldText);
				log.warning("SpeedMin: ValueInvalid");
			}
		});

		// validate speed max input to be a number/double
		speedMax.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_VehicleSpeed = new Vector2D(UI.viewFilter_VehicleSpeed.x, Math.abs(Double.parseDouble(newText)));
				speedMax.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
				SimView2D.getWorld().requestUpdate();
			} catch (Exception e) {
				speedMax.textProperty().set(oldText);
				log.warning("SpeedMax: ValueInvalid");
			}
		});

		// validate radius input to be a number/double
		radius.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				UI.viewFilter_PositionRadius = Math.abs(Double.parseDouble(newText));
				radius.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
				SimView2D.getWorld().requestUpdate();
			} catch (Exception e) {
				radius.textProperty().set(oldText);
				log.warning("Radius: ValueInvalid");
			}
		});
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Triggered when the Add button of the color filter list is pressed.
	 *
	 * @author Joel
	 */
	@FXML
	private void onColorAddButtonClicked() {
		// setup fxml loader
		FXMLLoader loader = new FXMLLoader(
				SimView2D.class.getResource("/org/group_three/ui/fxml/FilterVehicleColorElement.fxml")
		);

		// try to load and setup color picker elements
		try {
			// load fxml and set it up with data
			Node detailsNode = loader.load();
			((FilterVehicleColorElementController) loader.getController()).setup(this);

			// add color picker element to list
			colorFilterList.getChildren().add(colorFilterList.getChildren().size() - 1, detailsNode);

		} catch (IOException e) {
			log.severe("Failed to load and setup color picker elements.");
		}
	}

	/**
	 * Triggered when the select button in the ui is pressed.
	 * Request to get the next click position in the world passed to itself.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSelectPos() {
		SimView2D.requestPosition(this);
	}

	/**
	 * A method that gets called when the object receives a position from the world, after requesting it.
	 * Used to set the center location for the vehicle filter radius.
	 *
	 * @param pos The received world position.
	 * @author Joel
	 */
	public void receivePosition(Vector2D pos) {
		UI.viewFilter_Position = pos;
		SimView2D.getWorld().requestUpdate();
	}


	/**
	 * A method that is called when a color picker element is added or rmeoved,
	 * binds to color changes.
	 *
	 * @param picker
	 * @param remove
	 * @author Joel
	 */
	public void updatedColorPickerList(ColorPicker picker, boolean remove) {
		// add/remove listeners
		if (remove) {
			colorPickers.remove(picker);
			picker.valueProperty().removeListener((_) -> onColorPickerValueChanged());
		} else {
			colorPickers.add(picker);
			picker.valueProperty().addListener((_) -> onColorPickerValueChanged());
		}

		// request value update
		onColorPickerValueChanged();
	}

	/**
	 * A method that is called when the value of one of the color picker elements changes.
	 * Also request a world update.
	 *
	 * @author Joel
	 */
	private void onColorPickerValueChanged() {
		// update view filter color list
		UI.viewFilter_VehicleColor.clear();
		for (ColorPicker picker : colorPickers) {
			UI.viewFilter_VehicleColor.add(picker.getValue());
		}

		SimView2D.getWorld().requestUpdate();
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}