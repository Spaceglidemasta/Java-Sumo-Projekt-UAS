package org.group_three.basicGui;

import org.group_three.debug.Debug;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;


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

	public String getResourcePath(String resource) {
		String resourcePath = "/org/group_three/basicGui/";
		return resourcePath + resource + ".png";
	}

	public Image getAppIcon() //add error handling, if no "icon" is found
	{
		return new Image(getClass().getResourceAsStream(getResourcePath("SumoLogoAdjustments3")));
	}

	@Override
	public void start(Stage stage) throws Exception {


		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/group_three/basicGui/fxml/MainWindow.fxml")
		);

		Parent root = loader.load();

		Scene scene = new Scene(root);

		stage.setMinWidth(960);   // Mindestbreite
		stage.setMinHeight(540);  // Mindesthöhe


		// SPawn Window in the middle of the screen at 60% monitor size
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();

		double w = screen.getWidth() * 0.60;
		double h = screen.getHeight() * 0.60;

		stage.setWidth(w);
		stage.setHeight(h);

		stage.setX((screen.getWidth() - w) / 2);
		stage.setY((screen.getHeight() - h) / 2);

		Debug.print(stage.getX() + ":" + stage.getY());
		//-------------------------------------------------------------


		// CSS hinzufügen
		//scene.getStylesheets().add(
		//        getClass().getResource("style.css").toExternalForm()
		//);


		stage.setTitle(windowTitle);
		stage.getIcons().add(getAppIcon());
		stage.setScene(scene);
		stage.show();

		/*PauseTransition delay = new PauseTransition(Duration.seconds(5));
		delay.setOnFinished(event -> {
			// neue Scene hier setzen
			stage.setScene(scene2);
			Debug.print("Switched scenes.");
		});
		delay.play();*/

		/*
		Vector2D v0 = new Vector2D(0,0);
		Vector2D v1 = new Vector2D(10,15);
		Vector2D v2 = new Vector2D(50,75);

		Debug.print(Meth.getRelativeLocation(v1, 0, v0).toString());
		Debug.print(Meth.getRelativeLocation(v2, 0, v1).toString());
		Debug.print(Meth.getRelativeLocation(v1, 45, v0).toString());
		Debug.print(Meth.getRelativeLocation(v2, 45, v1).toString());
		Debug.print(Meth.getRelativeLocation(v1, 90, v0).toString());
		Debug.print(Meth.getRelativeLocation(v2, 90, v1).toString());

		Debug.print(Meth.addRelativeLocation(v0, 0, v1).toString());
		Debug.print(Meth.addRelativeLocation(v0, 45, v1).toString());
		Debug.print(Meth.addRelativeLocation(v0, 90, v1).toString());
		Debug.print(Meth.addRelativeLocation(v0, 180, v1).toString());
		*/
	}

	public void start(String[] args) {
		launch(args);
	}

	//public static void main(String[] args) {
	//	launch(args);
	//}
}