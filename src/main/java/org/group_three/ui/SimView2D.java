package org.group_three.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.*;
import org.group_three.ui.controllers.BodyController;
import org.group_three.ui.world.*;

import java.util.ArrayList;
import java.util.List;

import static org.group_three.ui.Meth.lerp;

/**
 * The class to manage and control the 2d simulation view.
 *
 * @author Joel
 */
public class SimView2D {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The canvas to render non-moving objects on.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static Canvas worldStaticRenderTarget;

	/**
	 * The canvas to moving objects on.
	 * Not used yet.
	 *
	 * @author Joel
	 */
	@SuppressWarnings({"unused", "FieldCanBeLocal", "JavadocDeclaration"})
	private static Canvas worldDynamicRenderTarget;

	/**
	 * Represents the window size of the simulation view.
	 * Is used to adjust the canvas sizes, as they don't scale by default.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static Pane renderTargetBounds;


	/**
	 * The graphics context which is used to draw on a canvas.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static GraphicsContext worldStaticRenderTarget_GraphicsContext;


	/**
	 * The world reference, which will change on runtime but should always be valid after ini.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static World world;

	/**
	 * The selected world object.
	 * Might be null.
	 * Maybe change later for a list so group selection is possible.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static WorldObject selected;

	/**
	 * All currently active vehicle id's.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static final List<String> vehicleIds = new ArrayList<>();

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++InitializeClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to initialize this class.
	 *
	 * @param wSRT The worldStaticRenderTarget.
	 * @param wDRT The worldDynamicRenderTarget.
	 * @param rTB  The renderBoundsPane.
	 * @author Joel
	 */
	public static void initialize(Canvas wSRT, Canvas wDRT, Pane rTB) {
		worldStaticRenderTarget = wSRT;
		worldDynamicRenderTarget = wDRT;
		renderTargetBounds = rTB;
		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();

		// create new world on ini so the default view is just an empty world instead of undefined.
		newWorld();
	}

	//--------------------------------------------------InitializeClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets the currently selected world object.
	 * Might be null.
	 *
	 * @return The currently selected world object.
	 * @author Joel
	 */
	@SuppressWarnings("unused")
	public static WorldObject getSelected() {
		return selected;
	}

	/**
	 * Gets the currently active world.
	 *
	 * @return The currently active world.
	 * @author Joel
	 */
	public static World getWorld() {
		return world;
	}

	//--------------------------------------------------GetterClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A setter method to set the currently selected world object.
	 * Updates the details panel after selection.
	 *
	 * @param selected The world object to select.
	 * @author Joel
	 */
	public static void setSelected(WorldObject selected) {
		if (SimView2D.selected == selected) return;

		SimView2D.selected = selected;

		// update details panel
		SimView2D.selected.setupDetailsPanel(BodyController.setDetailsPanel(SimView2D.selected.detailClassPath));
	}

