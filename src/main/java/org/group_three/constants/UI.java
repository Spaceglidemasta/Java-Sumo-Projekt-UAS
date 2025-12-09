package org.group_three.constants;

import javafx.scene.paint.Color;

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
	public static final String carIcon = "/org/group_three/ui/icons/car.png";
	// The default sphere collision color of world objects.
	public static final Color sphereCollisionColor = new Color(1,0,0,0.1);
	// The default box collision color of world objects.
	public static final Color boxCollisionColor = new Color(0,0,1,0.1);
	// The default color of vehicles when they are created.
	public static final Color defaultVehicleColor = new Color(1, 1, 1, 1);


	// The minimal window width.
	public static final int appMinWidth = 960;
	// The minimal window height.
	public static final int appMinHeight = 540;
}
