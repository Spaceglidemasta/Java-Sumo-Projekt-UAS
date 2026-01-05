package org.group_three;

import org.group_three.debug.Debug;
import org.group_three.api.SimController;
import org.group_three.ui.MainApp;

/**
 * The main class which creates the MainApp and does sumo process clean up.
 *
 * @author Joel, Luca
 */
public class Main {

	/**
	 * The main method for this whole project.
	 * This is where the MainApp get created and
	 * when the app closes it closes the sumo simulation if needed.
	 *
	 * @param args The launch/start arguments array.
	 * @author Joel, Luca
	 */
	static void main(String[] args) {
		Debug.print("Program Start");

		// Create and start the GUI
		MainApp aMainGui = new MainApp();
		aMainGui.start(args);

		// Close main simulation on program end to make sure all threads get killed
		SimController simcon = SimController.getMainsimcon();
		if (simcon != null) simcon.close();
	}

}