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
 * @see #initialize(Scene)
 */
public class Keyboard {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A list of all currently pressed KeyCodes.
	 *
	 * @author Joel
	 */
	private static final List<KeyCode> keyCodes = new ArrayList<>();

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++InitializeClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The method to initialize this class.
	 * It needs the Scene object to be able to listen to the key events.
	 *
	 * @param scene The Scene object from the MainApp class.
	 * @author Joel
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

	//--------------------------------------------------InitializeClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to check if the given key on the keyboard is currently pressed.
	 *
	 * @param key The key to check for if it's currently pressed.
	 * @return If the given key is pressed.
	 * @author Joel
	 */
	public static boolean isKeyPressed(KeyCode key) {
		return keyCodes.contains(key);
	}

	/**
	 * A method to check if the Alt-key on the keyboard is currently pressed.
	 *
	 * @return If the Alt-key is pressed.
	 * @author Joel
	 * @see #isKeyPressed(KeyCode)
	 */
	public static boolean isAltKeyPressed() {
		return isKeyPressed(KeyCode.ALT);
	}

	/**
	 * A method to check if the Ctrl-key on the keyboard is currently pressed.
	 *
	 * @return If the Ctrl-key is pressed.
	 * @author Joel
	 * @see #isKeyPressed(KeyCode)
	 */
	public static boolean isCtrlKeyPressed() {
		return isKeyPressed(KeyCode.CONTROL);
	}

	//--------------------------------------------------ClassMethods--------------------------------------------------

}