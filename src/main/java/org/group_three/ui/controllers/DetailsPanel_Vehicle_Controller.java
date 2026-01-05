package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.*;

/**
 * The controller for the VehicleDetails.
 *
 * @author Joel
 */
public class DetailsPanel_Vehicle_Controller {

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
	 * The vehicle speed to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField speedFactor;

	/**
	 * The vehicle color to display.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private ColorPicker color;

	/**
	 * The world vehicle that is owning this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private WorldVehicle worldVehicle;

	/**
	 * The button to enable route selection.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private Button selectRouteButton;


	/**
	 * The initialize method of the vehicle details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {

		speedFactor.textProperty().addListener((_, oldText, newText) -> {
			try {
				// set vehicle speed to a corrected value (for example "EEE" is not a valid speed) from the input
				worldVehicle.getwVehicle().setSpeedFactor(Math.abs(Double.parseDouble(newText)));
				speedFactor.setText(String.valueOf(Math.abs(Double.parseDouble(newText))));
			} catch (Exception e) {
				speedFactor.textProperty().set(oldText);
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

	@FXML
	private void onRouteSelectPressed() {
		SimView2D.setRouteSelection(worldVehicle);
	}

	private String targetRouteId;

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
			route.add(worldVehicle.getwVehicle().getRouteEdges().get(worldVehicle.getwVehicle().getRouteIndex()));
			route.add(((WorldRoad) worldObject).getwEdge().getId());

			Debug.print(route);

			targetRouteId = SimController.getMainsimcon().addRoute(route);

			Debug.print(targetRouteId);
			//Debug.print(SimController.getMainsimcon().get);

			if (targetRouteId != null) {

				worldVehicle.getwVehicle().changeRoute(route);

				//worldVehicle.getwVehicle().setRoute(targetRouteId);
				//worldVehicle.getwVehicle().reroute();



				worldRoute = new WorldRoute(
						worldVehicle.getWorld(),
						worldVehicle.getWorld().worldStaticRenderTarget,
						"",
						worldVehicle.getwVehicle().getRouteEdges());

			} else {
				targetRouteId = null;
			}

		}
	}

	private WorldRoute worldRoute;

	public void createWorlVehicleRoute() {
		worldRoute = new WorldRoute(
				worldVehicle.getWorld(),
				worldVehicle.getWorld().worldStaticRenderTarget,
				"",
				worldVehicle.getwVehicle().getRouteEdges());
	}

	public void removeWorlVehicleRoute() {
		worldRoute.remove();
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
		speedFactor.setText(String.valueOf(worldVehicle.getwVehicle().getSpeedFactor()));
		color.setValue(worldVehicle.getColor());
	}

	/**
	 * The update method for this class to update its data.
	 *
	 * @author Joel
	 */
	public void update() {
		speed.setText(String.valueOf(worldVehicle.getwVehicle().getSpeed()));
		speedFactor.setText(String.valueOf(worldVehicle.getwVehicle().getSpeedFactor()));
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
		selectRouteButton.setDisable(true);
		speedFactor.setDisable(true);
		removeWorlVehicleRoute();
	}
}
