package org.group_three.constants;

import javafx.scene.paint.Color;
import org.group_three.ui.Vector2D;

public final class UI {
    private UI() {} //prevents init.

	// The window name which can be seen next to the icon in the window itself
	// and the name when you hover over the window in the taskbar.
	public static final String appTitle = "Java-Sumo-Projekt-UAS";
	// The image which is loaded as the main window icon and as the icon in the taskbar.
	public static final String appIcon = "/org/group_three/ui/icons/SumoLogoAdjustments3.png";
	// The main FXML class which is loaded into the main window.
	// Other UI/FXML parts will be loaded into it.
	public static final String appFXML = "/org/group_three/ui/fxml/MainWindow.fxml";
	// The base icon to be used and colored for cars.
	public static final String carIcon = "/org/group_three/ui/icons/carCustom2.png";
	// The default sphere collision color of world objects.
	public static final Color sphereCollisionColor = new Color(1,0,0,0.1);
	// The default box collision color of world objects.
	public static final Color boxCollisionColor = new Color(0,1,1,0.1);
	// The default color of vehicles when they are created.
	public static final Color defaultVehicleColor = new Color(1, 1, 1, 1);
	// A bool to decide if WorldObject collision should be visible or not.
	public static final boolean showCollision = false;
	// A bool to decide if sphere collision should always be visible when WorldObject collision is visible.
	public static final boolean forceShowSphereCollision = false;
	// The default world color.
	public static final Color worldColor = new Color(0.2,0.5,0.2,1);
	// The default road color.
	public static final Color roadColor = new Color(0.1,0.1,0.1,1);
	// A bool to decide if misc polygons should be rendered.
	public static final boolean showPolys = true;
	// A Vector2D to decide how far one can zoom in and out of the UI. (x = min, y = max)
	public static final Vector2D zoomLimit = new Vector2D(0.1, 10);
	// The limit to decide the minimum and maximum value of the speedModificator.
	public static final Vector2D simulationSpeedLimit = new Vector2D(0.01, 50);


	// The minimal window width.
	public static final int appMinWidth = 960;
	// The minimal window height.
	public static final int appMinHeight = 540;
}
