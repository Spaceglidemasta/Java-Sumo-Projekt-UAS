package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.group_three.model.WTrafficLight;
import org.group_three.ui.world.WorldTrafficLight;
import java.util.ArrayList;
import java.util.HashMap;
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

    private static final String DEFAULT_PROGRAM_ID = "0";

    private WorldTrafficLight worldTrafficLight;

    private String pendingRYGState = null;

    private static final Map<String, SumoTLSController> savedControllers = new HashMap<>();

    public void setup(WorldTrafficLight worldTrafficLight) {
		this.worldTrafficLight = worldTrafficLight;

        phaseTime.setText(Double.toString(this.worldTrafficLight.getwTrafficLight().getPhaseLen()));
        selectedLink.setText("StopLineIndex: " + this.worldTrafficLight.getwLink().getTLIndex());
		displayName.setText(this.worldTrafficLight.getDisplayName());
		sumoId.setText(this.worldTrafficLight.getwTrafficLight().getId());
	}

    public void update() {
            phaseTime.setText(Double.toString(this.worldTrafficLight.getwTrafficLight().getPhaseLen()));
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
        buttonUpdater('r');
    }


	@FXML
	private void onYellowButtonClicked() {
        buttonUpdater('y');
    }


	@FXML
	private void onGreenButtonClicked() {
        buttonUpdater('G');
    }

    private void buttonUpdater(char newState) {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        String state = wtl.getRYGState(wtl.getId());
        int laneIndex = worldTrafficLight.getwLink().getTLIndex();
        StringBuilder stateBuilder = new StringBuilder(state);

        if (laneIndex >= 0 && laneIndex < stateBuilder.length()) {
            stateBuilder.setCharAt(laneIndex, newState);
            pendingRYGState = stateBuilder.toString();
        }
    }

    @FXML
    private void onSaveCurrentStateClicked(){
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        SumoTLSController currentController = wtl.getProgram();
        String currentTLID = this.worldTrafficLight.getwTrafficLight().getId();
        savedControllers.put(currentTLID, currentController);
    }


    @FXML
    private void onApplyButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        SumoTLSController controller = wtl.getProgram();
        SumoTLSProgram program = controller.get(DEFAULT_PROGRAM_ID);
        int currentPhaseIdx = program.currentPhaseIndex;

        int newDuration = (int) program.phases.get(currentPhaseIdx).duration;
        String newPhase;

        String userInputDuration = changePhaseDuration.getText();
        if (userInputDuration != null&& !userInputDuration.trim().isEmpty()) {
            newDuration = Integer.parseInt(userInputDuration.trim());
            wtl.setPhaseLen(newDuration);
        }

        if (pendingRYGState != null) {
            newPhase = pendingRYGState;
        } else {
            newPhase = program.phases.get(currentPhaseIdx).phasedef;
        }

        SumoTLSPhase updatedPhase = new SumoTLSPhase(newDuration, newPhase);

        SumoTLSProgram newProgram = new SumoTLSProgram();
        newProgram.subID = program.subID;
        newProgram.type = program.type;
        newProgram.currentPhaseIndex = program.currentPhaseIndex;

        newProgram.phases = new ArrayList<>();
        for (int i = 0; i < program.phases.size(); i++) {
            if (i == currentPhaseIdx) {
                newProgram.phases.add(updatedPhase);
            } else {
                newProgram.phases.add(program.phases.get(i));
            }
        }
        wtl.setProgram(newProgram);
    }


    @FXML
    private void onResetButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        String currentTLID = this.worldTrafficLight.getwTrafficLight().getId();
        if (savedControllers.containsKey(currentTLID)) {
            SumoTLSController savedController = savedControllers.get(currentTLID);
            SumoTLSProgram program = savedController.programs.get(DEFAULT_PROGRAM_ID);
            if (program != null) {
                String currentPhase = wtl.getRYGState(wtl.getId());
                int currentCount = currentPhase.length();

                String savedPhase = program.phases.getFirst().phasedef;
                int savedCount = savedPhase.length();

                if (currentCount == savedCount) {
                    wtl.setProgram(program);
                }
            }
        }
    }



}
