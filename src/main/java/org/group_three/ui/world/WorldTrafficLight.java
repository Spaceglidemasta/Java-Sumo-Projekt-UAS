package org.group_three.ui.world;

import de.tudresden.sumo.objects.SumoTLSController;
import de.tudresden.sumo.objects.SumoTLSPhase;
import de.tudresden.sumo.objects.SumoTLSProgram;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.model.WLink;
import org.group_three.model.WTrafficLight;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.DetailsPanel_TrafficLight_Controller;

/**
 * The class to render TrafficLights.
 * Incomplete and only displays stop lines yet.
 *
 * @author Joel
 */
public class WorldTrafficLight extends WorldObject {

	//++++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	public WTrafficLight getwTrafficLight() {
		return wTrafficLight;
	}

    /**
     * time until the next switch of the tl.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    private int initialRemainingTimeUntilSwitch = 0;

    /**
     * Countdown time (set to -1 if not initialized).
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    private int countdownStartSimTime = -1;
    /**
	 * The WTrafficLight object which is grouping the WLink classes.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WTrafficLight wTrafficLight;

	public WLink getwLink() {
		return wLink;
	}

	/**
	 * The WLink object which owns this class.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final WLink wLink;

	/**
	 * The size of the traffic lights stop line.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Vector2D size;
	private DetailsPanel_TrafficLight_Controller detailsPanelTrafficLightController;

	//--------------------------------------------------MemberVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++Constructors++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public WorldTrafficLight() {
		super();
		this.wTrafficLight = null;
		this.wLink = null;
		this.size = new Vector2D();
		remove();
	}

	/**
	 * The default WorldTrafficLight constructor to spawn a new WorldTrafficLight in a world.
	 *
	 * @param world         The world to which the WorldRoad should be added.
	 * @param canvas        The canvas of the world.
	 * @param displayName   The display name which should show up on selection.
	 * @param wTrafficLight The WTrafficLight reference.
	 * @param wLink         The WLink reference.
	 * @author Joel
	 */
	public WorldTrafficLight(World world, Canvas canvas, String displayName, WTrafficLight wTrafficLight, WLink wLink) {
		super(world, canvas, displayName);
		this.wTrafficLight = wTrafficLight;
		this.wLink = wLink;

		setPosition(wLink.mid);
		setRotation(wLink.angle);

		size = new Vector2D(wLink.getLen(), wLink.getWidth());

		setInteractable(true);
		setSphereCollision(size.y / 2);
		detailClassPath = "/org/group_three/ui/fxml/DetailsPanel_TrafficLight.fxml";

		/*wTrafficLight.getPhaseIndex();
		wLink.getTLIndex();
		wTrafficLight.getPhaseLen();
		wTrafficLight.setProgram();
		wTrafficLight.pr*/


		//Debug.print(wTrafficLight.getProgram().programs.get(wTrafficLight.getProgramID()));

	}

	//--------------------------------------------------Constructors--------------------------------------------------

	/**
	 * The update method which is used to draw the WorldTrafficLight in the world.
     * It also draws the time if <code>showTLTiming</code> is true.
	 *
	 * @author Joel + Leon
	 */
	@Override
	public void update() {
		Color color = Color.GRAY;
		try {
			color = Meth.SumoClrToClr(wLink.getColor());
		} catch (Exception e) {
			//throw new RuntimeException(e);
			color = Color.GRAY;
		}

		drawRectangle(size.div(2), color);

		if (UI.showTLTiming) {
			SimController sim = SimController.getMainsimcon();

            int currentSimTime = sim.getTime();
            int baseRemaining = getTimeUntilNextState();
            if (countdownStartSimTime < 0 || baseRemaining != initialRemainingTimeUntilSwitch) {
                initialRemainingTimeUntilSwitch = baseRemaining;
                countdownStartSimTime = Math.max(0, currentSimTime - 1);
            }

            int elapsed = Math.max(0, currentSimTime - countdownStartSimTime);
            int remainingTimeUntilSwitch = Math.max(0, initialRemainingTimeUntilSwitch - elapsed);


			String text = Integer.toString(remainingTimeUntilSwitch);
			GraphicsContext gc = getGraphicsContext();
			gc.save();
			setDrawTransform();
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			
			double heightPx = getDrawSize(size.div(2)).y;
			gc.setFont(Font.font(Math.max(10, heightPx * 0.6)));
			double yOffset = -heightPx * 0.15;
			gc.rotate(90);
			gc.scale(-1, 1);

			gc.setStroke(Color.BLACK);
			gc.setLineWidth(Math.max(1.0, heightPx * 0.08));
			gc.strokeText(text, 0, yOffset);
			gc.setFill(Color.WHITE);
			gc.fillText(text, 0, yOffset);
			gc.restore();
		}
		
        updateDetailsPanel();
	}

	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		detailsPanelTrafficLightController = fxmlLoader.getController();
		detailsPanelTrafficLightController.setup(this);
	}

    @Override
    public void updateDetailsPanel() {
        if (detailsPanelTrafficLightController == null) {
            return;
        }
        detailsPanelTrafficLightController.update();
    }

    /**
     * Method to calculate when the tl will switch next.
     * It sums up the phase time (or the minDur) up until
     * a change occurs (tl changes color)
     *
     * @author Leon
     */
	private int getTimeUntilNextState() {
        SumoTLSController controller = wTrafficLight.getProgram();
        SumoTLSProgram program = controller.get("0");
        int currentPhaseIdx = program.currentPhaseIndex;
        SumoTLSPhase currentPhase = program.phases.get(currentPhaseIdx);
        String phaseString = currentPhase.phasedef;
        int tlIndex = wLink.getTLIndex();
        int remainingTime = 0;

        for (int i = currentPhaseIdx; i < program.phases.size(); i++) {
            SumoTLSPhase phase = program.phases.get(i);
            String phaseDef = phase.phasedef;
            double minDur = phase.minDur;
            int duration = (int) phase.duration;

            if (tlIndex >= phaseDef.length()) {
                break;
            }

            char stateChar = phaseDef.charAt(tlIndex);
            char currentChar = phaseString.charAt(tlIndex);

            if (stateChar == currentChar) {
                if (minDur > 0) {
                    remainingTime += (int) minDur;
                } else {
                    remainingTime += duration;
                }
            } else {
                break;
            }
        }
		return remainingTime;
	}

}