package org.group_three.ui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.group_three.debug.Console;
import org.group_three.utils.SystemUsage;

/**
 * The small bar at the bottom of the window.
 * No use yet.
 *
 * @author Joel
 */
public class MainWindow_Tail_Controller {

	/**
	 * The default initialize method.
	 *
	 * @author Joel
	 */
	@FXML
	public void initialize() {
		fpsDisplay = fps;

        Timeline systemUsageUpdates = new Timeline(new KeyFrame(Duration.seconds(1), e -> update()));
		systemUsageUpdates.setCycleCount(Timeline.INDEFINITE);
		systemUsageUpdates.play();

	}

	@FXML
	private Label cpu;
	@FXML
	private Label ram;

    private void update() {
		final int cpuPercent = SystemUsage.getSystemCpuPercent();
		final double used = SystemUsage.getUsedPhysicalMemoryGB();
		final double total = SystemUsage.getTotalPhysicalMemoryGB();

		cpu.setText("CPU: " + cpuPercent + "%");
		ram.setText(String.format("RAM %.1f/%.1fGB", used, total));
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