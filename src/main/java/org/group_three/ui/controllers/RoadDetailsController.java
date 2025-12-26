package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoStringList;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WEdge;
import org.group_three.model.WVehicle;
import org.group_three.ui.Meth;
import org.group_three.ui.world.WorldRoad;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

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
				vehicleSpawnSpeed.setText(String.valueOf(Math.abs(Integer.parseInt(newText))));
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

		vehicleSpawnRoute.setDisable(true); // route is always random currently, will be changed
	}

	/**
	 * The button to spawn vehicles on edge.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSpawnPressed() {

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

		List<WEdge> roads = simcon.getAllroads().values().stream().toList();

		for (int i = 0; i < Integer.parseInt(vehicleSpawnAmount.textProperty().getValue()); i++) {
			SumoStringList strings = new SumoStringList();
			strings.add(worldRoad.getwEdge().getId());

			int randomIndex = ThreadLocalRandom.current().nextInt(roads.size());
			strings.add(roads.get(randomIndex).getId());

			String routeId = SimController.getMainsimcon().addRoute(strings);

			if (routeId != null) {
				Debug.print("Try Create Veh: " + routeId);
				WVehicle wVehicle = SimController.getMainsimcon().addVehicle(
						"DEFAULT_VEHTYPE",
						routeId,
						0,
						0,
						Integer.parseInt(vehicleSpawnSpeed.getText())
						,0
				);
				if (wVehicle != null) wVehicle.setColor(Meth.ClrToSumoClr(vehicleSpawnColor.getValue()));
			}
		}
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
