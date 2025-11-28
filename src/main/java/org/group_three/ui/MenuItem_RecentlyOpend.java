package org.group_three.ui;

import javafx.scene.control.MenuItem;

/**
 * Just the basic MenuItem class extended with a single string variable to contain path data.
 *
 * @author Joel
 */
public class MenuItem_RecentlyOpend extends MenuItem {

	private String path;

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @see #MenuItem_RecentlyOpend(String text, String path)
	 */
	public MenuItem_RecentlyOpend() {
		super();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @param text
	 * Param-Comment
	 *
	 * @param path
	 * Param-Comment
	 *
	 * @see #MenuItem_RecentlyOpend()
	 */
	public MenuItem_RecentlyOpend(String text, String path) {
		super(text);
		this.path = path;
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 *
	 * @return
	 * Return-Comment
	 */
	public String getPath() {
		return path;
	}

}
