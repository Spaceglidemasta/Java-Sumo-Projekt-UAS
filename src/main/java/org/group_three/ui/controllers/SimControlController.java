package org.group_three.ui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.SimView2D;

import java.io.IOException;

public class SimControlController {
	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @throws IOException
	 * Throw-Comment
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.print("Controls loaded.");

		timeline = new Timeline(
				new KeyFrame(Duration.seconds(0.1), e -> onTick())
		);
		timeline.setCycleCount(Timeline.INDEFINITE);
	}

	private Timeline timeline;

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onStepClicked() {
		Debug.print("Step clicked.");
		SimController.getMainsim().step();
		SimView2D.update();
	}

	public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;

		if (play) {
			timeline.play();
		} else {
			timeline.stop();
		}
	}

	private boolean play = false;

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onPlayClicked() {
		Debug.print("Play clicked.");
		setPlay(!isPlay());
	}

	private void onTick() {
		onStepClicked();
	}
}
