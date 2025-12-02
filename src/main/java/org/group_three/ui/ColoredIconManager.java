package org.group_three.ui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;

import java.util.HashMap;
import java.util.Map;

public class ColoredIconManager {
	private static Map<ColoredIcon, Image> icons = new HashMap<>() {};

	public static Image getIcon(ColoredIcon coloredIcon) {
		// Check if icon with specified color already exists
		Image icon = icons.get(coloredIcon);

		if (icon == null) {
			// Create icon with specified color if icon doesn't exist yet and add it to map
			//icon = addImageTint(ColoredIcon.class.getResourceAsStream("/org/group_three/ui/fxml/car.png"), coloredIcon.color);
			icons.put(coloredIcon, icon);
			Debug.print(coloredIcon.toString() + " ### " + coloredIcon.color.toString() + ": " + icons.size());
		}

		return icon;
	}

	private static Image addImageTint(Image icon, Color color) {
		//GPT
		int w = (int) icon.getWidth();
		int h = (int) icon.getHeight();

		WritableImage tinted = new WritableImage(w, h);
		PixelReader reader = icon.getPixelReader();
		PixelWriter writer = tinted.getPixelWriter();


		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Color c = reader.getColor(x, y);

				if (c.getOpacity() > 0) {
					Color newColor = new Color(
							color.getRed(),
							color.getGreen(),
							color.getBlue(),
							c.getOpacity()
					);
					writer.setColor(x, y, newColor);
				} else {
					writer.setColor(x, y, c);
				}
			}
		}

		return  tinted;

	}
}
