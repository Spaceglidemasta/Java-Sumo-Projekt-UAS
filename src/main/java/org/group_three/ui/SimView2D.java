package org.group_three.ui;

import de.tudresden.sumo.objects.SumoStringList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.SumoRoad;
import org.group_three.model.WPolygon;
import org.group_three.model.WTrafficLight;
import org.group_three.model.WVehicle;
import org.group_three.ui.controllers.BodyController;
import org.group_three.ui.world.*;
import org.group_three.utils.TLStopLine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.group_three.ui.Meth.lerp;

public class SimView2D {

	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	private static Canvas worldStaticRenderTarget;
	@SuppressWarnings({"unused", "FieldCanBeLocal"})
	private static Canvas worldDynamicRenderTarget;
	private static Pane renderTargetBounds;

	private static GraphicsContext worldStaticRenderTarget_GraphicsContext;

	private static World world;
	private static WorldObject selected;
	private static final List<String> vehicleIds = new ArrayList<>();

	//--------------------------------------------------ClassVariables--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++InitializeClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	public static void initialize(Canvas wSRT, Canvas wDRT, Pane rTB) {
		worldStaticRenderTarget = wSRT;
		worldDynamicRenderTarget = wDRT;
		renderTargetBounds = rTB;
		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();

		newWorld();
	}

	//--------------------------------------------------InitializeClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++GetterClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	@SuppressWarnings("unused")
	public static WorldObject getSelected() {
		return selected;
	}

	public static World getWorld() {
		return world;
	}

	//--------------------------------------------------GetterClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++SetterClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	public static void setSelected(WorldObject selected) {
		if (SimView2D.selected == selected) return;

		SimView2D.selected = selected;

		SimView2D.selected.setupDetailsPanel(BodyController.setDetailsPanel(SimView2D.selected.detailClassPath));
	}

	//--------------------------------------------------SetterClassMethods--------------------------------------------------


	//++++++++++++++++++++++++++++++++++++++++++++++++++ClassMethods++++++++++++++++++++++++++++++++++++++++++++++++++

	public static void newWorld() {
		world = new World();

		world.worldStaticRenderTarget = worldStaticRenderTarget;
		world.graphicsContext = worldStaticRenderTarget_GraphicsContext;

		renderTargetBounds.widthProperty().addListener((_, _, newValue) ->
				world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y))));

		renderTargetBounds.heightProperty().addListener((_, _, newValue) ->
				world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2))));

		world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth() / 2, worldStaticRenderTarget.getHeight() / 2));


		// skip if simulation is null
		if (SimController.getMainsimcon() == null) return;


		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();


		// TODO: following out-commented code is kinda important, but the junction adding logic itself can be nuked
		//rendering Junctions
		/*for (String jid : SimController.getMainsimcon().getJunctionIDList()) {
			Vector2D jidV = new Vector2D(SimController.getMainsimcon().getJunctionPos(jid).x, SimController.getMainsimcon().getJunctionPos(jid).y);
			boolean firstIteration = jid.equals(SimController.getMainsimcon().getJunctionIDList().getFirst());

			if (rnHeight.x > jidV.x || firstIteration)
				rnHeight.x = jidV.x;
			if (rnHeight.y < jidV.x || firstIteration) //noinspection SuspiciousNameCombination
				rnHeight.y = jidV.x;
			if (rnWidth.x > jidV.y || firstIteration) //noinspection SuspiciousNameCombination
				rnWidth.x = jidV.y;
			if (rnWidth.y < jidV.y || firstIteration)
				rnWidth.y = jidV.y;

			new WorldPoint(
					world,
					worldStaticRenderTarget,
					"WorldPoint_" + jid,
					Color.RED,
					8
			).setPosition(jidV);
			//Debug.print(SimController.getMainsimcon().getJunctionPos(jid));
		}*/
		//Debug.print(rnHeight + " --- " + rnWidth);



		addPolygons(worldStaticRenderTarget);
		addJunctions(worldStaticRenderTarget);
		addRoads(worldStaticRenderTarget);


		// TODO:    move all the traffic light adding code here to its own function,
		//          as it is above for example with the addRoads function
		//          @Leon
        SumoStringList tls = SimController.getMainsimcon().getTrafficLightsIDList();

        for(String tl: tls){
            WTrafficLight wtl = new WTrafficLight(tl);
            List<TLStopLine> laneDataList = wtl.getStopLineData(tl);
            if (laneDataList != null) {
                for (TLStopLine data : laneDataList) {
                        Debug.print(data.laneID);
                        new WorldTrafficLight(world,
                            worldStaticRenderTarget,
                            "WorldTrafficLight", wtl
                                ,laneDataList.indexOf(data)
                    );
                }
            }
        }
		// TODO:    ----------------------------------------------------------------

		addVehicles(worldStaticRenderTarget);


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
		// load polys
		WPolygon.loadAllPolys();

		// add all polys to world
		for ( WPolygon poly : WPolygon.getAllPolys()) {
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
		for (String junctionId : SimController.getMainsimcon().getJunctionIDList()) {
			new WorldJunction(
					world,
					renderLayer,
					"WorldJunction",
					UI.roadColor,
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
		// loop through all roads
		for (SumoRoad sumoRoad : SumoRoad.getAllroads()) {
			// loop through all lanes
			for (String laneId : sumoRoad.getLaneIDs()) {
				// create lane sub point list
				List<Vector2D> list = Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(laneId));

				// loop through all lane sub points
				for (Vector2D subPoint : list) {
					if (list.indexOf(subPoint) > 0) {
						new WorldRoad(
								world,
								renderLayer,
								"SubPoint",
								UI.roadColor,
								list.get(list.indexOf(subPoint) -1),
								subPoint,
								SimController.getMainsimcon().getLaneWidth(laneId)/2
						);
					}
				}
			}
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
			WVehicle wVehicle = new WVehicle(id, SimController.getMainsimcon().getStc());
			WorldVehicle worldVehicle = new WorldVehicle(
					world,
					renderLayer,
					"Object TestCarSim"
			);
			worldVehicle.setwVehicle(wVehicle);
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
			WVehicle wVehicle = new WVehicle(id, SimController.getMainsimcon().getStc());
			WorldVehicle worldVehicle = new WorldVehicle(
					world,
					renderLayer,
					"Object TestCarSim"
			);
			worldVehicle.setwVehicle(wVehicle);
			vehicleIds.add(id);
		}
	}

	public static void update() {
		updateVehicles(worldStaticRenderTarget);

		world.requestUpdate();
	}

	//--------------------------------------------------ClassMethods--------------------------------------------------

}