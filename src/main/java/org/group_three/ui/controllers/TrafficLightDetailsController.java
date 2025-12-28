package org.group_three.ui.controllers;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;
import it.polito.appeal.traci.SumoTraciConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WTrafficLight;
import org.group_three.ui.Meth;
import org.group_three.ui.SimView2D;
import org.group_three.ui.world.WorldRoute;
import org.group_three.ui.world.WorldTrafficLight;
import org.group_three.ui.world.WorldVehicle;

import java.util.ArrayList;
import java.util.Map;

/**
 * The controller for the VehicleDetails.
 *
 * @author Joel
 */
public class TrafficLightDetailsController {


	/**
	 * The world objects display name.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField displayName;

	/**
	 * The sumo vehicle id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField sumoId;

    /**
     * The phase time.
     *
     * @author Joel
     */
    @SuppressWarnings("JavadocDeclaration")
    @FXML
    private TextField phaseTime;

    @FXML
    private TextField selectedLink;

    @FXML
    private TextField changePhaseDuration = null;
	@FXML
	private Button redButton;
	@FXML
	private Button yellowButton;
	@FXML
	private Button greenButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button saveCurrentStateButton;


    private WorldTrafficLight worldTrafficLight;

    private SumoTLSController sumoTLSController;

    private String pendingRYGState = null;

    private SumoTLSController savedProgram;

    private boolean isManualPhaseTimeSet = false;

    public void setup(WorldTrafficLight worldTrafficLight) {
		this.worldTrafficLight = worldTrafficLight;

        phaseTime.setText(Double.toString(this.worldTrafficLight.getwTrafficLight().getPhaseLen()));
        selectedLink.setText("StopLineIndex: " + this.worldTrafficLight.getwLink().getTLIndex());
		displayName.setText(this.worldTrafficLight.getDisplayName());
		sumoId.setText(this.worldTrafficLight.getwTrafficLight().getId());
	}

    public void update() {
        if (!isManualPhaseTimeSet) {
            phaseTime.setText(Double.toString(this.worldTrafficLight.getwTrafficLight().getPhaseLen()));
        }
    }


	/**
	 * The initialize method of the traffic light details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {


	}



	@FXML
	private void onRedButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();

        String stateString = wtl.getRYGState(this.worldTrafficLight.getwTrafficLight().getId());
        int selectedLaneIndex = this.worldTrafficLight.getwLink().getTLIndex();
        StringBuilder stateBuilder = new StringBuilder(stateString);

        if (selectedLaneIndex >= 0 && selectedLaneIndex < stateBuilder.length()) {
            stateBuilder.setCharAt(selectedLaneIndex, 'r');
            pendingRYGState = stateBuilder.toString();
	    }
    }


	@FXML
	private void onYellowButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();

        String stateString = wtl.getRYGState(this.worldTrafficLight.getwTrafficLight().getId());
        int selectedLaneIndex = this.worldTrafficLight.getwLink().getTLIndex();
        StringBuilder stateBuilder = new StringBuilder(stateString);

        if (selectedLaneIndex >= 0 && selectedLaneIndex < stateBuilder.length()) {
            stateBuilder.setCharAt(selectedLaneIndex, 'y');
            pendingRYGState = stateBuilder.toString();
        }
    }


	@FXML
	private void onGreenButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();

        String stateString = wtl.getRYGState(this.worldTrafficLight.getwTrafficLight().getId());
        int selectedLaneIndex = this.worldTrafficLight.getwLink().getTLIndex();
        StringBuilder stateBuilder = new StringBuilder(stateString);

        if (selectedLaneIndex >= 0 && selectedLaneIndex < stateBuilder.length()) {
            stateBuilder.setCharAt(selectedLaneIndex, 'G');
            pendingRYGState = stateBuilder.toString();
        }
    }

    @FXML
    private void onSaveCurrentStateClicked(){
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        savedProgram = wtl.getProgram();

        for (Map.Entry<String, SumoTLSProgram> entry : savedProgram.programs.entrySet()) {
            String subID = entry.getKey();
            SumoTLSProgram program = entry.getValue();

            System.out.println("Program SubID: " + subID);
            System.out.println("Type: " + program.type);
            System.out.println("Current Phase Index: " + program.currentPhaseIndex);
            System.out.println("Phases:");
            for (SumoTLSPhase phase : program.phases) {
                System.out.println(" - " + phase.phasedef);
            }
            System.out.println("----------------------------");
        }
    }


    @FXML
    private void onApplyButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        SumoTLSController controller = wtl.getProgram();
        SumoTLSProgram program = controller.get("0");
        int currentPhaseIdx = program.currentPhaseIndex;

        SumoTLSProgram newProgram = new SumoTLSProgram();
        newProgram.subID = program.subID;
        newProgram.type = program.type;
        newProgram.currentPhaseIndex = program.currentPhaseIndex;

        newProgram.phases = new ArrayList<>();
        for (int i = 0; i < program.phases.size(); i++) {
            if (i == currentPhaseIdx) {
                SumoTLSPhase updatedPhase = new SumoTLSPhase(
                        (int) program.phases.get(i).duration,
                        pendingRYGState != null ? pendingRYGState : program.phases.get(i).phasedef
                );
                newProgram.phases.add(updatedPhase);
            } else {
                newProgram.phases.add(program.phases.get(i));
            }
        }
        wtl.setProgram(newProgram);
    }


    @FXML
    private void onResetButtonClicked() {
        if (savedProgram != null) {
            SumoTLSProgram program = savedProgram.programs.get("0");
            if (program != null) {
                WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
                wtl.setProgram(program);
            } else {
                System.out.println("No program found for key '0'.");
            }
        } else {
            System.out.println("No saved program to reset to.");
        }
    }



}
