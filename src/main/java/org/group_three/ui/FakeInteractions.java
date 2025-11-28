package org.group_three.ui;

import org.group_three.debug.Debug;

import java.util.List;

public class FakeInteractions {
	public static boolean loadSimulation(List<String> paths) {

        if(paths.size() != 2){

            return false;
        }

		Debug.toConsole("Loading Files:");
		for (String path : paths) {
			Debug.toConsole(path);
		}
		return true;
	}
}
