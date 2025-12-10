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

	private WorldVehicle worldVehicle;

	@FXML
	private void initialize() throws IOException {
		Debug.print("VehicleDetails Controller loaded.");

		speed.textProperty().addListener(
				(obs, oldText, newText) -> {
					String oldSpeedValue = speed.textProperty().getValue();
					try {
						worldVehicle.getwVehicle().setSpeed(Math.abs(Double.parseDouble(newText)));
						//speed.textProperty().set(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
						speed.textProperty().set(String.valueOf(Math.abs(Double.parseDouble(newText))));
					} catch (Exception e) {
						speed.textProperty().set(oldSpeedValue);
					}
					Debug.print("Speed changed.");
				});

		color.valueProperty().addListener(
				(obs, oldColor, newColor) -> {
					try {
						//if (color.getValue() != worldVehicle.getColor()) {
						//worldVehicle.getwVehicle().setColor(Meth.convertColorToSumoColor(color.getValue()));
						//}
						worldVehicle.getwVehicle().setColor(new SumoColor(0, 255, 0, 255));//Meth.convertColorToSumoColor(new Color(1,0,0,1)));
                        worldVehicle.getwVehicle().update();
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
	}
}
