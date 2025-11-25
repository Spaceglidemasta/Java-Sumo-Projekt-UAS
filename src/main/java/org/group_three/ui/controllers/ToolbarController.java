package org.group_three.ui.controllers;

import java.io.File;
// import java.io.IOException; for what was that?

import org.group_three.debug.Console;
import org.group_three.ui.idkyet.MenuItem_RecentlyOpend;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class ToolbarController {

	// FX:ID's
	@FXML
	private Menu simulationOpenRecent;
	@FXML
	private MenuItem simulationClose;
	@FXML
	private MenuItem simulationReload;
	@FXML
	private MenuItem simulationExport;

	// Ini
	@FXML
	private void initialize() { //throws IOException { for what was that?
		Debug.toConsole("Toolbar loaded.");

		String[][] recentlyOpenedSimulations = {{"Sim0", "SimulationPath0"}, {"Sim1", "SimulationPath1"}, {"Sim2", "SimulationPath2"}};

		initializeOpenRecentList(recentlyOpenedSimulations);
	}

	private void initializeOpenRecentList(String[][] recentlyOpenedSimulations) {
		for (String[] simulation : recentlyOpenedSimulations) {
			MenuItem_RecentlyOpend item = new MenuItem_RecentlyOpend(simulation[0], simulation[1]);
			item.setOnAction(_ -> onSimulationOpenRecentClicked(item)); // _ = event
			simulationOpenRecent.getItems().add(item);
		}
	}


	private void setSimulationLoaded(boolean loaded) { //rename? load sim?? 100% needs rework
		simulationClose.setDisable(!loaded);
		simulationReload.setDisable(!loaded);
		simulationExport.setDisable(!loaded);
	}


	// Simulation -> ButtonClicked
	@FXML
	private void onSimulationOpenClicked() {
		Debug.toConsole("Simulation -> Open...");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select file");

		// desktop path, works for windows macOS and linux
		String desktopPath = System.getProperty("user.home") + "/Desktop";
		File desktopDir = new File(desktopPath);

		if (desktopDir.exists()) {
			fileChooser.setInitialDirectory(desktopDir);
		}

		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*.sumocfg", "*.net.xml", "*.rou.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SUMO Config", "*.sumocfg"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Network", "*.net.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Route", "*.rou.xml"));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			Debug.toConsole("Selected: " + file);
			setSimulationLoaded(true);
		}
	}

	private void onSimulationOpenRecentClicked(MenuItem_RecentlyOpend item) {
		Debug.toConsole("Simulation -> OpenRecent -> " + item.getText() + " /" + item.getPath() + "/");
		setSimulationLoaded(true);
	}

	@FXML
	private void onSimulationCloseClicked() {
		Debug.toConsole("Simulation -> Close");
		setSimulationLoaded(false);
	}

	@FXML
	private void onSimulationReloadClicked() {
		Debug.toConsole("Simulation -> Reload");
	}

	@FXML
	private void onSimulationExportClicked() {
		Debug.toConsole("Simulation -> Export");
	}

	@FXML
	private void onSettingsClicked() {
		Debug.toConsole("Settings");
	}

    @FXML
    private void onConsoleOpen() {
        Console console = Console.getInstance();  // Get the single instance of the Console
        console.show();  // Show the debug window
        console.log("Debug window opened.");
    }

	@FXML
	private void onHelpClicked() {
		Debug.toConsole("Help");
	}

}