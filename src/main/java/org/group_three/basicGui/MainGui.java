package org.group_three.basicGui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import javafx.animation.PauseTransition;
import javafx.util.Duration;



public class MainGui extends Application {

	// Basic GUI Adjustments
	String windowTitle = "Java-Sumo-Projekt-UAS";

	String[] colorPalette = {
		"#171D25",

		"#2D333C",
		"#24282F",
		"#3D4249",
		"#1999FF",
		"#DCDEDF",
		"#BFD8EE",
		"#24282F",
		"#606774",
		"#3E4047"
	};

	@Override
	public void start(Stage stage) {


		Label label2 = new Label("Hallo JavaFX 25 mit Maven!");
		StackPane root2 = new StackPane(label2);
		Scene scene2 = new Scene(root2, 400, 300);

		root2.setStyle("-fx-background-color: #BFD8EE;"); // Blau

		Label label = new Label("Hallo JavaFX 25 mit Maven!");
		StackPane root = new StackPane(label);
		Scene scene = new Scene(root, 400, 300);

		root.setStyle("-fx-background-color: #171D25;"); // Blau

		stage.setTitle(windowTitle);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/group_three/basicGui/icon.png")));
		stage.setScene(scene);
		stage.show();

		PauseTransition delay = new PauseTransition(Duration.seconds(5));
		delay.setOnFinished(event -> {
			// neue Scene hier setzen
			stage.setScene(scene2);
		});
		delay.play();
	}

	public static void main(String[] args) {
		launch(args);
	}
}