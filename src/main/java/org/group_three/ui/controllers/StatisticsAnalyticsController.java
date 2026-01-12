package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import org.group_three.api.SimController;
import org.group_three.service.records.EdgeRec;
import org.group_three.service.records.VehicleRec;


import java.util.*;

/**
 * The controller for the statistics and analytics tab.
 *
 * @author Joel
 */
public class StatisticsAnalyticsController {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The static list view for the edge density.
	 *
	 * @author Joel
	 */
	private static ListView<String> edgeDensityRef;

	/**
	 * A chart series for the average speed of the line chart.
	 *
	 * @author Joel
	 */
	private static final XYChart.Series<Number, Number> series_avgSpeed = new XYChart.Series<>();

	/**
	 * The list of average vehicle speeds.
	 *
	 * @author Joel
	 */
	private static final List<Integer> avgVehicleSpeed = new ArrayList<>();

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The line chart for the average speeds.
	 *
	 * @author Joel
	 */
	@FXML
	private LineChart<Number, Number> avgSpeed;

	/**
	 * The list view for the edge density.
	 *
	 * @author Joel
	 */
	@FXML
	private ListView<String> edgeDensity;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The initialize method for this controller.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {
		edgeDensityRef = edgeDensity;

		// add chart series to line chart
		avgSpeed.getData().add(series_avgSpeed);

		// clear all data point by replacing them with 0
		for (int i = 0; i <= 30; i++) {
			avgVehicleSpeed.add(0);
		}

		// update data display
		update();
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++GetterSetterClassMethods+++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to get the current avg speed of all vehicles combined.
	 *
	 * @return The avg speed of all vehicles combined.
	 * @author Joel
	 */
	private static int getAverageSpeed() {
		// get sim controller and validate
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return 0;

		double v = 0;
		// get all vehicle records
		List<VehicleRec> vehicleRecs = VehicleRec.collect(SimController.getMainsimcon());

		// loop through all records and extract their average speed and multiply it by 3.6 to convert it from m/s to km/h
		for (VehicleRec vrec : vehicleRecs) {
			v += vrec.avgspeed() * 3.6;
		}

		// get and return the middle value
		return (int) Math.round(v / vehicleRecs.size());
	}

	/**
	 * A method to get a list of all sorted edges by density.
	 *
	 * @return A list of sorted edge data.
	 * @author Joel
	 */
	private static List<String> getEdgeDensityData() {
		// get sim controller and validate
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return new ArrayList<>();

		// grab all edge records
		List<EdgeRec> EdgeRecs = EdgeRec.collect(SimController.getMainsimcon());

		HashMap<String, Double> usage = new HashMap<>();
		HashMap<String, Double> length = new HashMap<>();

		// loop through every record
		for (EdgeRec erec : EdgeRecs) {
			String name = erec.name();
			Double use = erec.usage();
			Double len = erec.length();

			// check if street already exists in map
			if (usage.containsKey(name)) {
				// if it already exists add data to the maps with the given key
				usage.put(name, usage.get(name) + use);
				length.put(name, length.get(name) + len);

			} else {
				// if it doesn't already exist add the new data to the map
				usage.put(name, use);
				length.put(name, len);
			}
		}

		List<String> v = new ArrayList<>();
		// loop through all streets
		for (String name : usage.keySet()) {
			// if usage is less than 0.01 aka 1% skip/don't display
			if (usage.get(name) < 0.01) continue;

			// format edge data string and add it to the list
			v.add(String.format("%.3f", usage.get(name)) + " - " + name + ", Length: " + Math.round(length.get(name)) + "m");
		}

		// sort the list in reverse to have the highest usage at the top
		v.sort(Collections.reverseOrder());

		return v;
	}

	//---------------------------------------------GetterSetterClassMethods---------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to clear all stat entries.
	 *
	 * @author Joel
	 */
	public static void clear() {
		// clear all data point and replace them with 0
		avgVehicleSpeed.clear();
		for (int i = 0; i <= 30; i++) {
			avgVehicleSpeed.add(0);
		}

		// clear all edge density entries
		edgeDensityRef.getItems().clear();

		// update data display
		update();
	}

	/**
	 * A method to update the stat data and display.
	 *
	 * @author Joel
	 */
	public static void update() {
		// insert new avg speed at front
		avgVehicleSpeed.addFirst(getAverageSpeed());

		// clear chart series and move all old point times one back
		series_avgSpeed.getData().clear();
		for (int i = 0; i <= 30; i++) {
			series_avgSpeed.getData().add(new XYChart.Data<>(i, avgVehicleSpeed.get(i)));
		}

		// remove not displayed overflowing chart entries
		if (avgVehicleSpeed.size() > 31) avgVehicleSpeed.removeLast();


		// clear all edge density entries
		edgeDensityRef.getItems().clear();
		// get new entries and add them to the list view
		for (String entry : getEdgeDensityData()) {
			edgeDensityRef.getItems().add(entry);
		}
	}

	//---------------------------------------------------ClassMethods---------------------------------------------------

}