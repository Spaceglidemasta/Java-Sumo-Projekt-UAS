package org.group_three.ui;

import jdk.jshell.spi.ExecutionControl;
import org.group_three.debug.Debug;
import org.group_three.debug.exceptions.InvalidFilesSelected;

import java.util.List;
import java.util.regex.Pattern;


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
            case 0:
                Debug.toConsole("InvalidFilesSelected: No Files Selected");
                throw new InvalidFilesSelected("No Files Selected");

            case 1:
                if(!paths.getFirst().matches(".*\\.sumocfg$")){
                    Debug.toConsole("InvalidFilesSelected: Selected File is not of type .sumocfg");
                    throw new InvalidFilesSelected("Selected File is not of type .sumocfg");
                }
                config = paths.getFirst();

            case 2:

                if(paths.get(0).matches(".*\\.net\\.xml$") && paths.get(1).matches(".*\\.rou\\.xml$")){
                    network = paths.get(0);
                    route = paths.get(1);
                } else if (paths.get(0).matches(".*\\.rou\\.xml$") && paths.get(1).matches(".*\\.net\\.xml$")) {
                    route = paths.get(0);
                    network = paths.get(1);
                }
                else {
                    Debug.toConsole("InvalidFilesSelected: Selected Files are of wrong format.");
                    throw new InvalidFilesSelected("Selected Files are of wrong format.");
                }

                break;

            // More than 2 files selected
            default:
                Debug.toConsole("InvalidFilesSelected: To many Files selected");
                throw new InvalidFilesSelected("To many Files selected");
        }

        return true;
	}
}
