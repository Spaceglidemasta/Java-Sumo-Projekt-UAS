package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import org.group_three.api.SimController;
import org.group_three.service.records.EdgeRec;
import org.group_three.service.records.VehicleRec;


import java.util.*;

public class StatisticsAnalytics_Controller {

	@FXML
	private LineChart<Number, Number> avgSpeed;

	@FXML
	private ListView<String> edgeDensity;
	private static ListView<String> edgeDensityRef;

	private static XYChart.Series<Number, Number> seriesVehicle = new XYChart.Series<>();



	@FXML
	private void initialize() {
		edgeDensityRef = edgeDensity;
		avgSpeed.getData().add(seriesVehicle);


		for (int i = 0; i <= 30; i++) {
			avgVehicleSpeed.add(0);
		}

		update();
	}

	public static void clear() {
		avgVehicleSpeed.clear();
		for (int i = 0; i <= 30; i++) {
			avgVehicleSpeed.add(0);
		}

		edgeDensityRef.getItems().clear();

		update();
	}

	public static void update() {
		avgVehicleSpeed.addFirst(getAverageSpeed());

		seriesVehicle.getData().clear();
		for (int i = 0; i <= 30; i++) {
			seriesVehicle.getData().add(new XYChart.Data<>(i, avgVehicleSpeed.get(i)));
		}

		if (avgVehicleSpeed.size() > 31) avgVehicleSpeed.removeLast();


		edgeDensityRef.getItems().clear();
		for (String entry : getEdgeDensityData()) {
			edgeDensityRef.getItems().add(entry);
		}

	}

	private static List<Integer> avgVehicleSpeed = new ArrayList<>();

	private static int getAverageSpeed() {
		// get sim controller and validate
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return 0;

		double v = 0;
		List<VehicleRec> vehicleRecs = VehicleRec.collect(SimController.getMainsimcon());

		for (VehicleRec vrec : vehicleRecs) {
			v += vrec.avgspeed()*3.6;
		}

		return (int) Math.round(v/vehicleRecs.size());
	}

	private static List<String> getEdgeDensityData() {
		// get sim controller and validate
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return new ArrayList<>();

		List<EdgeRec> EdgeRecs = EdgeRec.collect(SimController.getMainsimcon());

		HashMap<String, Double> usage = new HashMap<>();
		HashMap<String, Double> length = new HashMap<>();

		for (EdgeRec erec : EdgeRecs) {
			String name = erec.name();
			Double use = erec.usage();
			Double len = erec.length();

			if (usage.keySet().contains(name)) {
				usage.put(name, usage.get(name) + use);
				length.put(name, length.get(name) + len);
			} else {
				usage.put(name, use);
				length.put(name, len);
			}
		}

		List<String> v = new ArrayList<>();
		for (String name : usage.keySet()) {
			if (usage.get(name) < 0.01) continue;
			v.add(String.format("%.3f", usage.get(name)) + " - " + name + ", Length: " + Math.round(length.get(name)) + "m");
		}
		v.sort(Collections.reverseOrder());

		return v;
	}
}
