package org.group_three.ui.controllers;

import java.io.IOException;

import org.group_three.ui.*;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	 * for dynamic world elements: cars,...
	 *
	 * @author Joel
	 */
	@FXML
	private Canvas worldDynamicRenderTarget;
	/**
	 * a reference to adjust the render target sizes dynamically on window resize
	 *
	 * @author Joel
	 */
	@FXML
	private Pane renderTargetBounds;

	private GraphicsContext worldStaticRenderTarget_GraphicsContext;

	//private World world = new World();

	/**
	 * Comment
	 *
	 * @throws IOException Throw-Comment
	 * @author Joel
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.toConsole("Canvas loaded.");

		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();

		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		worldDynamicRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldDynamicRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		SimView2D.initialize(worldStaticRenderTarget,
				worldDynamicRenderTarget,
				renderTargetBounds);
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

		/*WorldObject test = new WorldObject();
		test.world = world;
		test.graphicsContext = worldStaticRenderTarget_GraphicsContext;
		test.renderTarget = worldStaticRenderTarget;
		test.setPosition(worldspaceMousePosition);
		world.addWorldObject(test);*/

		//world.world

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
        Debug.toConsole("onCanvasDragged");

		double x = event.getX();
		double y = event.getY();

		deltaX = x - lastX;
		deltaY = y - lastY;

		double startRot = new Vector2D(lastX, lastY).sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double rot = new Vector2D(x, y).sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
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
			SimView2D.getWorld().addViewerPosition(new Vector2D(deltaX, deltaY));
		}

		lastX = x;
		lastY = y;
	}

	/**
	 * Comment
	 *
	 * @param event Param-Comment
	 * @author Joel
	 */
	@FXML
	private void onCanvasPressed(MouseEvent event) {
		Debug.toConsole("onCanvasPressed");

		lastX = event.getX();
		lastY = event.getY();
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
		Debug.toConsole("onScroll");

		double zoomDelta = event.getDeltaY() * 0.01;
		double mlp = zoomDelta < 0 ? 1 : 1;
		double oldZoom = SimView2D.getWorld().getViewerZoom();
		//Debug.print(mlp);
		SimView2D.getWorld().addViewerZoom(zoomDelta);
		//world.setViewerPosition(world.getViewerPosition().mul(world.getViewerZoom()/oldZoom));
		SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().mul(SimView2D.getWorld().getViewerZoom() / oldZoom));
		//Debug.print(world.getViewerPosition());
	}


	private double lastX;
	private double lastY;

	public double deltaX;
	public double deltaY;

	// draw handler needed so it doesn't waste performance
}
