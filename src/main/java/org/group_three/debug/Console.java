package org.group_three.debug;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Console {

    private static Console instance = null;
    private TextArea debugTextArea;
    private Stage debugStage;

    // Private constructor for Singleton
    private Console() {
        // Create the Debug window (JavaFX Stage)
        debugStage = new Stage();
        debugStage.setTitle("Debug Window");

        // Create the TextArea for displaying debug messages
        debugTextArea = new TextArea();
        debugTextArea.setEditable(false);  // Make sure the user can't edit this
        debugTextArea.setWrapText(true);   // Allow text wrapping

        // Create a BorderPane as the layout for the window
        BorderPane root = new BorderPane();
        root.setCenter(debugTextArea);

        // Create the Scene and add it to the Stage
        Scene scene = new Scene(root, 600, 400);  // Adjust size as needed
        debugStage.setScene(scene);

        // Set the TextArea in Debug class
        Debug.setDebugTextArea(debugTextArea);

        // Redirect System.out to the TextArea using TextAreaPrintStream
    }

    // Singleton method to get the single instance
    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    // Method to log messages (you can call this from anywhere)
    public void log(String message) {
        Platform.runLater(() -> {
            debugTextArea.appendText(message + "\n");
            debugTextArea.setScrollTop(Double.MAX_VALUE);  // Scroll to the bottom
        });
    }

    // Method to show the debug window
    public void show() {
        debugStage.show();
    }

    // Method to hide the debug window
    public void hide() {
        debugStage.hide();
    }
}
