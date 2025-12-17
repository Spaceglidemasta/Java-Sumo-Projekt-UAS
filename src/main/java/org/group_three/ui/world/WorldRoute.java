package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
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

	private List<String> route;
	private final List<Vector2D> routePoints = new ArrayList<>();

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

		//drawLine(routePoints, 1, Color.RED);
	}

	public List<String> getRoute() {
		return route;
	}

	public void setRoute(List<String> route) {
		this.route = route;

		routePoints.clear();
		for (String edge : route) {
			List<String> lanes = WEdge.getRoad(edge).getLaneIDs();

			routePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lanes.get(lanes.size()/2))));

			/*for (String lane : lanes.get(lanes.size()/2)) {
				routePoints.addAll(Meth.convertSumoCoords(SimController.getMainsimcon().getLaneShape(lane)));
			}*/
		}
	}
}
