package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/**
 * The controller of the color picker elements for the vehicle filter.
 *
 * @author Joel
 */
public class FilterVehicleColorElementController {

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The color picker for this color picker element.
	 *
	 * @author Joel
	 */
	@FXML
	private ColorPicker colorPicker;

	/**
	 * The root element of this object.
	 *
	 * @author Joel
	 */
	@FXML
	private AnchorPane root;

	/**
	 * The controller of the parent, of which this element is part of.
	 *
	 * @author Joel
	 */
	private FilterVehicleController filterVehicleController;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The setup method to fill this object with data,
	 * and to add this object to the list of its parent.
	 *
	 * @param filterVehicleController The parent.
	 * @author Joel
	 */
	public void setup(FilterVehicleController filterVehicleController) {
		this.filterVehicleController = filterVehicleController;
		filterVehicleController.updatedColorPickerList(colorPicker, false);
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A button to remove this entry from the ui and the list of the parent.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRemoveButtonClicked() {
		filterVehicleController.updatedColorPickerList(colorPicker, true);
		((VBox) root.getParent()).getChildren().remove(root);
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}