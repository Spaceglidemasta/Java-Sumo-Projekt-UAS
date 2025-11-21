package org.group_three.basicGui;

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