	//--------------------------------------------------SetterClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The method to create a new world.
	 * Will be called on simulation load.
	 *
	 * @author Joel
	 */
	public static void newWorld() {
		world = new World();

		world.worldStaticRenderTarget = worldStaticRenderTarget;
		world.graphicsContext = worldStaticRenderTarget_GraphicsContext;

		// bind to view size changes to adjust viewer position offset
		renderTargetBounds.widthProperty().addListener((_, _, newValue) ->
				world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y))));

		renderTargetBounds.heightProperty().addListener((_, _, newValue) ->
				world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2))));

		// initialize viewer position with current view size
		world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth() / 2, worldStaticRenderTarget.getHeight() / 2));


		// skip if simulation is null
		if (SimController.getMainsimcon() == null) return;


		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();


        List<String> allJIDs = SimController.getMainsimcon().getJunctionIDList();

        if(allJIDs == null) return;
        // calculate rn, what was rn? something related to full world size and moving viewer initially to center of world
		for (String jid : allJIDs) {
			Vector2D jidV = new Vector2D(SimController.getMainsimcon().getJunctionPos(jid).x, SimController.getMainsimcon().getJunctionPos(jid).y);
			boolean firstIteration = jid.equals(allJIDs.getFirst());

			if (rnHeight.x > jidV.x || firstIteration)
				rnHeight.x = jidV.x;
			if (rnHeight.y < jidV.x || firstIteration) //noinspection SuspiciousNameCombination
				rnHeight.y = jidV.x;
			if (rnWidth.x > jidV.y || firstIteration) //noinspection SuspiciousNameCombination
				rnWidth.x = jidV.y;
			if (rnWidth.y < jidV.y || firstIteration)
				rnWidth.y = jidV.y;
		}

		// add world objects, order matters
		addPolygons(worldStaticRenderTarget);
		addJunctions(worldStaticRenderTarget);
		addRoads(worldStaticRenderTarget);
		addTrafficLights(worldStaticRenderTarget);
		addVehicles(worldStaticRenderTarget);


		// see rn above, make proper comments later
		world.setWorldSize(new Vector2D(Math.abs(rnHeight.x - rnHeight.y), Math.abs(rnWidth.x - rnWidth.y)).add(new Vector2D(128, 128)));
		world.setViewerPosition(new Vector2D(lerp(rnHeight.x, rnHeight.y, 0.5), lerp(rnWidth.x, rnWidth.y, 0.5)).negate());
		world.setWorldOffset(new Vector2D(lerp(rnHeight.x, rnHeight.y, 0.5), lerp(rnWidth.x, rnWidth.y, 0.5)));
	}

	/**
	 * A method to add all SUMO polygons to the world.
	 *
	 * @param renderLayer The render layer to which the object should be added.
	 * @author Joel
	 */
	private static void addPolygons(Canvas renderLayer) {

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

		// add all polys to world
		for (WPolygon poly : simcon.getAllPolys()) {
			new WorldPoly(
					world,
					renderLayer,
					poly
			);
		}
	}

	/**
	 * A method to add all SUMO junctions to the world.
	 *
	 * @param renderLayer The render layer to which the object should be added.
	 * @author Joel
	 */
	private static void addJunctions(Canvas renderLayer) {

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

		for (String junctionId : SimController.getMainsimcon().getJunctionIDList()) {
			new WorldJunction(
					world,
					renderLayer,
					"WorldJunction",
					junctionId
			);
		}
	}

	/**
	 * A method to add all SUMO roads to the world.
	 *
	 * @param renderLayer The render layer to which the object should be added.
	 * @author Joel
	 */
	private static void addRoads(Canvas renderLayer) {

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

		// loop through all roads
		for (WEdge wEdge : simcon.getAllroads().values()) {
			// loop through all lanes
			for (String laneId : wEdge.getLaneIDs()) {
				// create lane sub point list
				List<Vector2D> list = Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(laneId));

				// loop through all lane sub points
				for (Vector2D subPoint : list) {
					if (list.indexOf(subPoint) > 0) {
						new WorldRoad(
								world,
								renderLayer,
								wEdge.getName(),
								UI.roadColor,
								list.get(list.indexOf(subPoint) - 1),
								subPoint,
								SimController.getMainsimcon().getLaneWidth(laneId) / 2,
								laneId + " (" + (list.indexOf(subPoint) - 1) + ")",
								wEdge
						);
					}
				}
			}
		}
	}

	/**
	 * A method to add all traffic lights to the world.
	 *
	 * @param renderLayer The render layer to which the object should be added.
	 * @author Joel
	 */
	private static void addTrafficLights(Canvas renderLayer) {

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

        for (WTrafficLight wTrafficLight : simcon.getAllWTLs().values()) {
			wTrafficLight.loadLinkedStateColors();

			for (WLink wLink : wTrafficLight.getAllWlinks()) {
				new WorldTrafficLight(
						world,
						renderLayer,
						"WorldTrafficLight",
						wTrafficLight,
						wLink
				);
			}
		}
	}

	/**
	 * A method to add and update all SUMO traffic lights in the world.
	 *
	 * @param renderLayer The render layer to which the object to update is.
	 * @author Joel
	 */
	private static void updateTrafficLights(Canvas renderLayer) {

        SimController simcon = SimController.getMainsimcon();

        if(simcon == null){
            Debug.print("Main Simcon instance is null");
            return;
        }

		for (WTrafficLight wTrafficLight : simcon.getAllWTLs().values()) {
			wTrafficLight.loadLinkedStateColors();
		}
	}

	/**
	 * A method to add all initial SUMO vehicles to the world.
	 *
	 * @param renderLayer The render layer to which the object should be added.
	 * @author Joel
	 */
	private static void addVehicles(Canvas renderLayer) {
		for (String id : SimController.getMainsimcon().getVehicleIDList()) {
			new WorldVehicle(
					world,
					renderLayer,
					"Object TestCarSim",
					new WVehicle(id, SimController.getMainsimcon().getStc())
			);
			vehicleIds.add(id);
		}
	}

	/**
	 * A method to add and update all SUMO vehicles in the world.
	 *
	 * @param renderLayer The render layer to which the object to update is.
	 * @author Joel
	 */
	private static void updateVehicles(Canvas renderLayer) {
		List<String> currentVehicleList = SimController.getMainsimcon().getVehicleIDList();
		List<WorldObject> removeVehicleList = new ArrayList<>();

		for (WorldObject worldObject : world.getWorldObjects()) {
			if (worldObject.getClass() == WorldVehicle.class) {
				if (currentVehicleList.contains(((WorldVehicle) worldObject).getwVehicle().getID()))
					worldObject.updateSim();
				else removeVehicleList.add(worldObject);
			}
		}

		for (WorldObject worldObject : removeVehicleList) {
			worldObject.remove();
		}


		for (String id : currentVehicleList) {
			if (vehicleIds.contains(id)) continue;
			new WorldVehicle(
					world,
					renderLayer,
					"Object TestCarSim",
					new WVehicle(id, SimController.getMainsimcon().getStc())
			);
			vehicleIds.add(id);
		}
	}

	/**
	 * A method to tell the renderer to update.
	 *
	 * @author Joel
	 */
	public static void update() {
		updateTrafficLights(worldStaticRenderTarget);


        SimController simcon = SimController.getMainsimcon();

        if(simcon != null){
            WVehicle.loadnupdateAll(simcon);
            simcon.collectTelemetry();

        }



        updateVehicles(worldStaticRenderTarget);

		world.requestUpdate();
	}

	//--------------------------------------------------ClassMethods--------------------------------------------------

}