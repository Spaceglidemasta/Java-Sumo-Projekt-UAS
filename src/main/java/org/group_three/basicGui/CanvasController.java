package org.group_three.basicGui;

import java.io.IOException;

import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasController {

	@FXML private Canvas worldStaticRenderTarget;	// for static world elements: roads, traffic lights,...
	@FXML private Canvas worldDynamicRenderTarget;	// for dynamic world elements: cars,...
	@FXML private Pane renderTargetBounds;			// a reference to adjust the render target sizes dynamically on window resize

	private GraphicsContext worldStaticRenderTarget_GraphicsContext;

	@FXML
	public void initialize() throws IOException {
		Debug.print("Canvas loaded.");

		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();
		

		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		worldDynamicRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldDynamicRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		renderTargetBounds.widthProperty().addListener((observable, oldValue, newValue) -> {
			Debug.print("RenderTargetSize.X: " + newValue);

			posCameraOffset.x = newValue.doubleValue()/2;
			update();
		});

		renderTargetBounds.heightProperty().addListener((observable, oldValue, newValue) -> {
			Debug.print("RenderTargetSize.Y: " + newValue);
			posCameraOffset.y = newValue.doubleValue()/2;
			update();
		});
	}

	@FXML
	private void onMouseClicked() {
		Debug.print("Canvas clicked.");

		setRotation(rotation+15);
		update();
	
	}

	@FXML
	private void onCanvasDragged(MouseEvent event) {
		Debug.print("onCanvasDragged");

		double x = event.getX();
		double y = event.getY();

		deltaX = x - lastX;
		deltaY = y - lastY;

		pos.x += deltaX;
		pos.y += deltaY;
		update();

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

		double deltaY = event.getDeltaY();
		zoom += deltaY*0.01;
		Debug.print("Zoom: " + zoom);
		
		update();
   
	}


	private double lastX;
	private double lastY;

	public double deltaX;
	public double deltaY;

	// draw handler needed so it doesnt waste performance

	private double zoom = 1;
	private Vector2D pos = new Vector2D();
	private Vector2D posCameraOffset = new Vector2D();
	private double rotation = 0;
	private Vector2D worldSize = new Vector2D(512, 256);

	private void setRotation(double rotation) {
		this.rotation = rotation;

		// Clamp rotation from 0 to 359.99...
		while (this.rotation < 0)
		{
			this.rotation += 360;
		}
		while (this.rotation >= 360)
		{
			this.rotation -= 360;
		}

		Debug.print(rotation);
	}


	private void update() {
		worldStaticRenderTarget_GraphicsContext.save();
		worldStaticRenderTarget_GraphicsContext.setFill(Color.GREY);
		worldStaticRenderTarget_GraphicsContext.fillRect(0, 0, worldStaticRenderTarget.getWidth(), worldStaticRenderTarget.getHeight());
		worldStaticRenderTarget_GraphicsContext.restore();

		
		worldStaticRenderTarget_GraphicsContext.save();
		worldStaticRenderTarget_GraphicsContext.translate(pos.x + posCameraOffset.x, pos.y + posCameraOffset.y); // Object Location
		worldStaticRenderTarget_GraphicsContext.rotate(rotation);
		worldStaticRenderTarget_GraphicsContext.clearRect(-256*zoom, -128*zoom, 512*zoom, 256*zoom);
		worldStaticRenderTarget_GraphicsContext.restore();

		Debug.print("PosXY: "+ pos.x + " | " + pos.y);

		worldStaticRenderTarget_GraphicsContext.save();
		worldStaticRenderTarget_GraphicsContext.setFill(Color.BLUE);
		worldStaticRenderTarget_GraphicsContext.translate(pos.x + posCameraOffset.x, pos.y + posCameraOffset.y); // Object Location
		worldStaticRenderTarget_GraphicsContext.rotate(rotation);
		worldStaticRenderTarget_GraphicsContext.fillRect(-16*zoom, -16*zoom, 32*zoom, 32*zoom);
		worldStaticRenderTarget_GraphicsContext.restore();

	}
}
