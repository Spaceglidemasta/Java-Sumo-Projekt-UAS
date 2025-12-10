package org.group_three.ui.controllers;

import java.io.IOException;

import org.group_three.debug.Debug;

import javafx.fxml.FXML;

/**
 * The small bar at the bottom of the window.
 *
 * @author Joel
 */
public class TailController {
	/**
	 * The default initialize method.
	 *
	 * @throws IOException Throw-Comment
	 * @author Joel
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.toConsole("Tail loaded.");
	}

}