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
 * The controller for the TrafficLightDetails.
 *
 * @author Leon
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
	 * The sumo traffic light id.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	@FXML
	private TextField sumoId;

    /**
     * The phase duration.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    @FXML
    private TextField phaseTime;

    /**
     * The selected link (or stop line).
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    @FXML
    private TextField selectedLink;

    /**
     * The text field to change the phase duration.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
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

    /**
     * pendingRYGState is the edited state by the user.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    private String pendingRYGState = null;
    /**
     * savedControllers is the tlID together with the program of that tl in a hashmap.
     * This allows the user to have tl-states from multiple different tls saved simultaneously.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    private static final Map<String, SumoTLSController> savedControllers = new HashMap<>();


    /**
     * The setup method for this class to fill it with data.
     *
     * @author Leon
     */
    public void setup(WorldTrafficLight worldTrafficLight) {
		this.worldTrafficLight = worldTrafficLight;

        phaseTime.setText(Double.toString(this.worldTrafficLight.getwTrafficLight().getPhaseLen()));
        selectedLink.setText("StopLineIndex: " + this.worldTrafficLight.getwLink().getTLIndex());
		displayName.setText(this.worldTrafficLight.getDisplayName());
		sumoId.setText(this.worldTrafficLight.getwTrafficLight().getId());
	}

    /**
     * The update method for this class to update its data.
     *
     * @author Leon
     */
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

    /**
     * Modifies the tls current state string to be used later. <br>
     * This function works in tandem with <code>buttonUpdater</code>.
     *
     * @author Leon
     */
	@FXML
	private void onRedButtonClicked() {
        buttonUpdater('r');
    }

    /**
     * Modifies the tls current state string to be used later. <br>
     * This function works in tandem with <code>buttonUpdater</code>.
     *
     * @author Leon
     */
	@FXML
	private void onYellowButtonClicked() {
        buttonUpdater('y');
    }

    /**
     * Modifies the tls current state string to be used later. <br>
     * This function works in tandem with <code>buttonUpdater</code>.
     *
     * @author Leon
     */
	@FXML
	private void onGreenButtonClicked() {
        buttonUpdater('G');
    }


    /**
     * This method modifies the character in the position that is dictated by the lane index
     * and sets it to the char that was passed in by one of the function above.
     * The string is then saved for later use.
     *
     * @param newState is the char that was set by one of the three previous functions.
     * @author Leon
     */
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

    /**
     * Saves the current program with the associated tlID.
     *
     * @author Leon
     */
    @FXML
    private void onSaveCurrentStateClicked(){
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        SumoTLSController currentController = wtl.getProgram();
        String currentTLID = this.worldTrafficLight.getwTrafficLight().getId();
        savedControllers.put(currentTLID, currentController);
    }


    /**
     * This method applies all changes that have been made by the user.
     * If only one of two parameters have been changed
     * (phase length or phase state), the other one remains set to its default.
     * When the updating happens, only the current phase is modified,
     * leaving the other ones unchanged.
     *
     * @author Leon
     */
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
            // This sets the duration for the phase right after it is changed.
            // If this is not implemented, the user would have to wait one entire cycle to see the effect.
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

        // Only changed the phase that matches the one that is currently active.
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


    /**
     * This method resets the tls program to the one that was saved.
     * Looks if the tlID is in the hashmap, so that it can be restored
     * correctly. This also allows for many states to be saved simultaneously.
     *
     * @author Leon
     */
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
