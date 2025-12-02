package org.group_three.ui;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;

import java.util.Objects;

public class ColoredIcon {
	public String iconPath = "";
	public Color color = Color.WHITE;

	public ColoredIcon() {}

	public ColoredIcon(String iconPath, Color color) {
		this.iconPath = iconPath;
		this.color = color;

		//Debug.print(icon.getUrl());

	}
}
