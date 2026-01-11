package org.group_three.ui.controllers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import org.group_three.constants.UI;
import org.group_three.ui.*;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.group_three.ui.world.WorldObject;

/**
 * current controller for the 2d view, name needs to be changed later
 * needs much clean up, is intended to be the manager of the world and world objects
 *
 * @author Joel
 */
public class SimulationViewController {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The static ref of the rotation indicator.
	 *
	 * @author Joel
	 */
	private static AnchorPane rotationIndicatorStatic;

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * For static world elements: roads, traffic lights,...
	 *
	 * @author Joel
	 */
	@FXML
	private Canvas worldStaticRenderTarget;

	/**
	 * a reference to adjust the render target sizes dynamically on window resize
	 *
	 * @author Joel
	 */
	@FXML
	private Pane renderTargetBounds;

	/**
	 * The checkbox to toggle the environment aka polys.
	 *
	 * @author Joel
	 */
	@FXML
	private CheckBox environmentToggle;

	/**
	 * The checkbox to toggle the high contrast mode.
	 *
	 * @author Joel
	 */
	@FXML
	private CheckBox highContrastToggle;

	/**
	 * The checkbox to toggle traffic light timings.
	 *
	 * @author Joel
	 */
	@FXML
	private CheckBox tlTimingToggle;

	/**
	 * The rotation indicator.
	 *
	 * @author Joel
	 */
	@FXML
	private AnchorPane rotationIndicator;

	/**
	 * The slider for the vehicle size.
	 *
	 * @author Joel
	 */
	@FXML
	private Slider vehicleSizeSlider;

	/**
	 * The last known curser location on.
	 *
	 * @author Joel
	 */
	private Vector2D last = new Vector2D();

	/**
	 * The current mouse position in canvas space.
	 *
	 * @author Joel
	 */
	private Vector2D mousePosition = new Vector2D();

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The initialize method for this controller.
	 * Handles checkbox value changes.
	 *
	 * @author Joel
	 */
	@FXML
	public void initialize() {
		rotationIndicatorStatic = rotationIndicator;

		// bind the canvas size to the size of the parent pane
		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		SimView2D.initialize(worldStaticRenderTarget,
				renderTargetBounds);


		// update toggle values and then request render update
		// ----->

		environmentToggle.selectedProperty().addListener((_, _, value) -> {
			UI.showPolys = value;
			SimView2D.getWorld().requestUpdate();
		});

		highContrastToggle.selectedProperty().addListener((_, _, value) -> {
			UI.highContrast = value;
			SimView2D.getWorld().requestUpdate();
		});

		tlTimingToggle.selectedProperty().addListener((_, _, value) -> {
			UI.showTLTiming = value;
			SimView2D.getWorld().requestUpdate();
		});

		vehicleSizeSlider.valueProperty().addListener((_, _, newValue) -> {
			UI.vehicleScale = newValue.doubleValue();
			SimView2D.getWorld().requestUpdate();
		});

		// <----
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++GetterSetterClassMethods+++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to set the rotation of the rotation indicator.
	 *
	 * @param rotation The new rotation to indicate.
	 * @author Joel
	 */
	public static void setIndicatorRotation(double rotation) {
		rotationIndicatorStatic.setRotate(rotation);
	}

	//---------------------------------------------GetterSetterClassMethods---------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method that triggers when a mouse button is clicked.
	 * To interact with the world.
	 *
	 * @author Joel
	 */
	@FXML
	private void onMouseClicked() {
		// convert mouse click location to in world position
		Vector2D nMP = mousePosition.sub(SimView2D.getWorld().getViewerPositionOffset());
		Vector2D worldspaceMousePosition = Meth.getRelativeLocation(SimView2D.getWorld().getViewerPosition(), SimView2D.getWorld().getViewerRotation(), nMP).mul(1 / SimView2D.getWorld().getViewerZoom());

		// execute a click in the world
		SimView2D.clickInWorld(worldspaceMousePosition);

		// check for interactions
		WorldObject interacted = SimView2D.getWorld().interact(worldspaceMousePosition);
		if (interacted != null) SimView2D.setSelected(interacted);

		// request render update
		SimView2D.getWorld().requestUpdate();
	}

	/**
	 * A method that gets triggered when you drag in the canvas.
	 * Used for panning and if alt key is pressed also rotating the world.
	 *
	 * @param event MouseEvent
	 * @author Joel
	 */
	@FXML
	private void onCanvasDragged(MouseEvent event) {
		// current mos pos
		Vector2D current = new Vector2D(event.getX(), event.getY());

		// move pos delta
		Vector2D delta = current.sub(last);

		// rotation data adjusted for default flipped y axis of java fx
		double startRot = last.sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double rot = current.sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double deltaRot = rot - startRot;

		if (Keyboard.isAltKeyPressed()) {
			// rotation logic
			SimView2D.getWorld().addViewerRotation(deltaRot);
			SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().rotate(deltaRot));

		} else {
			// panning logic
			SimView2D.getWorld().addViewerPosition(delta);
		}

		// update last mouse pos
		last = current;
	}

	/**
	 * Gets triggered when the canvas is pressed,
	 * Handles part of the dragging logic by setting the initial cursor position for drag start.
	 *
	 * @param event MouseEvent
	 * @author Joel
	 */
	@FXML
	private void onCanvasPressed(MouseEvent event) {
		last = new Vector2D(event.getX(), event.getY());
	}

	/**
	 * A method that gets triggered when the mouse moves.
	 * Used to calculate world dragging/panning.
	 *
	 * @param event MouseEvent
	 * @author Joel
	 */
	@FXML
	private void onMouseMoved(MouseEvent event) {
		mousePosition = new Vector2D(event.getX(), event.getY());
	}

	/**
	 * A method that gets triggered on mouse scroll.
	 * Handles map zooming.
	 *
	 * @param event MouseEvent
	 * @author Joel
	 */
	@FXML
	private void onScroll(ScrollEvent event) {
		// calculate zoom values
		double zoomDelta = event.getDeltaY() * 0.01;
		double oldZoom = SimView2D.getWorld().getViewerZoom();

		// adjust zoom value
		SimView2D.getWorld().addViewerZoom(zoomDelta);

		// adjust viewer position
		SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().mul(SimView2D.getWorld().getViewerZoom() / oldZoom));
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}