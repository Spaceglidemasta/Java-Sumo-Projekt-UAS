package org.group_three.ui;

import org.group_three.debug.Debug;

import javafx.animation.RotateTransition;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class SimulationView3D {

	private SubScene subScene;

	public Group createView() {
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
		root3D.getChildren().add(box2);

		// Kamera
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setTranslateZ(-500);  // Kamera etwas zurück
		camera.setNearClip(0.1);
		camera.setFarClip(10000);

		// SubScene für 3D (empfohlen)
		subScene = new SubScene(root3D, 512, 512, true, SceneAntialiasing.DISABLED);
		subScene.setCamera(camera);
		subScene.setFill(Color.GRAY);


		// Haupt-Root (2D), hier könnte auch UI drüber liegen
		Group root3d = new Group(subScene);
		//Scene scene3d = new Scene(root3d, 800, 600, true);

		// einfache Rotation-Animation
		RotateTransition rt = new RotateTransition(Duration.seconds(10), box);
		rt.setAxis(Rotate.Y_AXIS);
		rt.setFromAngle(0);
		rt.setToAngle(360);
		rt.setCycleCount(RotateTransition.INDEFINITE);
		rt.play();
		//----------------------------onMouseClicked

		return root3d;
	}

	public void onMouseClicked(Pane binder) {
		Debug.print("Body -> SubScene -> SimulationView3D");
		//subScene.setCamera(null);

		subScene.widthProperty().bind(binder.widthProperty());
		subScene.heightProperty().bind(binder.heightProperty());

	}

}
