package org.group_three.debug;

import com.sun.glass.ui.PlatformFactory;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.group_three.ui.MainApp;

/**
 * Console class contains the controls for the console window
 * (This class is subject to change)
 * @author Leon
 * */

public class Console {

    private static Console instance = null;
    private final Stage debugStage;


    // Spawns the window
    private Console() {

        debugStage = new Stage();
        debugStage.setTitle("Debug Window");
		debugStage.getIcons().add(MainApp.getAppIcon());

        TextArea debugTextArea = new TextArea();
        debugTextArea.setEditable(false);
        debugTextArea.setWrapText(true);

        BorderPane root = new BorderPane();
        root.setCenter(debugTextArea);


        Scene scene = new Scene(root, 600, 400);
        debugStage.setScene(scene);

        // Set the TextArea in Debug class,
        // this is where the messages are passed to, to be displayed
        Debug.setDebugTextArea(debugTextArea);

        // When the console window closes, clear the debug text area reference
        // so messages get buffered until the console is opened again
        debugStage.setOnCloseRequest(event -> {
            Debug.clearDebugTextArea();
        });

    }

    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    public void show() {
        debugStage.show();
    }

    // may be used later
    public void hide() {
        debugStage.hide();
    }
}
