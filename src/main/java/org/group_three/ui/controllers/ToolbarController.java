package org.group_three.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// import java.io.IOException; for what was that?

import org.group_three.api.SimController;
import org.group_three.debug.Console;
import org.group_three.debug.exceptions.InvalidFilesSelected;
import org.group_three.debug.Debug;
import org.group_three.ui.FakeInteractions;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.group_three.ui.SimView2D;

/**
 * Controller for the toolbar to manage button interactions,
 * as well as loading the simulation
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
	 * Adds the recently opened simulations to the "recent" menu tab
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
     * Function to disable/reactivate the
     * "close", "reload" and "export" buttons when simulation is loaded
	 *
	 * @param disabled Param-Comment
	 * @author Joel
	 */
	private void setSimulationButtonStates(boolean disabled) {
		simulationClose.setDisable(disabled);
		simulationReload.setDisable(true); // not implemented yet
		simulationExport.setDisable(disabled);
	}

	private List<String> recentlyLoadedSimulations = new ArrayList<String>() {
	};
	private String loadedSimulation = null;

	/**
	 * Function to activate buttons related to the simulation,
     * such as "close" and add the opened simulation to
     * the top of the list of recently opened simulations
	 *
	 * @param path Takes in the path as String
	 * @author Joel
	 */
	private void setLoadedSimulation(String path) {
		if (path == null) {
			loadedSimulation = null;
			setSimulationButtonStates(true);
			return;
		}

		loadedSimulation = path;
		setSimulationButtonStates(false);

		// try to remove entry first before adding it at the start of the list to always have the newest selection at the first entry
		recentlyLoadedSimulations.remove(loadedSimulation);
		recentlyLoadedSimulations.addFirst(loadedSimulation);

		initializeOpenRecentList();

		Debug.toConsole(recentlyLoadedSimulations.size());
	}

	/**
	 * Function that tries to load the simulation from the passed in
     * config or xml files
	 *
	 * @param paths takes in the path(s) of the opened file(s)
	 * @author Joel
	 */
	private void tryLoadingSimulation(List<File> paths) {
		validateRecentlyLoadedSimulations();

		StringBuilder mergedPath = new StringBuilder();

		for (File path : paths) {
			mergedPath.append(path.getAbsolutePath() + "\n");
		}

		// don't attempt to load the same simulation if its currently loaded
		if (mergedPath.toString().equals(loadedSimulation))
			return; // ----------- add a check to not display the currently loaded file in recently opend

		// Normally we would handle errors with booleans (like every professional C lib), but apparently we need custom Exceptions.
		try {
			FakeInteractions.loadSimulation(paths);
			setLoadedSimulation(mergedPath.toString());
		} catch (InvalidFilesSelected ifs) {
			ifs.printStackTrace();
		}

	}

	/**
	 * Function to validate the recently opened File locations,
     * if no file is found in the entry is not displayed
	 *
	 * @author Joel
	 */
	private void validateRecentlyLoadedSimulations() {
		List<String> fails = new ArrayList<String>() {
		};

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
     * Function for "open" button to open fileChooser,
     * select a desired Sumo config or net as well as rou xml
     * file and try to load the simulation
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
	 * Function to load a recently selected simulation
	 *
	 * @param item Param-Comment
	 * @author Joel
	 */
	private void onSimulationOpenRecentClicked(MenuItem item) {
		Debug.toConsole("Simulation -> OpenRecent -> " + item.getText());

        List<File> files = new ArrayList<>();

        for(String strfile : item.getText().split("\n")){
            files.add(new File(strfile));
        }

		tryLoadingSimulation(files);
	}

	/**
	 * Function to close the simulation if the
     * "Close" button is clicked
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationCloseClicked() {
		Debug.toConsole("Simulation -> Close");
		setLoadedSimulation(null);
		SimController.getMainsimcon().close();
		SimView2D.newWorld();
	}

	/**
	 * Function to reload the already loaded simulation
	 * (Yet to be implemented)
	 * @author Joel
	 */
	@FXML
	private void onSimulationReloadClicked() {
		Debug.toConsole("Simulation -> Reload");
	}

	/**
	 * A method to export gathered data from the simulation as an .csv file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationExportClicked() {
		Debug.toConsole("Simulation -> Export");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export simulation");

		// desktop path, works for windows macOS and linux
		String desktopPath = System.getProperty("user.home") + "/Desktop";
		File desktopDir = new File(desktopPath);


		if (desktopDir.exists()) {
			fileChooser.setInitialDirectory(desktopDir);
		}

		// add selectable data types to the file chooser
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

		fileChooser.setInitialFileName("");

		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			String fileExtension = fileChooser.getSelectedExtensionFilter().getExtensions().getFirst().substring(1);

			SimController.getMainsimcon().saveState(fileExtension);
		}
	}

	/**
	 * A method to export gathered data from the simulation as a .xml file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationExportXmlClicked() {
		Debug.toConsole("Simulation -> Export -> .xml");
		SimController.getMainsimcon().saveState(".xml");
	}

	/**
	 * A method to export gathered data from the simulation as an .csv file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onSimulationExportCsvClicked() {
		Debug.toConsole("Simulation -> Export -> .csv");
		SimController.getMainsimcon().saveState(".csv");
	}

	/**
	 * Function for "Settings" Tab in toolbar
	 *
	 * @author Joel
	 */
	@FXML
	private void onSettingsClicked() {
		Debug.toConsole("Settings");
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

	/**
	 * Function for "Help" Tab in toolbar
	 *
	 * @author Joel
	 */
	@FXML
	private void onHelpClicked() {
		Debug.toConsole("Help");
	}

}