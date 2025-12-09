package org.group_three.ui.world;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.group_three.constants.UI;
import org.group_three.model.WVehicle;
import org.group_three.ui.ColoredIconManager;
import org.group_three.ui.Meth;
import org.group_three.ui.Vector2D;

/**
 * @author Leon
 */
public class WorldTrafficLight extends WorldObject {
//     /**
//         * @author Leon
//         */
//        public static ColoredIconManager iconManager = new ColoredIconManager(UI.carIcon);
//
//        /**
//         * Comment
//         *
//         * @author Leon
//         */
//        public WorldTrafficLight() {
//            super();
//            remove();
//        }
//
//        public WorldTrafficLight(World world, Canvas canvas, String displayName) {
//            super(world, canvas, displayName);
//            setInteractable(true);
//            detailClassPath = "/org/group_three/ui/fxml/TrafficLightDetails.fxml";
//        }
//
//        public void setwTrafficLight(String tflID) {
//            this.wVehicle = wVehicle;
//            updateSim();
//        }
//
//
//        public Color getColor() {
//            return color;
//        }
//
//        public void setColor(Color color) {
//        this.color = color;
//    }
//
//
//
//        private Color color = UI.defaultTrafficLightColor;
//
//
//        public void update() {
//            drawCollision();
//            Image visualImage = iconManager.getIcon(getColor());
//            Vector2D rect = new Vector2D(visualImage.getWidth(), visualImage.getHeight());
//            rect = rect.div(10);
//            getGraphicsContext().save();
//            //graphicsContext.setFill(Color.BLUE);
//            Vector2D drawLoc = Meth.addRelativeLocation(getWorld().getViewerPosition(), getWorld().getViewerRotation(), getPosition().mul(getWorld().getViewerZoom()));
//
//            getGraphicsContext().translate(drawLoc.x + getWorld().getViewerPositionOffset().x, drawLoc.y + getWorld().getViewerPositionOffset().y); // Object Location
//            getGraphicsContext().rotate(Meth.addRelativeRotation(getWorld().getViewerRotation(), getRotation()));
//            getGraphicsContext().drawImage(visualImage, (rect.x / 2) * getWorld().getViewerZoom() * -1, (rect.y / 2) * getWorld().getViewerZoom() * -1, rect.x * getWorld().getViewerZoom(), rect.y * getWorld().getViewerZoom());
//            //graphicsContext.fillRect((rect.x/2) * world.getViewerZoom() * -1, (rect.y/2) * world.getViewerZoom() * -1, rect.x * world.getViewerZoom(), rect.y * world.getViewerZoom());
//            getGraphicsContext().restore();
//        }
//
}
