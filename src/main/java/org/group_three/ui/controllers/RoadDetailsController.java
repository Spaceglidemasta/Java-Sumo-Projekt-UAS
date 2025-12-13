package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.debug.Debug;
import org.group_three.ui.world.WorldRoad;

import java.io.IOException;

/**
 * The controller for the RoadDetails.
 *
 * @author Joel
 */
public class RoadDetailsController {

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
	 * Aka the street name.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField displayName;

	/**
	 * The sumo street/lane id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField sumoId;

	/**
	 * How many vehicles to spawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField vehicleSpawnAmount;

	/**
	 * THe speed of the vehicles to spawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField vehicleSpawnSpeed;

	/**
	 * The color of the vehicles to spawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ColorPicker vehicleSpawnColor;

	/**
	 * The route of the vehicles to spawn.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField vehicleSpawnRoute;


	/**
	 * The world road that is owning this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings({"JavadocDeclaration", "FieldCanBeLocal"})
	private WorldRoad worldRoad;

	/**
	 * The initialize method of the road details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {
		Debug.print("VehicleDetails Controller loaded.");
		// following has no real use yet, comments later
		vehicleSpawnSpeed.textProperty().addListener((_, oldText, newText) -> {
			try {
				vehicleSpawnSpeed.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				vehicleSpawnSpeed.setText(oldText);
			}
			Debug.print("Speed changed.");
		});

		vehicleSpawnColor.valueProperty().addListener(
				(_, _, newColor) -> {
					try {
						//
					} catch (Exception e) {
						//throw new RuntimeException(e);
					}
					Debug.print("Color changed.");
				});
	}

	/**
	 * The setup method for this class to fill it with data.
	 *
	 * @param worldRoad The world road which this controller is from.
	 * @author Joel
	 */
	public void setup(WorldRoad worldRoad) {
		this.worldRoad = worldRoad;

		id.setText(worldRoad.getId());
		displayName.setText(worldRoad.getDisplayName());
		sumoId.setText(worldRoad.id);
	}
}
