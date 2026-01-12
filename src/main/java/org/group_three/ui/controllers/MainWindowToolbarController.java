package org.group_three.ui.controllers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.exceptions.InvalidFilesSelected;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.group_three.service.StressTest;
import org.group_three.ui.MainApp;
import org.group_three.ui.RecentlyOpenedData;
import org.group_three.ui.SimView2D;

/**
 * Controller for the toolbar to manage button interactions,
 * as well as loading the simulation
 *
 * @author Joel
 */
public class MainWindowToolbarController {

	// Logger
	private static final Logger log = Logger.getLogger(MainWindowToolbarController.class.getName());

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The menu for recently opened simulations.
	 *
	 * @author Joel
	 */
	@FXML
	private Menu simulationOpenRecent;

	/**
	 * The button to close the currently active simulation.
	 *
	 * @author Joel
	 */
	@FXML
	private MenuItem simulationClose;

	/**
	 * The button to trigger a stress test.
	 *
	 * @author Joel
	 */
	@FXML
	private MenuItem stressTest;

	/**
	 * The menu for statistics exports.
	 *
	 * @author Joel
	 */
	@FXML
	private Menu export;

	/**
	 * The currently loaded simulation string path.
	 *
	 * @author Joel
	 */
	private String loadedSimulation = null;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Initializes toolbar
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {

		RecentlyOpenedData.load();
		RecentlyOpenedData.validate();

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
		for (String path : RecentlyOpenedData.getSimulations()) {
			MenuItem item = new MenuItem(path);
			item.setOnAction(_ -> onSimulationOpenRecentClicked(item));
			simulationOpenRecent.getItems().add(item);
		}

		// enable/disable button depending on entry count
		simulationOpenRecent.setDisable(simulationOpenRecent.getItems().isEmpty());
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++GetterSetterMethods++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Function to disable/reactivate the
	 * "close", "reload" and "export" buttons when simulation is loaded
	 *
	 * @param disabled If buttons should be disabled or not
	 * @author Joel
	 */
	private void setSimulationButtonStates(boolean disabled) {
		simulationClose.setDisable(disabled);
		export.setDisable(disabled);
		stressTest.setDisable(disabled);
	}

	/**
	 * Function to activate buttons related to the simulation,
	 * such as "close" and add the opened simulation to
	 * the top of the list of recently opened simulations.
	 * Also sets the button states to enabled/disabled.
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
		RecentlyOpenedData.removeSimulation(loadedSimulation);
		RecentlyOpenedData.addSimulation(loadedSimulation);

		initializeOpenRecentList();

	}

	//-----------------------------------------------GetterSetterMethods------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Function that tries to load the simulation from the passed in
	 * config or xml files
	 *
	 * @param paths takes in the path(s) of the opened file(s)
	 * @author Joel
	 */
	private void tryLoadingSimulation(List<File> paths) {
		RecentlyOpenedData.validate();
		initializeOpenRecentList();

		StringBuilder mergedPath = new StringBuilder();

		// merge the loaded paths into one string
		for (File path : paths) {
			mergedPath
					.append(path.getAbsolutePath())
					.append("\n");
		}

		// don't attempt to load the same simulation if its currently loaded
		if (mergedPath.toString().equals(loadedSimulation))
			return;

		// try to load the simulation
		try {
			SimController.loadSimulation(paths);
			setLoadedSimulation(mergedPath.toString());

		} catch (InvalidFilesSelected ifs) {
			log.severe("Simulation could not be loaded.");
		}
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
		// create new file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select file");

		// desktop path, works for windows macOS and linux
		String desktopPath = System.getProperty("user.home") + "/Desktop";
		File desktopDir = new File(desktopPath);

		// set default to desktop
		if (desktopDir.exists()) {
			fileChooser.setInitialDirectory(desktopDir);
		}

		// add selectable data types to the file chooser
		for (String[][] fileExtension : UI.simulationOpenFileExtensions) {
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileExtension[0][0], fileExtension[1]));
		}

		// show the actual dialog window, blocks until closed
		List<File> files = fileChooser.showOpenMultipleDialog(null);

		// if there are selected files try loading them
		if (files != null) tryLoadingSimulation(files);
	}

	/**
	 * Function to load a recently selected simulation
	 *
	 * @param item The menu item that represents file paths
	 * @author Joel, Luca
	 */
	private void onSimulationOpenRecentClicked(MenuItem item) {
		List<File> files = new ArrayList<>();

		// extract files from string
		for (String strfile : item.getText().split("\n")) {
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
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		setLoadedSimulation(null);
		simcon.close();
		SimView2D.newWorld();
	}

	/**
	 * A method to export gathered data from the simulation to a file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onExportCSV() {
		// validate sim controller
		if (SimController.getMainsimcon() == null) return;

		// try to spawn creation filter
		try {
			Stage pdfFilter = new Stage();
			pdfFilter.setTitle("Export as .csv");
			pdfFilter.getIcons().add(MainApp.getAppIcon());
			pdfFilter.setResizable(false);

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/group_three/ui/fxml/SaveWindowCreationFilter.fxml"));

			pdfFilter.setScene(new Scene(fxmlLoader.load()));

			((SaveWindowCreationFilterController) fxmlLoader.getController()).stage = pdfFilter;

			((SaveWindowCreationFilterController) fxmlLoader.getController()).disableStyle();

			pdfFilter.show();

		} catch (IOException e) {
			log.severe("Failed to create CreationFilter.");
		}
	}

	/**
	 * A method to export gathered data from the simulation to a file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onExportXML() {
		// get sim controller and validate
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		simcon.saveState(".xml");
	}

	/**
	 * A method to export gathered data from the simulation to a file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onExportPDF() {
		// validate sim controller
		if (SimController.getMainsimcon() == null) return;

		// try to spawn creation filter
		try {
			Stage pdfFilter = new Stage();
			pdfFilter.setTitle("Export as .pdf");
			pdfFilter.getIcons().add(MainApp.getAppIcon());
			pdfFilter.setResizable(false);

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/group_three/ui/fxml/SaveWindowCreationFilter.fxml"));

			pdfFilter.setScene(new Scene(fxmlLoader.load()));

			((SaveWindowCreationFilterController) fxmlLoader.getController()).stage = pdfFilter;

			pdfFilter.show();

		} catch (IOException e) {
			log.severe("Failed to create CreationFilter.");
		}
	}


	/**
	 * Function to perform a stresstest
	 *
	 * @author Leon
	 */
	@FXML
	private void onStressTestClick() {
		new StressTest().Test();
	}

	/**
	 * A button to open the user guide in a browser.
	 *
	 * @author Joel
	 */
	@FXML
	private void onUserGuide() {
		try {
			Desktop.getDesktop().browse(new URI(UI.userGuideLink));
		} catch (Exception e) {
			log.severe("Failed to open UserGuide.");
		}
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}