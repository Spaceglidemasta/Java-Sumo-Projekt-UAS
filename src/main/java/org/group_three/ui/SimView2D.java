package org.group_three.ui;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.objects.SumoPosition2D;
import de.tudresden.sumo.objects.SumoStringList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.SumoRoad;
import org.group_three.model.WTrafficLight;
import org.group_three.model.WVehicle;
import org.group_three.ui.controllers.BodyController;
import org.group_three.ui.world.*;
import org.group_three.api.SimController;

import java.sql.Timestamp;
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


		if (SimController.getMainsimcon() == null) return;


		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();


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

		/*for ( String polyId : SimController.getMainsimcon().getPolygonIDList()) {
			new WorldPoly(
					world,
					worldStaticRenderTarget,
					"WorldPoly",
					polyId
			);
		}*/


		for (String junctionId : SimController.getMainsimcon().getJunctionIDList()) {
			new WorldJunction(
					world,
					worldStaticRenderTarget,
					"WorldJunction",
					Color.WHITE,
					junctionId
			);
		}

		for (SumoRoad sumoRoad : SumoRoad.getAllroads()) {
			/*new WorldRoad(
					world,
					worldStaticRenderTarget,
					"WorldRoad" + sumoRoad.getEdgeID(),
					Color.WHITE,
					sumoRoad
			);*/
			for (String laneId : sumoRoad.getLaneIDs())
			{
				List<Vector2D> list = Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(laneId));
				for (Vector2D subPoint : list) {
					if (list.indexOf(subPoint) > 0) {
						new WorldRoad(
								world,
								worldStaticRenderTarget,
								"SubPoint",
								Color.WHITE,
								list.get(list.indexOf(subPoint) -1),
								subPoint,
								SimController.getMainsimcon().getLaneWidth(laneId)/2
						);
					}
				}
			}
		}

        SumoStringList tls = SimController.getMainsimcon().getTrafficLightsIDList();

        for(String tl: tls){
            WTrafficLight wtl = new WTrafficLight(tl);
            List<SumoPosition2D> stopLinePoints = wtl.getStopLinePoint(tl);
            if (stopLinePoints != null) {
                List<Vector2D> uiPoints = Meth.convertSumoCoords(new LinkedList<>(stopLinePoints));
                for (Vector2D uiPos : uiPoints) {
                    Debug.print("Drawing rectangle at: " + uiPos.x + ", " + uiPos.y);
                    new WorldTrafficLight(world,
                            worldStaticRenderTarget,
                            "WorldTrafficLight", wtl
                    ).setPosition(uiPos);
                }
            }
        }


		//Debug.print(rnHeight + " --- " + rnWidth);

		world.setWorldSize(new Vector2D(Math.abs(rnHeight.x - rnHeight.y), Math.abs(rnWidth.x - rnWidth.y)).add(new Vector2D(128, 128)));
		world.setViewerPosition(new Vector2D(lerp(rnHeight.x, rnHeight.y, 0.5), lerp(rnWidth.x, rnWidth.y, 0.5)).negate());
		world.setWorldOffset(new Vector2D(lerp(rnHeight.x, rnHeight.y, 0.5), lerp(rnWidth.x, rnWidth.y, 0.5)));


		//rendering cars
		for (String id : SimController.getMainsimcon().getVehicleIDList()) {
			WVehicle wVehicle = new WVehicle(id, SimController.getMainsimcon().getStc());
			WorldVehicle worldVehicle = new WorldVehicle(
					world,
					worldStaticRenderTarget,
					"Object TestCarSim"
			);
			worldVehicle.setwVehicle(wVehicle);
			vehicleIds.add(id);
		}


		//Debug.print(world.getWorldOffset());
	}

	public static void update() {
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
					worldStaticRenderTarget,
					"Object TestCarSim"
			);
			worldVehicle.setwVehicle(wVehicle);
			vehicleIds.add(id);
		}

		world.requestUpdate();
	}

	//--------------------------------------------------ClassMethods--------------------------------------------------

}