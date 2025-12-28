package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.WVehicle;
import org.group_three.service.Table;
import org.group_three.model.WEdge;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * THe controller class for the gui simulation controls.
 *
 * @author Joel
 */
public class SimControlController {

	/**
	 * The speed mod text field to display and change the value via ui.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField speedModifier;

	@FXML
	private ToggleButton playPauseButton;

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A double to set the simulation play speed mod.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static double speedModValue = 1;

	/**
	 * The timeline that controls the trigger timing of the steps for the simulation.
	 * Runs on the JavaFX thread.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static Timeline timeline;

	/**
	 * A boolean to tell if the simulation is currently playing.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static boolean play = false;

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets if the simulation is currently playing.
	 *
	 * @return If the simulation is currently playing.
	 * @author Joel
	 */
	public static boolean isPlay() {
		return play;
	}

	/**
	 * Sets if the simulation should play or not.
	 *
	 * @param play If the simulation should play.
	 * @author Joel
	 */
	public static void setPlay(boolean play) {
		SimControlController.play = play;

		if (play) {
			timeline.play();
		} else {
			timeline.stop();
		}
	}

	//---------------------------------------------------ClassMethods---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default ini method of the controller.
	 *
	 * @throws IOException The default exception to the controller.
	 * @author Joel
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.print("Controls loaded.");

		timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> onTick())
		);
		timeline.setCycleCount(Timeline.INDEFINITE);

		speedModifier.textProperty().addListener(
				(obs, oldText, newText) -> {
					try {
						speedModValue = Math.clamp(Math.abs(Double.parseDouble(newText)), UI.simulationSpeedLimit.x, UI.simulationSpeedLimit.y);
						speedModifier.textProperty().set(String.valueOf(speedModValue));
						timeline.setRate(speedModValue);

					} catch (Exception e) {
						speedModifier.textProperty().set(String.valueOf(speedModValue));
					}

				});
	}

	/**
	 * Triggers when the gui step button is pressed.
	 *
	 * @author Joel
	 */
	@FXML
	private void onStepClicked() {

        /*Print table example
        Table cars = new Table("vehID", "Color", "Position");

        SimController sc = SimController.getMainsimcon();

        for(String VID : sc.getVehicleIDList()){
            WVehicle v = new WVehicle(VID, sc.getStc());
            v.update();
            cars.add(
                    VID,
                    v.getColor().toString(),
                    v.getPos().toString()
            );
        }

        cars.print();
        */

		//Debug.print("Step clicked.");

        SimController simcon = SimController.getMainsimcon();
        if(simcon == null) return;

		SimController.getMainsimcon().step();
		SimView2D.update();
	}

	/**
	 * Triggers when the gui play button is pressed.
	 *
	 * @author Joel
	 */
	private void onPlayClicked() {
		//Debug.print("Play clicked.");
		//setPlay(!isPlay());

        if(SimController.getMainsimcon() != null) setPlay(true);
	}

	/**
	 * Triggers when the gui pause button is pressed.
	 *
	 * @author Joel
	 */
	private void onPauseClicked() {
		setPlay(false);
	}

	@FXML
	private void onPlayPauseClicked() {
		if (playPauseButton.isSelected()) {
			onPlayClicked();
		} else {
			onPauseClicked();
		}
	}

	/**
	 * The tick method for the simulation replay.
	 *
	 * @author Joel
	 */
	private void onTick() {
		onStepClicked();
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}