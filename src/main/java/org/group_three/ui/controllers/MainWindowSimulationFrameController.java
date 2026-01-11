package org.group_three.ui.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

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
public class MainWindowSimulationFrameController {

	// Logger
	private static final Logger log = Logger.getLogger(MainWindowSimulationFrameController.class.getName());

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The same as detailsAnchor,
	 * but static to be able to access it from anywhere.
	 *
	 * @author Joel
	 * @see #detailsAnchor
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static AnchorPane detailsPanel;

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

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

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The method to initialize the MainWindowSimulationFrameController.
	 * Gets called after FXML body creation.
	 *
	 * @author Joel
	 */
	@FXML
	public void initialize() {
		detailsPanel = detailsAnchor;

		// bind the subscene size to the binder size to always resize it when the window size changes,
		// by default it always stays the same size even when the parent gets resized
		subsceneView.widthProperty().bind(binder.widthProperty());
		subsceneView.heightProperty().bind(binder.heightProperty());

		// try to load 2d view
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/org/group_three/ui/fxml/SimulationView.fxml")
			);
			Parent root = loader.load();
			subsceneView.setRoot(root);

		} catch (IOException e) {
			log.severe("Failed to load SimulationView.");
		}
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

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

		// try to set up details panel
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
			log.severe("Failed to setup DetailsPanel.");
			return null;
		}

		return loader;
	}

	//---------------------------------------------------ClassMethods---------------------------------------------------

}