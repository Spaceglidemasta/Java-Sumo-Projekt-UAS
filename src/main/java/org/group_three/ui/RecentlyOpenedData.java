package org.group_three.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RecentlyOpenedData {
	public RecentlyOpenedData() {}

	private static List<String> simulations = new ArrayList<>();

	public static void load() {
		File rOD = new File(UI.recentlyOpenedDataFileName);

		if (!rOD.exists()) {
			try {
				rOD.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

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
			throw new RuntimeException(e);
		}

	}

	public static void removeSimulation(String sim) {
		simulations.remove(sim);
		save();
	}
	public static void addSimulation(String sim) {
		simulations.addFirst(sim);
		save();
	}

	public static List<String> getSimulations() {return simulations;}

	public static void save() {
		try {
			FileWriter fileWriter = new FileWriter(UI.recentlyOpenedDataFileName);

			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();

			Gson gson = builder.create();

			fileWriter.write(gson.toJson(new RecentlyOpenedDataJson(getSimulations())));

			fileWriter.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void validate() {
			List<String> fails = new ArrayList<String>() {
			};

			for (String path : getSimulations()) {
				try {
					for (String subPath : path.split("\n")) {
						if (!(new File(subPath).exists() && !subPath.isEmpty() )) {
							fails.add(path);
							break;
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			for (String path : fails) {
				removeSimulation(path);
			}

	}
}
