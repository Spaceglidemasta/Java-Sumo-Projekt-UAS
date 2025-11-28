package org.group_three.ui;

import org.group_three.debug.Debug;

import java.util.List;

public class FakeInteractions {
	public static boolean loadSimulation(List<String> paths) {
		Debug.toConsole("FakeInteractions:");
		for (String path : paths) {
			Debug.toConsole(path);
		}
		return true;
	}
}
