package org.group_three.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import java.io.IOException; for what was that?

import org.group_three.debug.Console;
import org.group_three.debug.exceptions.InvalidFilesSelected;
import org.group_three.debug.Debug;
import org.group_three.ui.FakeInteractions;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

/**
 * Comment
 *
 * @author Joel
 */
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

	// TODO: Fix issues from multi file selection

	/**
	 * Initializes toolbar
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() { //throws IOException  for what was that?
		Debug.toConsole("Toolbar loaded.");

		// --> add load recentlyLoadedSimulations from file code here <--
		validateRecentlyLoadedSimulations();

		initializeOpenRecentList();
	}

	/**
	 * adds the recent simulations to the open recent menu tab
	 *
	 * @author Joel
	 */
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

	/**
	 * func to disable/reactivate the close, reload and export buttons when no simulation is loaded
	 *
	 * @author Joel
	 *
	 * @param disabled
	 * Param-Comment
	 */
	private void setSimulationButtonStates(boolean disabled) {
		simulationClose.setDisable(disabled);
		simulationReload.setDisable(disabled);
		simulationExport.setDisable(disabled);
	}

	private List<String> recentlyLoadedSimulations = new ArrayList<String>() {};
	private String loadedSimulation = null;

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param path
	 * Param-Comment
	 */
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

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param paths
	 * Param-Comment
	 */
	private void tryLoadingSimulation(List<File> paths) {
		validateRecentlyLoadedSimulations();

		StringBuilder mergedPath = new StringBuilder();

		for (File path : paths) {
			mergedPath.append(path.getAbsolutePath());
		}

		// don't attempt to load the same simulation if its currently loaded
		if (mergedPath.toString().equals(loadedSimulation)) return; // ----------- add a check to not display the currently loaded file in recently opend

        // Normally we would handle errors with booleans (like every professional C lib), but apparently we need custom Exceptions.
        try {
            FakeInteractions.loadSimulation(paths);
            setLoadedSimulation(mergedPath.toString());
        }
        catch (InvalidFilesSelected ifs){
            ifs.printStackTrace();
        }

	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	private void validateRecentlyLoadedSimulations() {
		List<String> fails = new ArrayList<String>() {};

		for (String path : recentlyLoadedSimulations) {
			try {
				new FileReader(path).close();
			} catch (FileNotFoundException e) {
				Debug.toConsole("FileNotFoundException " + e.getMessage());
				// remove path if file at path doesn't exist
				fails.add(path); // don't modify looping list while using it

			} catch (IOException e) {
				Debug.toConsole("IOException e " + e.getMessage());
				throw new RuntimeException(e);
			}
		}

		for (String path : fails) {
			recentlyLoadedSimulations.remove(path);
		}

		initializeOpenRecentList(); // maybe? !makes ini run multiple times in some places right now, TODO:change that
	}


	/**
	 * Simulation -> ButtonClicked
	 *
	 * @author Joel
	 */
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

		List<File> files = fileChooser.showOpenMultipleDialog(null);
		if (files != null) {
			//List<String> paths = new ArrayList<String>() {};

			for (File file : files) {
				// if a file was selected do something with it here
				Debug.toConsole("Selected: " + file.getName());
				Debug.toConsole(file.getPath());
				//paths.add(file.getPath());
			}

			tryLoadingSimulation(files);
		}
	}

	/*private void onSimulationOpenRecentClicked(MenuItem_RecentlyOpend item) {
		Debug.toConsole("Simulation -> OpenRecent -> " + item.getText() + " /" + item.getPath() + "/");
		tryLoadingSimulation(item.getPath());
	}*/

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param item
	 * Param-Comment
	 */
	private void onSimulationOpenRecentClicked(MenuItem item) {
		Debug.toConsole("Simulation -> OpenRecent -> " + item.getText());
		tryLoadingSimulation(new ArrayList<>());
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationCloseClicked() {
		Debug.toConsole("Simulation -> Close");
		setLoadedSimulation(null);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationReloadClicked() {
		Debug.toConsole("Simulation -> Reload");
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationExportClicked() {
		Debug.toConsole("Simulation -> Export");
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onSettingsClicked() {
		Debug.toConsole("Settings");
	}

	/**
	 * Comment
	 *
	 * @author Leon
	 */
    @FXML
    private void onConsoleOpen() {
        Console console = Console.getInstance();  // Get the single instance of the Console
        console.show();  // Show the debug window
        console.log("Debug window opened.");
    }

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onHelpClicked() {
		Debug.toConsole("Help");
	}

}