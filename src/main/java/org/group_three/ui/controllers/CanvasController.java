package org.group_three.ui.controllers;

import java.io.IOException;

import org.group_three.basicGui.Keyboard;
import org.group_three.basicGui.Vector2D;
import org.group_three.basicGui.world.World;
import org.group_three.basicGui.world.WorldObject;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class CanvasController {

	@FXML
	private Canvas worldStaticRenderTarget;    // for static world elements: roads, traffic lights,...
	@FXML
	private Canvas worldDynamicRenderTarget;    // for dynamic world elements: cars,...
	@FXML
	private Pane renderTargetBounds;            // a reference to adjust the render target sizes dynamically on window resize

	private GraphicsContext worldStaticRenderTarget_GraphicsContext;

	private World world = new World();

	@FXML
	public void initialize() throws IOException {
		Debug.print("Canvas loaded.");

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

		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		worldDynamicRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldDynamicRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		renderTargetBounds.widthProperty().addListener((observable, oldValue, newValue) -> {
			Debug.print("RenderTargetSize.X: " + newValue);

			world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y)));
		});

		renderTargetBounds.heightProperty().addListener((observable, oldValue, newValue) -> {
			Debug.print("RenderTargetSize.Y: " + newValue);

			world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2)));
		});

		world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth() / 2, worldStaticRenderTarget.getHeight() / 2));

		Debug.print(new Vector2D(0, 10).getRotation()); // 0°
		Debug.print(new Vector2D(10, 10).getRotation()); // 45°
		Debug.print(new Vector2D(10, 0).getRotation()); // 90°
		Debug.print(new Vector2D(10, -10).getRotation()); // 135°
		Debug.print(new Vector2D(0, -10).getRotation()); // 180°
		Debug.print(new Vector2D(-10, -10).getRotation()); // 225°
		Debug.print(new Vector2D(-10, 0).getRotation()); // 270°
		Debug.print(new Vector2D(-10, 10).getRotation()); // 315°
		Debug.print(new Vector2D(-0.001, 10).getRotation()); // 0/360°
	}

	@FXML
	private void onMouseClicked() {
		Debug.print("Canvas clicked.");

		//world.addViewerRotation(15);
	}

	@FXML
	private void onCanvasDragged(MouseEvent event) {
		Debug.print("onCanvasDragged");

		double x = event.getX();
		double y = event.getY();

		deltaX = x - lastX;
		deltaY = y - lastY;

		if (!Keyboard.ctrlKey && Keyboard.altKey) { // start rotation freely, no snapping
			double rot = new Vector2D(x, y).sub(world.getViewerPositionOffset()).flipY().getRotation();
			world.setViewerRotation(rot);

		} else if (Keyboard.ctrlKey && Keyboard.altKey) { // start rotation with 45 degree snapping
			double rot = new Vector2D(x, y).sub(world.getViewerPositionOffset()).flipY().getRotation();

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

	@FXML
	private void onCanvasPressed(MouseEvent event) {
		Debug.print("onCanvasPressed");

		lastX = event.getX();
		lastY = event.getY();
	}

	@FXML
	private void onScroll(ScrollEvent event) {
		Debug.print("onScroll");

		world.addViewerZoom(event.getDeltaY() * 0.01);
	}


	private double lastX;
	private double lastY;

	public double deltaX;
	public double deltaY;

	// draw handler needed so it doesn't waste performance
}
