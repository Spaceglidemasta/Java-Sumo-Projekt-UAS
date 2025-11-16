package org.group_three.basicGui;

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

	public String getResourcePath(String resource)
	{
		String resourcePath = "/org/group_three/basicGui/";
		return resourcePath + resource + ".png";
	}

	public Image getAppIcon() //add error handling, if no "icon" is found
	{
		return new Image(getClass().getResourceAsStream(getResourcePath("icon")));
	}

	@Override
	public void start(Stage stage) throws Exception {

		


		

		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("/org/group_three/basicGui/fxml/MainWindow.fxml")
		);

        Parent root = loader.load();

        Scene scene = new Scene(root);



		// SPawn Window in the middle of the screen at 60% monitor size
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        double w = screen.getWidth() * 0.60;
        double h = screen.getHeight() * 0.60;

        stage.setWidth(w);
        stage.setHeight(h);

        stage.setX((screen.getWidth() - w) / 2);
        stage.setY((screen.getHeight() - h) / 2);

		System.out.println(stage.getX() + ":" + stage.getY());
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
			System.out.println("Switched scenes.");
		});
		delay.play();*/
		
	}

	public void start(String[] args)
	{
		launch(args);
	}

	//public static void main(String[] args) {
	//	launch(args);
	//}
}