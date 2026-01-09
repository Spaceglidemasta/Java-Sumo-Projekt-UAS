package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
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

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The route as a list of edges.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private List<String> route;

	/**
	 * The route as a list of edge sub positions, accurately representing the route in the world.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final List<Vector2D> routePoints = new ArrayList<>();

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default WorldRoute constructor to spawn a new WorldRoute in a world.
	 *
	 * @param world       The world to which the WorldRoute should be added.
	 * @param canvas      The canvas of the world.
	 * @param displayName The display name which should show up on selection.
	 * @param route       The route to display
	 * @author Joel
	 */
	public WorldRoute(World world, Canvas canvas, String displayName, List<String> route) {
		super(world, canvas, displayName);
		setRoute(route);
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++GetterSetterMethods++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * A getter to get the route list.
	 *
	 * @return The route as a list.
	 * @author Joel
	 */
	public List<String> getRoute() {
		return route;
	}

	/**
	 * The setter method for the route.
	 * Sets the new route and converts the route list
	 * to the route points which adjust to the center of the road.
	 *
	 * @param route The new route.
	 * @author Joel
	 */
	public void setRoute(List<String> route) {
		SimController simcon = SimController.getMainsimcon();
		if (simcon == null) return;

		// set new route
		this.route = route;

		// clear route points from previous route
		routePoints.clear();

		// loop through all edges from route and calculate the center and sub points
		for (String edge : route) {
			// get all lanes
			List<String> lanes = simcon.getRoad(edge).getLaneIDs();

			// get all sub points for every lane
			List<List<Vector2D>> allLanePoints = new ArrayList<>();
			for (String lane : lanes) {
				allLanePoints.add(Meth.convertSumoCoords(simcon.getLaneShape(lane)));
			}

			int laneCount = allLanePoints.size();

			// loop through every sub point and calc the center based on the lane count
			for (int i = 0; i < allLanePoints.getFirst().size(); i++) {
				Vector2D point = new Vector2D();

				// add all sub points together
				for (int b = 0; b < laneCount; b++) {
					point = point.add(allLanePoints.get(b).get(i));
				}

				// divide by count to get the center
				point = point.div(laneCount);

				routePoints.add(point);
			}
		}
	}

	//-----------------------------------------------GetterSetterMethods------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The update method which is used to draw the WorldRoute in the world.
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		// draw route with a zoom independent scale of 1
		drawLine(routePoints, 1, Color.RED);
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}