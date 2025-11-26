package org.group_three.ui.idkyet;

import javafx.scene.control.MenuItem;

// Just the basic MenuItem class extended with a single string variable to contain path data.
public class MenuItem_RecentlyOpend extends MenuItem {

	private String path;

	public MenuItem_RecentlyOpend() {
		super();
	}

	public MenuItem_RecentlyOpend(String text, String path) {
		super(text);
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
