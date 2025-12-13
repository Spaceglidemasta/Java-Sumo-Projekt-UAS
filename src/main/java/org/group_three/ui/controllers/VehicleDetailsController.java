package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.debug.Debug;
import org.group_three.ui.world.WorldVehicle;

/**
 * The controller for the VehicleDetails.
 *
 * @author Joel
 */
public class VehicleDetailsController {

	/**
	 * The world object id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField id;

	/**
	 * The world objects display name.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField displayName;

	/**
	 * The sumo vehicle id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField sumoId;


	/**
	 * The vehicle speed to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField speed;

	/**
	 * The vehicle color to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ColorPicker color;

	/**
	 * The route of the vehicle to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField route;

	/**
	 * The world vehicle that is owning this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private WorldVehicle worldVehicle;


	/**
	 * The initialize method of the vehicle details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {

		speed.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				worldVehicle.getwVehicle().setSpeed(Math.abs(Double.parseDouble(newText)));
				speed.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				speed.textProperty().set(oldText);
			}
		});

		color.valueProperty().addListener(
				(_, _, newColor) -> {
					try {
						worldVehicle.setColor(newColor);
						worldVehicle.update();
					} catch (Exception e) {
						//throw new RuntimeException(e);
					}
				});
	}

	/**
	 * The setup method for this class to fill it with data.
	 *
	 * @param worldVehicle The world vehicle which this controller is from.
	 * @author Joel
	 */
	public void setup(WorldVehicle worldVehicle) {
		this.worldVehicle = worldVehicle;

		id.setText(worldVehicle.getId());
		displayName.setText(worldVehicle.getDisplayName());
		sumoId.setText(worldVehicle.getwVehicle().getID());
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		color.setValue(worldVehicle.getColor());
	}

	/**
	 * The update method for this class to update its data.
	 *
	 * @author Joel
	 */
	public void update() {
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		color.setValue(worldVehicle.getColor());
		route.setText(worldVehicle.getwVehicle().getRouteEdges().toString());
	}

	/**
	 * A method to visually kill the details tab. (greyed out and locked controls)
	 * Should be called when the WorldObject class gets removed.
	 *
	 * @author Joel
	 */
	public void kill() {
		// disable everything
		id.setDisable(true);
		displayName.setDisable(true);
		sumoId.setDisable(true);
		speed.setDisable(true);
		color.setDisable(true);
		route.setDisable(true);
	}
}
