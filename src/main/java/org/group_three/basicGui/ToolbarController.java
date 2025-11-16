package org.group_three.basicGui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ToolbarController {

	// FX:ID's
	@FXML private Menu simulationOpenRecent;
	@FXML private MenuItem simulationClose;
	@FXML private MenuItem simulationReload;
	@FXML private MenuItem simulationExport;

	// Ini
	@FXML
	private void initialize() throws IOException {
		System.out.println("Toolbar loaded.");

		String[][] recentlyOpendSimulations = {{"Sim0", "SimulationPath0"},{"Sim1", "SimulationPath1"},{"Sim2", "SimulationPath2"}};

		initializeOpenRecentList(recentlyOpendSimulations);
	}

	private void initializeOpenRecentList(String[][] recentlyOpendSimulations)
	{
		for (String[] simulation : recentlyOpendSimulations) {
			MenuItem_RecentlyOpend item = new MenuItem_RecentlyOpend(simulation[0], simulation[1]);
			item.setOnAction(event -> onSimulationOpenRecentClicked(item));
			simulationOpenRecent.getItems().add(item);
		}
	}

	private void setSimulationLoaded(boolean loaded) //rename? load sim?? 100% needs rework
	{
		simulationClose.setDisable(!loaded);
		simulationReload.setDisable(!loaded);
		simulationExport.setDisable(!loaded);
	}

	// Simulation -> ButtonClicked
	@FXML
	private void onSimulationOpenClicked() {
		System.out.println("Simulation -> Open...");
		setSimulationLoaded(true);
	}

	private void onSimulationOpenRecentClicked(MenuItem_RecentlyOpend item) {
		System.out.println("Simulation -> OpenRecent -> " + item.getText() + " /" + item.getPath() + "/");
		setSimulationLoaded(true);
	}

	@FXML
	private void onSimulationCloseClicked() {
		System.out.println("Simulation -> Close");
		setSimulationLoaded(false);
	}

	@FXML
	private void onSimulationReloadClicked() {
		System.out.println("Simulation -> Reload");
	}
	
	@FXML
	private void onSimulationExportClicked() {
		System.out.println("Simulation -> Export");
	}

	@FXML
	private void onSimulationPreferencesClicked() {
		System.out.println("Simulation -> Preferences");
	}
	
}