package org.group_three.ui;

import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.debug.exceptions.InvalidFilesSelected;

import java.util.List;


/**
 * Invoked upon loading one or more files via GUI -> Settings -> Open
 * @author Joel, Luca
 * */
public class FakeInteractions {
	public static boolean loadSimulation(List<String> paths) throws InvalidFilesSelected { // change Exception later

        String network = null;
        String route = null;
        String config = null;

        switch (paths.size()){
            //No files selected => Exception
            case 0:
                Debug.toConsole("InvalidFilesSelected: No Files Selected");
                throw new InvalidFilesSelected("No Files Selected");

            //1 File selected => expect .sumocfg file
            case 1:
                //Throw custom exception if not .sumocfg file
                if(!paths.getFirst().matches(".*\\.sumocfg$")){
                    Debug.toConsole("InvalidFilesSelected: Selected File is not of type .sumocfg");
                    throw new InvalidFilesSelected("Selected File is not of type .sumocfg");
                }
                config = paths.getFirst();
                break;

            //2 Files selected => expect route and network file
            case 2:
                // check if both filetypes are present
                if(paths.get(0).matches(".*\\.net\\.xml$") && paths.get(1).matches(".*\\.rou\\.xml$")){
                    network = paths.get(0);
                    route = paths.get(1);
                } else if (paths.get(0).matches(".*\\.rou\\.xml$") && paths.get(1).matches(".*\\.net\\.xml$")) {
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
        if(config != null) {
            simcon = new SimController(config);
        }
        else {
            simcon = new SimController(network, route);
        }

        //set selected simulation as the main, global / static simulation.
        simcon.setMainsim(true);

        return true;
	}
}
