package org.group_three.ui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.util.Duration;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.service.records.VehicleRec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsAnalyticsController {



	@FXML
	private LineChart<Integer, Integer> avgSpeed;

	@FXML
	private void initialize() {

		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), _ -> onTick())
		);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private void onTick() {
		if (!SimController.isValid()) return;
	}

	private HashMap<String, Double> edgeDensity = new HashMap<>();
	private List<Integer> avgVehicleSpeed = new ArrayList<>();

	private int getAverageSpeed() {
		if (!SimController.isValid()) return -1;

		double v = 0;
		List<VehicleRec> vehicleRecs = VehicleRec.collect(SimController.getMainsimcon());

		for (VehicleRec vrec : vehicleRecs) {
			v += vrec.avgspeed();
		}

		return (int) Math.round(v/vehicleRecs.size());
	}
}
