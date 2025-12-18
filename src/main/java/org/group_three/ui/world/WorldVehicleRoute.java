package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldVehicleRoute extends WorldRoute {

	private final WorldVehicle worldVehicle;

	public WorldVehicleRoute(World world, Canvas canvas, String displayName, List<String> route, WorldVehicle worldVehicle) {
		super(world, canvas, displayName, route);
		this.worldVehicle = worldVehicle;
		//this.updatedRoutePoints = getRoutePoints();

		//updatedRoutePoints.removeFirst();
	}

	//private final List<Vector2D> updatedRoutePoints;

	/**
	 * The update method which is used to draw the WorldRoute in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();
		//if (worldVehicle == null) return;

		//if (Meth.getRelativeLocation(worldVehicle.getPosition(), worldVehicle.getRotation(), Meth.addRelativeLocation(getPosition(), getRotation(), updatedRoutePoints.getFirst())).x <= 0) {
		//	updatedRoutePoints.removeFirst();
		//}

		/*

		List<Vector2D> currentRoutePoints = new ArrayList<>();

		for (int i = worldVehicle.getwVehicle().getRouteIndex(); i < route.size(); i++) {

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

		//drawLine(currentRoutePoints, 1, Color.RED);
	}

	/*@Override
	public void setRoute(List<String> route) {
		super.setRoute(route);

		routePoints.clear();

		String startLane = worldVehicle.getwVehicle().getRouteID();
		boolean bStartLane = false;

		for (String edge : route) {

			//if (edge.equals(startLane)) bStartLane = true;

			//if (!bStartLane) continue;

			List<String> lanes = SimController.getMainsimcon().getRoad(edge).getLaneIDs();

			routePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2))));
		}
	}*/
}
