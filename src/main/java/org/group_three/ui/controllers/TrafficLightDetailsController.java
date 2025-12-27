package org.group_three.ui.controllers;

import de.tudresden.sumo.cmd.Trafficlight;
import it.polito.appeal.traci.SumoTraciConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.WorldRoute;
import org.group_three.ui.world.WorldTrafficLight;
import org.group_three.ui.world.WorldVehicle;

/**
 * The controller for the VehicleDetails.
 *
 * @author Joel
 */
public class TrafficLightDetailsController {

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

    private  boolean active = false;

	@FXML
	private TextField remainingRedTime;
	@FXML
	private TextField remainingYellowTime;
	@FXML
	private TextField remainingGreenTime;
	@FXML
	private Button redButton;
	@FXML
	private Button yellowButton;
	@FXML
	private Button greenButton;
    @FXML
    private Button resetButton;
	@FXML
	private CheckBox override;


	private WorldTrafficLight worldTrafficLight;

	public void setup(WorldTrafficLight worldTrafficLight) {
		this.worldTrafficLight = worldTrafficLight;

		id.setText(this.worldTrafficLight.getId());
		displayName.setText(this.worldTrafficLight.getDisplayName());
		sumoId.setText(this.worldTrafficLight.getwTrafficLight().getId());
		override.setSelected(this.worldTrafficLight.getwTrafficLight().isCustomPhasesActive());

		override.selectedProperty().addListener((_, _, newValue) -> {
			this.worldTrafficLight.getwTrafficLight().setCustomPhasesActive(newValue);
		});

	}


	/**
	 * The initialize method of the vehicle details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {


	}

    @FXML
    private void onCheckBoxClicked(ActionEvent event) {
        this.active = override.isSelected();    }

	/**
	 * The event to toggle view locking of the vehicle.
	 *
	 * @author Joel
	 */
	@FXML
	private void onRedButtonClicked() {
        if(active){
            String stateString = SimController.getMainsimcon().getRYGState(this.worldTrafficLight.getwTrafficLight().getId());
            int length = stateString.length();
            String rString = "r".repeat(length);
            SimController.getMainsimcon().setRYGState(this.worldTrafficLight.getwTrafficLight().getId(),rString);
        }
	}

	/**
	 * The event to toggle view locking of the vehicle.
	 *
	 * @author Joel
	 */
	@FXML
	private void onYellowButtonClicked() {
        if(active){
            String stateString = SimController.getMainsimcon().getRYGState(this.worldTrafficLight.getwTrafficLight().getId());
            int length = stateString.length();
            String rString = "y".repeat(length);
            SimController.getMainsimcon().setRYGState(this.worldTrafficLight.getwTrafficLight().getId(),rString);
        }
	}

	/**
	 * The event to toggle view locking of the vehicle.
	 *
	 * @author Joel
	 */
	@FXML
	private void onGreenButtonClicked() {
        if(active){
            String stateString = SimController.getMainsimcon().getRYGState(this.worldTrafficLight.getwTrafficLight().getId());
            int length = stateString.length();
            String rString = "G".repeat(length);
            SimController.getMainsimcon().setRYGState(this.worldTrafficLight.getwTrafficLight().getId(),rString);
        }
	}


    private SumoTraciConnection stc;

    @FXML
    private void onResetButtonClicked() throws Exception {
        if(active){
            Debug.print("Reset Button");
        }
    }



}
