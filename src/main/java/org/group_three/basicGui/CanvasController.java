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

    @FXML private Canvas canvasObject;
    @FXML private Pane binder;

    private GraphicsContext gc;

    @FXML
	public void initialize() throws IOException {
        Debug.print("Canvas loaded.");

        gc = canvasObject.getGraphicsContext2D();

        canvasObject.widthProperty().bind(binder.widthProperty());
    	canvasObject.heightProperty().bind(binder.heightProperty());

        canvasObject.widthProperty().addListener((observable, oldValue, newValue) -> {
            Debug.print("Neue Breite: " + newValue);

            posCameraOffset.x = newValue.doubleValue()/2;
            update();
        });

        canvasObject.heightProperty().addListener((observable, oldValue, newValue) -> {
            Debug.print("Neue Breite: " + newValue);
            posCameraOffset.y = newValue.doubleValue()/2;
            update();
        });



        //t.setDaemon(true); // optional: beendet sich mit dem Programm
        //t.start();

    }

    @FXML
	private void onMouseClicked() {
		Debug.print("Canvas clicked.");
    
	}

    @FXML
	private void onCanvasDragged(MouseEvent event) {
		Debug.print("onCanvasDragged");

        double x = event.getX();
        double y = event.getY();

        // Delta berechnen
        deltaX = x - lastX;
        deltaY = y - lastY;

        // Optional: zur Kontrolle
        Debug.print("Drag Δ = " + deltaX + " / " + deltaY);
        pos.x += deltaX;
        pos.y += deltaY;
        update();

        // neuen Startpunkt setzen
        lastX = x;
        lastY = y;
   
	}

    @FXML
	private void onCanvasPressed(MouseEvent event) {
		Debug.print("onCanvasPressed");

        // Startpunkt fürs Draggen speichern
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





    private double zoom = 1;
    private Vector2D pos = new Vector2D();
    private Vector2D posCameraOffset = new Vector2D();


    private void update()
    {
        gc.clearRect(0, 0, canvasObject.getWidth(), canvasObject.getHeight());

        gc.setFill(Color.BLACK);
        gc.fillRect(posCameraOffset.x+pos.x-16*zoom, posCameraOffset.y+pos.y-16*zoom, 32*zoom, 32*zoom);

        Debug.print("PosXY: "+ pos.x + " | " + pos.y);
    }


    

    
}
