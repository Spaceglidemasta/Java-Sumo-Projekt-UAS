package org.group_three.ui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import java.io.IOException; for what was that?

import de.tudresden.sumo.objects.SumoColor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.group_three.Main;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.constants.enums.stats.EdgeSortOption;
import org.group_three.debug.exceptions.InvalidFilesSelected;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.group_three.service.StressTest;
import org.group_three.ui.MainApp;
import org.group_three.ui.Meth;
import org.group_three.ui.RecentlyOpenedData;
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
	private MenuItem stressTest;
	@FXML
	private Menu export;

	/**
	 * Initializes toolbar
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {
		Debug.toConsole("Toolbar loaded.");

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
		export.setDisable(disabled);
		stressTest.setDisable(disabled);
	}

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
		RecentlyOpenedData.removeSimulation(loadedSimulation);
		RecentlyOpenedData.addSimulation(loadedSimulation);

		initializeOpenRecentList();

		Debug.toConsole(RecentlyOpenedData.getSimulations().size());
	}

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

		for (File path : paths) {
			mergedPath.append(path.getAbsolutePath() + "\n");
		}

		// don't attempt to load the same simulation if its currently loaded
		if (mergedPath.toString().equals(loadedSimulation))
			return; // ----------- add a check to not display the currently loaded file in recently opend


		try {
			SimController.loadSimulation(paths);
			setLoadedSimulation(mergedPath.toString());
		} catch (InvalidFilesSelected ifs) {
			ifs.printStackTrace();
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
		for (String[][] fileExtension : UI.simulationOpenFileExtensions) {
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileExtension[0][0], fileExtension[1]));
		}


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
	 * A method to export gathered data from the simulation to a file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onExportCSV() {
		Debug.toConsole("Simulation -> Export");

		if(!SimController.isValid()) return;

		try {
			Stage pdfFilter = new Stage();
			pdfFilter.setTitle("Export as .csv");
			pdfFilter.getIcons().add(MainApp.getAppIcon());
			pdfFilter.setResizable(false);

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/group_three/ui/fxml/PDFCreationFilter.fxml"));

			pdfFilter.setScene(new Scene(fxmlLoader.load()));

			((PDFCreationFilterController) fxmlLoader.getController()).stage = pdfFilter;

			pdfFilter.show();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A method to export gathered data from the simulation to a file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onExportXML() {
		if(!SimController.isValid()) return;

		SimController.getMainsimcon().saveState(".xml");

	}

	/**
	 * A method to export gathered data from the simulation to a file.
	 *
	 * @author Joel
	 */
	@FXML
	private void onExportPDF() {

		if(!SimController.isValid()) return;

		try {
			Stage pdfFilter = new Stage();
			pdfFilter.setTitle("Export as .pdf");
			pdfFilter.getIcons().add(MainApp.getAppIcon());
			pdfFilter.setResizable(false);

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/group_three/ui/fxml/PDFCreationFilter.fxml"));

			pdfFilter.setScene(new Scene(fxmlLoader.load()));

			((PDFCreationFilterController) fxmlLoader.getController()).stage = pdfFilter;

			pdfFilter.show();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}


	}



    /**
     * Function to perform a stresstest
     *
     * @author Leon
     */
    @FXML
    private void onStressTestClick(){
        new StressTest().Test();
        }

}