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
import org.group_three.ui.controllers.DetailsPanelTrafficLightController;

import java.util.logging.Logger;

/**
 * The class to render TrafficLights.
 * Incomplete and only displays stop lines yet.
 *
 * @author Joel, Leon
 */
public class WorldTrafficLight extends WorldObject {

	// Logger
	private static final Logger log = Logger.getLogger(WorldTrafficLight.class.getName());

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

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

	/**
	 * The details panel reference for this world traffic light.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private DetailsPanelTrafficLightController detailsPanelTrafficLightController;

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * It's an invalid creation so it will directly be removed from the world after creation.
	 *
	 * @author Joel
	 */
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
	 * @param world         The world to which the WorldTrafficLight should be added.
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
		setDetailClassPath("DetailsPanelTrafficLight.fxml");
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldTrafficLight in the world.
	 * It also draws the time if <code>showTLTiming</code> is true.
	 *
	 * @author Joel + Leon
	 */
	@Override
	public void update() {
		// update traffic light stop line color
		Color color;
		try {
			color = Meth.SumoClrToClr(wLink.getColor());
		} catch (Exception e) {
			log.warning("Failed to set WorldTrafficLight color.");
			color = Color.GRAY;
		}

		// draw stop line
		drawRectangle(size.div(2), color);

		if (UI.showTLTiming) {
			SimController simcon = SimController.getMainsimcon();
			if (simcon == null) return;

			int currentSimTime = simcon.getTime();
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

	/**
	 * A method to set up the details panel for this class.
	 *
	 * @param fxmlLoader The FXML Loader reference to load the details panel into.
	 * @author Joel
	 */
	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		// set controller reference
		detailsPanelTrafficLightController = fxmlLoader.getController();

		// run setup process
		detailsPanelTrafficLightController.setup(this);
	}

	/**
	 * A method to update the details panel for this class.
	 *
	 * @author Joel
	 */
	@Override
	public void updateDetailsPanel() {
		// skip if controller is invalid
		if (detailsPanelTrafficLightController == null) {
			return;
		}

		// run update process
		detailsPanelTrafficLightController.update();
	}

	/**
	 * A getter to return the WTrafficLight reference of this object.
	 *
	 * @return The WTrafficLight reference of this object.
	 * @author Joel
	 */
	public WTrafficLight getwTrafficLight() {
		return wTrafficLight;
	}

	/**
	 * A getter to return the WLink reference of this object.
	 *
	 * @return The WLink reference of this object.
	 * @author Joel
	 */
	public WLink getwLink() {
		return wLink;
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

	//-----------------------------------------------------Methods------------------------------------------------------

}