package org.group_three.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.model.*;
import org.group_three.ui.controllers.FilterVehicleController;
import org.group_three.ui.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.group_three.ui.Meth.lerp;

/**
 * The class to manage and control the 2d simulation view.
 *
 * @author Joel
 */
public class SimView2D {

	// Logger
	private static final Logger log = Logger.getLogger(SimView2D.class.getName());

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The canvas to render non-moving objects on.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static Canvas renderTarget;

	/**
	 * Represents the window size of the simulation view.
	 * Is used to adjust the canvas sizes, as they don't scale by default.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static Pane renderTargetBounds;

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
	 * A boolean to reroute the selection process,
	 * to allow for route selection.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static WorldObject routeSelection;

	/**
	 * All currently active vehicle id's.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static final List<String> vehicleIds = new ArrayList<>();

	/**
	 * The vehicle filter controller.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private static FilterVehicleController filterVehicleController;

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A method to initialize this class.
	 *
	 * @param wSRT The worldStaticRenderTarget.
	 * @param rTB  The renderBoundsPane.
	 * @author Joel
	 */
	public static void initialize(Canvas wSRT, Pane rTB) {
		renderTarget = wSRT;
		renderTargetBounds = rTB;

		// create new world on ini so the default view is just an empty world instead of undefined.
		newWorld();
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++GetterSetterClassMethods+++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * Gets the currently selected world object.
	 * Might be null.
	 *
	 * @return The currently selected world object.
	 * @author Joel
	 */
	public static WorldObject getSelected() {
		return selected;
	}

	/**
	 * A setter method to set the currently selected world object.
	 * Updates the details panel after selection.
	 *
	 * @param selected The world object to select.
	 * @author Joel
	 */
	public static void setSelected(WorldObject selected) {

		// go into route selection mode if not null
		if (routeSelection != null) {

			if (routeSelection.getClass() == WorldRoad.class) {
				((WorldRoad) routeSelection).getDetailsPanelRoadController().routeSelected(selected);
			}

			if (routeSelection.getClass() == WorldVehicle.class) {
				((WorldVehicle) routeSelection).getDetailsPanelVehicleController().routeSelected(selected);
			}

			log.info("New Route \"" + selected.getDisplayName() + "\" selected for \"" + routeSelection.getDisplayName() + "\".");

			routeSelection = null;
			return;
		}

		if (SimView2D.selected == selected) return;

		// deselect old object
		if (SimView2D.selected != null) {
			SimView2D.selected.deselect();
		}

		// select new object
		SimView2D.selected = selected;
		if (selected != null) {
			selected.select();
		}

	}

	/**
	 * A method to select a route.
	 *
	 * @param routeSelection The object that asks for a route selection.
	 * @author Joel
	 */
	public static void setRouteSelection(WorldObject routeSelection) {
		SimView2D.routeSelection = routeSelection;
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

	//---------------------------------------------GetterSetterClassMethods---------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The method to create a new world.
	 * Will be called on simulation load.
	 *
	 * @author Joel
	 */
	public static void newWorld() {
		if (world != null) world.getUpdateTimer().stop();

		world = new World(renderTarget);

		// bind to view size changes to adjust viewer position offset
		renderTargetBounds.widthProperty().addListener((_, _, newValue) ->
				world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y))));

		renderTargetBounds.heightProperty().addListener((_, _, newValue) ->
				world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2))));

		// initialize viewer position with current view size
		world.setViewerPositionOffset(new Vector2D(renderTarget.getWidth() / 2, renderTarget.getHeight() / 2));


		// skip if simulation is null
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// road network min max values
		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();

		List<String> allJIDs = simcon.getJunctionIDList();

		if (allJIDs == null) return;
		// calculate the road network size
		for (String jid : allJIDs) {
			Vector2D jidV = new Vector2D(simcon.getJunctionPos(jid).x, simcon.getJunctionPos(jid).y);
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
		addPolygons(renderTarget);
		addJunctions(renderTarget);
		addRoads(renderTarget);
		addTrafficLights(renderTarget);
		addVehicles(renderTarget);


		// set viewer position and offset to center of road network
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
		if (simcon == null) return;

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
		if (simcon == null) return;

		// loop through each junction and add world junctions
		for (String junctionId : simcon.getJunctionIDList()) {
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
		if (simcon == null) return;

		// loop through all roads
		for (WEdge wEdge : simcon.getAllroads().values()) {
			// loop through all lanes
			for (String laneId : wEdge.getLaneIDs()) {
				// create lane sub point list
				List<Vector2D> list = Meth.convertSumoCoords(simcon.getLaneShape(laneId));

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
								simcon.getLaneWidth(laneId) / 2,
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
		if (simcon == null) return;

		// loop through traffic light clusters and add all stop lines
		for (WTrafficLight wTrafficLight : simcon.getAllWTLs().values()) {
			wTrafficLight.loadLinkedStateColors();

			// add traffic light stop lines
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
	 * @author Joel
	 */
	private static void updateTrafficLights() {
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// update link data
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
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// add all initially existing vehicles
		for (String id : simcon.getVehicleIDList()) {
			new WorldVehicle(
					world,
					renderLayer,
					"Object TestCarSim",
					new WVehicle(id, simcon.getStc())
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
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		List<String> currentVehicleList = simcon.getVehicleIDList();
		List<WorldObject> removeVehicleList = new ArrayList<>();

		// create invalid vehicle list
		for (WorldObject worldObject : world.getWorldObjects()) {
			if (worldObject.getClass() == WorldVehicle.class) {
				if (currentVehicleList.contains(((WorldVehicle) worldObject).getwVehicle().getID()))
					worldObject.updateSim();
				else removeVehicleList.add(worldObject);
			}
		}

		// remove invalid vehicles
		for (WorldObject worldObject : removeVehicleList) {
			worldObject.remove();
		}

		// add new vehicles
		for (String id : currentVehicleList) {
			if (vehicleIds.contains(id)) continue;
			new WorldVehicle(
					world,
					renderLayer,
					"Object TestCarSim",
					new WVehicle(id, simcon.getStc())
			);
			vehicleIds.add(id);
		}
	}

	/**
	 * A method to tell the renderer to update.
	 * Also updates vehicle and traffic light data from sumo.
	 *
	 * @author Joel
	 */
	public static void update() {
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		updateTrafficLights();

		WVehicle.loadnupdateAll(simcon);
		simcon.collectTelemetry();
		updateVehicles(renderTarget);

		// render changes
		world.requestUpdate();
	}

	/**
	 * A method to request world position data for the vehicle filter.
	 *
	 * @param filterVehicleController The controller that requests the position.
	 * @author Joel
	 */
	public static void requestPosition(FilterVehicleController filterVehicleController) {
		SimView2D.filterVehicleController = filterVehicleController;
	}

	/**
	 * A method to interact with the world base don a position.
	 *
	 * @param pos The position in the world.
	 * @author Joel
	 */
	public static void clickInWorld(Vector2D pos) {
		if (filterVehicleController != null) {
			filterVehicleController.receivePosition(pos);
			filterVehicleController = null;
		}
	}

	//---------------------------------------------------ClassMethods---------------------------------------------------

}