package org.group_three.ui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.group_three.debug.Console;
import org.group_three.utils.SystemUsage;

import java.util.logging.Logger;

/**
 * The small bar at the bottom of the window.
 * Contains a button to open the output log
 * and a performance display for fps, cpu and ram usage.
 *
 * @author Joel
 */
public class MainWindowTailController {

	// Logger
	private static final Logger log = Logger.getLogger(MainWindowTailController.class.getName());

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The static variant of "fps".
	 *
	 * @author Joel
	 */
	private static Label fpsDisplay;

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The label for the cpu usage.
	 *
	 * @author Joel
	 */
	@FXML
	private Label cpu;

	/**
	 * The label for the ram usage.
	 *
	 * @author Joel
	 */
	@FXML
	private Label ram;

	/**
	 * The non-static label for the fps.
	 *
	 * @author Joel
	 */
	@FXML
	private Label fps;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default initialize method.
	 *
	 * @author Joel
	 */
	@FXML
	public void initialize() {
		// set the static variable for the fps display
		fpsDisplay = fps;

		// timer to update system usage data
		Timeline systemUsageUpdates = new Timeline(new KeyFrame(Duration.seconds(1), _ -> update()));
		systemUsageUpdates.setCycleCount(Timeline.INDEFINITE);
		systemUsageUpdates.play();
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to set/update the fps display.
	 *
	 * @param fps The new fps value.
	 * @author Joel
	 */
	public static void setFPS(int fps) {
		// if static fps reference is not valid -> skip
		if (fpsDisplay == null) return;

		fpsDisplay.setText("FPS: " + fps);
	}

	//---------------------------------------------------ClassMethods---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * @author Leon
	 */
	private void update() {
		final int cpuPercent = SystemUsage.getSystemCpuPercent();
		final double used = SystemUsage.getUsedPhysicalMemoryGB();
		final double total = SystemUsage.getTotalPhysicalMemoryGB();

		cpu.setText("CPU: " + cpuPercent + "%");
		ram.setText(String.format("RAM %.1f/%.1fGB", used, total));
	}

	/**
	 * Function to open the console tab in the Settings many
	 *
	 * @author Leon
	 */
	@FXML
	private void onConsoleOpen() {
		Console console = Console.getInstance();  // Get the single instance of the Console
		console.show();

		log.info("Console opened.");
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}