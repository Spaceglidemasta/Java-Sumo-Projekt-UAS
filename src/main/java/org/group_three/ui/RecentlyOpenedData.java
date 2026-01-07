package org.group_three.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.group_three.constants.UI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * The data manager for the recently opened functionality of the ui.
 * Used to load/save/modify the data.
 *
 * @author Joel
 */
public class RecentlyOpenedData {

	// Logger
	private static final Logger log = Logger.getLogger(RecentlyOpenedData.class.getName());

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The simulations which were loaded in the past.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static List<String> simulations = new ArrayList<>();

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++GetterSetterClassMethods+++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to remove a simulation.
	 * Saves to a file after change.
	 *
	 * @param sim The simulation to remove
	 * @author Joel
	 */
	public static void removeSimulation(String sim) {
		simulations.remove(sim);
		log.info("Removed recently loaded simulation: " + sim);
		save();
	}

	/**
	 * A method to add a simulation.
	 * Saves to a file after change.
	 *
	 * @param sim The simulation to add.
	 * @author Joel
	 */
	public static void addSimulation(String sim) {
		simulations.addFirst(sim);
		log.info("Added recently loaded simulation: " + sim);
		save();
	}

	/**
	 * A method to get all simulations.
	 *
	 * @return All simulations.
	 * @author Joel
	 */
	public static List<String> getSimulations() {
		return simulations;
	}

	//---------------------------------------------GetterSetterClassMethods---------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to load a simulation list from a file. (JSON)
	 *
	 * @author Joel
	 */
	public static void load() {
		File rOD = new File(UI.recentlyOpenedDataFileName);

		// check if file exists, if not cancel
		if (!rOD.exists()) {
			log.severe("JSON File not found.");
			return;
		}

		// try to read data from file and parse it using GSON
		try {
			StringBuilder data = new StringBuilder();
			Scanner fileReader = new Scanner(rOD);

			// read each line in file and write it into "data"
			while (fileReader.hasNextLine()) {
				data.append(fileReader.nextLine());
			}

			// create gson builder
			Gson gson = new GsonBuilder().create();
			// convert json string to RecentlyOpenedDataJson object,
			// and set the simulations variable to sims
			simulations = gson.fromJson(data.toString(), RecentlyOpenedDataJson.class).sims;

			log.info("Loaded JSON Data: " + data);

		} catch (FileNotFoundException e) {
			log.warning("JSON File: not found or corrupted");
		} catch (Exception e) {
			log.severe("RecentlyOpenedData loading failed.");
		}

	}

	/**
	 * A method to save the current simulation list to a file. (JSON)
	 *
	 * @author Joel
	 */
	public static void save() {
		try {
			FileWriter fileWriter = new FileWriter(UI.recentlyOpenedDataFileName);

			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting(); // nicely formatted json output
			Gson gson = builder.create();

			// convert data object to json string
			String jsonString = gson.toJson(new RecentlyOpenedDataJson(getSimulations()));

			// write data to file
			fileWriter.write(jsonString);
			fileWriter.close();

			log.info("JSON File: save successful. Saved: " + jsonString);

		} catch (Exception e) {
			log.severe("JSON File: save failed.");
		}
	}

	/**
	 * A method to validate simulation paths.
	 * Removes entries where simulation data is not valid.
	 * (For example moved or removed files)
	 *
	 * @author Joel
	 */
	public static void validate() {
		List<String> fails = new ArrayList<>();

		// loop through paths and check if they are not empty and exist
		for (String path : getSimulations()) {
			for (String subPath : path.split("\n")) {
				if (!(new File(subPath).exists() && !subPath.isEmpty())) {
					fails.add(path);
					break;
				}
			}
		}

		for (String path : fails) {
			log.warning("Simulation path is not valid: " + path);
			removeSimulation(path);
		}

	}

	//---------------------------------------------------ClassMethods---------------------------------------------------

}