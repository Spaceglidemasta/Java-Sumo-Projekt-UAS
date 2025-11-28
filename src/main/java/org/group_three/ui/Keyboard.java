package org.group_three.ui;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.group_three.debug.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * A static class that listens to keyboard events to check if a button on the keyboard is being pressed or not.
 * Gets initialized in the MainApp class and binds to the EventDispatchers from the base scene to grab the keyboard's data.
 *
 * @author Joel
 * 
 * @see #initialize(Scene) 
 */
public class Keyboard {
	private static List<KeyCode> keyCodes = new ArrayList<>() {};

	/**
	 * The method to initialize this class.
	 * It needs the Scene object to be able to listen to the key events.
	 *
	 * @author Joel
	 *
	 * @param scene
	 * The Scene object from the MainApp class.
	 */
	public static void initialize(Scene scene) {
		scene.setOnKeyPressed(
			e -> {
				if (!keyCodes.contains(e.getCode())) {
					keyCodes.add(e.getCode());

					Debug.toConsole("Pressed: " + e.getCode());
				}
			}
		);

		scene.setOnKeyReleased(e -> {
			keyCodes.remove(e.getCode());
			}
		);
	}

	/**
	 * A method to check if the given key on the keyboard is currently pressed.
	 *
	 * @author Joel
	 *
	 * @param key
	 * The key to check for if it's currently pressed.
	 *
	 * @return
	 * If the given key is pressed.
	 */
	public static boolean isKeyPressed(KeyCode key) {
		return keyCodes.contains(key);
	}

	/**
	 * A method to check if the Alt-key on the keyboard is currently pressed.
	 *
	 * @author Joel
	 *
	 * @return
	 * If the Alt-key is pressed.
	 *
	 * @see #isKeyPressed(KeyCode)
	 */
	public static boolean isAltKeyPressed() {
		return isKeyPressed(KeyCode.ALT);
	}

	/**
	 * A method to check if the Ctrl-key on the keyboard is currently pressed.
	 *
	 * @author Joel
	 *
	 * @return
	 * If the Ctrl-key is pressed.
	 *
	 * @see #isKeyPressed(KeyCode) 
	 */
	public static boolean isCtrlKeyPressed() {
		return isKeyPressed(KeyCode.CONTROL);
	}
}
