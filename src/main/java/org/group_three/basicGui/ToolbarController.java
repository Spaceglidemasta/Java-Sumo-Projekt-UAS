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
	}

	// Simulation -> ButtonClicked
	@FXML
	private void onSimulationOpenClicked() {
		System.out.println("Simulation -> Open...");
	}

	@FXML
	private void onSimulationCloseClicked() {
		System.out.println("Simulation -> Close");
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