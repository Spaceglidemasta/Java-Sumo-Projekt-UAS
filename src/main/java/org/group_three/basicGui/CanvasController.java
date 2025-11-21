package org.group_three.basicGui;

import java.io.IOException;

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

		world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth()/2, worldStaticRenderTarget.getHeight()/2));
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

		if(Keyboard.altKey) { // start rotation
			Vector2D origin = new Vector2D(x, y).add(world.getViewerPositionOffset());
			Debug.print(origin);

			Debug.print(world.getViewerPositionOffset());

			if (Keyboard.ctrlKey) { // rotate with 45 degree snapping

			} else { // rotate freely, no snapping

			}

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
