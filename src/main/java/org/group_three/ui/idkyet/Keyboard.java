package org.group_three.ui.idkyet;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.group_three.debug.Debug;

// A static class that contains if a keyboard button is pressed or not.
// Gets initialized in the MainApp class and binds to the EventDispatchers from the base scene to grab the keyboard's data.
// Current version should be swapped to an Array/List approach to directly cover all keys and then using a getter function to check if a key is pressed or not.
public class Keyboard {
	public static boolean altKey = false;
	public static boolean ctrlKey = false;

	public static void initialize(Scene scene) {
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case KeyCode.ALT:
					altKey = true;
					Debug.print("altKey: " + true);
					break;

				case KeyCode.CONTROL:
					ctrlKey = true;
					Debug.print("ctrlKey: " + true);
					break;
			}
		});

		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
				case KeyCode.ALT:
					altKey = false;
					Debug.print("altKey: " + false);
					break;

				case KeyCode.CONTROL:
					ctrlKey = false;
					Debug.print("ctrlKey: " + false);
					break;
			}
		});
	}
}
