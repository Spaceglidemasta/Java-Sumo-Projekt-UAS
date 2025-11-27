package org.group_three.ui.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
// import java.io.IOException; for what was that?

import org.group_three.debug.Console;
import org.group_three.ui.idkyet.MenuItem_RecentlyOpend;
import org.group_three.debug.Debug;
import org.group_three.ui.FakeInteractions;

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

		initializeOpenRecentList();
	}

	// adds the recent simulations to the open recent menu tab
	private void initializeOpenRecentList() {
		// clear entry list to avoid duplicates
		simulationOpenRecent.getItems().clear();

		// add options
		for (String path : recentlyLoadedSimulations) {
			//MenuItem_RecentlyOpend item = new MenuItem_RecentlyOpend(path, path);
			MenuItem item = new MenuItem(path);
			item.setOnAction(_ -> onSimulationOpenRecentClicked(item)); // _ = event
			simulationOpenRecent.getItems().add(item);
		}

		// enable/disable button depending on entry count
		simulationOpenRecent.setDisable(simulationOpenRecent.getItems().isEmpty());
	}


	// func to disable/reactivate the close, reload and export buttons when no simulation is loaded
	private void setSimulationButtonStates(boolean disabled) {
		simulationClose.setDisable(disabled);
		simulationReload.setDisable(disabled);
		simulationExport.setDisable(disabled);
	}

	private List<String> recentlyLoadedSimulations = new ArrayList<String>() {};
	private String loadedSimulation = null;

	private void setLoadedSimulation(String path) {
		if (path == null) {
			loadedSimulation = null;
			setSimulationButtonStates(false);
			return;
		}

		loadedSimulation = path;
		setSimulationButtonStates(true);

		// try to remove entry first before adding it at the start of the list to always have the newest selection at the first entry
		recentlyLoadedSimulations.remove(loadedSimulation);
		recentlyLoadedSimulations.addFirst(loadedSimulation);

		initializeOpenRecentList();

		Debug.toConsole(recentlyLoadedSimulations.size());
	}

	private void tryLoadingSimulation(String path) {
		// don't attempt to load the same simulation if its currently loaded
		if (path.equals(loadedSimulation)) return;

		if (FakeInteractions.loadSimulation(path)) {
			setLoadedSimulation(path);
		}
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

		// add selectable data types to the file chooser
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All", "*.sumocfg", "*.net.xml", "*.rou.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SUMO Config", "*.sumocfg"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Network", "*.net.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Route", "*.rou.xml"));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			// if a file was selected do something with it here
			Debug.toConsole("Selected: " + file.getName());
			Debug.toConsole(file.getPath());
			tryLoadingSimulation(file.getPath());
		}
	}

	/*private void onSimulationOpenRecentClicked(MenuItem_RecentlyOpend item) {
		Debug.toConsole("Simulation -> OpenRecent -> " + item.getText() + " /" + item.getPath() + "/");
		tryLoadingSimulation(item.getPath());
	}*/

	private void onSimulationOpenRecentClicked(MenuItem item) {
		Debug.toConsole("Simulation -> OpenRecent -> " + item.getText());
		tryLoadingSimulation(item.getText());
	}

	@FXML
	private void onSimulationCloseClicked() {
		Debug.toConsole("Simulation -> Close");
		setLoadedSimulation(null);
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