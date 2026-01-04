package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.group_three.debug.Console;

/**
 * The small bar at the bottom of the window.
 * No use yet.
 *
 * @author Joel
 */
public class TailController {

	/**
	 * The default initialize method.
	 *
	 * @author Joel
	 */
	@FXML
	public void initialize() {
		fpsDisplay = fps;

		/*Timeline systemUsageUpdates = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> update())
		);
		systemUsageUpdates.setCycleCount(Timeline.INDEFINITE);
		systemUsageUpdates.play();*/

	}

	@FXML
	private Label cpu;
	@FXML
	private Label ram;

	private void update() {
		//cpu.setText("CPU: " + SystemUsage.getCpuUsage() + "%");
		//ram.setText("RAM: " + SystemUsage.getRamUsage() + "/" + SystemUsage.getTotalRam() + "GB");
	}

	/**
	 * Function to open the console tab in the Settings meny
	 *
	 * @author Leon
	 */
	@FXML
	private void onConsoleOpen() {
		Console console = Console.getInstance();  // Get the single instance of the Console
		console.show();
	}

	@FXML
	private Label fps;

	private static Label fpsDisplay;

	public static void setFPS(int fps) {
		if (fpsDisplay == null) return;
		fpsDisplay.setText("FPS: " + fps);
	}

}