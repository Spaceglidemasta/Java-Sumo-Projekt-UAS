package org.group_three.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import org.group_three.api.SimController;
import org.group_three.debug.Debug;
import org.group_three.model.WVehicle;
import org.group_three.ui.world.World;
import org.group_three.ui.world.WorldObject;
import org.group_three.ui.world.WorldPoint;
import org.group_three.ui.world.WorldVehicle;

import java.util.ArrayList;
import java.util.List;

public class SimView2D {
	private static Canvas worldStaticRenderTarget;
	private static Canvas worldDynamicRenderTarget;
	private static Pane renderTargetBounds;

	private static GraphicsContext worldStaticRenderTarget_GraphicsContext;

	public static World getWorld() {
		return world;
	}

	private static World world;

	public SimView2D(Canvas wSRT, Canvas wDRT, Pane rTB) {
		worldStaticRenderTarget = wSRT;
		worldDynamicRenderTarget = wDRT;
		renderTargetBounds = rTB;
		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();
	}

	private static List<String> vehicleIds = new ArrayList<>() {};

	public static void newWorld() {
		world = new World();

		world.worldStaticRenderTarget = worldStaticRenderTarget;
		world.graphicsContext = worldStaticRenderTarget_GraphicsContext;

		renderTargetBounds.widthProperty().addListener((observable, oldValue, newValue) -> {
			world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y)));
		});

		renderTargetBounds.heightProperty().addListener((observable, oldValue, newValue) -> {
			world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2)));
		});

		world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth() / 2, worldStaticRenderTarget.getHeight() / 2));



		if (SimController.getMainstc() == null) return;









		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();


		for (String jid : SimController.getMainstc().getJunctionIDList()) {
			Vector2D jidV = new Vector2D(SimController.getMainstc().getJunctionPos(jid).x, SimController.getMainstc().getJunctionPos(jid).y);
			boolean firstIteration = jid.equals(SimController.getMainstc().getJunctionIDList().getFirst());

			if (rnHeight.x > jidV.x || firstIteration) rnHeight.x = jidV.x;
			if (rnHeight.y < jidV.x || firstIteration) rnHeight.y = jidV.x;
			if (rnWidth.x > jidV.y || firstIteration) rnWidth.x = jidV.y;
			if (rnWidth.y < jidV.y || firstIteration) rnWidth.y = jidV.y;

			new WorldPoint(
					world,
					worldStaticRenderTarget,
					"WorldPoint_" + jid
			).setPosition(jidV);
			Debug.print(SimController.getMainstc().getJunctionPos(jid));
		}

		Debug.print(rnHeight + " --- " + rnWidth);

		world.setWorldSize(new Vector2D(Math.abs(rnHeight.x - rnHeight.y), Math.abs(rnWidth.x - rnWidth.y)).add(new Vector2D(128,128)));
		world.setViewerPosition(new Vector2D(Meth.lerp(rnHeight.x, rnHeight.y, 0.5), Meth.lerp(rnWidth.x, rnWidth.y, 0.5)).negate());
		world.setWorldOffset(new Vector2D(Meth.lerp(rnHeight.x, rnHeight.y, 0.5), Meth.lerp(rnWidth.x, rnWidth.y, 0.5)));



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
			if (worldObject.getClass() == WorldVehicle.class)
			{
				if (currentVehicleList.contains(((WorldVehicle)worldObject).getwVehicle().getID())) worldObject.updateSim();
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
}
