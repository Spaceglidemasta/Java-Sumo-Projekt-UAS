package org.group_three.ui.idkyet;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.group_three.debug.Debug;

public class Keyboard {
	public static boolean altKey = false;
	public static boolean ctrlKey = false;

	public static void initialize(Scene scene) {
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case KeyCode.ALT:
					altKey = true;
					Debug.toConsole("altKey: " + true);
					break;

				case KeyCode.CONTROL:
					ctrlKey = true;
					Debug.toConsole("ctrlKey: " + true);
					break;
			}
		});

		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
				case KeyCode.ALT:
					altKey = false;
					Debug.toConsole("altKey: " + false);
					break;

				case KeyCode.CONTROL:
					ctrlKey = false;
					Debug.toConsole("ctrlKey: " + false);
					break;
			}
		});
	}
}
