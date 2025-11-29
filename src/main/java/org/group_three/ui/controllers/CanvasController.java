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

/**
 * current controller for the 2d view, name needs to be changed later
 * needs much clean up, is intended to be the manager of the world and world objects
 *
 * @author Joel
 */
public class CanvasController {

	@FXML
	private Canvas worldStaticRenderTarget;    // for static world elements: roads, traffic lights,...
	@FXML
	private Canvas worldDynamicRenderTarget;    // for dynamic world elements: cars,...
	@FXML
	private Pane renderTargetBounds;            // a reference to adjust the render target sizes dynamically on window resize

	private GraphicsContext worldStaticRenderTarget_GraphicsContext;

	private World world = new World();

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @throws IOException
	 * Throw-Comment
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.toConsole("Canvas loaded.");

		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();
		world.worldStaticRenderTarget = worldStaticRenderTarget;
		world.graphicsContext = worldStaticRenderTarget_GraphicsContext;

		WorldObject test = new WorldObject();
		test.world = world;
		test.graphicsContext = worldStaticRenderTarget_GraphicsContext;
		test.renderTarget = worldStaticRenderTarget;
		test.setPosition(new Vector2D(32, 64));
		test.setRotation(30);
		world.addWorldObject(test);

		WorldObject test2 = new WorldObject();
		test2.world = world;
		test2.graphicsContext = worldStaticRenderTarget_GraphicsContext;
		test2.renderTarget = worldStaticRenderTarget;
		test2.setPosition(new Vector2D(128, -64));
		test2.setRotation(30);
		world.addWorldObject(test2);

		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		worldDynamicRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldDynamicRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		renderTargetBounds.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("RenderTargetSize.X: " + newValue);

			world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y)));
		});

		renderTargetBounds.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("RenderTargetSize.Y: " + newValue);

			world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2)));
		});

		world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth() / 2, worldStaticRenderTarget.getHeight() / 2));

		Debug.toConsole(new Vector2D(0, 10).getRotation()); // 0°
		Debug.toConsole(new Vector2D(10, 10).getRotation()); // 45°
		Debug.toConsole(new Vector2D(10, 0).getRotation()); // 90°
		Debug.toConsole(new Vector2D(10, -10).getRotation()); // 135°
		Debug.toConsole(new Vector2D(0, -10).getRotation()); // 180°
		Debug.toConsole(new Vector2D(-10, -10).getRotation()); // 225°
		Debug.toConsole(new Vector2D(-10, 0).getRotation()); // 270°
		Debug.toConsole(new Vector2D(-10, 10).getRotation()); // 315°
		Debug.toConsole(new Vector2D(-0.001, 10).getRotation()); // 0/360°
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onMouseClicked() {
		if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("Canvas clicked.");

		//world.addViewerRotation(15);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onCanvasDragged(MouseEvent event) {
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onCanvasDragged");

		double x = event.getX();
		double y = event.getY();

		deltaX = x - lastX;
		deltaY = y - lastY;

		double startRot = new Vector2D(lastX, lastY).sub(world.getViewerPositionOffset()).flipY().getRotation();
		double rot = new Vector2D(x, y).sub(world.getViewerPositionOffset()).flipY().getRotation();
		double deltaRot = rot-startRot;

		if (/*!Keyboard.isCtrlKeyPressed() && */Keyboard.isAltKeyPressed()) { // start rotation freely, no snapping
			world.addViewerRotation(deltaRot);
			world.setViewerPosition(world.getViewerPosition().rotate(deltaRot)); // move to rotate viewer func?!?

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

			world.setViewerRotation(rot);

		} else {
			world.addViewerPosition(new Vector2D(deltaX, deltaY));
		}

		lastX = x;
		lastY = y;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onCanvasPressed(MouseEvent event) {
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onCanvasPressed");

		lastX = event.getX();
		lastY = event.getY();
	}

	private Vector2D mousePosition = new Vector2D();

	@FXML
	private void onMouseMoved(MouseEvent event) {
		mousePosition = new Vector2D(event.getX(), event.getY());
		//Debug.print(mousePosition);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onScroll(ScrollEvent event) {
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onScroll");

		double zoomDelta = event.getDeltaY() * 0.01;
		double mlp = zoomDelta < 0 ? 1 : 1;
		double oldZoom = world.getViewerZoom();
		//Debug.print(mlp);
		world.addViewerZoom(zoomDelta);
		world.setViewerPosition(world.getViewerPosition().mul(world.getViewerZoom()/oldZoom));
		Debug.print(world.getViewerPosition());
	}


	private double lastX;
	private double lastY;

	public double deltaX;
	public double deltaY;

	// draw handler needed so it doesn't waste performance
}
