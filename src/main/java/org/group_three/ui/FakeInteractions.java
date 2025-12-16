package org.group_three.ui;

import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.debug.StaticTester;
import org.group_three.debug.exceptions.InvalidFilesSelected;
import org.group_three.model.WEdge;
import org.group_three.model.WPolygon;
import org.group_three.model.WTrafficLight;
import org.group_three.service.Table;
import org.group_three.utils.PathUtils;

import java.io.File;
import java.util.List;

import static org.group_three.utils.PathUtils.getRelativePath;


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

        //load road network
        if(config != null){

            try {
                //you always need the network file for this, so you'll need to extract it from the sumocfg if u use one
                File net = PathUtils.getNetFromSCFG(config);
                WEdge.loadRoads(net);
                //WEdge.printAll();
                //WEdge.getRoad("132964154").print();
            }
            catch (Exception e){
                Debug.print("CRITICAL ERROR: STREETS CANNOT BE RENDERED");
                e.printStackTrace();
            }

        }  else {
            WEdge.loadRoads(network);
        }

        //set selected simulation as the main, global / static simulation.
        simcon.setMainstc(true);

        WPolygon.loadAllPolys();

        WTrafficLight.loadAll();

        /*
        StaticTester.TableToCSVExample();
        */

        StaticTester.StatisticGraphExample();

        simcon.saveState(".state.xml");


		// Create a new World for the opened simulation
		SimView2D.newWorld();

        //StatUtils.exportState(PathUtils.outputgen());

		return true;
	}




    //looking for something? look in PathUtils.
    //it just made more sense
}
