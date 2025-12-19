package org.group_three;

import org.group_three.debug.Debug;
import org.group_three.api.SimController;
import org.group_three.ui.MainApp;

public class Main {
	static void main(String[] args) {
		Debug.print("Program Start");

		// Create and start the GUI
		MainApp aMainGui = new MainApp();
		aMainGui.start(args);

		// Close main simulation on program end to make sure all threads get killed
        if(SimController.getMainsimcon() != null) SimController.getMainsimcon().close();
	}
}