package org.group_three.basicGui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;

public class BodyController {

	@FXML
	private SubScene subsceneView;

	@FXML
	public void initialize() throws IOException {
		System.out.println("Body loaded.");
		//System.out.println("SubScene: " + subsceneView);

		SimulationView3D sv3d = new SimulationView3D();
		SimulationView2D sv2d = new SimulationView2D();


		switch (0)
		{
			case 0:
				subsceneView.setRoot(sv3d.createView());
				subsceneView.setCamera(new PerspectiveCamera());
			break;

			case 1:
				subsceneView.setRoot(sv2d.createView());
			break;

			case 2:
				FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/org/group_three/basicGui/fxml/RandomTestView.fxml")
				);

				Parent root = loader.load();
				subsceneView.setRoot(root);
			break;
		}


	}

}