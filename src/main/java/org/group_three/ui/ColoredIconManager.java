package org.group_three.ui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.group_three.debug.Debug;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * A class to create and manage colored icon variants.
 * Avoids creating the same colored variant more than once,
 * saving computing power and memory.
 *
 * @author Joel
 */
public class ColoredIconManager {

	//+++++++++++++++++++++++++++++++++++++++++++++++++MemberVariables++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The image which should be colored.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Image baseIcon;

	/**
	 * The image width.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final int iconWidth;

	/**
	 * The image height.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final int iconHeight;

	/**
	 * A list of all previously colored image variants.
	 *
	 * @author Joel
	 */
	@SuppressWarnings("JavadocDeclaration")
	private final Map<Color, Image> icons = new HashMap<>();

	//-------------------------------------------------MemberVariables--------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++Constructors+++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The default empty constructor.
	 * Creates an invalid object.
	 *
	 * @author Joel
	 */
	public ColoredIconManager() {
		this.baseIcon = null;
		this.iconWidth = 0;
		this.iconHeight = 0;
	}

	/**
	 * The default constructor for this class.
	 *
	 * @param iconPath The icon path of the image that should be modified in this class.
	 * @author Joel
	 */
	public ColoredIconManager(String iconPath) {
		Image loadedImage = null;

		// try loading the image from its path,
		// can fail if the path is incorrect
		try {
			loadedImage = new Image(getClass().getResourceAsStream(iconPath));
		} catch (Exception e) {
			//throw new RuntimeException(e);
		}

		this.baseIcon = loadedImage;

		if (this.baseIcon != null) {
			iconWidth = (int) baseIcon.getWidth();
			iconHeight = (int) baseIcon.getHeight();
		} else {
			this.iconWidth = 0;
			this.iconHeight = 0;
		}
	}

	//---------------------------------------------------Constructors---------------------------------------------------


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++Methods++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * The method to get a colored icon.
	 * If the colored icon doesn't exist yet,
	 * it will be created.
	 *
	 * @param color The color which the icon should be modified with.
	 * @return The colored icon image.
	 * @author Joel
	 */
	public Image getIcon(Color color) {
		// Check if icon with specified color already exists
		Image icon = icons.get(color);

		if (icon == null) {
			// Create icon with specified color if icon doesn't exist yet and add it to map
			icon = addImageTint(color);
			icons.put(color, icon);
			Debug.log("IconCount: " + icons.size(), Level.FINE);
		}

		return icon;
	}

	/**
	 * The method to create colored variants of an icon.
	 *
	 * @param color The color which the icon should be modified with.
	 * @return The colored icon image.
	 * @author Joel
	 */
	private Image addImageTint(Color color) {
		WritableImage tinted = new WritableImage(iconWidth, iconHeight);
		PixelReader reader = baseIcon.getPixelReader();
		PixelWriter writer = tinted.getPixelWriter();


		for (int y = 0; y < iconHeight; y++) {
			for (int x = 0; x < iconWidth; x++) {
				Color c = reader.getColor(x, y);

				if (c.getOpacity() >= 0.5) {
					Color newColor = new Color(
							c.getRed() * color.getRed(),
							c.getGreen() * color.getGreen(),
							c.getBlue() * color.getBlue(),
							c.getOpacity() * color.getOpacity()
					);
					writer.setColor(x, y, newColor);
				} else {
					writer.setColor(x, y, c);
				}
			}
		}

		return tinted;
	}

	//-----------------------------------------------------Methods------------------------------------------------------

}
