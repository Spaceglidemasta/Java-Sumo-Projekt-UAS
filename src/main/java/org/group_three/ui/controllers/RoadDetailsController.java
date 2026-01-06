package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.WEdge;
import org.group_three.model.WVehicle;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.WorldObject;
import org.group_three.ui.world.WorldRoad;
import org.group_three.ui.world.WorldRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
	private Button routeSelectButton;


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

			String routeId;
			if (targetRouteId == null) {
				SumoStringList strings = new SumoStringList();
				strings.add(worldRoad.getwEdge().getId());

				int randomIndex = ThreadLocalRandom.current().nextInt(roads.size());
				strings.add(roads.get(randomIndex).getId());

				routeId = SimController.getMainsimcon().addRoute(strings);
			} else {
				routeId = targetRouteId;
			}


			Debug.print(SimController.getMainsimcon().getRouteEdges(routeId));

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
				Color vColor = vehicleSpawnColor.getValue().getOpacity() == 0 ? UI.getRandomVehicleColor() : vehicleSpawnColor.getValue();
				if (wVehicle != null) wVehicle.setColor(Meth.ClrToSumoClr(vColor));
			}
		}
        Debug.toConsole(vehicleSpawnAmount.textProperty().getValue() + " Vehicle(s) spawned on " + worldRoad.getId());
	}

	private String targetRouteId;

	private WorldRoute worldRoute;

	@FXML
	private void onRouteSelectPressed() {
		SimView2D.setRouteSelection(worldRoad);
	}

	public void routeSelected(WorldObject worldObject) {
		Debug.print(worldObject.getClass());
		Debug.print(WorldRoad.class);
		Debug.print(((WorldRoad) worldObject).getwEdge().getId());
		Debug.print(worldObject.getClass() == WorldRoad.class);
		if ( worldObject.getClass() == WorldRoad.class) {
			if (worldRoute != null) {
				worldRoute.remove();
				worldRoute = null;
			}


			SumoStringList route = new SumoStringList();
			route.add(worldRoad.getwEdge().getId());
			route.add(((WorldRoad) worldObject).getwEdge().getId());

			Debug.print(route);

			targetRouteId = SimController.getMainsimcon().addRoute(route);

			Debug.print(targetRouteId);

			if (targetRouteId != null) {

				worldRoute = new WorldRoute(
						worldRoad.getWorld(),
						worldRoad.getRenderTarget(),
						"",
						SimController.getMainsimcon().generateFullRoute(targetRouteId)
				);
			} else {
				targetRouteId = null;
			}

		}
	}

	public void deselect() {
		if (worldRoute != null) {
			worldRoute.remove();
			worldRoute = null;
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
