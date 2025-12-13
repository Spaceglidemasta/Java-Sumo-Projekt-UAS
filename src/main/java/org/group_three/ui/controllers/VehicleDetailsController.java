package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoColor;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.world.WorldVehicle;

import java.io.IOException;

public class VehicleDetailsController {
	@FXML
	private TextField id;
	@FXML
	private TextField displayName;
	@FXML
	private TextField sumoId;
	@FXML
	private TextField speed;
	@FXML
	private ColorPicker color;
	@FXML
	private TextField route;

	private WorldVehicle worldVehicle;

	@FXML
	private void initialize() throws IOException {
		Debug.print("VehicleDetails Controller loaded.");

		speed.textProperty().addListener((_, oldText, newText) -> {
			try {
				worldVehicle.getwVehicle().setSpeed(Math.abs(Double.parseDouble(newText)));
				//speed.textProperty().set(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
				speed.textProperty().set(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				speed.textProperty().set(oldText);
			}
			Debug.print("Speed changed.");
		});

		color.valueProperty().addListener(
				(_, _, newColor) -> {
					try {
						worldVehicle.setColor(newColor);
						worldVehicle.update();
					} catch (Exception e) {
						//throw new RuntimeException(e);
					}
					Debug.print("Color changed.");
				});
	}

	public void setup(WorldVehicle worldVehicle) {
		this.worldVehicle = worldVehicle;

		id.setText(worldVehicle.getId());
		displayName.setText(worldVehicle.getDisplayName());
		sumoId.setText(worldVehicle.getwVehicle().getID());
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		color.setValue(worldVehicle.getColor());
	}

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
