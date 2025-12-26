package org.group_three.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.ui.world.WorldVehicle;

public class StatisticsController {

    private SimController simcon;

    @FXML
    private TextField averageSpeed;

    @FXML
    private TextField carAmount;

    @FXML
    private TextField networkLength;

    public void setup() {
        averageSpeed.setText(Double.toString(simcon.getAverageVehSpeed()));
        carAmount.setText(Integer.toString(simcon.getVehicleIDList().size()));
        networkLength.setText(Double.toString(simcon.getNetworkLength()));
    }

    public void update() {
        averageSpeed.setText(Double.toString(simcon.getAverageVehSpeed()));
        carAmount.setText(Integer.toString(simcon.getVehicleIDList().size()));
    }
}
