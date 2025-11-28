package org.group_three.ui;

import org.group_three.constants.UI;
import org.group_three.debug.Debug;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(UI.appFXML));

		Parent root = loader.load();

		Scene scene = new Scene(root);

		// set minium app window size
		stage.setMinWidth(UI.appMinWidth);
		stage.setMinHeight(UI.appMinHeight);


		// Spawn window in the middle of the screen at 60% monitor size
		// is this even needed?
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();

		double w = screen.getWidth() * 0.60;
		double h = screen.getHeight() * 0.60;

		stage.setWidth(w);
		stage.setHeight(h);

		stage.setX((screen.getWidth() - w) / 2);
		stage.setY((screen.getHeight() - h) / 2);

        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole(stage.getX() + ":" + stage.getY());
		//-------------------------------------------------------------

		stage.setTitle(UI.appTitle);
		stage.getIcons().add(getAppIcon());
		stage.setScene(scene); // can also be used to switch scenes later on
		stage.show();

		Keyboard.initialize(scene);
	}

	public void start(String[] args) {
		launch(args);
	}

	public Image getAppIcon() //add error handling, if no "icons" is found
	{
		return new Image(getClass().getResourceAsStream(UI.appIcon));
	}
}