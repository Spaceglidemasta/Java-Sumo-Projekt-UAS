package org.group_three.basicGui;

import java.io.IOException;

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
        System.out.println("Canvas loaded.");

        gc = canvasObject.getGraphicsContext2D();

        canvasObject.widthProperty().bind(binder.widthProperty());
    	canvasObject.heightProperty().bind(binder.heightProperty());

        canvasObject.widthProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Neue Breite: " + newValue);

            posCameraOffset.x = newValue.doubleValue()/2;
            update();
        });

        canvasObject.heightProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Neue Breite: " + newValue);
            posCameraOffset.y = newValue.doubleValue()/2;
            update();
        });



        //t.setDaemon(true); // optional: beendet sich mit dem Programm
        //t.start();

    }

    @FXML
	private void onMouseClicked() {
		System.out.println("Canvas clicked.");
    
	}

    @FXML
	private void onCanvasDragged(MouseEvent event) {
		System.out.println("onCanvasDragged");

        double x = event.getX();
        double y = event.getY();

        // Delta berechnen
        deltaX = x - lastX;
        deltaY = y - lastY;

        // Optional: zur Kontrolle
        System.out.println("Drag Δ = " + deltaX + " / " + deltaY);
        pos.x += deltaX;
        pos.y += deltaY;
        update();

        // neuen Startpunkt setzen
        lastX = x;
        lastY = y;
   
	}

    @FXML
	private void onCanvasPressed(MouseEvent event) {
		System.out.println("onCanvasPressed");

        // Startpunkt fürs Draggen speichern
        lastX = event.getX();
        lastY = event.getY();
   
	}

    @FXML
	private void onScroll(ScrollEvent event) {
		System.out.println("onScroll");

        double deltaY = event.getDeltaY();
        zoom += deltaY*0.01;
        System.out.println("Zoom: " + zoom);
        
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

        // ---- dein Code jede 10 ms ----
        //System.out.println("Tick");
        gc.setFill(Color.BLACK);
        gc.fillRect(posCameraOffset.x+pos.x-16*zoom, posCameraOffset.y+pos.y-16*zoom, 32*zoom, 32*zoom);

        System.out.println("PosXY: "+ pos.x + " | " + pos.y);
    }


    

    
}
