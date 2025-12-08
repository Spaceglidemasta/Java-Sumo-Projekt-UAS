package org.group_three.ui;

import de.tudresden.sumo.cmd.Edge;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.debug.exceptions.InvalidFilesSelected;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Invoked upon loading one or more files via GUI -> Settings -> Open
 *
 * @author Joel, Luca
 */
public class FakeInteractions {
	public static boolean loadSimulation(List<String> paths) throws InvalidFilesSelected { // change Exception later

		String network = null;
		String route = null;
		String config = null;

		switch (paths.size()) {
			//No files selected => Exception
			case 0:
				Debug.toConsole("InvalidFilesSelected: No Files Selected");
				throw new InvalidFilesSelected("No Files Selected");

				//1 File selected => expect .sumocfg file
			case 1:
				//Throw custom exception if not .sumocfg file
				if (!paths.getFirst().matches(".*\\.sumocfg$")) {
					Debug.toConsole("InvalidFilesSelected: Selected File is not of type .sumocfg");
					throw new InvalidFilesSelected("Selected File is not of type .sumocfg");
				}
				//convert path to a relative path based on the SumoConfig path
				config = getRelativePath(paths.getFirst());
				break;

			//2 Files selected => expect route and network file
			case 2:
				// check if both filetypes are present
				if (paths.get(0).matches(".*\\.net\\.xml$") && paths.get(1).matches(".*\\.rou\\.xml$")) {
					//convert path to a relative path based on the SumoConfig path
					network = getRelativePath(paths.get(0));
					route = getRelativePath(paths.get(1));
				} else if (paths.get(0).matches(".*\\.rou\\.xml$") && paths.get(1).matches(".*\\.net\\.xml$")) {
					//convert path to a relative path based on the SumoConfig path
					route = getRelativePath(paths.get(0));
					network = getRelativePath(paths.get(1));
				}
				//throw custom error if they aren't
				else {
					Debug.toConsole("InvalidFilesSelected: Selected Files are of wrong format.");
					Debug.toConsole(paths);
					throw new InvalidFilesSelected("Selected Files are of wrong format.");
				}

				break;

			// More than 2 files selected => throw custom error once again
			default:
				Debug.toConsole("InvalidFilesSelected: To many Files selected");
				throw new InvalidFilesSelected("To many Files selected");

		}


		SimController simcon = null;
		//check which constructor needs to be invoked
		if (config != null) {
			simcon = new SimController(config);
		} else {
			simcon = new SimController(network, route);
		}

		//set selected simulation as the main, global / static simulation.
		simcon.setMainsim(true);

		// Create a new World for the opened simulation
		SimView2D.newWorld();

        return true;
	}

	/**
	 * A method to convert an absolute path to a relative path based on the SumoConfig path.
	 * <br>
	 * AI was used for help on the path conversion.
	 *
	 * @param absolutePath The absolute path which should be converted to a relative path.
	 * @return The relative path which was created.
	 * @author Joel
	 */
	public static String getRelativePath(String absolutePath) {
		// get SumoConfig path
		Path sumoConfigPath = Paths.get(new File(SimController.getSumoLoc(), "SumoConfig").getPath());

		// create and return a relative path based on the SumoConfig path
		return sumoConfigPath.relativize(Paths.get(absolutePath)).toString();

		// Used AI code part explanations
		// Paths.get(String)        <-- converts a string to a Path
		// path0.relativize(path1)  <--returns the relative path of path1 relative to path0
	}


}
