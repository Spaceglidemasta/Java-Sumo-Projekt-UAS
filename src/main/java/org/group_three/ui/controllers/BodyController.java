package org.group_three.ui.controllers;

import java.io.IOException;

//import org.group_three.ui.idkyet.SimulationView2D;
//import org.group_three.ui.idkyet.SimulationView3D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.WorldObject;

/**
 * basically has no real use yet, is just used as a proxy for the 2d view right now
 *
 * @author Joel
 */
public class BodyController {

	@FXML
	private SubScene subsceneView;

	@FXML
	private Pane binder;

	@FXML
	private AnchorPane detailsAnchor;

	private static AnchorPane detailsPanel;

	//private SimulationView3D sv3d;
	//private SimulationView2D sv2d;

	/**
	 * Comment
	 * Code-Snippet: {@code code}
	 *
	 * @author Joel
	 *
	 * @throws IOException
	 * Throw-Comment
	 */
	@FXML
	public void initialize() throws IOException {
		detailsPanel = detailsAnchor;
		Debug.toConsole("Body loaded.");
		//Debug.toConsole("SubScene: " + subsceneView);

		//sv3d = new SimulationView3D();
		//sv2d = new SimulationView2D();

		subsceneView.widthProperty().bind(binder.widthProperty());
		subsceneView.heightProperty().bind(binder.heightProperty());

		switch (2) {
			case 0:
				//subsceneView.setRoot(sv3d.createView());
				//subsceneView.setCamera(new PerspectiveCamera());
				break;

			case 1:
				//subsceneView.setRoot(sv2d.createView());
				break;

			case 2:
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/org/group_three/ui/fxml/CanvasView.fxml")
				);

				Parent root = loader.load();
				subsceneView.setRoot(root);
				break;
		}



	}

	public static FXMLLoader setDetailsPanel(String fxmlPath) {
		FXMLLoader loader = new FXMLLoader(
				SimView2D.class.getResource(fxmlPath)
		);

		Debug.print(loader.getLocation().getPath());

		Node detailsNode;
		try {
			detailsNode = loader.load();
			AnchorPane.setLeftAnchor(detailsNode, 0.0);
			AnchorPane.setRightAnchor(detailsNode, 0.0);
			AnchorPane.setTopAnchor(detailsNode, 0.0);
			AnchorPane.setBottomAnchor(detailsNode, 0.0);
			Debug.print(detailsNode);
			Debug.print(detailsPanel);
			Debug.print(detailsPanel.getChildren().size());
			detailsPanel.getChildren().clear();
			Debug.print(detailsPanel.getChildren().size());
			detailsPanel.getChildren().add(detailsNode);
			Debug.print(detailsPanel.getChildren().size());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}



		return loader;
	}

	/*@FXML
	private void onMouseClicked() {
		Debug.toConsole("Body -> SubScene");
		sv3d.onMouseClicked(binder);
	}*/

}