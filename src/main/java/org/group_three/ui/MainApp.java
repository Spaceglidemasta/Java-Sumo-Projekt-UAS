package org.group_three.ui;

import javafx.animation.AnimationTimer;
import org.group_three.constants.UI;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import org.group_three.debug.annotations.MayReturnNull;
import org.group_three.ui.controllers.TailController;

/**
 * The MainApp class which is the GUI.
 * Any UI element will be a part or be referenced in some way here.
 *
 * @author Joel
 */
public class MainApp extends Application {

	//++++++++++++++++++++++++++++++++++++++++++++++++++StartMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A custom start method, to initialize the UI manually with custom parameters.
	 *
	 * @param args The launch/start arguments array.
	 * @author Joel
	 */
	public void start(String[] args) {
		launch(args);
	}


    private AnimationTimer fpsTimer;

	/**
	 * The applications start method which can be called after the application was created.
	 *
	 * @param stage The default stage which is created by the Application class
	 * @author Joel, Luca
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// Create and Load the FXML based ui into a scene
		Scene mainScene = new Scene(new FXMLLoader(getClass().getResource(UI.appFXML)).load());

		// set minium app window size
		stage.setMinWidth(UI.appMinWidth);
		stage.setMinHeight(UI.appMinHeight);

		// Spawn window in the middle of the screen at 60% monitor size
		setWindowSpawnSettings(stage, 0.6, 0.6, true);

		// Set the window title
		stage.setTitle(UI.appTitle);

		// Set the window icon
		stage.getIcons().add(getAppIcon());

		// Set and show the created FXML scene in the window
		stage.setScene(mainScene); // can also be used to switch scenes later on
		stage.show();

		// Initialize the Keyboard class so it listens to keyboard events
		Keyboard.initialize(mainScene);

        fpsTimer = new AnimationTimer() {
            private long lastTime = 0;
            private int frames = 0;

            @Override
            public void handle(long now) {
                frames++;
                if (lastTime == 0) lastTime = now;

                if (now - lastTime >= 1_000_000_000L) {
                    TailController.setFPS(frames);
                    frames = 0;
                    lastTime = now;
                }
            }
        };

        fpsTimer.start();
	}

	//--------------------------------------------------StartMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to specify how the window should be spawned.
	 * (size and location)
	 *
	 * @param stage            The stage which is created by the start method of the Application class.
	 * @param widthPercentage  A double ranging from 0 to 1. 0 being 0% of the screen and 1 being 100% of the screen.
	 * @param heightPercentage A double ranging from 0 to 1. 0 being 0% of the screen and 1 being 100% of the screen.
	 * @param center           A boolean to decide if the window location should be perfectly in the center or if it should use the default spawn lcoation.
	 * @author Joel
	 */
	public void setWindowSpawnSettings(Stage stage, double widthPercentage, double heightPercentage, boolean center) {
		// Calculate window size values
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();
		double w = screen.getWidth() * widthPercentage;
		double h = screen.getHeight() * heightPercentage;

		// Set window size
		stage.setWidth(w);
		stage.setHeight(h);

		// Move window to screen center
		if (center) {
			stage.setX((screen.getWidth() - w) / 2);
			stage.setY((screen.getHeight() - h) / 2);
		}
	}

	/**
	 * A simple method to load(stream) and get the app icon as an image.
	 *
	 * @return The app icon as an image.
	 * @author Joel
	 */
    @MayReturnNull
	public Image getAppIcon() {
		//add error handling, if no "icons" is found
		try {
			return new Image(getClass().getResourceAsStream(UI.appIcon));
		} catch (Exception e) {
			//throw new RuntimeException(e);
		}

		return null;
	}

    /**
     * Gets automaticall called by JavaFX when the window is closed.
     * @author Luca
     * */
    @Override
    public void stop() {
        if (fpsTimer != null) {
            fpsTimer.stop();
        }
    }


	//--------------------------------------------------Methods--------------------------------------------------

}