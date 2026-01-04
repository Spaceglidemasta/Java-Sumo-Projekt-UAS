package org.group_three.ui.controllers;

import de.tudresden.sumo.cmd.Lane;
import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.group_three.api.SimController;
import org.group_three.model.WTrafficLight;
import org.group_three.ui.world.WorldTrafficLight;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @FXML
    private Button redButton;

    @FXML
    private Button yellowButton;

    @FXML
    private Button greenButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button applyButton;

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
    
    private static final Map<String, Integer> adaptiveStepCounters = new HashMap<>();
    private static final Map<String, String> adaptiveState = new HashMap<>(); // "IDLE" or "GREEN"
    private static final int ADAPTIVE_CHECK_INTERVAL = 10;
    private static final int ADAPTIVE_GREEN_DURATION = 10;

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
        updateAdaptiveButtonState(this.worldTrafficLight.getwTrafficLight().getId());
	}

    /**
     * The update method for this class to update its data.
     * This update is called whenever a traffic light change occurs.
     *
     * @author Leon
     */
    public void update() {
        phaseTime.setText(Double.toString(getRealPhaseLength()));
        String tlID = this.worldTrafficLight.getwTrafficLight().getId();

        updateResetButtonState(tlID);
        updateAdaptiveButtonState(tlID);
        updateAdaptiveControl(tlID);
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

    /**
     * Update the adaptive toggle button text to reflect the stored state for the given tlId.
     *
     * @author Leon
     */
    private void updateAdaptiveButtonState(String tlId) {
        boolean isAdaptiveActive = adaptiveSavedStates.containsKey(tlId);
        adaptiveStateButton.setText(isAdaptiveActive ? "Static mode" : "Adaptive mode");
    }

    /**
     * Enable or disable all control buttons and text fields.
     *
     * @param enabled true to enable controls, false to disable
     * @author Leon
     */
    private void setControlsEnabled(boolean enabled) {
        phaseTime.setEditable(enabled);
        resetButton.setDisable(!enabled);
        redButton.setDisable(!enabled);
        yellowButton.setDisable(!enabled);
        greenButton.setDisable(!enabled);
        saveButton.setDisable(!enabled);
        applyButton.setDisable(!enabled);
    }

    /**
     * Find all lanes with the maximum number of halting vehicles.
     *
     * @param tlID the traffic light ID
     * @return a list of lane IDs that all have the maximum halting count, or empty list if no vehicles
     * @author Leon
     */
    private List<String> findLanesWithMostHaltingVehicles(String tlID) {
        List<String> controlledLanes = SimController.getMainsimcon().getControlledLanes(tlID);
        List<String> result = new ArrayList<>();
        
        if (controlledLanes == null || controlledLanes.isEmpty()) {
            return result;
        }
        
        int maxHaltingCars = 0;
        Map<String, Integer> laneHaltingCounts = new HashMap<>();
        
        for (String laneID : controlledLanes) {
            int carsHalting = (int) SimController.getMainsimcon().jobget(Lane.getLastStepHaltingNumber(laneID));
            int haltingCount = 0;
            if (carsHalting != 0) {
                haltingCount = carsHalting;
            }
            
            laneHaltingCounts.put(laneID, haltingCount);
            if (haltingCount > maxHaltingCars) {
                maxHaltingCars = haltingCount;
            }
        }
        
        if (maxHaltingCars > 0) {
            for (String laneID : controlledLanes) {
                if (laneHaltingCounts.get(laneID) == maxHaltingCars) {
                    result.add(laneID);
                }
            }
        }
        
        return result;
    }

    /**
     * Set specific lanes to green while keeping all others red.
     * The phase duration is set to ADAPTIVE_GREEN_DURATION.
     *
     * @param tlID the traffic light ID
     * @param greenLaneIDs list of lane IDs to set to green
     * @author Leon
     */
    private void setLanesToGreen(String tlID, List<String> greenLaneIDs) {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        String currentRYGState = wtl.getRYGState(tlID);
        List<String> controlledLanes = SimController.getMainsimcon().getControlledLanes(tlID);
        
        if (controlledLanes == null) {
            return;
        }
        
        StringBuilder greenState = new StringBuilder();
        for (int i = 0; i < currentRYGState.length(); i++) {
            greenState.append('r');
        }
        
        for (String greenLaneID : greenLaneIDs) {
            int laneIndex = controlledLanes.indexOf(greenLaneID);
            if (laneIndex >= 0 && laneIndex < greenState.length()) {
                greenState.setCharAt(laneIndex, 'G');
            }
        }
        
        SumoTLSController controller = wtl.getProgram();
        SumoTLSProgram program = controller.get(DEFAULT_PROGRAM_ID);
        
        SumoTLSPhase greenPhase = new SumoTLSPhase(ADAPTIVE_GREEN_DURATION, greenState.toString());
        SumoTLSProgram newProgram = new SumoTLSProgram();

        newProgram.subID = program.subID;
        newProgram.type = program.type;
        newProgram.currentPhaseIndex = program.currentPhaseIndex;
        newProgram.phases = new ArrayList<>();
        for (int i = 0; i < program.phases.size(); i++) {
            if (i == program.currentPhaseIndex) {
                newProgram.phases.add(greenPhase);
            } else {
                newProgram.phases.add(program.phases.get(i));
            }
        }
        
        wtl.setPhaseLen(ADAPTIVE_GREEN_DURATION);
        wtl.setProgram(newProgram);
        adaptiveState.put(tlID, "GREEN");
        adaptiveStepCounters.put(tlID, 0);
    }

    /**
     * Set all lanes controlled by the traffic light to red for 1000 seconds.
     *
     * @param tlID the traffic light ID
     * @author Leon
     */
    private void setIdleState(String tlID) {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        String currentRYGState = wtl.getRYGState(tlID);
        
        StringBuilder redState = new StringBuilder();
        for (int i = 0; i < currentRYGState.length(); i++) {
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
            newProgram.phases.add(redPhase);
        }
        
        wtl.setPhaseLen(1000);
        wtl.setProgram(newProgram);
        adaptiveState.put(tlID, "IDLE");
        adaptiveStepCounters.put(tlID, 0);
    }

    /**
     * Update method to check for current state. It counts the time that has passed
     * and eather sets a lane to green if halting cars are detected, or back to idle
     * if no cars are detected.
     *
     * @author Leon
     */
    private void updateAdaptiveControl(String tlID) {
        if (!adaptiveSavedStates.containsKey(tlID)) {
            return;
        }
        
        String state = adaptiveState.getOrDefault(tlID, "IDLE");
        Integer stepCounter = adaptiveStepCounters.getOrDefault(tlID, 0);
        
        if ("IDLE".equals(state)) {
            if (stepCounter >= ADAPTIVE_CHECK_INTERVAL) {
                List<String> lanesWithMostCars = findLanesWithMostHaltingVehicles(tlID);
                if (!lanesWithMostCars.isEmpty()) {
                    setLanesToGreen(tlID, lanesWithMostCars);
                } else {
                    adaptiveStepCounters.put(tlID, 0);
                }
            } else {
                stepCounter++;
                adaptiveStepCounters.put(tlID, stepCounter);
            }
        } else if ("GREEN".equals(state)) {
            if (stepCounter >= ADAPTIVE_GREEN_DURATION) {
                setIdleState(tlID);
            } else {
                stepCounter++;
                adaptiveStepCounters.put(tlID, stepCounter);
            }
        }
    }


    /**
     * Button manager for when the user clicks on the "Adaptive mode" button.
     * If the button is clicked, the logic above applies and the simulation state is saved.
     * By clicking on the button again it can be restored.
     *
     * @author Leon
     */
    @FXML
    private void onAdaptiveButtonClicked() {
        WTrafficLight wtl = worldTrafficLight.getwTrafficLight();
        String currentText = adaptiveStateButton.getText();
        String currentTLID = wtl.getId();

        if (currentText.equals("Adaptive mode")) {
            SumoTLSController currentController = wtl.getProgram();
            
            if (!adaptiveSavedStates.containsKey(currentTLID)) {
                adaptiveSavedStates.put(currentTLID, currentController);
            }

            setIdleState(currentTLID);
            adaptiveStateButton.setText("Static mode");
            setControlsEnabled(false);

        } else {
            if (adaptiveSavedStates.containsKey(currentTLID)) {
                SumoTLSController savedController = adaptiveSavedStates.get(currentTLID);
                SumoTLSProgram program = savedController.programs.get(DEFAULT_PROGRAM_ID);
                wtl.setPhaseLen(program.phases.get(program.currentPhaseIndex).duration);
                wtl.setProgram(program);
                adaptiveSavedStates.remove(currentTLID);
            }
            
            adaptiveStepCounters.remove(currentTLID);
            adaptiveState.remove(currentTLID);
            
            adaptiveStateButton.setText("Adaptive mode");
            setControlsEnabled(true);
        }
    }

}
