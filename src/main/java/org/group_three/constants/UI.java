package org.group_three.constants;

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


	// The minimal window width.
	public static final int appMinWidth = 960;
	// The minimal window height.
	public static final int appMinHeight = 540;

    // @Joel bitte auch die ganzen farben hier rein packen, aber nicht nur UI.BLAU und so, sondern
    // auch sowas wie UI.TITEL_BAR_RGB, UI.BACKGROUND_RGB und so


}
