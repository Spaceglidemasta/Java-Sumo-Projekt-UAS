package org.group_three.ui.world;

import de.tudresden.sumo.objects.SumoColor;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.debug.Debug;
import org.group_three.model.WVehicle;
import org.group_three.ui.ColoredIconManager;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;
import org.group_three.ui.controllers.VehicleDetailsController;

import static org.group_three.ui.Meth.ClrToSumoClr;
import static org.group_three.ui.Meth.SumoClrToClr;

/**
 * Vehicle to be Rendered on the Canvas
 * @author Joel
 */
public class WorldVehicle extends WorldObject {

    //adjust this to change size
    private static double scale_size = 8;
    private static double y_size = 1 * scale_size;
    private static double x_size = 2 * scale_size;


	/**
	 * @author Joel
	 */
	public static ColoredIconManager iconManager = new ColoredIconManager(UI.carIcon);

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldVehicle() {
		super();
		remove();
	}

	/**
	 * Comment
	 *
	 * @author Joel
	 */
	public WorldVehicle(World world, Canvas canvas, String displayName) {
		super(world, canvas, displayName);
		setInteractable(true);
		detailClassPath = "/org/group_three/ui/fxml/VehicleDetails.fxml";
		setUseBoxCollision(true);
		setBoxCollision(new Vector2D(x_size, y_size));
	}

	public WVehicle getwVehicle() {
		return wVehicle;
	}

	/**
	 * @param wVehicle
	 */
	public void setwVehicle(WVehicle wVehicle) {
		this.wVehicle = wVehicle;
		updateSim();
	}

	@Override
	public void updateSim() {
		wVehicle.update();
		setPosition(new Vector2D(wVehicle.getPos()));
		//setRotation(wVehicle.getAngle());

		Debug.print(wVehicle.getColor());

		//setColor(convertWVColor());
		updateDetailsPanel();
	}

	public Color convertWVColor() {
		if (wVehicle.getColor().r == -1 &&
				wVehicle.getColor().g == -1 &&
				wVehicle.getColor().b == 0 &&
				wVehicle.getColor().a == -1
		) return getColor();

		return Meth.SumoClrToClr(wVehicle.getColor());
	}

	private WVehicle wVehicle;

	/**
	 * @return
	 * @author Joel
	 */
	public Color getColor() {
		return color;
	}

	/**
     * Sets the color via wVehicle
	 * @param color in Color (JavaFX)
	 * @author Luca
	 */
	public boolean setColor(Color color) {

        if(wVehicle.setColor(ClrToSumoClr(color))){
            this.color = color;
            return true;
        }
        else{
            return false;
        }
	}


    /**
     * Sets the color via wVehicle
     * @param color in SumoColor
     * @author Luca
     */
    public boolean setColor(SumoColor color){
        if(wVehicle.setColor(color)){
            this.color = SumoClrToClr(color);
            return true;
        }
        else {
            return false;
        }
    }

	/**
	 * @author Joel
	 */
	private Color color = UI.defaultVehicleColor;
	/**
	 * @author Joel
	 */
	private double worldSize = 1; // in meters


	/**
	 * Comment
	 *
	 * @author Joel
	 */
	@Override
	public void update() {
		super.update();
		Image visualImage = iconManager.getIcon(getColor());
		Vector2D rect = new Vector2D(visualImage.getWidth(), visualImage.getHeight());
		rect = rect.div(80 / scale_size);
		getGraphicsContext().save();
		//graphicsContext.setFill(Color.BLUE);
		Vector2D drawLoc = Meth.addRelativeLocation(getWorld().getViewerPosition(),
                getWorld().getViewerRotation(),
                getPosition().mul(getWorld().getViewerZoom())
        );

		getGraphicsContext().translate(drawLoc.x + getWorld().getViewerPositionOffset().x,
                                    drawLoc.y + getWorld().getViewerPositionOffset().y
        ); // Object Location
		getGraphicsContext().rotate(Meth.addRelativeRotation(getWorld().getViewerRotation(), getRotation()));
		getGraphicsContext().drawImage(visualImage,
                (rect.x / 2) * getWorld().getViewerZoom() * -1,
                (rect.y / 2) * getWorld().getViewerZoom() * -1,
                rect.x * getWorld().getViewerZoom(),
                rect.y * getWorld().getViewerZoom()
        );
		//graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
		getGraphicsContext().restore();
	}

	VehicleDetailsController vehicleDetailsController;

	@Override
	public void setupDetailsPanel(FXMLLoader fxmlLoader) {
		vehicleDetailsController = fxmlLoader.getController();
		vehicleDetailsController.setup(this);
	}

	@Override
	public void updateDetailsPanel() {
		if (vehicleDetailsController == null) {
			//Debug.print("vehicleDetailsController is invalid.");
			return;
		}

		vehicleDetailsController.update();
	}

	@Override
	public void remove() {
		super.remove();
		vehicleDetailsController.kill();
	}
}
