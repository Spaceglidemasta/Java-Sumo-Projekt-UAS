package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WorldVehicleRoute extends WorldRoute {

	private final WorldVehicle worldVehicle;

	public WorldVehicleRoute(World world, Canvas canvas, String displayName, List<String> route, WorldVehicle worldVehicle) {
		super(world, canvas, displayName, route);
		this.worldVehicle = worldVehicle;
		this.updatedRoutePoints = getRoutePoints();

		//updatedRoutePoints.removeFirst();
	}

	private final List<Vector2D> updatedRoutePoints;

	/**
	 * The update method which is used to draw the WorldRoute in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		drawCollision();
		//if (worldVehicle == null) return;

		//if (Meth.getRelativeLocation(worldVehicle.getPosition(), worldVehicle.getRotation(), Meth.addRelativeLocation(getPosition(), getRotation(), updatedRoutePoints.getFirst())).x <= 0) {
		//	updatedRoutePoints.removeFirst();
		//}



		//List<Vector2D> currentRoutePoints = new ArrayList<>();

		/*for (int i = worldVehicle.getwVehicle().getRouteIndex(); i < route.size(); i++) {

			List<String> lanes = new ArrayList<>();

			if (i == worldVehicle.getwVehicle().getRouteIndex()) {
				List<String> laneIDs = SimController.getMainsimcon().getRoad(route.get(i)).getLaneIDs();

				lanes = SimController.getMainsimcon().getRoad(route.get(i)).getLaneIDs();
				Debug.print(Meth.getVector2DListLength(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2)))));

				//for (int d = laneIDs.indexOf(worldVehicle.getwVehicle().getLaneID()); d < laneIDs.size(); i++) {
				//	lanes.add();
				//}

			} else {
				lanes = SimController.getMainsimcon().getRoad(route.get(i)).getLaneIDs();
			}




			if (lanes.size() <= 0) continue;




			currentRoutePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2))));
		}*/

		//Debug.print(worldVehicle.getwVehicle().getLaneID());


		//Debug.print(worldVehicle.getwVehicle().getLanePosition());


		//currentRoutePoints.addFirst(worldVehicle.getPosition());

		/*int endIndex = 0;endPointIndex.get(worldVehicle.getwVehicle().getLaneID());

		for (int i = endIndex; i < updatedRoutePoints.size(); i++) {
			currentRoutePoints.add(updatedRoutePoints.get(i));
		}*/

		//currentRoutePoints.addAll(updatedRoutePoints);


		//Debug.print(currentRoutePoints.size());



		//drawLine(currentRoutePoints, 1, Color.RED);




		routePoints.clear();

		for (String edge : route) {
			List<String> lanes = SimController.getMainsimcon().getRoad(edge).getLaneIDs();

			List<List<Vector2D>> allLanePoints = new ArrayList<>();
			for (String lane : lanes) {
				allLanePoints.add(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lane)));
			}

			int laneCount = allLanePoints.size();

			for (int i = 0; i < allLanePoints.getFirst().size(); i++) {
				Vector2D point = new Vector2D();
				for (int b = 0; b < laneCount; b++) {
					point = point.add(allLanePoints.get(b).get(i));
				}
				point = point.div(laneCount);
				routePoints.add(point);
			}
		}

		/*reachedPoints.clear();

		for (String edge : route) {
			String laneId = worldVehicle.getwVehicle().getLaneID();
			if (laneId.startsWith(":")) {
				laneId = lastLaneId;
			}

			//Debug.print(worldVehicle.getwVehicle().getLaneID());

			List<String> lanes = SimController.getMainsimcon().getRoad(edge).getLaneIDs();

			List<List<Vector2D>> allLanePoints = new ArrayList<>();
			for (String lane : lanes) {

				allLanePoints.add(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lane)));
			}

			int laneCount = allLanePoints.size();

			for (int i = 0; i < allLanePoints.getFirst().size(); i++) {
				Vector2D point = new Vector2D();
				for (int b = 0; b < laneCount; b++) {
					point = point.add(allLanePoints.get(b).get(i));
				}
				point = point.div(laneCount);
				reachedPoints.add(point);
			}
		}*/

		//routePoints.removeAll(reachedPoints);















		drawLine(reachedPoints, 1, Color.RED);
		Debug.print("superUpdate");
	}

	String lastLaneId = "";

	List<Vector2D> reachedPoints = new ArrayList<>();



	HashMap<String, Integer> endPointIndex = new HashMap<String, Integer>();
	List<Double> lengthList = new ArrayList<>();

	@Override
	public void setRoute(List<String> route) {
		//super.setRoute(route);
		Debug.print("superROute");

		SimController simcon = SimController.getMainsimcon();

		if(simcon == null){
			Debug.print("Main Simcon instance is null");
			return;
		}

		this.route = route;



		/*routePoints.clear();

		String startLane = worldVehicle.getwVehicle().getRouteID();
		boolean bStartLane = false;

		for (String edge : route) {

			//if (edge.equals(startLane)) bStartLane = true;

			//if (!bStartLane) continue;

			List<String> lanes = SimController.getMainsimcon().getRoad(edge).getLaneIDs();

			routePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2))));
		}*/


		/*SimController simcon = SimController.getMainsimcon();

		if(simcon == null){
			Debug.print("Main Simcon instance is null");
			return;
		}

		this.route = route;

		routePoints.clear();



		for (String edge : route) {


			List<String> lanes = simcon.getRoad(edge).getLaneIDs();

			List<List<Vector2D>> allLanePoints = new ArrayList<>();
			for (String lane : lanes) {
				allLanePoints.add(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lane)));
			}

			int laneCount = allLanePoints.size();

			for (int i = 0; i < allLanePoints.getFirst().size(); i++) {
				Vector2D point = new Vector2D();
				for (int b = 0; b < laneCount; b++) {
					point = point.add(allLanePoints.get(b).get(i));
				}
				point = point.div(laneCount);
				routePoints.add(point);
			}

			endPointIndex.put(edge, routePoints.size()-1);



			//routePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2))));
		}

		for (int i = 0; i < routePoints.size()-1; i++) {
			lengthList.add(routePoints.get(i).getDistance(routePoints.get(i+1)));
		}


		Debug.print("HasMap: ");
		for (String s : route) {
			Debug.print(s + ": " + endPointIndex.get(s));
		}*/
	}
}
