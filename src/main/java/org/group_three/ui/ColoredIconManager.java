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

/**
 * @author Joel
 */
public class ColoredIconManager {
	/**
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Image image = null;
	/**
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private Map<Color, Image> icons = new HashMap<>() {
	};

	/**
	 * @author Joel
	 */
	public ColoredIconManager() {
		Debug.print("ColoredIconManager created.");
	}

	/**
	 * @param iconPath
	 * @author Joel
	 */
	public ColoredIconManager(String iconPath) {
		try {
			image = new Image(getClass().getResourceAsStream(iconPath));
			Debug.print("ColoredIconManager created.");
		} catch (Exception e) {
			//throw new RuntimeException(e);
			Debug.print("ColoredIconManager failed to create!");
		}
	}

	/**
	 * @param color
	 * @return
	 * @author Joel
	 */
	public Image getIcon(Color color) {
		// Check if icon with specified color already exists
		Image icon = icons.get(color);

		if (icon == null) {
			// Create icon with specified color if icon doesn't exist yet and add it to map
			icon = addImageTint(image, color);
			icons.put(color, icon);
			Debug.print("IconCount: " + icons.size());
		}

		return icon;
	}

	/**
	 * @param icon
	 * @param color
	 * @return
	 * @author Joel
	 */
	private Image addImageTint(Image icon, Color color) {
		int w = (int) icon.getWidth();
		int h = (int) icon.getHeight();

		WritableImage tinted = new WritableImage(w, h);
		PixelReader reader = icon.getPixelReader();
		PixelWriter writer = tinted.getPixelWriter();


		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				Color c = reader.getColor(x, y);

				if (c == UI.vehicleIconBaseColor) {
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

		return tinted;

	}
}
