package ecs.items;

import ecs.components.AnimationComponent;
import ecs.components.HitboxComponent;
import ecs.components.ItemComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import java.util.ArrayList;
import java.util.Random;
import level.elements.tile.FloorTile;
import starter.Game;
import tools.Point;

/** Class which creates all needed Components for a basic WorldItem */
public class WorldItemBuilder {

    /**
     * Creates an Entity which then can be added to the game
     *
     * @param itemData the Data which should be given to the world Item
     * @return the newly created Entity
     */
    public static Entity buildWorldItem(ItemData itemData) {
        Entity droppedItem = new Entity();
        new PositionComponent(droppedItem, getRandomAccesiblePoint());
        new AnimationComponent(droppedItem, itemData.getWorldTexture());
        new ItemComponent(droppedItem, itemData);
        HitboxComponent component = new HitboxComponent(droppedItem);
        component.setiCollideEnter(
                (a, b, direction) -> {
                    itemData.triggerCollect(a, b);
                });
        return droppedItem;
    }

    /**
     * Creates an Entity at a specified Position which then can be added to the game
     *
     * @param itemData the Data which should be given to the world Item
     * @param point Position of the created Entity
     */
    public void buildWorldItemAtPosition(ItemData itemData, Point point) {
        Entity droppedItem = new Entity();
        new PositionComponent(droppedItem, point);
        new AnimationComponent(droppedItem, itemData.getWorldTexture());
        new ItemComponent(droppedItem, itemData);
        HitboxComponent component = new HitboxComponent(droppedItem);
        component.setiCollideEnter(
                (a, b, direction) -> {
                    itemData.triggerCollect(a, b);
                });
    }

    private static Point getRandomAccesiblePoint() {
        ArrayList<FloorTile> ft = (ArrayList) Game.currentLevel.getFloorTiles();
        Random rd = new Random();
        return ft.get(rd.nextInt(0, ft.size())).getCoordinateAsPoint();
    }
}
