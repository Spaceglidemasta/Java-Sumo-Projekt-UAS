package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WEdge;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to visually represent a route in the world.
 * Non-interactable.
 *
 * @author Joel
 */
public class WorldRoute extends WorldObject {

	protected List<String> route;

	protected List<Vector2D> routePoints = new ArrayList<>();

	public WorldRoute(World world, Canvas canvas, String displayName, List<String> route) {
		super(world, canvas, displayName);
		setRoute(route);
	}

	/**
	 * The update method which is used to draw the WorldRoute in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();

		drawLine(routePoints, 1, Color.RED);
	}

	public List<Vector2D> getRoutePoints() {
		return routePoints;
	}

	public List<String> getRoute() {
		return route;
	}

	public void setRoute(List<String> route) {

        SimController simcon = SimController.getMainsimcon();

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



			//routePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2))));
		}
	}
}
