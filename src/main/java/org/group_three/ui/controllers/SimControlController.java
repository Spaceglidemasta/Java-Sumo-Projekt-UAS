package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;

import java.io.IOException;

public class SimControlController {
	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @throws IOException
	 * Throw-Comment
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.print("Controls loaded.");
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onStepClicked() {
		Debug.print("Step clicked.");
		SimController.getMainsim().step();
	}
}
