package org.group_three.ui.idkyet;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.*;



// Remove later!!!
public class SimulationView2D {

	public Group createView() {

		Label label2 = new Label("Hallo JavaFX 25 mit Maven!");
		StackPane root2 = new StackPane(label2);

		root2.setStyle("-fx-background-color: #BFD8EE;"); // Blau

		Label label = new Label("Hallo JavaFX 25 mit Maven!");
		StackPane root = new StackPane(label);
		Group root2d = new Group(root);

		root.setStyle("-fx-background-color: #171D25;"); // Blau

		return root2d;
	}

	// FXGL Lib???
}
