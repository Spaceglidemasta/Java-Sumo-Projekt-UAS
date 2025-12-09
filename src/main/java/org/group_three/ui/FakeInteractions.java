package org.group_three.ui;

import de.tudresden.sumo.cmd.Edge;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoStringList;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.debug.exceptions.InvalidFilesSelected;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Invoked upon loading one or more files via GUI -> Settings -> Open
 *
 * @author Joel, Luca
 */
public class FakeInteractions {

    /**
     * Gets called after selecting one or multiple Files. <br>
     * Loads the Simulation with the selected files and sets it to main.
     * @param paths A List of Paths to be opened via SimController(...)
     * @author Luca, Joel
     * */
	public static boolean loadSimulation(List<File> paths) throws InvalidFilesSelected {

        File network = null;
        File route = null;
        File config = null;

		switch (paths.size()) {
			//No files selected => Exception
			case 0:
				Debug.toConsole("InvalidFilesSelected: No Files Selected");
				throw new InvalidFilesSelected("No Files Selected");

				//1 File selected => expect .sumocfg file
			case 1:
				//Throw custom exception if not .sumocfg file
				if (!paths.getFirst().toString().matches(".*\\.sumocfg$")) {
					Debug.toConsole("InvalidFilesSelected: Selected File is not of type .sumocfg");
					throw new InvalidFilesSelected("Selected File is not of type .sumocfg");
				}
				//convert path to a relative path based on the SumoConfig path
				config = paths.getFirst();
				break;

			//2 Files selected => expect route and network file
			case 2:
				// check if both filetypes are present, and in which order
				if (       paths.get(0).toString().matches(".*\\.net\\.xml$")
                        && paths.get(1).toString().matches(".*\\.rou\\.xml$")) {
					//convert path to a relative path based on the SumoConfig path
					network = paths.get(0);
					route = paths.get(1);

				} else if (paths.get(0).toString().matches(".*\\.rou\\.xml$")
                        && paths.get(1).toString().matches(".*\\.net\\.xml$")) {
					//convert path to a relative path based on the SumoConfig path
					route = paths.get(0);
					network = paths.get(1);
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
			simcon = new SimController(getRelativePath(config.getAbsolutePath()));
		} else {
			simcon = new SimController( getRelativePath(network.getAbsolutePath()),
                                        getRelativePath(route.getAbsolutePath()));
		}


        //set selected simulation as the main, global / static simulation.
        simcon.setMainstc(true);

		// Create a new World for the opened simulation
		SimView2D.newWorld();

        //!This is all testing
        //Remove when done
        Debug.print(simcon.getJunctionIDList());
        Debug.print(simcon.getEdgeIDList());
        Debug.print(simcon.getLaneIDList());
        Debug.print(simcon.getLaneEdgeParam("clusterJ4_J5_0_0"));

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
