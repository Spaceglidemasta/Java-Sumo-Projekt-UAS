package org.group_three.basicGui;

import java.io.IOException;

import org.group_three.debug.Debug;

import javafx.fxml.FXML;

public class TailController {

	@FXML
	public void initialize() throws IOException {
		Debug.print("Tail loaded.");
	}

}