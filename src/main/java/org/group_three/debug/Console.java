package org.group_three.debug;

import com.sun.glass.ui.PlatformFactory;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Console {

    private static Console instance = null;
    private TextArea debugTextArea;
    private Stage debugStage;

    private Console() {

        debugStage = new Stage();
        debugStage.setTitle("Debug Window");

        // Create the TextArea for displaying debug messages
        debugTextArea = new TextArea();
        debugTextArea.setEditable(false);
        debugTextArea.setWrapText(true);

        BorderPane root = new BorderPane();
        root.setCenter(debugTextArea);


        Scene scene = new Scene(root, 600, 400);
        debugStage.setScene(scene); // add scene to the stage

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

    public void log(String message) {
        Platform.runLater(() -> { // pass lambda function
            debugTextArea.appendText(message + "\n");
            Debug.trimLines(debugTextArea);
            debugTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    public void show() {
        debugStage.show();
    }

    public void hide() {
        Debug.flushEverything();
        debugStage.hide();
    }
}
