package org.group_three.ui.controllers;

import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.group_three.debug.Debug;
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
     * The text field to view and change the phase duration.
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

//    /**
//     *
//     *
//     * @author Leon
//     */
//    @SuppressWarnings("JavadocDeclaration")
//    @FXML
//    private TextField timeTillNextChange;
    /**
     * The reset button to restore saved state.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    @FXML
    private Button resetButton;


    @FXML
    private Button adaptiveStateButton;

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

    private static final Map<String, SumoTLSController> adaptiveSavedStates = new HashMap<>();
    //  private double timeRemainingSeconds = -1;
//    private static boolean firstCountdown = true;
    /**
     * The setup method for this class to fill it with data.
     *
     * @author Leon
     */
    public void setup(WorldTrafficLight worldTrafficLight) {
		this.worldTrafficLight = worldTrafficLight;

        phaseTime.setText(Double.toString(getRealPhaseLength()));
        selectedLink.setText(Integer.toString(this.worldTrafficLight.getwLink().getTLIndex()));

        updateResetButtonState(this.worldTrafficLight.getwTrafficLight().getId());
//        this.timeRemainingSeconds = getTimeUntilNextState();
//        timeTillNextChange.setText(Double.toString(timeRemainingSeconds));
	}

    /**
     * The update method for this class to update its data.
     * This update is called whenever a traffic light change occurs.
     *
     * @author Leon
     */
    public void update() {
        phaseTime.setText(Double.toString(getRealPhaseLength()));
        updateResetButtonState(this.worldTrafficLight.getwTrafficLight().getId());

//        if (timeRemainingSeconds > 0) {
//            timeRemainingSeconds--;
//        } else {
//            firstCountdown = false;
//            timeRemainingSeconds = getTimeUntilNextState();
//        }
//        timeTillNextChange.setText(Double.toString(timeRemainingSeconds));
    }


	/**
	 * The initialize method of the traffic light details panel.
	 *
	 * @author Joel
	 */
	@FXML
	private void initialize() {
	}

    private double getRealPhaseLength(){
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        SumoTLSController controller = wtl.getProgram();
        SumoTLSProgram program = controller.get(DEFAULT_PROGRAM_ID);
        SumoTLSPhase phase = program.phases.get(wtl.getPhaseIndex());

        if(phase.minDur > 0){
            return phase.minDur;
        }
        return wtl.getPhaseLen();
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
     * Saves the current program with the associated tlID
     * in <code>savedControllers</code>.
     *
     * @author Leon
     */
    @FXML
    private void onSaveCurrentStateClicked(){
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        SumoTLSController currentController = wtl.getProgram();
        String currentTLID = wtl.getId();
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

        String userInputDuration = phaseTime.getText();
        if (userInputDuration != null && !userInputDuration.trim().isEmpty()) {
            double parsedValue = Double.parseDouble(userInputDuration.trim());
            newDuration = (int) parsedValue;
            // This sets the duration for the phase right after the change is applied.
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
        String currentTLID = wtl.getId();
        if (savedControllers.containsKey(currentTLID)) {
            SumoTLSController savedController = savedControllers.get(currentTLID);
            SumoTLSProgram program = savedController.programs.get(DEFAULT_PROGRAM_ID);
            wtl.setProgram(program);
        }
    }

    /**
     * Makes the reset buton disabled if there is no saved state to reset to,
     * depending on selected tl
     *
     * @author Leon
     */
    private void updateResetButtonState(String tlId) {
        boolean hasSavedController = savedControllers.containsKey(tlId);
        resetButton.setDisable(!hasSavedController);
    }


    @FXML
    private void onAdaptiveButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        String currentText = adaptiveStateButton.getText();
        if (currentText.equals("Adaptive mode")) {


            SumoTLSController currentController = wtl.getProgram();
            String currentTLID = wtl.getId();
            if (!adaptiveSavedStates.containsKey(currentTLID)) {
                adaptiveSavedStates.put(currentTLID, currentController);
            }
            adaptiveStateButton.setText("Static mode");


            String currentPhaseDef = wtl.getRYGState(currentTLID);

            StringBuilder redState = new StringBuilder();
            int numLanes = currentPhaseDef.length();
            for (int i = 0; i < numLanes; i++) {
                redState.append('r');
            }

            SumoTLSController controller = wtl.getProgram();
            SumoTLSProgram program = controller.get(DEFAULT_PROGRAM_ID);

            SumoTLSPhase redPhase = new SumoTLSPhase(1000, redState.toString());
            SumoTLSProgram newProgram = new SumoTLSProgram();
            newProgram.subID = program.subID;
            newProgram.type = program.type;
            newProgram.currentPhaseIndex = program.currentPhaseIndex;
            newProgram.phases = new ArrayList<>();
            for (int i = 0; i < program.phases.size(); i++) {
                if (i == program.currentPhaseIndex) {
                    newProgram.phases.add(redPhase);
                } else {
                    newProgram.phases.add(program.phases.get(i));
                }
            }
            wtl.setPhaseLen(1000);
            wtl.setProgram(newProgram);

        } else {
            String currentTLID = wtl.getId();
            if (adaptiveSavedStates.containsKey(currentTLID)) {
                SumoTLSController savedController = adaptiveSavedStates.get(currentTLID);
                SumoTLSProgram program = savedController.programs.get(DEFAULT_PROGRAM_ID);
                wtl.setPhaseLen(program.phases.get(program.currentPhaseIndex).duration);
                wtl.setProgram(program);
            }
            adaptiveStateButton.setText("Adaptive mode");
        }
    }

//    private double getTimeUntilNextState() {
//        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
//        SumoTLSController controller = wtl.getProgram();
//        SumoTLSProgram program = controller.get(DEFAULT_PROGRAM_ID);
//        int currentPhaseIdx = program.currentPhaseIndex;
//        SumoTLSPhase currentPhase = program.phases.get(currentPhaseIdx);
//
//        String phaseString = currentPhase.phasedef;
//        int tlIndex = this.worldTrafficLight.getwLink().getTLIndex();
//
//        int remainingTime = 0;
//
//        for (int i = currentPhaseIdx; i < program.phases.size(); i++) {
//            SumoTLSPhase phase = program.phases.get(i);
//            String phaseDef = phase.phasedef;
//            double minDur = phase.minDur;
//            int duration = (int) phase.duration;
//            if (tlIndex >= phaseDef.length()) {
//                break;
//            }
//            char stateChar = phaseDef.charAt(tlIndex);
//            char currentChar = phaseString.charAt(tlIndex);
//
//            if (stateChar == currentChar) {
//                if(minDur > 0){
//                    remainingTime += (int) minDur;
//                }
//                else remainingTime += duration;
//            } else {
//                break;
//            }
//        }
//        if(firstCountdown){
//            return remainingTime;
//        }
//        else{
//            return remainingTime - 1;
//        }
//
//    }

}
