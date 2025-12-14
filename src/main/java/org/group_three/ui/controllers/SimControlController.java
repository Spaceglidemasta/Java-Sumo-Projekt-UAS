package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WEdge;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimControlController {

	@FXML
	private TextField speedModifier;

	private static double speedModValue = 1;

	private static double time = 1;

	/**
	 * Comment
	 *
	 * @throws IOException Throw-Comment
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
						speedModValue = Math.abs(Double.parseDouble(newText));
						if (speedModValue <= 0) speedModValue = 0.1;
						speedModifier.textProperty().set(String.valueOf(speedModValue));
						timeline.setRate(speedModValue);

					} catch (Exception e) {
						speedModifier.textProperty().set(String.valueOf(speedModValue));
					}

				});
	}

	private static Timeline timeline;

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onStepClicked() {
		//Debug.print("Step clicked.");
		SimController.getMainsimcon().step();
		time++;
		//Debug.print((int) time);
		SimView2D.update();
	}

	public static boolean isPlay() {
		return play;
	}

	public static void setPlay(boolean play) {
		SimControlController.play = play;

		if (play) {
			timeline.play();
		} else {
			timeline.stop();
		}
	}

	private static boolean play = false;

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onPlayClicked() {
		//Debug.print("Play clicked.");
		//setPlay(!isPlay());
		setPlay(true);
	}

	@FXML
	private void onPauseClicked() {
		setPlay(false);
	}

	private void onTick() {
		onStepClicked();

		/*List<WEdge> roads = WEdge.getAllroads();
		int randomIndex = ThreadLocalRandom.current().nextInt(roads.size());

		SumoStringList strings = new SumoStringList();
		strings.add(roads.get(randomIndex).getEdgeID());

		SimController.getMainsimcon().addVehicle(
				"DEFAULT_VEHTYPE",
				SimController.getMainsimcon().addRoute(strings),
				SimController.getMainsimcon().gettim,
				0,
				10
				,0
				).setColor(Meth.ClrToSumoClr(Color.GREEN));*/
	}
}
