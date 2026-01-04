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