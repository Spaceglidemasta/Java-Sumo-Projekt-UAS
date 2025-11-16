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
	public void initialize() throws IOException {
		System.out.println("Toolbar loaded.");

		String[] recentlyOpendSimulations = {"0", "1", "2", "3"};

		initializeOpenRecentList(recentlyOpendSimulations);
	}

	private void initializeOpenRecentList(String[] recentlyOpendSimulations)
	{
		for (String simulation : recentlyOpendSimulations) {
			MenuItem item = new MenuItem(simulation);
			item.setOnAction(e -> onSimulationOpenRecentClicked(item));
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

	private void onSimulationOpenRecentClicked(MenuItem item) {
		System.out.println("Simulation -> OpenRecent -> " + item.getText());
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