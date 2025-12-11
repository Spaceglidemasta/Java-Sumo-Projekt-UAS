package org.group_three.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WVehicle;
import org.group_three.ui.controllers.BodyController;
import org.group_three.ui.world.World;
import org.group_three.ui.world.WorldObject;
import org.group_three.ui.world.WorldPoint;
import org.group_three.ui.world.WorldVehicle;

import java.util.ArrayList;
import java.util.List;

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


		if (SimController.getMainstc() == null) return;


		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();


		for (String jid : SimController.getMainstc().getJunctionIDList()) {
			Vector2D jidV = new Vector2D(SimController.getMainstc().getJunctionPos(jid).x, SimController.getMainstc().getJunctionPos(jid).y);
			boolean firstIteration = jid.equals(SimController.getMainstc().getJunctionIDList().getFirst());

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
					Color.RED
			).setPosition(jidV);
			Debug.print(SimController.getMainstc().getJunctionPos(jid));
		}

		Debug.print(rnHeight + " --- " + rnWidth);

		world.setWorldSize(new Vector2D(Math.abs(rnHeight.x - rnHeight.y), Math.abs(rnWidth.x - rnWidth.y)).add(new Vector2D(128, 128)));
		world.setViewerPosition(new Vector2D(Meth.lerp(rnHeight.x, rnHeight.y, 0.5), Meth.lerp(rnWidth.x, rnWidth.y, 0.5)).negate());
		world.setWorldOffset(new Vector2D(Meth.lerp(rnHeight.x, rnHeight.y, 0.5), Meth.lerp(rnWidth.x, rnWidth.y, 0.5)));

		//TODO add for-loop for spawning traffic lights

		for (String id : SimController.getMainstc().getVehicleIDList()) {
			WVehicle wVehicle = new WVehicle(id, SimController.getMainstc().getStc());
			WorldVehicle worldVehicle = new WorldVehicle(
					world,
					worldStaticRenderTarget,
					"Object TestCarSim"
			);
			worldVehicle.setwVehicle(wVehicle);
			vehicleIds.add(id);
		}


		Debug.print(world.getWorldOffset());
	}

	public static void update() {
		List<String> currentVehicleList = SimController.getMainstc().getVehicleIDList();

		for (WorldObject worldObject : world.getWorldObjects()) {
			if (worldObject.getClass() == WorldVehicle.class) {
				if (currentVehicleList.contains(((WorldVehicle) worldObject).getwVehicle().getID()))
					worldObject.updateSim();
				else worldObject.remove();
			}
		}


		for (String id : currentVehicleList) {
			if (vehicleIds.contains(id)) continue;
			WVehicle wVehicle = new WVehicle(id, SimController.getMainstc().getStc());
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