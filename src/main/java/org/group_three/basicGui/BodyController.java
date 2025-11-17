package org.group_three.basicGui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;

public class BodyController {

	@FXML private SubScene subsceneView;

	@FXML private Pane binder;

	private SimulationView3D sv3d;
	private SimulationView2D sv2d;

	@FXML
	public void initialize() throws IOException {
		System.out.println("Body loaded.");
		//System.out.println("SubScene: " + subsceneView);

		sv3d = new SimulationView3D();
		sv2d = new SimulationView2D();

		subsceneView.widthProperty().bind(binder.widthProperty());
    	subsceneView.heightProperty().bind(binder.heightProperty());

		switch (2)
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
				getClass().getResource("/org/group_three/basicGui/fxml/CanvasView.fxml")
				);

				Parent root = loader.load();
				subsceneView.setRoot(root);
			break;
		}


	}

	/*@FXML
	private void onMouseClicked() {
		System.out.println("Body -> SubScene");
		sv3d.onMouseClicked(binder);
	}*/

}