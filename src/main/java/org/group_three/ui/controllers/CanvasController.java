package org.group_three.ui.controllers;

import java.io.IOException;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import org.group_three.constants.UI;
import org.group_three.ui.*;
import org.group_three.debug.Debug;

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
public class CanvasController {

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

	private Vector2D last = new Vector2D();
	private Vector2D delta = new Vector2D();

	@FXML
	private CheckBox environmentToggle;
	@FXML
	private CheckBox highContrastToggle;
	@FXML
	private CheckBox tlTimingToggle;
	@FXML
	private AnchorPane rotationBase;
	public static AnchorPane rotationBaseStatic;
	@FXML private AnchorPane rotationIndicator;
	public static AnchorPane rotationIndicatorStatic;

	/**
	 * Comment
	 *
	 * @throws IOException Throw-Comment
	 * @author Joel
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.toConsole("Canvas loaded.");
		rotationIndicatorStatic = rotationIndicator;
		rotationBaseStatic = rotationBase;

		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		SimView2D.initialize(worldStaticRenderTarget,
				renderTargetBounds);

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
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onMouseClicked() {
		Vector2D nMP = mousePosition.sub(SimView2D.getWorld().getViewerPositionOffset());
		Vector2D worldspaceMousePosition = Meth.getRelativeLocation(SimView2D.getWorld().getViewerPosition(), SimView2D.getWorld().getViewerRotation(), nMP).mul(1 / SimView2D.getWorld().getViewerZoom());

		SimView2D.clickInWorld(worldspaceMousePosition);

		try {
			WorldObject interacted = SimView2D.getWorld().interact(worldspaceMousePosition);
			if (interacted != null) SimView2D.setSelected(interacted);
		} catch (Exception e) {
		}

		SimView2D.getWorld().requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @param event Param-Comment
	 * @author Joel
	 */
	@FXML
	private void onCanvasDragged(MouseEvent event) {
		if (Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onCanvasDragged");

		Vector2D current = new Vector2D(event.getX(), event.getY());

		delta = current.sub(last);

		double startRot = last.sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double rot = current.sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double deltaRot = rot - startRot;

		if (/*!Keyboard.isCtrlKeyPressed() && */Keyboard.isAltKeyPressed()) { // start rotation freely, no snapping
			SimView2D.getWorld().addViewerRotation(deltaRot);
			SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().rotate(deltaRot)); // move to rotate viewer func?!?

		} else if (Keyboard.isCtrlKeyPressed() && Keyboard.isAltKeyPressed() && false) { // start rotation with 45 degree snapping
			if (((rot >= 0) && (rot < 22.5)) || rot >= 337.5) rot = 0;
			else if ((rot >= 22.5) && (rot < 67.5)) rot = 45;
			else if ((rot >= 67.5) && (rot < 112.5)) rot = 90;
			else if ((rot >= 112.5) && (rot < 157.5)) rot = 135;
			else if ((rot >= 157.5) && (rot < 202.5)) rot = 180;
			else if ((rot >= 202.5) && (rot < 247.5)) rot = 225;
			else if ((rot >= 247.5) && (rot < 292.5)) rot = 270;
			else if (rot >= 292.5) rot = 315;
			else throw new RuntimeException("Rotation reached an impossible value!");

			SimView2D.getWorld().setViewerRotation(rot);

		} else {
			SimView2D.getWorld().addViewerPosition(delta);
		}

		last = current;
	}

	/**
	 * Comment
	 *
	 * @param event Param-Comment
	 * @author Joel
	 */
	@FXML
	private void onCanvasPressed(MouseEvent event) {
		if (Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onCanvasPressed");

		last = new Vector2D(event.getX(), event.getY());
	}

	private Vector2D mousePosition = new Vector2D();

	/**
	 * Comment
	 *
	 * @param event Param-Comment
	 * @author Joel
	 */
	@FXML
	private void onMouseMoved(MouseEvent event) {
		mousePosition = new Vector2D(event.getX(), event.getY());
		//Debug.print(mousePosition);
	}

	/**
	 * Comment
	 *
	 * @param event Param-Comment
	 * @author Joel
	 */
	@FXML
	private void onScroll(ScrollEvent event) {
		if (Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onScroll");

		double zoomDelta = event.getDeltaY() * 0.01;
		double mlp = zoomDelta < 0 ? 1 : 1;
		double oldZoom = SimView2D.getWorld().getViewerZoom();
		//Debug.print(mlp);
		SimView2D.getWorld().addViewerZoom(zoomDelta);
		//world.setViewerPosition(world.getViewerPosition().mul(world.getViewerZoom()/oldZoom));
		SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().mul(SimView2D.getWorld().getViewerZoom() / oldZoom));
		//Debug.print(world.getViewerPosition());
	}

	// draw handler needed so it doesn't waste performance











}
