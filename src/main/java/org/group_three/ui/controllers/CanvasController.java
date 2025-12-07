package org.group_three.ui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoStringList;
import javafx.scene.paint.Color;
import org.group_three.api.SimController;
import org.group_three.model.WVehicle;
import org.group_three.ui.*;
import org.group_three.debug.Debug;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.group_three.ui.world.World;
import org.group_three.ui.world.WorldPoint;
import org.group_three.ui.world.WorldVehicle;

/**
 * current controller for the 2d view, name needs to be changed later
 * needs much clean up, is intended to be the manager of the world and world objects
 *
 * @author Joel
 */
public class CanvasController {

	@FXML
	private Canvas worldStaticRenderTarget;    // for static world elements: roads, traffic lights,...
	@FXML
	private Canvas worldDynamicRenderTarget;    // for dynamic world elements: cars,...
	@FXML
	private Pane renderTargetBounds;            // a reference to adjust the render target sizes dynamically on window resize

	private GraphicsContext worldStaticRenderTarget_GraphicsContext;

	//private World world = new World();

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @throws IOException
	 * Throw-Comment
	 */
	@FXML
	public void initialize() throws IOException {
		Debug.toConsole("Canvas loaded.");

		worldStaticRenderTarget_GraphicsContext = worldStaticRenderTarget.getGraphicsContext2D();



		//world.worldStaticRenderTarget = worldStaticRenderTarget;
		//world.graphicsContext = worldStaticRenderTarget_GraphicsContext;

		/*WorldVehicle test = new WorldVehicle(
				world,
				worldStaticRenderTarget,
				"Object TestCar"
		);
		test.setColor(new Color(0,1,1,1));
		test.setPosition(new Vector2D(32, 64));
		test.setRotation(0);

		WorldVehicle test2 = new WorldVehicle(
				world,
				worldStaticRenderTarget,
				"Object TestCar2"
		);
		test2.setPosition(new Vector2D(128, -64));
		test2.setRotation(30);

		WorldVehicle test3 = new WorldVehicle(
				world,
				worldStaticRenderTarget,
				"Object TestCar3"
		);
		test3.setPosition(new Vector2D(-64, -64));
		test3.setRotation(90);

		WorldPoint point0 = new WorldPoint(
				world,
				worldStaticRenderTarget,
				"WorldPoint"
		);*/

		/*
		SimController simController = new SimController("uascity/osm.sumocfg");//new SimController("net.net.xml", "net.rou.xml");


		Vector2D rnHeight = new Vector2D();
		Vector2D rnWidth = new Vector2D();


		for (String jid : simController.getJunctionIDList()) {
			Vector2D jidV = new Vector2D(simController.getJunctionPos(jid).x, simController.getJunctionPos(jid).y);
			boolean firstIteration = jid.equals(simController.getJunctionIDList().getFirst());

			if (rnHeight.x > jidV.x || firstIteration) rnHeight.x = jidV.x;
			if (rnHeight.y < jidV.x || firstIteration) rnHeight.y = jidV.x;
			if (rnWidth.x > jidV.y || firstIteration) rnWidth.x = jidV.y;
			if (rnWidth.y < jidV.y || firstIteration) rnWidth.y = jidV.y;

			new WorldPoint(
					world,
					worldStaticRenderTarget,
					"WorldPoint_" + jid
			).setPosition(jidV);
			Debug.print(simController.getJunctionPos(jid));
		}

		Debug.print(rnHeight + " --- " + rnWidth);

		world.setWorldSize(new Vector2D(Math.abs(rnHeight.x - rnHeight.y), Math.abs(rnWidth.x - rnWidth.y)).add(new Vector2D(128,128)));
		world.setViewerPosition(new Vector2D(Meth.lerp(rnHeight.x, rnHeight.y, 0.5), Meth.lerp(rnWidth.x, rnWidth.y, 0.5)).negate());
		world.setWorldOffset(new Vector2D(Meth.lerp(rnHeight.x, rnHeight.y, 0.5), Meth.lerp(rnWidth.x, rnWidth.y, 0.5)));

		for (String id : simController.getVehicleIDList()) {
			WVehicle wVehicle = new WVehicle(id, simController.get_sumcon());
			wVehicle.update();
			new WorldVehicle(
					world,
					worldStaticRenderTarget,
					"Object TestCarSim"
			).setPosition(new Vector2D(wVehicle.getPos()));
		}

		simController.close();
		*/

		worldStaticRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldStaticRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		worldDynamicRenderTarget.widthProperty().bind(renderTargetBounds.widthProperty());
		worldDynamicRenderTarget.heightProperty().bind(renderTargetBounds.heightProperty());

		/*
		renderTargetBounds.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("RenderTargetSize.X: " + newValue);

			world.setViewerPositionOffset(new Vector2D((newValue.doubleValue() / 2), (world.getViewerPositionOffset().y)));
		});

		renderTargetBounds.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("RenderTargetSize.Y: " + newValue);

			world.setViewerPositionOffset(new Vector2D((world.getViewerPositionOffset().x), (newValue.doubleValue() / 2)));
		});*/

		//world.setViewerPositionOffset(new Vector2D(worldStaticRenderTarget.getWidth() / 2, worldStaticRenderTarget.getHeight() / 2));

		Debug.toConsole(new Vector2D(0, 10).getRotation()); // 0°
		Debug.toConsole(new Vector2D(10, 10).getRotation()); // 45°
		Debug.toConsole(new Vector2D(10, 0).getRotation()); // 90°
		Debug.toConsole(new Vector2D(10, -10).getRotation()); // 135°
		Debug.toConsole(new Vector2D(0, -10).getRotation()); // 180°
		Debug.toConsole(new Vector2D(-10, -10).getRotation()); // 225°
		Debug.toConsole(new Vector2D(-10, 0).getRotation()); // 270°
		Debug.toConsole(new Vector2D(-10, 10).getRotation()); // 315°
		Debug.toConsole(new Vector2D(-0.001, 10).getRotation()); // 0/360°





		SimView2D simView2D = new SimView2D(worldStaticRenderTarget,
				worldDynamicRenderTarget,
				renderTargetBounds);

		SimView2D.newWorld();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@FXML
	private void onMouseClicked() {
		//Debug.print("Canvas clicked.");

		Vector2D nMP = mousePosition.sub(SimView2D.getWorld().getViewerPositionOffset());
		Vector2D worldspaceMousePosition = Meth.getRelativeLocation(SimView2D.getWorld().getViewerPosition(), SimView2D.getWorld().getViewerRotation(), nMP).mul(1/SimView2D.getWorld().getViewerZoom());

		/*WorldObject test = new WorldObject();
		test.world = world;
		test.graphicsContext = worldStaticRenderTarget_GraphicsContext;
		test.renderTarget = worldStaticRenderTarget;
		test.setPosition(worldspaceMousePosition);
		world.addWorldObject(test);*/

		//world.world

		try {
			//Debug.print(world.interact(worldspaceMousePosition).getDisplayName());
		} catch (Exception e) {
			//throw new RuntimeException(e);
			//Debug.print("NULL");
		}

		SimView2D.getWorld().requestUpdate();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onCanvasDragged(MouseEvent event) {
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onCanvasDragged");

		double x = event.getX();
		double y = event.getY();

		deltaX = x - lastX;
		deltaY = y - lastY;

		double startRot = new Vector2D(lastX, lastY).sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double rot = new Vector2D(x, y).sub(SimView2D.getWorld().getViewerPositionOffset()).flipY().getRotation();
		double deltaRot = rot-startRot;

		if (/*!Keyboard.isCtrlKeyPressed() && */Keyboard.isAltKeyPressed()) { // start rotation freely, no snapping
			SimView2D.getWorld().addViewerRotation(deltaRot);
			SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().rotate(deltaRot)); // move to rotate viewer func?!?

		} else if (Keyboard.isCtrlKeyPressed() && Keyboard.isAltKeyPressed() && false) { // start rotation with 45 degree snapping
			if (((rot >= 0) && (rot < 22.5)) || rot >= 337.5) rot = 0;
			else if ((rot >= 22.5) && (rot < 67.5)) rot = 45;
			else if ((rot >= 67.5) && (rot < 112.5)) rot = 90;
			else if ((rot >= 112.5) && (rot < 157.5)) rot = 135;
			else if ((rot >= 157.5) && (rot < 202.5)) rot = 180;
			else if ((rot >= 202.5) && (rot < 247.5)) rot = 225;
			else if ((rot >= 247.5) && (rot < 292.5)) rot = 270;
			else if (rot >= 292.5) rot = 315;
			else throw new RuntimeException("Rotation reached an impossible value!");

			SimView2D.getWorld().setViewerRotation(rot);

		} else {
			SimView2D.getWorld().addViewerPosition(new Vector2D(deltaX, deltaY));
		}

		lastX = x;
		lastY = y;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onCanvasPressed(MouseEvent event) {
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onCanvasPressed");

		lastX = event.getX();
		lastY = event.getY();
	}

	private Vector2D mousePosition = new Vector2D();

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onMouseMoved(MouseEvent event) {
		mousePosition = new Vector2D(event.getX(), event.getY());
		//Debug.print(mousePosition);
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param event
	 * Param-Comment
	 */
	@FXML
	private void onScroll(ScrollEvent event) {
        if(Debug.JAVAFX_FULL_DEBUG) Debug.toConsole("onScroll");

		double zoomDelta = event.getDeltaY() * 0.01;
		double mlp = zoomDelta < 0 ? 1 : 1;
		double oldZoom = SimView2D.getWorld().getViewerZoom();
		//Debug.print(mlp);
		SimView2D.getWorld().addViewerZoom(zoomDelta);
		//world.setViewerPosition(world.getViewerPosition().mul(world.getViewerZoom()/oldZoom));
		SimView2D.getWorld().setViewerPosition(SimView2D.getWorld().getViewerPosition().mul(SimView2D.getWorld().getViewerZoom()/oldZoom));
		//Debug.print(world.getViewerPosition());
	}


	private double lastX;
	private double lastY;

	public double deltaX;
	public double deltaY;

	// draw handler needed so it doesn't waste performance
}
