package org.group_three.constants;

import javafx.scene.paint.Color;
import org.group_three.ui.Vector2D;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public final class UI {
    private UI() {} //prevents init.

	// The window name which can be seen next to the icon in the window itself
	// and the name when you hover over the window in the taskbar.
	public static final String appTitle = "Java-Sumo-Projekt-UAS";
	// The image which is loaded as the main window icon and as the icon in the taskbar.
	public static final String appIcon = "/org/group_three/ui/icons/icon.png";
	// The main FXML class which is loaded into the main window.
	// Other UI/FXML parts will be loaded into it.
	public static final String appFXML = "/org/group_three/ui/fxml/MainWindow.fxml";
	// The base icon to be used and colored for cars.
	public static final String carIcon = "/org/group_three/ui/icons/carIcon.png";
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
	public static final Color worldHighContrastColor = new Color(0,0,0,1);
	// The default road color.
	public static final Color roadColor = new Color(0.1,0.1,0.1,1);
	// A bool to decide if misc polygons should be rendered.
	public static boolean showPolys = true;
	// A Vector2D to decide how far one can zoom in and out of the UI. (x = min, y = max)
	public static final Vector2D zoomLimit = new Vector2D(0.1, 10);
	// The limit to decide the minimum and maximum value of the speedModificator.
	public static final Vector2D simulationSpeedLimit = new Vector2D(0.01, 25);
	// The file extension filter data for the simulation open file chooser window.
	public static final String[][][] simulationOpenFileExtensions = {
			{ {"SUMO Config"}, {"*.sumocfg"} },
			{ {"Network & Route"}, {"*.net.xml", "*.rou.xml"} }
	};
	public static final String userGuideLink = "https://github.com/Spaceglidemasta/Java-Sumo-Projekt-UAS/blob/main/UserGuide.pdf";

	public static final String detailClassFolderPath = "/org/group_three/ui/fxml/";
	public static final String recentlyOpenedDataFileName = "recentlyOpenedData.json";

	public static final double maxSimulationViewFps = 30;

	public static boolean highContrast = false;
	public static boolean showTLTiming = false;
	public static double vehicleScale = 1;
	public static Vector2D viewFilter_VehicleSpeed = new Vector2D(0, -1);
	public static List<Color> viewFilter_VehicleColor = new ArrayList<>();
	public static Vector2D viewFilter_Position = new Vector2D();
	public static double viewFilter_PositionRadius = 0;

	public static final Color[] randomVehicleColors = {Color.LIGHTBLUE, Color.YELLOW, Color.ORANGE, Color.BLUE, Color.PINK, Color.RED, Color.GREEN, Color.LIGHTGREEN};
	public static Color getRandomVehicleColor() {
		return randomVehicleColors[Math.toIntExact(Math.round(Math.random() * 100)) % randomVehicleColors.length];
	}


	// The minimal window width.
	public static final int appMinWidth = 960;
	// The minimal window height.
	public static final int appMinHeight = 540;
}
