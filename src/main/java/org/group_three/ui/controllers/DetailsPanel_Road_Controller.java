package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.model.WEdge;
import org.group_three.model.WVehicle;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.WorldObject;
import org.group_three.ui.world.WorldRoad;
import org.group_three.ui.world.WorldRoute;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * The controller for the RoadDetails.
 *
 * @author Joel
 */
public class DetailsPanel_Road_Controller {

	// Logger
	private static final Logger log = Logger.getLogger(DetailsPanel_Road_Controller.class.getName());

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

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
	 * The world road that is owning this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings({"JavadocDeclaration", "FieldCanBeLocal"})
	private WorldRoad worldRoad;

	/**
	 * The target route id of the controlled object.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private String targetRouteId;

	/**
	 * The world route reference for when the controlled object is selected.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private WorldRoute worldRoute;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The initialize method of the road details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {
		log.info("Controller loaded.");

		// validate input for the vehicle spawn speed
		vehicleSpawnSpeed.textProperty().addListener((_, oldText, newText) -> {
			try {
				vehicleSpawnSpeed.setText(String.valueOf(Math.abs(Integer.parseInt(newText))));
			} catch (Exception e) {
				vehicleSpawnSpeed.setText(oldText);
				log.warning("InputValue: \"" + newText + "\" is not valid.");
			}
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

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The button to spawn vehicles on edge.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSpawnPressed() {
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// get all roads
		List<WEdge> roads = simcon.getAllroads().values().stream().toList();

		// try to spawn x amount of vehicles
		for (int i = 0; i < Integer.parseInt(vehicleSpawnAmount.textProperty().getValue()); i++) {

			String routeId;
			// create routes to random edges, if not target is given
			if (targetRouteId == null) {
				SumoStringList strings = new SumoStringList();
				strings.add(worldRoad.getwEdge().getId());

				int randomIndex = ThreadLocalRandom.current().nextInt(roads.size());
				strings.add(roads.get(randomIndex).getId());

				routeId = simcon.addRoute(strings);
			} else {
				routeId = targetRouteId;
			}

			// spawn vehicle if there is a valid route
			if (routeId != null) {
				log.info("Try Create Veh: " + routeId);
				WVehicle wVehicle = simcon.addVehicle(
						"DEFAULT_VEHTYPE",
						routeId,
						0,
						0,
						Integer.parseInt(vehicleSpawnSpeed.getText())
						, 0
				);
				// set vehicle color
				Color vColor = vehicleSpawnColor.getValue().getOpacity() == 0 ? UI.getRandomVehicleColor() : vehicleSpawnColor.getValue();
				if (wVehicle != null) wVehicle.setColor(Meth.ClrToSumoClr(vColor));
			}
		}

		log.info(vehicleSpawnAmount.textProperty().getValue() + " Vehicle(s) spawned on " + worldRoad.getId());
	}

	/**
	 * Triggers when the route selection button is pressed.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRouteSelectPressed() {
		SimView2D.setRouteSelection(worldRoad);
	}

	/**
	 * A method to change the route of the vehicles to spawn.
	 *
	 * @param worldObject The WorldRoad to drive to.
	 * @author Joel
	 */
	public void routeSelected(WorldObject worldObject) {
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// check if selection is a world road and if not cancel
		if (worldObject.getClass() != WorldRoad.class) {
			log.severe("Selection is not a WorldRoad: " + worldObject.getClass().getName());
			return;
		}

		// remove existing world route if present
		if (worldRoute != null) {
			worldRoute.remove();
			worldRoute = null;
		}

		// create route start and end points
		SumoStringList route = new SumoStringList();
		route.add(worldRoad.getwEdge().getId());
		WEdge wEdge = ((WorldRoad) worldObject).getwEdge();
		String tR = null;
		if (wEdge != null) tR = wEdge.getId();
		if (tR != null) route.add(tR);

		// create route id
		targetRouteId = simcon.addRoute(route);

		// if route id is valid add new world route
		if (targetRouteId != null) {
			worldRoute = new WorldRoute(
					worldRoad.getWorld(),
					worldRoad.getRenderTarget(),
					"",
					simcon.generateFullRoute(targetRouteId)
			);
		}
	}

	/**
	 * A method to remove the world route and its reference on deselection.
	 *
	 * @author Joel
	 */
	public void deselect() {
		if (worldRoute != null) {
			worldRoute.remove();
			worldRoute = null;
		}
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}