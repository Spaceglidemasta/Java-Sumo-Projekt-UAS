package org.group_three.basicGui;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.animation.RotateTransition;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
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

		//--------------------------------------
		
		// GPT EXAMPLE
		// 3D Objekt
        Box box = new Box(100, 100, 100);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.DODGERBLUE);
        material.setSpecularColor(Color.LIGHTBLUE);
        box.setMaterial(material);

		// 3D Objekt
        Box box2 = new Box(200, 50, 50);
        PhongMaterial material2 = new PhongMaterial();
        material2.setDiffuseColor(Color.DODGERBLUE);
        material2.setSpecularColor(Color.LIGHTBLUE);
        box2.setMaterial(material2);

        // Licht
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-200);
        light.setTranslateY(-100);
        light.setTranslateZ(-200);

        AmbientLight ambient = new AmbientLight(Color.color(0.3, 0.3, 0.3));

        // Gruppe für 3D-Szene
        Group root3D = new Group(box, light, ambient);

        // Kamera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);  // Kamera etwas zurück
        camera.setNearClip(0.1);
        camera.setFarClip(10000);

        // SubScene für 3D (empfohlen)
        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.GRAY);

        // Haupt-Root (2D), hier könnte auch UI drüber liegen
        Group root3d = new Group(subScene);
        Scene scene3d = new Scene(root3d, 800, 600, true);

        // einfache Rotation-Animation
        RotateTransition rt = new RotateTransition(Duration.seconds(3), box);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.play();
		//----------------------------


		/*
		Label label2 = new Label("Hallo JavaFX 25 mit Maven!");
		StackPane root2 = new StackPane(label2);
		Scene scene2 = new Scene(root2, 400, 300);

		root2.setStyle("-fx-background-color: #BFD8EE;"); // Blau

		Label label = new Label("Hallo JavaFX 25 mit Maven!");
		StackPane root = new StackPane(label);
		Scene scene = new Scene(root, 400, 300);

		root.setStyle("-fx-background-color: #171D25;"); // Blau*/

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

	public static void main(String[] args) {
		launch(args);
	}
}