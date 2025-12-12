package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.debug.Debug;
import org.group_three.ui.world.WorldRoad;
import org.group_three.ui.world.WorldVehicle;

import java.io.IOException;

public class RoadDetailsController {
	@FXML
	private TextField id;
	@FXML
	private TextField displayName;
	@FXML
	private TextField sumoId;
	@FXML
	private TextField vehicleSpawnAmount;
	@FXML
	private TextField vehicleSpawnSpeed;
	@FXML
	private ColorPicker vehicleSpawnColor;
	@FXML
	private TextField vehicleSpawnRoute;

	private WorldRoad worldRoad;

	@FXML
	private void initialize() throws IOException {
		Debug.print("VehicleDetails Controller loaded.");

		vehicleSpawnSpeed.textProperty().addListener((_, oldText, newText) -> {
			try {
				//worldVehicle.getwVehicle().setSpeed(Math.abs(Double.parseDouble(newText)));
				//speed.textProperty().set(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
				vehicleSpawnSpeed.textProperty().set(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				vehicleSpawnSpeed.textProperty().set(oldText);
			}
			Debug.print("Speed changed.");
		});

		vehicleSpawnColor.valueProperty().addListener(
				(_, _, newColor) -> {
					try {
						//if (color.getValue() != worldVehicle.getColor()) {
						//worldVehicle.getwVehicle().setColor(Meth.convertColorToSumoColor(color.getValue()));
						//}
						//worldVehicle.getwVehicle().setColor(new SumoColor(0, 255, 0, 255));//Meth.convertColorToSumoColor(new Color(1,0,0,1)));
						//worldVehicle.setColor(newColor);
						//worldVehicle.getwVehicle().update();
					} catch (Exception e) {
						//throw new RuntimeException(e);
					}
					Debug.print("Color changed.");
				});
	}

	public void setup(WorldRoad worldRoad) {
		this.worldRoad = worldRoad;

		id.setText(worldRoad.getId());
		displayName.setText(worldRoad.getDisplayName());
		sumoId.setText(worldRoad.sumoRoad.getEdgeID());
	}
}
