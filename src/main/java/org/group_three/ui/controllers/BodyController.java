package org.group_three.ui.controllers;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.SimView2D;

/**
 * The body controller class.
 * Contains the SimulationViews, and sidebar with details panel,... .
 *
 * @author Joel
 */
public class BodyController {

	/**
	 * The subscene that will contain the simulation views.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private SubScene subsceneView;

	/**
	 * The binder of the subscene to scale it with the window when needed.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private Pane binder;

	/**
	 * The AnchorPane to place the FXML sidebar into.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private AnchorPane detailsAnchor;

	/**
	 * The same as detailsAnchor,
	 * but static to be able to access it from anywhere.
	 *
	 * @author Joel
	 * @see #detailsAnchor
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static AnchorPane detailsPanel;


	/**
	 * The method to initialize the BodyController.
	 * Gets called after FXML body creation.
	 *
	 * @throws IOException The fxml loading might throw an IOException.
	 * @author Joel
	 */
	@FXML
	public void initialize() throws IOException {
		detailsPanel = detailsAnchor;

		// bind the subscene size to the binder size to always resize it when the window size changes,
		// by default it always stays the same size even when the parent gets resized
		subsceneView.widthProperty().bind(binder.widthProperty());
		subsceneView.heightProperty().bind(binder.heightProperty());

		// decide which simulation view should be loaded
		switch (0) {

			//noinspection DataFlowIssue
			case 0: // 2d view
				// try loading fxml file
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/org/group_three/ui/fxml/CanvasView.fxml")
				);

				Parent root = loader.load();
				subsceneView.setRoot(root);
				break;

			case 1: // 3d view
				//subsceneView.setRoot(sv3d.createView());
				//subsceneView.setCamera(new PerspectiveCamera());
				break;
		}
	}

	/**
	 * A method to create and assign the details panel.
	 *
	 * @param fxmlPath The path to the FXML file of the details panel.
	 * @return The FXMLLoader of the details panel.
	 * @author Joel
	 */
    @MayReturnNull
	public static FXMLLoader setDetailsPanel(String fxmlPath) {
		// try loading fxml file
		FXMLLoader loader = new FXMLLoader(
				SimView2D.class.getResource(fxmlPath)
		);

		//Debug.print(loader.getLocation().getPath());

		Node detailsNode;
		try {
			detailsNode = loader.load();

			// set constraints
			AnchorPane.setLeftAnchor(detailsNode, 0.0);
			AnchorPane.setRightAnchor(detailsNode, 0.0);
			AnchorPane.setTopAnchor(detailsNode, 0.0);
			AnchorPane.setBottomAnchor(detailsNode, 0.0);

			// remove old details panel data and add new panel (from selected param)
			detailsPanel.getChildren().clear();
			detailsPanel.getChildren().add(detailsNode);

		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}


		return loader;
	}

}