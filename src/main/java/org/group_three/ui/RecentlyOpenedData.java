package org.group_three.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The data manager for the recently opened functionality of the ui.
 * Used to load/save/modify the data.
 *
 * @author Joel
 */
public class RecentlyOpenedData {

	/**
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static List<String> simulations = new ArrayList<>();

	/**
	 * @author Joel
	 */
	public RecentlyOpenedData() {
	}

	/**
	 * @author Joel
	 */
	public static void load() {
		File rOD = new File(UI.recentlyOpenedDataFileName);

		if (!rOD.exists()) return;

		try {
			StringBuilder data = new StringBuilder();
			Scanner fileReader = new Scanner(rOD);
			while (fileReader.hasNextLine()) {
				data.append(fileReader.nextLine());
			}

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			simulations = gson.fromJson(data.toString(), RecentlyOpenedDataJson.class).sims;

			Debug.print(data);

		} catch (FileNotFoundException e) {
			//throw new RuntimeException(e);
		}

	}

	/**
	 * @param sim
	 * @author Joel
	 */
	public static void removeSimulation(String sim) {
		simulations.remove(sim);
		save();
	}

	/**
	 * @param sim
	 * @author Joel
	 */
	public static void addSimulation(String sim) {
		simulations.addFirst(sim);
		save();
	}

	/**
	 * @return
	 * @author Joel
	 */
	public static List<String> getSimulations() {
		return simulations;
	}

	/**
	 * @author Joel
	 */
	public static void save() {
		try {
			FileWriter fileWriter = new FileWriter(UI.recentlyOpenedDataFileName);

			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();

			Gson gson = builder.create();

			fileWriter.write(gson.toJson(new RecentlyOpenedDataJson(getSimulations())));

			fileWriter.close();

		} catch (Exception e) {
			//throw new RuntimeException(e);
		}
	}

	/**
	 * @author Joel
	 */
	public static void validate() {
		List<String> fails = new ArrayList<String>() {
		};

		for (String path : getSimulations()) {
			try {
				for (String subPath : path.split("\n")) {
					if (!(new File(subPath).exists() && !subPath.isEmpty())) {
						fails.add(path);
						break;
					}
				}
			} catch (Exception e) {
				//throw new RuntimeException(e);
			}
		}

		for (String path : fails) {
			removeSimulation(path);
		}

	}
}