package org.group_three.basicGui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainGui extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hallo JavaFX 25 mit Maven!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("JavaFX Demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}